package com.dynious.refinedrelocation.container.slot;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;

public class SlotPhantom extends Slot
{

    public SlotPhantom(IInventory inventory, int index, int xPos, int yPos)
    {
        super(inventory, index, xPos, yPos);
    }

    public void setSlotIndex(int index)
    {
        this.slotIndex = index;
    }
}
