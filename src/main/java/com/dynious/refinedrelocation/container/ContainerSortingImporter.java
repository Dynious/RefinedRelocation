package com.dynious.refinedrelocation.container;

import com.dynious.refinedrelocation.container.slot.SlotPhantom;
import com.dynious.refinedrelocation.tileentity.TileSortingImporter;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerSortingImporter extends ContainerPhantom
{
    private TileSortingImporter inventory;
    private int page = 0;

    public ContainerSortingImporter(EntityPlayer player, TileSortingImporter inventory)
    {
        allowStackSizes = false;
        this.inventory = inventory;
        inventory.openInventory();
        inventory.addCrafter(player);

        this.addSlotToContainer(new Slot(inventory, 0, 26, 35));

        for (int y = 0; y < 3; y++)
        {
            for (int x = 0; x < 3; x++)
            {
                this.addSlotToContainer(new SlotPhantom(inventory, 1 + y * 3 + x, 80 + x * 18, 17 + y * 18));
            }
        }

        for (int y = 0; y < 3; ++y)
        {
            for (int x = 0; x < 9; ++x)
            {
                this.addSlotToContainer(new Slot(player.inventory, x + y * 9 + 9, 8 + x * 18, 84 + y * 18));
            }
        }

        for (int x = 0; x < 9; ++x)
        {
            this.addSlotToContainer(new Slot(player.inventory, x, 8 + x * 18, 142));
        }
    }

    @Override
    public boolean canInteractWith(EntityPlayer entityplayer)
    {
        return this.inventory.isUseableByPlayer(entityplayer);
    }

    @Override
    public void onContainerClosed(EntityPlayer par1EntityPlayer)
    {
        super.onContainerClosed(par1EntityPlayer);
        this.inventory.closeInventory();
        this.inventory.removeCrafter(par1EntityPlayer);
    }

    public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int par2)
    {
        Slot slot = getSlot(par2);
        if (slot != null && slot.getHasStack())
        {
            ItemStack stack = slot.getStack().copy();
            if (par2 == 0)
            {
                if (!mergeItemStack(slot.getStack(), 9, this.inventorySlots.size(), true))
                {
                    return null;
                }
                else
                {
                    if (slot.getStack().stackSize == 0)
                    {
                        putStackInSlot(par2, null);
                    }
                    else
                    {
                        slot.onSlotChanged();
                    }
                    return stack;
                }
            }
            else if (par2 >= 9)
            {
                if (!mergeItemStack(slot.getStack(), 0, 1, true))
                {
                    return null;
                }
                else
                {
                    if (slot.getStack().stackSize == 0)
                    {
                        slot.putStack(null);
                    }
                    else
                    {
                        slot.onSlotChanged();
                    }
                    return stack;
                }
            }
        }
        return null;
    }

    @Override
    public void putStackInSlot(int par1, ItemStack par2ItemStack)
    {
        if (par1 == 0)
        {
            inventory.bufferInventory[0] = par2ItemStack;
        }
        else
        {
            super.putStackInSlot(par1, par2ItemStack);
        }
    }

    public void nextPage()
    {
        if (((SlotPhantom) inventorySlots.get(1)).getSlotIndex() + 8 > inventory.getItemListSize())
        {
            return;
        }

        for (int i = 1; i < 10; i++)
        {
            SlotPhantom slot = (SlotPhantom) inventorySlots.get(i);
            slot.setSlotIndex(slot.getSlotIndex() + 9);
        }
        page++;
    }

    public void previousPage()
    {
        if (((SlotPhantom) inventorySlots.get(1)).getSlotIndex() - 9 < 0)
        {
            return;
        }
        for (int i = 1; i < 10; i++)
        {
            SlotPhantom slot = (SlotPhantom) inventorySlots.get(i);
            slot.setSlotIndex(slot.getSlotIndex() - 9);
        }
        page--;
    }

    public int getPage()
    {
        return page;
    }
}
