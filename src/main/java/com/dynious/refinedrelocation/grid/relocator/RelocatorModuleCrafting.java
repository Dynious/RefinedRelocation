package com.dynious.refinedrelocation.grid.relocator;

import com.dynious.refinedrelocation.api.APIUtils;
import com.dynious.refinedrelocation.api.relocator.IItemRelocator;
import com.dynious.refinedrelocation.api.relocator.IRelocatorModule;
import com.dynious.refinedrelocation.api.relocator.RelocatorModuleBase;
import com.dynious.refinedrelocation.client.gui.GuiModuleCrafting;
import com.dynious.refinedrelocation.container.ContainerModuleCrafting;
import com.dynious.refinedrelocation.helper.IOHelper;
import com.dynious.refinedrelocation.helper.ItemStackHelper;
import com.dynious.refinedrelocation.item.ModItems;
import com.dynious.refinedrelocation.lib.Names;
import com.dynious.refinedrelocation.lib.Resources;
import com.dynious.refinedrelocation.lib.Settings;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCraftResult;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.ArrayList;
import java.util.List;

public class RelocatorModuleCrafting extends RelocatorModuleBase
{
    private static IIcon icon;
    public final LocalInventoryCrafting CRAFT_MATRIX = new LocalInventoryCrafting();
    public final IInventory CRAFT_RESULT = new InventoryCraftResult();
    public int craftTick = 0;
    public int normalTick = 0;
    public ItemStack outputStack = null;
    public boolean isStuffed = false;
    private int maxCraftStack = 64;
    private boolean input = false;

    @Override
    public boolean isItemDestination()
    {
        return true;
    }

    @Override
    public ItemStack receiveItemStack(IItemRelocator relocator, int side, ItemStack stack, boolean input, boolean simulate)
    {
        if (!simulate)
        {
            this.input = input;
            int currentAmount = 0;
            List<Integer> slots = new ArrayList<Integer>();
            for (int i = 0; i < CRAFT_MATRIX.getSizeInventory(); i++)
            {
                ItemStack craftStack = CRAFT_MATRIX.getStackInSlot(i);
                if (ItemStackHelper.areItemStacksEqual(stack, craftStack))
                {
                    currentAmount += craftStack.stackSize;
                    slots.add(i);
                }
            }

            if (slots.isEmpty())
                return stack;

            int needed = (slots.size() * maxCraftStack) - currentAmount;

            if (needed < 0)
                return stack;

            int toMove = Math.min(needed, stack.stackSize);
            int amountPerStack = (toMove + currentAmount) / slots.size();
            int extra = (toMove + currentAmount) % slots.size();

            for (int slot : slots)
            {
                ItemStack craftStack = CRAFT_MATRIX.getStackInSlot(slot);
                if (extra > 0)
                {
                    craftStack.stackSize = amountPerStack + 1;
                    extra--;
                }
                else
                {
                    craftStack.stackSize = amountPerStack;
                }
            }

            stack.stackSize -= toMove;

            if (stack.stackSize == 0)
            {
                return null;
            }
        }
        else
        {
            for (int i = 0; i < CRAFT_MATRIX.getSizeInventory(); i++)
            {
                ItemStack craftStack = CRAFT_MATRIX.getStackInSlot(i);
                if (ItemStackHelper.areItemStacksEqual(stack, craftStack))
                {
                    int toMove = Math.min(maxCraftStack - craftStack.stackSize, stack.stackSize);
                    if (toMove < 0)
                        continue;
                    stack.stackSize -= toMove;

                    if (stack.stackSize == 0)
                    {
                        return null;
                    }
                }
            }
        }
        return stack;
    }

    @Override
    public void onUpdate(IItemRelocator relocator, int side)
    {
        super.onUpdate(relocator, side);

        if (relocator.getTileEntity().getWorldObj().isRemote)
            return;

        if (canCraft())
        {
            craftTick++;
            if (craftTick > Settings.CRAFTING_MODULE_TICKS_BETWEEN_CRAFTING)
            {
                craftTick = 0;
                craft(relocator, side);
            }
        }
        else
        {
            craftTick = 0;
            normalTick++;
            if (normalTick > Settings.RELOCATOR_MIN_TICKS_BETWEEN_EXTRACTION)
            {
                if (outputStack != null)
                {
                    if (CRAFT_RESULT.getStackInSlot(0) == null || !CRAFT_RESULT.getStackInSlot(0).isItemEqual(outputStack))
                    {
                        IOHelper.spawnItemInWorld(relocator.getTileEntity().getWorldObj(), outputStack, relocator.getTileEntity().xCoord, relocator.getTileEntity().yCoord, relocator.getTileEntity().zCoord);
                        outputStack = null;
                    }
                    outputStack(relocator, side);
                }
            }
        }
    }

    public void craft(IItemRelocator relocator, int side)
    {
        for (int slot = 0; slot < CRAFT_MATRIX.getSizeInventory(); slot++)
        {
            ItemStack stack = CRAFT_MATRIX.getStackInSlot(slot);
            if (stack != null)
            {
                stack.stackSize--;
            }
        }
        outputStack = CRAFT_RESULT.getStackInSlot(0).copy();
        outputStack(relocator, side);
    }

