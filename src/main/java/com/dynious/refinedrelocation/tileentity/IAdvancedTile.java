package com.dynious.refinedrelocation.tileentity;

public interface IAdvancedTile {
    byte[] getInsertDirection();

    void setInsertDirection(int from, int value);

    byte getMaxStackSize();

    void setMaxStackSize(byte maxStackSize);

    boolean getSpreadItems();

    void setSpreadItems(boolean spreadItems);
}
