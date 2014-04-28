package com.dynious.refinedrelocation.tileentity;

import com.dynious.refinedrelocation.api.APIUtils;
import com.dynious.refinedrelocation.api.filter.IFilterGUI;
import com.dynious.refinedrelocation.api.tileentity.IFilterTileGUI;
import com.dynious.refinedrelocation.api.tileentity.ISortingInventory;
import com.dynious.refinedrelocation.api.tileentity.ISortingMember;
import com.dynious.refinedrelocation.api.tileentity.handlers.ISortingInventoryHandler;
import com.dynious.refinedrelocation.helper.DirectionHelper;
import com.dynious.refinedrelocation.helper.IOHelper;
import com.dynious.refinedrelocation.lib.Names;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet132TileEntityData;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

public class TileSortingInterface extends TileSortingConnector implements ISortingInventory, IFilterTileGUI
{
    private ISortingInventoryHandler sortingHandler = APIUtils.createSortingInventoryHandler(this);
    private IFilterGUI filter = APIUtils.createStandardFilter();
    public ItemStack[] bufferInventory = new ItemStack[1];
    private int counter;
    private ForgeDirection connectedSide = ForgeDirection.UNKNOWN;

    @Override
    public ISortingInventoryHandler getSortingHandler()
    {
        return sortingHandler;
    }

    @Override
    public ItemStack[] getInventory()
    {
        return bufferInventory;
    }

    @Override
    public ItemStack putInInventory(ItemStack itemStack)
    {
        if (connectedSide != ForgeDirection.UNKNOWN)
        {
            TileEntity tile = DirectionHelper.getTileAtSide(worldObj, xCoord, yCoord, zCoord, connectedSide);
            if (tile != null && !(tile instanceof ISortingMember))
            {
                itemStack = IOHelper.insert(tile, itemStack, connectedSide.getOpposite());
                if (itemStack == null || itemStack.stackSize == 0)
                    return null;
            }
        }
        return itemStack;
    }

    @Override
    public final boolean putStackInSlot(ItemStack itemStack, int slotIndex)
    {
        itemStack = putInInventory(itemStack);
        if (itemStack != null)
        {
            bufferInventory[0] = itemStack;
        }
        return true;
    }

    @Override
    public Priority getPriority()
    {
        return filter.isBlacklisting() ? Priority.LOW : Priority.NORMAL;
    }

    @Override
    public IFilterGUI getFilter()
    {
        return filter;
    }

    @Override
    public void updateEntity()
    {
        super.updateEntity();
        if (bufferInventory[0] != null)
        {
            counter++;
            if (counter % 22 == 0)
            {
                ItemStack stack = bufferInventory[0].copy();
                bufferInventory[0] = null;
                sortingHandler.setInventorySlotContents(0, stack);
            }
        }
    }

    @Override
    public int getSizeInventory()
    {
        return 1;
    }

    @Override
    public ItemStack getStackInSlot(int i)
    {
        return bufferInventory[i];
    }

    @Override
    public ItemStack decrStackSize(int slotIndex, int decrementAmount)
    {
        ItemStack itemStack = getStackInSlot(slotIndex);
        if (itemStack != null)
        {
            if (itemStack.stackSize <= decrementAmount)
            {
                setInventorySlotContents(slotIndex, null);
            }
            else
            {
                itemStack = itemStack.splitStack(decrementAmount);
                if (itemStack.stackSize == 0)
                {
                    setInventorySlotContents(slotIndex, null);
                }
            }
        }

        return itemStack;
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int slotIndex)
    {
        ItemStack itemStack = getStackInSlot(slotIndex);
        if (itemStack != null)
        {
            setInventorySlotContents(slotIndex, null);
        }
        return itemStack;
    }

    @Override
    public void setInventorySlotContents(int slotIndex, ItemStack itemStack)
    {
        sortingHandler.setInventorySlotContents(slotIndex, itemStack);
    }

    @Override
    public String getInventoryName()
    {
        return Names.sortingInterface;
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
    public boolean isUseableByPlayer(EntityPlayer entityplayer)
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
    public boolean isItemValidForSlot(int i, ItemStack itemstack)
    {
        return bufferInventory[0] == null;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound)
    {
        super.readFromNBT(compound);
        filter.readFromNBT(compound);
        setConnectedSide(ForgeDirection.getOrientation(compound.getByte("side")));
        if (compound.hasKey("Items"))
        {
            NBTTagList tagList = compound.getTagList("Items", 10);
            this.bufferInventory[0] = ItemStack.loadItemStackFromNBT(tagList.getCompoundTagAt(0));
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound compound)
    {
        super.writeToNBT(compound);
        filter.writeToNBT(compound);
        compound.setByte("side", (byte) connectedSide.ordinal());
        if (bufferInventory[0] != null)
        {
            NBTTagList nbttaglist = new NBTTagList();
            NBTTagCompound tag = new NBTTagCompound();
            this.bufferInventory[0].writeToNBT(tag);
            nbttaglist.appendTag(tag);
            compound.setTag("Items", nbttaglist);
        }
    }

    @Override
    public void onDataPacket(INetworkManager net, Packet132TileEntityData pkt)
    {
        super.onDataPacket(net, pkt);
        setConnectedSide(ForgeDirection.getOrientation(pkt.data.getByte("side")));
    }

    @Override
    public Packet getDescriptionPacket()
    {
        Packet132TileEntityData pkt = (Packet132TileEntityData) super.getDescriptionPacket();
        pkt.data.setByte("side", (byte) connectedSide.ordinal());
        return pkt;
    }

    public boolean rotateBlock()
    {
        setConnectedSide(ForgeDirection.getOrientation((connectedSide.ordinal() + 1) % ForgeDirection.VALID_DIRECTIONS.length));
        return true;
    }

    public void setConnectedSide(ForgeDirection direction)
    {
        this.connectedSide = direction;
        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
    }

    public ForgeDirection getConnectedSide()
    {
        return connectedSide;
    }
}
