package com.dynious.refinedrelocation.grid.relocator;

import com.dynious.refinedrelocation.api.APIUtils;
import com.dynious.refinedrelocation.api.filter.RelocatorModuleBase;
import com.dynious.refinedrelocation.api.tileentity.IRelocator;
import com.dynious.refinedrelocation.gui.GuiModuleStock;
import com.dynious.refinedrelocation.gui.container.ContainerModuleStock;
import com.dynious.refinedrelocation.helper.IOHelper;
import com.dynious.refinedrelocation.helper.ItemStackHelper;
import com.dynious.refinedrelocation.item.ModItems;
import com.dynious.refinedrelocation.lib.Resources;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraftforge.common.ForgeDirection;

import java.util.Arrays;
import java.util.List;

public class RelocatorModuleStock extends RelocatorModuleBase implements IInventory
{
    private static Icon icon;

    private ItemStack[] itemStacksToStock = new ItemStack[9];

    @Override
    public ItemStack outputToSide(IRelocator relocator, int side, TileEntity tile, ItemStack stack, boolean simulate)
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
    public boolean onActivated(IRelocator relocator, EntityPlayer player, int side, ItemStack stack)
    {
        APIUtils.openRelocatorFilterGUI(relocator, player, side);
        return true;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public GuiScreen getGUI(IRelocator relocator, EntityPlayer player)
    {
        return new GuiModuleStock(player, this);
    }

    @Override
    public Container getContainer(IRelocator relocator, EntityPlayer player)
    {
        return new ContainerModuleStock(player, this);
    }

    @Override
    public List<ItemStack> getDrops(IRelocator relocator, int side)
    {
        return Arrays.asList(new ItemStack(ModItems.relocatorModule, 1, 6));
    }

    @Override
    public Icon getIcon(IRelocator relocator, int side)
    {
        return icon;
    }

    @Override
    public void registerIcons(IconRegister register)
    {
        icon = register.registerIcon(Resources.MOD_ID + ":" + "relocatorModuleStock");
    }

    @Override
    public int getSizeInventory()
    {
        return itemStacksToStock.length;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound)
    {
        super.readFromNBT(compound);

        NBTTagList nbttaglist = compound.getTagList("Items");
        for (int i = 0; i < nbttaglist.tagCount(); ++i)
        {
            NBTTagCompound nbttagcompound1 = (NBTTagCompound)nbttaglist.tagAt(i);
            int j = nbttagcompound1.getByte("Slot") & 255;

            if (j >= 0 && j < this.itemStacksToStock.length)
            {
                this.itemStacksToStock[j] = ItemStack.loadItemStackFromNBT(nbttagcompound1);
            }
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound compound)
    {
        super.writeToNBT(compound);

        NBTTagList nbttaglist = new NBTTagList();
        for (int i = 0; i < this.itemStacksToStock.length; ++i)
        {
            if (this.itemStacksToStock[i] != null)
            {
                NBTTagCompound nbttagcompound1 = new NBTTagCompound();
                nbttagcompound1.setByte("Slot", (byte)i);
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
                this.onInventoryChanged();
                return itemstack;
            }
            else
            {
                itemstack = this.itemStacksToStock[par1].splitStack(par2);

                if (this.itemStacksToStock[par1].stackSize == 0)
                {
                    this.itemStacksToStock[par1] = null;
                }

                this.onInventoryChanged();
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

        this.onInventoryChanged();
    }

    @Override
    public String getInvName()
    {
        return "relocatorModule.stock";
    }

    @Override
    public boolean isInvNameLocalized()
    {
        return false;
    }

    @Override
    public int getInventoryStackLimit()
    {
        return 64;
    }

    @Override
    public void onInventoryChanged()
    {

    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer par1EntityPlayer)
    {
        return true;
    }

    @Override
    public void openChest()
    {
    }

    @Override
    public void closeChest()
    {
    }

    @Override
    public boolean isItemValidForSlot(int par1, ItemStack par2ItemStack)
    {
        return true;
    }
}
