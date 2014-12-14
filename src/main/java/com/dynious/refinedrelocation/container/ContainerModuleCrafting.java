package com.dynious.refinedrelocation.container;

import com.dynious.refinedrelocation.grid.relocator.RelocatorModuleCrafting;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerModuleCrafting extends ContainerPhantom
{
    private RelocatorModuleCrafting moduleCrafting;

    public ContainerModuleCrafting(EntityPlayer player, RelocatorModuleCrafting moduleCrafting)
    {
        this.moduleCrafting = moduleCrafting;
        allowStackSizes = false;

        this.addSlotToContainer(new SlotUntouchable(moduleCrafting.CRAFT_RESULT, 0, 134, 35));

        for (int y = 0; y < 3; y++)
        {
            for (int x = 0; x < 3; x++)
            {
                this.addSlotToContainer(new SlotGhost(moduleCrafting.CRAFT_MATRIX, y * 3 + x, 62 + x * 18, 17 + y * 18));
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

        this.onCraftMatrixChanged(moduleCrafting.CRAFT_MATRIX);
    }

    @Override
    public boolean canInteractWith(EntityPlayer p_75145_1_)
    {
        return true;
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int par2)
    {
        return null;
    }
}
