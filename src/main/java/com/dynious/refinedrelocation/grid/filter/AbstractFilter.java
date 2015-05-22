package com.dynious.refinedrelocation.grid.filter;

import com.dynious.refinedrelocation.grid.MultiFilter;
import com.dynious.refinedrelocation.network.NetworkHandler;
import com.dynious.refinedrelocation.network.packet.filter.MessageSetFilterType;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public abstract class AbstractFilter {

    public static final byte TYPE_CUSTOM = 0;
    public static final byte TYPE_PRESET = 1;
    public static final byte TYPE_CREATIVETAB = 2;

    private final byte typeId;
    protected final MultiFilter parent;
    protected final int filterIndex;
    private boolean isDirty;

    public AbstractFilter(byte typeId, MultiFilter parent, int index) {
        this.typeId = typeId;
        this.parent = parent;
        this.filterIndex = index;
    }

    public byte getTypeId() {
        return typeId;
    }

    public abstract boolean isInFilter(ItemStack itemStack);
    public abstract void writeToNBT(NBTTagCompound compound);
    public abstract void readFromNBT(NBTTagCompound compound);

    public void sendInitialUpdate(EntityPlayerMP playerMP) {
        NetworkHandler.INSTANCE.sendTo(new MessageSetFilterType(filterIndex, typeId), playerMP);
        sendUpdate(playerMP);
    }

    public abstract void sendUpdate(EntityPlayerMP playerMP);

    public void setFilterString(int optionId, String value) {}
    public void setFilterBoolean(int optionId, boolean value) {}

    public int getFilterIndex() {
        return filterIndex;
    }

    public void markDirty(boolean dirty) {
        isDirty = dirty;
        parent.filterChanged();
    }

    public boolean isDirty() {
        return isDirty;
    }

}
