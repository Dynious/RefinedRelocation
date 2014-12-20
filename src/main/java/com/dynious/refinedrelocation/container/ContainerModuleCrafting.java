package com.dynious.refinedrelocation.container;

import com.dynious.refinedrelocation.grid.relocator.RelocatorModuleCrafting;
import cpw.mods.fml.common.FMLCommonHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerModuleCrafting extends ContainerPhantom
{
    private RelocatorModuleCrafting module;
    private boolean initialUpdate = true;
    private boolean isStuffed = false;

    public ContainerModuleCrafting(EntityPlayer player, RelocatorModuleCrafting module)
    {
        this.module = module;
        allowStackSizes = false;

        this.addSlotToContainer(new SlotUntouchable(module.CRAFT_RESULT, 0, 124, 35));

        for (int y = 0; y < 3; y++)
        {
            for (int x = 0; x < 3; x++)
            {
                this.addSlotToContainer(new SlotGhost(module.CRAFT_MATRIX, y * 3 + x, 30 + x * 18, 17 + y * 18));
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

        this.onCraftMatrixChanged(module.CRAFT_MATRIX);
    }

    @Override
    public void detectAndSendChanges()
    {
        super.detectAndSendChanges();

        if ((module.outputStack == null) == isStuffed || initialUpdate)
        {
            for (Object crafter : crafters)
            {
                ((ICrafting) crafter).sendProgressBarUpdate(this, 0, (module.outputStack != null) ? 1 : 0);
            }
            isStuffed = (module.outputStack != null);
        }

        if (initialUpdate)
            initialUpdate = false;
    }

    @Override
    public void updateProgressBar(int id, int value)
    {
        switch (id)
        {
            case 0:
                module.isStuffed = value == 1;
                break;
        }
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
