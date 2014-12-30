package com.dynious.refinedrelocation.container;

import com.dynious.refinedrelocation.container.slot.SlotPhantom;
import com.dynious.refinedrelocation.grid.relocator.RelocatorModuleStock;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerModuleStock extends ContainerPhantom
{
    private RelocatorModuleStock inventory;

    public ContainerModuleStock(EntityPlayer player, RelocatorModuleStock inventory)
    {
        this.inventory = inventory;
        inventory.openInventory();

        for (int y = 0; y < 3; y++)
        {
            for (int x = 0; x < 3; x++)
            {
                this.addSlotToContainer(new SlotPhantom(inventory, y * 3 + x, 62 + x * 18, 17 + y * 18));
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
        return true;
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int par2)
    {
        return null;
    }

    @Override
    public void onContainerClosed(EntityPlayer player)
    {
        super.onContainerClosed(player);
        this.inventory.closeInventory();
    }
}
