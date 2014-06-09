package com.dynious.refinedrelocation.tileentity;

import com.dynious.refinedrelocation.api.APIUtils;
import com.dynious.refinedrelocation.api.filter.IFilterGUI;
import com.dynious.refinedrelocation.api.tileentity.IFilterTileGUI;
import com.dynious.refinedrelocation.api.tileentity.ISortingInventory;
import com.dynious.refinedrelocation.api.tileentity.handlers.ISortingInventoryHandler;
import com.dynious.refinedrelocation.block.BlockSortingIronChest;
import com.dynious.refinedrelocation.block.ModBlocks;
import com.dynious.refinedrelocation.helper.ItemStackHelper;
import cpw.mods.fml.relauncher.ReflectionHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import cpw.mods.ironchest.IronChestType;
import cpw.mods.ironchest.ItemChestChanger;
import cpw.mods.ironchest.TileEntityIronChest;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

public class TileSortingIronChest extends TileEntityIronChest implements ISortingInventory, IFilterTileGUI
{
    public boolean isFirstRun = true;

    private IFilterGUI filter = APIUtils.createStandardFilter(this);

    private ISortingInventoryHandler sortingInventoryHandler = APIUtils.createSortingInventoryHandler(this);
    private Priority priority = Priority.NORMAL;

    public TileSortingIronChest()
    {
    }

    public TileSortingIronChest(IronChestType type)
    {
        super(type);
    }

    @Override
    public void updateEntity()
    {
        if (isFirstRun)
        {
            sortingInventoryHandler.onTileAdded();
            isFirstRun = false;
        }
        super.updateEntity();
    }

    @Override
    public void setInventorySlotContents(int i, ItemStack itemstack)
    {
        sortingInventoryHandler.setInventorySlotContents(i, itemstack);
    }

    @Override
    public TileEntityIronChest applyUpgradeItem(ItemChestChanger itemChestChanger)
    {
        if ((Integer) ReflectionHelper.getPrivateValue(TileEntityIronChest.class, this, "numUsingPlayers") > 0)
        {
            return null;
        }
        if (!itemChestChanger.getType().canUpgrade(this.getType()))
        {
            return null;
        }
        TileSortingIronChest newEntity = new TileSortingIronChest(IronChestType.values()[itemChestChanger.getTargetChestOrdinal(getType().ordinal())]);

        //Copy stacks and remove old stacks
        int newSize = newEntity.chestContents.length;
        System.arraycopy(chestContents, 0, newEntity.chestContents, 0, Math.min(newSize, chestContents.length));
        BlockSortingIronChest block = ModBlocks.sortingIronChest;
        block.dropContent(newSize, this, this.worldObj, this.xCoord, this.yCoord, this.zCoord);

        //Copy filter settings
        NBTTagCompound filterTag = new NBTTagCompound();
        filter.writeToNBT(filterTag);
        newEntity.filter.readFromNBT(filterTag);

        //Set facing, sort and reset syncTick
        newEntity.setFacing((Byte) ReflectionHelper.getPrivateValue(TileEntityIronChest.class, this, "facing"));
        newEntity.sortTopStacks();
        ReflectionHelper.setPrivateValue(TileEntityIronChest.class, this, -1, "ticksSinceSync");
        return newEntity;
    }

    @Override
    public final boolean putStackInSlot(ItemStack itemStack, int slotIndex)
    {
        if (slotIndex >= 0 && slotIndex < chestContents.length)
        {
            chestContents[slotIndex] = itemStack;
            return true;
        }
        return false;
    }

    @Override
    public ItemStack putInInventory(ItemStack itemStack)
    {
        for (int slot = 0; slot < getSizeInventory() && itemStack != null && itemStack.stackSize > 0; ++slot)
        {
            if (isItemValidForSlot(slot, itemStack))
            {
                ItemStack itemstack1 = getStackInSlot(slot);

                if (itemstack1 == null)
                {
                    int max = Math.min(itemStack.getMaxStackSize(), getInventoryStackLimit());
                    if (max >= itemStack.stackSize)
                    {
                        chestContents[slot] = itemStack;

                        onInventoryChanged();
                        itemStack = null;
                    }
                    else
                    {
                        chestContents[slot] = itemStack.splitStack(max);
                    }
                }
                else if (ItemStackHelper.areItemStacksEqual(itemstack1, itemStack))
                {
                    int max = Math.min(itemStack.getMaxStackSize(), getInventoryStackLimit());
                    if (max > itemstack1.stackSize)
                    {
                        int l = Math.min(itemStack.stackSize, max - itemstack1.stackSize);
                        itemStack.stackSize -= l;
                        itemstack1.stackSize += l;
                    }
                }
            }
        }
        return itemStack;
    }

    @Override
    public IFilterGUI getFilter()
    {
        return filter;
    }

    @Override
    public TileEntity getTileEntity()
    {
        return this;
    }

    @Override
    public void onFilterChanged()
    {
        this.onInventoryChanged();
    }

    @Override
    public Priority getPriority()
    {
        return priority;
    }

    @Override
    public void setPriority(Priority priority)
    {
        this.priority = priority;
    }

    @Override
    public ISortingInventoryHandler getHandler()
    {
        return sortingInventoryHandler;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public boolean shouldRenderInPass(int pass)
    {
        return pass == 0 || pass == 1;
    }

    @Override
    public TileEntityIronChest updateFromMetadata(int l)
    {
        if (worldObj != null && worldObj.isRemote)
        {
            if (l != getType().ordinal())
            {
                worldObj.setBlockTileEntity(xCoord, yCoord, zCoord, new TileSortingIronChest(IronChestType.values()[l]));
                return (TileEntityIronChest) worldObj.getBlockTileEntity(xCoord, yCoord, zCoord);
            }
        }
        return this;
    }

    public void fixType(IronChestType type)
    {
        if (type != getType())
        {
            ReflectionHelper.setPrivateValue(TileEntityIronChest.class, this, type, "type");
        }
        this.chestContents = new ItemStack[getSizeInventory()];
    }

    @Override
    public void readFromNBT(NBTTagCompound compound)
    {
        fixType(IronChestType.values()[compound.getByte("type")]);
        super.readFromNBT(compound);
        filter.readFromNBT(compound);
        if (compound.hasKey("priority"))
        {
            setPriority(Priority.values()[compound.getByte("priority")]);
        }
        else
        {
            setPriority(filter.isBlacklisting() ? Priority.LOW : Priority.NORMAL);
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound compound)
    {
        compound.setByte("type", (byte) getType().ordinal());
        super.writeToNBT(compound);
        filter.writeToNBT(compound);
        compound.setByte("priority", (byte) priority.ordinal());
    }

    @Override
    public void invalidate()
    {
        sortingInventoryHandler.onTileRemoved();
        super.invalidate();
    }

    @Override
    public void onChunkUnload()
    {
        sortingInventoryHandler.onTileRemoved();
        super.onChunkUnload();
    }
}