    public boolean canCraft()
    {
        if (outputStack != null || CRAFT_RESULT.getStackInSlot(0) == null)
            return false;

        for (int slot = 0; slot < CRAFT_MATRIX.getSizeInventory(); slot++)
        {
            ItemStack stack = CRAFT_MATRIX.getStackInSlot(slot);
            if (stack != null)
            {
                if (stack.stackSize < 1)
                    return false;
            }
        }
        return true;
    }

    public void outputStack(IItemRelocator relocator, int side)
    {
        if (input)
        {
            outputStack = relocator.insert(outputStack, side, false);
        }
        else
        {
            IItemRelocator relocator1 = relocator.getConnectedRelocators()[side];
            if (relocator1 != null)
            {
                IRelocatorModule module = relocator1.getRelocatorModule(ForgeDirection.OPPOSITES[side]);
                if (module != null && module.isItemDestination())
                {
                    outputStack = module.receiveItemStack(relocator1, ForgeDirection.OPPOSITES[side], outputStack, true, false);
                }
                else
                {
                    outputStack = relocator1.insert(outputStack, ForgeDirection.OPPOSITES[side], false);
                }
            }
            else
            {
                TileEntity tile = relocator.getConnectedInventories()[side];
                if (tile != null)
                {
                    outputStack = IOHelper.insert(tile, outputStack, ForgeDirection.getOrientation(side).getOpposite(), false);
                }
            }
        }
    }

    public int getMaxCraftStack()
    {
        return maxCraftStack;
    }

    public void setMaxCraftStack(int maxCraftStack)
    {
        this.maxCraftStack = maxCraftStack;
    }

    @Override
    public List<ItemStack> getDrops(IItemRelocator relocator, int side)
    {
        List<ItemStack> list = new ArrayList<ItemStack>();
        list.add(new ItemStack(ModItems.relocatorModule, 1, 12));
        return list;
    }

    @Override
    public boolean onActivated(IItemRelocator relocator, EntityPlayer player, int side, ItemStack stack)
    {
        APIUtils.openRelocatorModuleGUI(relocator, player, side);
        return true;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public GuiScreen getGUI(IItemRelocator relocator, int side, EntityPlayer player)
    {
        return new GuiModuleCrafting(player, this);
    }

    @Override
    public Container getContainer(IItemRelocator relocator, int side, EntityPlayer player)
    {
        return new ContainerModuleCrafting(player, this);
    }

    @Override
    public String getDisplayName()
    {
        return StatCollector.translateToLocal("item." + Names.relocatorModule + 12 + ".name");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(IItemRelocator relocator, int side)
    {
        return icon;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister register)
    {
        icon = register.registerIcon(Resources.MOD_ID + ":" + "relocatorModuleCrafting");
    }

    @Override
    public void readFromNBT(IItemRelocator relocator, int side, NBTTagCompound compound)
    {
        super.readFromNBT(relocator, side, compound);
        NBTTagList nbttaglist = compound.getTagList("Items", 10);

        for (int i = 0; i < nbttaglist.tagCount(); ++i)
        {
            NBTTagCompound nbttagcompound1 = nbttaglist.getCompoundTagAt(i);
            int j = nbttagcompound1.getByte("Slot") & 255;

            if (j >= 0 && j < this.CRAFT_MATRIX.getSizeInventory())
            {
                CRAFT_MATRIX.setInventorySlotContents(j, ItemStack.loadItemStackFromNBT(nbttagcompound1));
            }
        }

        outputStack = ItemStack.loadItemStackFromNBT(compound.getCompoundTag("outputStack"));
        input = compound.getBoolean("input");
        maxCraftStack = compound.getInteger("maxCraftStack");
    }

    @Override
    public void writeToNBT(IItemRelocator relocator, int side, NBTTagCompound compound)
    {
        super.writeToNBT(relocator, side, compound);
        NBTTagList nbttaglist = new NBTTagList();

        for (int i = 0; i < this.CRAFT_MATRIX.getSizeInventory(); ++i)
        {
            if (CRAFT_MATRIX.getStackInSlot(i) != null)
            {
                NBTTagCompound nbttagcompound1 = new NBTTagCompound();
                nbttagcompound1.setByte("Slot", (byte) i);
                CRAFT_MATRIX.getStackInSlot(i).writeToNBT(nbttagcompound1);
                nbttaglist.appendTag(nbttagcompound1);
            }
        }
        compound.setTag("Items", nbttaglist);

        if (outputStack != null)
        {
            NBTTagCompound nbttagcompound1 = new NBTTagCompound();
            outputStack.writeToNBT(nbttagcompound1);
            compound.setTag("outputStack", nbttagcompound1);
        }
        compound.setBoolean("input", input);
        compound.setInteger("maxCraftStack", maxCraftStack);
    }

    private class LocalInventoryCrafting extends InventoryCrafting
    {
        public LocalInventoryCrafting()
        {
            super(new Container()
            {
                @Override
                public boolean canInteractWith(EntityPlayer p_75145_1_)
                {
                    return false;
                }
            }, 3, 3);

        }

        @Override
        public void setInventorySlotContents(int p_70299_1_, ItemStack p_70299_2_)
        {
            super.setInventorySlotContents(p_70299_1_, p_70299_2_);
            CRAFT_RESULT.setInventorySlotContents(0, CraftingManager.getInstance().findMatchingRecipe(CRAFT_MATRIX, null));
        }
    }
}
