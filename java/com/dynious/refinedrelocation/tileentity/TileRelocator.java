package com.dynious.refinedrelocation.tileentity;

import com.dynious.refinedrelocation.api.filter.IFilter;
import com.dynious.refinedrelocation.api.tileentity.IRelocator;
import com.dynious.refinedrelocation.api.tileentity.handlers.IRelocatorHandler;
import com.dynious.refinedrelocation.grid.relocator.TravellingItem;
import com.dynious.refinedrelocation.helper.DirectionHelper;
import com.dynious.refinedrelocation.grid.relocator.RelocatorHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TileRelocator extends TileEntity implements IRelocator, ISidedInventory
{
    private boolean isFirstTick = true;
    private boolean blocksChanged = true;

    private IRelocatorHandler handler = new RelocatorHandler(this);
    private TileEntity[] inventories = new TileEntity[ForgeDirection.VALID_DIRECTIONS.length];
    private IRelocator[] relocators = new IRelocator[ForgeDirection.VALID_DIRECTIONS.length];
    private IFilter[] filters = new IFilter[ForgeDirection.VALID_DIRECTIONS.length];

    private TravellingItem cachedTravellingItem;
    private int maxStackSize = 64;

    private List<TravellingItem> items = new ArrayList<TravellingItem>();

    public void updateEntity()
    {
        super.updateEntity();

        if (isFirstTick)
        {
            handler.onTileAdded();

            isFirstTick = false;
        }

        if (blocksChanged)
        {
            inventories = new TileEntity[ForgeDirection.VALID_DIRECTIONS.length];

            for (ForgeDirection direction : ForgeDirection.VALID_DIRECTIONS)
            {
                inventories[direction.ordinal()] = DirectionHelper.getTileAtSide(this, direction);
            }
            blocksChanged = false;
        }

        cachedTravellingItem = null;
        maxStackSize = 64;
    }

    @Override
    public boolean isDecisionPoint()
    {
        return hasConnectedInventories();
    }

    private boolean hasConnectedInventories()
    {
        for (TileEntity inventory : inventories)
        {
            if (inventory != null)
                return true;
        }
        return false;
    }

    public IRelocator[] getConnectedRelocators()
    {
        return relocators;
    }

    public boolean passesFilter(ItemStack itemStack, int side)
    {
        return filters[side] == null || filters[side].passesFilter(itemStack);
    }

    public TileEntity[] getConnectedInventories()
    {
        return inventories;
    }

    @Override
    public IRelocatorHandler getHandler()
    {
        return handler;
    }

    @Override
    public void onChunkUnload()
    {
        handler.onTileRemoved();
        super.onChunkUnload();
    }

    @Override
    public void invalidate()
    {
        handler.onTileRemoved();
        super.invalidate();
    }

    /*
    ISidedInventory implementation
     */

    @Override
    public int[] getAccessibleSlotsFromSide(int side)
    {
        return new int[] { side };
    }

    @Override
    public boolean canInsertItem(int slot, ItemStack itemStack, int side)
    {
        if (passesFilter(itemStack, side))
        {
            cachedTravellingItem = getHandler().getGrid().findOutput(itemStack, this);
            if (cachedTravellingItem != null)
            {
                maxStackSize = cachedTravellingItem.getStackSize();
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean canExtractItem(int slot, ItemStack itemstack, int side)
    {
        return false;
    }

    @Override
    public int getSizeInventory()
    {
        return 6;
    }

    @Override
    public ItemStack getStackInSlot(int i)
    {
        return null;
    }

    @Override
    public ItemStack decrStackSize(int i, int j)
    {
        return null;
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int i)
    {
        return null;
    }

    @Override
    public void setInventorySlotContents(int side, ItemStack itemstack)
    {
        if (cachedTravellingItem.isItemSameAs(itemstack))
        {
            getHandler().getGrid().travelItem(cachedTravellingItem, int side);
        }
        //Input from side
    }

    @Override
    public String getInvName()
    {
        return "tile.relocator.name";
    }

    @Override
    public boolean isInvNameLocalized()
    {
        return false;
    }

    @Override
    public int getInventoryStackLimit()
    {
        int size = maxStackSize;
        maxStackSize = 64;
        return size;
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer entityplayer)
    {
        return false;
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
    public boolean isItemValidForSlot(int slot, ItemStack itemstack)
    {
        return true;
    }
}
