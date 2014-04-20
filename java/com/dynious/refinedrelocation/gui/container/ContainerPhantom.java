package com.dynious.refinedrelocation.gui.container;

import com.dynious.refinedrelocation.helper.ItemStackHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public abstract class ContainerPhantom extends Container
{
    @Override
    public ItemStack slotClick(int slotNum, int mouseButton, int modifier, EntityPlayer player)
    {
        Slot slot = slotNum < 0 ? null : (Slot) this.inventorySlots.get(slotNum);
        if (slot instanceof SlotPhantom)
        {
            return slotClickPhantom(slot, mouseButton, player);
        }
        return super.slotClick(slotNum, mouseButton, modifier, player);
    }

    private ItemStack slotClickPhantom(Slot slot, int mouseButton, EntityPlayer player)
    {
        if (mouseButton == 2)
        {
            slot.putStack(null);
        }
        else if (mouseButton == 0 || mouseButton == 1)
        {
            InventoryPlayer playerInv = player.inventory;
            ItemStack stackHeld = playerInv.getItemStack();
            if (stackHeld == null)
            {
                slot.putStack(null);
            }
            else
            {
                ItemStack stackSlot = slot.getStack();
                if (!ItemStackHelper.areItemStacksEqual(stackSlot, stackHeld))
                {
                    stackSlot = stackHeld.copy();
                    stackSlot.stackSize = 1;
                    slot.putStack(stackSlot);
                }
            }
        }
        return null;
    }

}
