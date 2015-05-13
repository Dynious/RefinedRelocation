package com.dynious.refinedrelocation.grid.filter;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public abstract class AbstractFilter {

    public static final byte TYPE_CUSTOM = 0;
    public static final byte TYPE_PRESET = 1;
    public static final byte TYPE_CREATIVETAB = 2;

    private byte typeId;

    public AbstractFilter(byte typeId) {
        this.typeId = typeId;
    }

    public abstract boolean isInFilter(ItemStack itemStack);
    public abstract void writeToNBT(NBTTagCompound compound);
    public abstract void readFromNBT(NBTTagCompound compound);

    public byte getTypeId() {
        return typeId;
    }
}
