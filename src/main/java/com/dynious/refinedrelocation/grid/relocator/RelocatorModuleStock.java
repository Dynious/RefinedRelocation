package com.dynious.refinedrelocation.grid.relocator;

import com.dynious.refinedrelocation.api.APIUtils;
import com.dynious.refinedrelocation.api.relocator.IItemRelocator;
import com.dynious.refinedrelocation.api.relocator.RelocatorModuleBase;
import com.dynious.refinedrelocation.client.gui.GuiModuleStock;
import com.dynious.refinedrelocation.container.ContainerModuleStock;
import com.dynious.refinedrelocation.helper.IOHelper;
import com.dynious.refinedrelocation.helper.ItemStackHelper;
import com.dynious.refinedrelocation.item.ModItems;
import com.dynious.refinedrelocation.lib.Names;
import com.dynious.refinedrelocation.lib.Resources;
import com.dynious.refinedrelocation.compat.waila.RelocatorHUDHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RelocatorModuleStock extends RelocatorModuleBase implements IInventory
{
    private static IIcon icon;

    private ItemStack[] itemStacksToStock = new ItemStack[9];

    public RelocatorModuleStock() {
        super(new ItemStack(ModItems.relocatorModule, 1, 6));
    }

    @Override
    public ItemStack outputToSide(IItemRelocator relocator, int side, TileEntity tile, ItemStack stack, boolean simulate)
    {
        if (!hasToStock(stack))
            return stack;

        if (tile != null && tile instanceof IInventory)
        {
            IInventory inventory = (IInventory) tile;
            int itemsNeeded = getAmountOfItemsNeededToStock(stack) - getAmountOfItemsInInventory(inventory, ForgeDirection.OPPOSITES[side], stack);
            if (itemsNeeded > 0)
            {
                if (stack.stackSize > itemsNeeded)
                {
                    ItemStack returnedStack = IOHelper.insert(inventory, stack.splitStack(itemsNeeded), ForgeDirection.OPPOSITES[side], simulate);
                    if (returnedStack != null)
                        stack.stackSize += returnedStack.stackSize;
                    return stack;
                }
                else
                {
                    return IOHelper.insert(inventory, stack, ForgeDirection.OPPOSITES[side], simulate);
                }
            }
            return stack;
        }
        return super.outputToSide(relocator, side, tile, stack, simulate);
    }

    public boolean hasToStock(ItemStack stack)
    {
        for (ItemStack itemToStock : itemStacksToStock)
        {
            if (ItemStackHelper.areItemStacksEqual(stack, itemToStock))
                return true;
        }
        return false;
    }

    public int getAmountOfItemsInInventory(IInventory inventory, int side, ItemStack stack)
    {
        int amount = 0;
        if (inventory instanceof ISidedInventory)
        {
            int[] accessibleSlots = ((ISidedInventory) inventory).getAccessibleSlotsFromSide(side);
            for (int slot : accessibleSlots)
            {
                amount += getAmountOfItemsInSlot(inventory, slot, stack);
            }
        }
        else
        {
            for (int slot = 0; slot < inventory.getSizeInventory(); slot++)
            {
                amount += getAmountOfItemsInSlot(inventory, slot, stack);
            }
        }
        return amount;
    }

    public int getAmountOfItemsInSlot(IInventory inventory, int slot, ItemStack stack)
    {
        ItemStack stackInSlot = inventory.getStackInSlot(slot);
        if (ItemStackHelper.areItemStacksEqual(stackInSlot, stack))
        {
            return stackInSlot.stackSize;
        }
        return 0;
    }

    public int getAmountOfItemsNeededToStock(ItemStack stack)
    {
        int amount = 0;
        for (ItemStack itemToStock : itemStacksToStock)
        {
            if (ItemStackHelper.areItemStacksEqual(stack, itemToStock))
                amount += itemToStock.stackSize;
        }
        return amount;
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
        return new GuiModuleStock(player, this);
    }

    @Override
    public Container getContainer(IItemRelocator relocator, int side, EntityPlayer player)
    {
        return new ContainerModuleStock(player, this);
    }

    @Override
    public List<ItemStack> getDrops(IItemRelocator relocator, int side)
    {
        return Arrays.asList(new ItemStack(ModItems.relocatorModule, 1, 6));
    }

    @Override
    public IIcon getIcon(IItemRelocator relocator, int side)
    {
        return icon;
    }

    @Override
    public void registerIcons(IIconRegister register)
    {
        icon = register.registerIcon(Resources.MOD_ID + ":" + "relocatorModuleStock");
    }

    @Override
    public String getDisplayName()
    {
        return StatCollector.translateToLocal("item." + Names.relocatorModule + 6 + ".name");
    }

    @Override
    public int getSizeInventory()
    {
        return itemStacksToStock.length;
    }

    @Override
    public void readFromNBT(IItemRelocator relocator, int side, NBTTagCompound compound)
    {
        NBTTagList nbttaglist = compound.getTagList("Items", 10);
        for (int i = 0; i < nbttaglist.tagCount(); ++i)
        {
            NBTTagCompound nbttagcompound1 = nbttaglist.getCompoundTagAt(i);
            int j = nbttagcompound1.getByte("Slot") & 255;

            if (j >= 0 && j < this.itemStacksToStock.length)
            {
                this.itemStacksToStock[j] = ItemStack.loadItemStackFromNBT(nbttagcompound1);
            }
        }
    }

    @Override
    public void writeToNBT(IItemRelocator relocator, int side, NBTTagCompound compound)
    {
        NBTTagList nbttaglist = new NBTTagList();
        for (int i = 0; i < this.itemStacksToStock.length; ++i)
        {
            if (this.itemStacksToStock[i] != null)
            {
                NBTTagCompound nbttagcompound1 = new NBTTagCompound();
                nbttagcompound1.setByte("Slot", (byte) i);
                this.itemStacksToStock[i].writeToNBT(nbttagcompound1);
                nbttaglist.appendTag(nbttagcompound1);
            }
        }
        compound.setTag("Items", nbttaglist);
    }

    /*
    IInventory
     */

    @Override
    public ItemStack getStackInSlot(int par1)
    {
        return this.itemStacksToStock[par1];
    }

    @Override
    public ItemStack decrStackSize(int par1, int par2)
    {
        if (this.itemStacksToStock[par1] != null)
        {
            ItemStack itemstack;

            if (this.itemStacksToStock[par1].stackSize <= par2)
            {
                itemstack = this.itemStacksToStock[par1];
                this.itemStacksToStock[par1] = null;
                this.markDirty();
                return itemstack;
            }
            else
            {
                itemstack = this.itemStacksToStock[par1].splitStack(par2);

                if (this.itemStacksToStock[par1].stackSize == 0)
                {
                    this.itemStacksToStock[par1] = null;
                }

                this.markDirty();
                return itemstack;
            }
        }
        else
        {
            return null;
        }
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int par1)
    {
        if (this.itemStacksToStock[par1] != null)
        {
            ItemStack itemstack = this.itemStacksToStock[par1];
            this.itemStacksToStock[par1] = null;
            return itemstack;
        }
        else
        {
            return null;
        }
    }

    @Override
    public void setInventorySlotContents(int par1, ItemStack par2ItemStack)
    {
        this.itemStacksToStock[par1] = par2ItemStack;

        if (par2ItemStack != null && par2ItemStack.stackSize > this.getInventoryStackLimit())
        {
            par2ItemStack.stackSize = this.getInventoryStackLimit();
        }

        this.markDirty();
    }

    @Override
    public String getInventoryName()
    {
        return "relocatorModule.stock";
    }

    @Override
    public boolean hasCustomInventoryName()
    {
        return false;
    }

    @Override
    public int getInventoryStackLimit()
    {
        return 64;
    }

    @Override
    public void markDirty()
    {

    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer par1EntityPlayer)
    {
        return true;
    }

    @Override
    public void openInventory()
    {

    }

    @Override
    public void closeInventory()
    {

    }

    @Override
    public boolean isItemValidForSlot(int par1, ItemStack par2ItemStack)
    {
        return true;
    }

    @Override
    public List<String> getWailaInformation(NBTTagCompound nbtData)
    {
        List<String> information = super.getWailaInformation(nbtData);
        List<ItemStack> stockedItemStacks = new ArrayList<ItemStack>();
        NBTTagList nbttaglist = nbtData.getTagList("Items", 10);
        for (int i = 0; i < nbttaglist.tagCount(); ++i)
        {
            NBTTagCompound nbttagcompound1 = nbttaglist.getCompoundTagAt(i);

            ItemStack itemStack = ItemStack.loadItemStackFromNBT(nbttagcompound1);
            RelocatorHUDHandler.addStack((ArrayList<ItemStack>) stockedItemStacks, itemStack);
        }

        for (ItemStack itemStack : stockedItemStacks)
        {
            information.add(itemStack.getDisplayName() + " x " + itemStack.stackSize);
        }

        return information;
    }
}
