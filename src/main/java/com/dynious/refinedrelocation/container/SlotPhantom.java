package com.dynious.refinedrelocation.container;

import cpw.mods.fml.relauncher.ReflectionHelper;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;

import java.lang.reflect.Field;

public class SlotPhantom extends Slot
{
    private static Field slotIndex = ReflectionHelper.findField(Slot.class, "slotIndex", "a", "field_75225_a");

    public SlotPhantom(IInventory inventory, int index, int xPos, int yPos)
    {
        super(inventory, index, xPos, yPos);
    }

    public void setSlotIndex(int index)
    {
        try
        {
            slotIndex.set(this, index);
        } catch (IllegalAccessException e)
        {
            e.printStackTrace();
        }
    }
}
