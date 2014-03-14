package com.dynious.refinedrelocation.tileentity;

import com.dynious.refinedrelocation.api.*;
import com.dynious.refinedrelocation.block.BlockSortingIronChest;
import com.dynious.refinedrelocation.block.ModBlocks;
import cpw.mods.fml.relauncher.ReflectionHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import cpw.mods.ironchest.IronChestType;
import cpw.mods.ironchest.ItemChestChanger;
import cpw.mods.ironchest.TileEntityIronChest;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class TileSortingIronChest extends TileEntityIronChest implements ISortingInventory, IFilterGUITile
{
    public boolean isFirstRun = true;

    private FilterStandard filter = new FilterStandard();

    private SortingInventoryHandler sortingInventoryHandler = new SortingInventoryHandler(this);

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
        int newSize = newEntity.chestContents.length;
        System.arraycopy(chestContents, 0, newEntity.chestContents, 0, Math.min(newSize, chestContents.length));
        BlockSortingIronChest block = ModBlocks.sortingIronChest;
        block.dropContent(newSize, this, this.worldObj, this.xCoord, this.yCoord, this.zCoord);
        newEntity.setFacing((Byte) ReflectionHelper.getPrivateValue(TileEntityIronChest.class, this, "facing"));
        newEntity.sortTopStacks();
        ReflectionHelper.setPrivateValue(TileEntityIronChest.class, this, -1, "ticksSinceSync");
        return newEntity;
    }

    @Override
    public ItemStack[] getInventory()
    {
        return chestContents;
    }

    @Override
    public IFilterGUI getFilter()
    {
        return filter;
    }

    @Override
    public Priority getPriority()
    {
        return getFilter().isBlacklisting() ? Priority.LOW : Priority.NORMAL;
    }

    @Override
    public SortingInventoryHandler getSortingHandler()
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
    public void readFromNBT(NBTTagCompound nbttagcompound)
    {
        fixType(IronChestType.values()[nbttagcompound.getByte("type")]);
        super.readFromNBT(nbttagcompound);
        filter.readFromNBT(nbttagcompound);
    }

    @Override
    public void writeToNBT(NBTTagCompound nbttagcompound)
    {
        nbttagcompound.setByte("type", (byte) getType().ordinal());
        super.writeToNBT(nbttagcompound);
        filter.writeToNBT(nbttagcompound);
    }
}
