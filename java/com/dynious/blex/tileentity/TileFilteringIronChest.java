package com.dynious.blex.tileentity;

import com.dynious.blex.api.Filter;
import com.dynious.blex.api.FilteringInventoryHandler;
import com.dynious.blex.api.FilteringMemberHandler;
import com.dynious.blex.api.IFilteringInventory;
import com.dynious.blex.block.BlockFilteringIronChest;
import com.dynious.blex.block.ModBlocks;
import cpw.mods.fml.relauncher.ReflectionHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import cpw.mods.ironchest.IronChestType;
import cpw.mods.ironchest.ItemChestChanger;
import cpw.mods.ironchest.TileEntityIronChest;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class TileFilteringIronChest extends TileEntityIronChest implements IFilteringInventory
{
    public boolean isFirstRun = true;

    private Filter filter = new Filter();
    private boolean blacklist = true;

    private FilteringInventoryHandler filteringInventoryHandler = new FilteringInventoryHandler(this);

    public TileFilteringIronChest()
    {
    }

    public TileFilteringIronChest(IronChestType type)
    {
        super(type);
    }

    @Override
    public void updateEntity()
    {
        if (isFirstRun)
        {
            filteringInventoryHandler.onTileAdded();
            isFirstRun = false;
        }
        super.updateEntity();
    }

    @Override
    public void setInventorySlotContents(int i, ItemStack itemstack)
    {
        filteringInventoryHandler.setInventorySlotContents(i, itemstack);
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
        TileFilteringIronChest newEntity = new TileFilteringIronChest(IronChestType.values()[itemChestChanger.getTargetChestOrdinal(getType().ordinal())]);
        int newSize = newEntity.chestContents.length;
        System.arraycopy(chestContents, 0, newEntity.chestContents, 0, Math.min(newSize, chestContents.length));
        BlockFilteringIronChest block = ModBlocks.filteringIronChest;
        block.dropContent(newSize, this, this.worldObj, this.xCoord, this.yCoord, this.zCoord);
        newEntity.setFacing((Byte) ReflectionHelper.getPrivateValue(TileEntityIronChest.class, this, "facing"));
        newEntity.sortTopStacks();
        ReflectionHelper.setPrivateValue(TileEntityIronChest.class, this, -1, "ticksSinceSync");
        return newEntity;
    }

    @Override
    public FilteringInventoryHandler getFilteringInventoryHandler()
    {
        return filteringInventoryHandler;
    }

    @Override
    public ItemStack[] getInventory()
    {
        return chestContents;
    }

    @Override
    public Filter getFilter()
    {
        return filter;
    }

    @Override
    public boolean getBlackList()
    {
        return blacklist;
    }

    @Override
    public void setBlackList(boolean value)
    {
        blacklist = value;
    }

    @Override
    public FilteringMemberHandler getFilteringMemberHandler()
    {
        return filteringInventoryHandler;
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
                worldObj.setBlockTileEntity(xCoord, yCoord, zCoord, new TileFilteringIronChest(IronChestType.values()[l]));
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
        blacklist = nbttagcompound.getBoolean("blacklist");
    }

    @Override
    public void writeToNBT(NBTTagCompound nbttagcompound)
    {
        nbttagcompound.setByte("type", (byte) getType().ordinal());
        super.writeToNBT(nbttagcompound);
        filter.writeToNBT(nbttagcompound);
        nbttagcompound.setBoolean("blacklist", blacklist);
    }
}
