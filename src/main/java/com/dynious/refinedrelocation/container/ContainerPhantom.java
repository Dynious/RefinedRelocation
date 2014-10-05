package com.dynious.refinedrelocation.container;

import com.dynious.refinedrelocation.helper.ItemStackHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public abstract class ContainerPhantom extends Container
{
    protected boolean allowStackSizes = true;

    @Override
    public ItemStack slotClick(int slotNum, int mouseButton, int modifier, EntityPlayer player)
    {
        Slot slot = slotNum < 0 ? null : (Slot) this.inventorySlots.get(slotNum);
        if (slot instanceof SlotUntouchable)
        {
            return null;
        }
        else if (slot instanceof SlotPhantom)
        {
            return (allowStackSizes ? slotClickPhantom(slot, mouseButton, modifier, player) : slotClickPhantom(slot, mouseButton, player));
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

    private ItemStack slotClickPhantom(Slot slot, int mouseButton, int modifier, EntityPlayer player)
    {
        ItemStack stack = null;

        if (mouseButton == 2)
        {
            slot.putStack(null);
        }
        else if (mouseButton == 0 || mouseButton == 1)
        {
            InventoryPlayer playerInv = player.inventory;
            slot.onSlotChanged();
            ItemStack stackSlot = slot.getStack();
            ItemStack stackHeld = playerInv.getItemStack();

            if (stackSlot != null)
            {
                stack = stackSlot.copy();
            }

            if (stackSlot == null)
            {
                if (stackHeld != null && slot.isItemValid(stackHeld))
                {
                    fillPhantomSlot(slot, stackHeld, mouseButton, modifier);
                }
            }
            else if (stackHeld == null)
            {
                adjustPhantomSlot(slot, mouseButton, modifier);
                slot.onPickupFromSlot(player, playerInv.getItemStack());
            }
            else if (slot.isItemValid(stackHeld))
            {
                if (ItemStackHelper.areItemStacksEqual(stackSlot, stackHeld))
                {
                    adjustPhantomSlot(slot, mouseButton, modifier);
                }
                else
                {
                    fillPhantomSlot(slot, stackHeld, mouseButton, modifier);
                }
            }
        }
        return stack;
    }

    protected void adjustPhantomSlot(Slot slot, int mouseButton, int modifier)
    {
        ItemStack stackSlot = slot.getStack();
        int stackSize;
        if (modifier == 1)
        {
            stackSize = mouseButton == 0 ? (stackSlot.stackSize + 1) / 2 : stackSlot.stackSize * 2;
        }
        else
        {
            stackSize = mouseButton == 0 ? stackSlot.stackSize - 1 : stackSlot.stackSize + 1;
        }

        if (stackSize > slot.getSlotStackLimit())
        {
            stackSize = slot.getSlotStackLimit();
        }

        stackSlot.stackSize = stackSize;

        if (stackSlot.stackSize <= 0)
        {
            slot.putStack(null);
        }
    }

    protected void fillPhantomSlot(Slot slot, ItemStack stackHeld, int mouseButton, int modifier)
    {
        int stackSize = mouseButton == 0 ? stackHeld.stackSize : 1;
        if (stackSize > slot.getSlotStackLimit())
        {
            stackSize = slot.getSlotStackLimit();
        }
        ItemStack phantomStack = stackHeld.copy();
        phantomStack.stackSize = stackSize;

        slot.putStack(phantomStack);
    }

}
