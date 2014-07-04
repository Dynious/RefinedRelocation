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
import net.minecraftforge.common.ForgeDirection;

public class TileSortingInterface extends TileSortingConnector implements ISortingInventory, IFilterTileGUI
{
    private ISortingInventoryHandler sortingHandler = APIUtils.createSortingInventoryHandler(this);
    private IFilterGUI filter = APIUtils.createStandardFilter(this);
    public ItemStack[] bufferInventory = new ItemStack[1];
    private boolean isStuffed = false;
    private int counter;
    private ForgeDirection connectedSide = ForgeDirection.UNKNOWN;
    private Priority priority = Priority.NORMAL;

    @Override
    public ISortingInventoryHandler getHandler()
    {
        return sortingHandler;
    }

    @Override
    public ItemStack putInInventory(ItemStack itemStack)
    {
        if (connectedSide != ForgeDirection.UNKNOWN)
        {
            TileEntity tile = DirectionHelper.getTileAtSide(worldObj, xCoord, yCoord, zCoord, connectedSide);
            if (tile != null && !(tile instanceof ISortingMember))
            {
                itemStack = IOHelper.insert(tile, itemStack, connectedSide.getOpposite(), false);
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
        bufferInventory[0] = itemStack;
        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
        return true;
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
    public IFilterGUI getFilter()
    {
        return filter;
    }

    @Override
    public void onFilterChanged()
    {
        this.onInventoryChanged();
    }

    @Override
    public TileEntity getTileEntity()
    {
        return this;
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
                bufferInventory[0] = putInInventory(bufferInventory[0]);
                if (bufferInventory[0] == null)
                {
                    worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
                }
            }
        }
    }

    public boolean isStuffed()
    {
        return isStuffed;
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
    public String getInvName()
    {
        return Names.sortingInterface;
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
        if (compound.hasKey("priority"))
        {
            setPriority(Priority.values()[compound.getByte("priority")]);
        }
        else
        {
            setPriority(filter.isBlacklisting() ? Priority.LOW : Priority.NORMAL);
        }
        if (compound.hasKey("Items"))
        {
            NBTTagList tagList = compound.getTagList("Items");
            this.bufferInventory[0] = ItemStack.loadItemStackFromNBT((NBTTagCompound) tagList.tagAt(0));
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound compound)
    {
        super.writeToNBT(compound);
        filter.writeToNBT(compound);
        compound.setByte("side", (byte) connectedSide.ordinal());
        compound.setByte("priority", (byte) priority.ordinal());
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
        isStuffed = pkt.data.getBoolean("stuffed");
    }

    @Override
    public Packet getDescriptionPacket()
    {
        Packet132TileEntityData pkt = (Packet132TileEntityData) super.getDescriptionPacket();
        pkt.data.setByte("side", (byte) connectedSide.ordinal());
        pkt.data.setBoolean("stuffed", bufferInventory[0] != null);
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
        if (worldObj != null)
            worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
    }

    public ForgeDirection getConnectedSide()
    {
        return connectedSide;
    }
}
