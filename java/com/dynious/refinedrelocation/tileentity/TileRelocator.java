package com.dynious.refinedrelocation.tileentity;

import com.dynious.refinedrelocation.api.filter.IFilter;
import com.dynious.refinedrelocation.api.tileentity.IRelocator;
import com.dynious.refinedrelocation.grid.relocator.RelocatorGrid;
import com.dynious.refinedrelocation.grid.relocator.TravellingItem;
import com.dynious.refinedrelocation.helper.DirectionHelper;
import com.dynious.refinedrelocation.helper.IOHelper;
import com.dynious.refinedrelocation.network.PacketTypeHandler;
import com.dynious.refinedrelocation.network.packet.PacketItemList;
import cpw.mods.fml.common.network.PacketDispatcher;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class TileRelocator extends TileEntity implements IRelocator, ISidedInventory
{
    public boolean blocksChanged = true;

    private TileEntity[] inventories = new TileEntity[ForgeDirection.VALID_DIRECTIONS.length];
    private IRelocator[] relocators = new IRelocator[ForgeDirection.VALID_DIRECTIONS.length];
    private IFilter[] filters = new IFilter[ForgeDirection.VALID_DIRECTIONS.length];

    private TravellingItem cachedTravellingItem;
    private int maxStackSize = 64;

    private List<TravellingItem> items = new ArrayList<TravellingItem>();
    private List<TravellingItem> itemsToAdd = new ArrayList<TravellingItem>();

    @Override
    public void updateEntity()
    {
        super.updateEntity();

        if (blocksChanged)
        {
            inventories = new TileEntity[ForgeDirection.VALID_DIRECTIONS.length];
            relocators = new IRelocator[ForgeDirection.VALID_DIRECTIONS.length];

            for (ForgeDirection direction : ForgeDirection.VALID_DIRECTIONS)
            {
                TileEntity tile = DirectionHelper.getTileAtSide(this, direction);
                if (tile != null)
                {
                    if (!(tile instanceof IRelocator))
                    {
                        if (IOHelper.canInterfaceWith(tile, direction.getOpposite()))
                            inventories[direction.ordinal()] = tile;
                    }
                    else
                    {
                        relocators[direction.ordinal()] = (IRelocator) tile;
                    }
                }
            }
            blocksChanged = false;
        }

        if (worldObj.isRemote)
        {
            clientSideUpdate();
        }
        else
        {
            serverSideUpdate();
        }
    }

    private void serverSideUpdate()
    {
        if (!itemsToAdd.isEmpty())
        {
            items.addAll(itemsToAdd);
            PacketDispatcher.sendPacketToAllAround(xCoord, yCoord, zCoord, 64, worldObj.provider.dimensionId, PacketTypeHandler.populatePacket(new PacketItemList(this, itemsToAdd)));
            itemsToAdd.clear();
        }

        for (Iterator<TravellingItem> iterator = items.iterator(); iterator.hasNext(); )
        {
            TravellingItem item = iterator.next();
            item.counter++;
            if (item.counter > TravellingItem.timePerRelocator)
            {
                iterator.remove();
                outputToSide(item, item.onOutput());
            }
        }

        cachedTravellingItem = null;
        maxStackSize = 64;
    }

    private void clientSideUpdate()
    {
        for (Iterator<TravellingItem> iterator = items.iterator(); iterator.hasNext(); )
        {
            TravellingItem item = iterator.next();
            item.counter++;
            if (item.counter > TravellingItem.timePerRelocator)
            {
                iterator.remove();
            }
        }
    }

    @Override
    public IRelocator[] getConnectedRelocators()
    {
        return relocators;
    }

    @Override
    public boolean passesFilter(ItemStack itemStack, int side)
    {
        return filters[side] == null || filters[side].passesFilter(itemStack);
    }

    @Override
    public TileEntity[] getConnectedInventories()
    {
        return inventories;
    }

    public List<TravellingItem> getItems(boolean includeItemsToAdd)
    {
        if (includeItemsToAdd)
        {
            List<TravellingItem> list = new ArrayList<TravellingItem>(items);
            list.addAll(itemsToAdd);
            return list;
        }
        return items;
    }

    @Override
    public void receiveTravellingItem(TravellingItem item)
    {
        if (worldObj.isRemote)
        {
            if (!item.getPath().isEmpty())
                items.add(item);
        }
        else
        {
            if (item.getPath().isEmpty())
            {
                retryOutput(item, item.input);
            }
            itemsToAdd.add(item);
        }
    }

    public void outputToSide(TravellingItem item, byte side)
    {
        if (getConnectedRelocators()[side] != null)
        {
            getConnectedRelocators()[side].receiveTravellingItem(item);
        }
        else if (getConnectedInventories()[side] != null)
        {
            ItemStack stack = IOHelper.insert(getConnectedInventories()[side], item.getItemStack().copy(), ForgeDirection.getOrientation(side).getOpposite(), false);
            if (stack != null)
            {
                item.getItemStack().stackSize = stack.stackSize;
                retryOutput(item, side);
            }
        }
        else
        {
            retryOutput(item, side);
        }
    }

    public void retryOutput(TravellingItem item, byte side)
    {
        ItemStack stack = item.getItemStack().copy();
        TravellingItem travellingItem = RelocatorGrid.findOutput(item.getItemStack(), this, side);
        if (travellingItem != null)
        {
            travellingItem.setStartingPoint(item.getStartingPoint());
            receiveTravellingItem(travellingItem);
            stack.stackSize -= item.getStackSize();
            if (stack.stackSize <= 0)
            {
                stack = null;
            }
        }
        if (stack != null)
        {
            System.out.println(stack);
            //GO BACK!
        }
    }

    @Override
    public ItemStack insert(ItemStack itemStack, int side, boolean simulate)
    {
        if (passesFilter(itemStack, side))
        {
            TravellingItem item = RelocatorGrid.findOutput(itemStack.copy(), this, side);
            if (item != null)
            {
                if (!simulate)
                {
                    receiveTravellingItem(item);
                }
                itemStack.stackSize -= item.getStackSize();
                if (itemStack.stackSize <= 0)
                {
                    return null;
                }
            }
        }
        return itemStack;
    }

    public boolean connectsToSide(int side)
    {
        return relocators[side] != null || inventories[side] != null;
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
            cachedTravellingItem = RelocatorGrid.findOutput(itemStack, this, side);
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
        if (side == cachedTravellingItem.input && cachedTravellingItem.isItemSameAs(itemstack))
        {
            receiveTravellingItem(cachedTravellingItem);
        }
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
        return maxStackSize;
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer entityplayer)
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
    public boolean isItemValidForSlot(int slot, ItemStack itemstack)
    {
        return true;
    }
}
