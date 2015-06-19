package com.dynious.refinedrelocation.container;

import com.dynious.refinedrelocation.container.slot.SlotGhost;
import com.dynious.refinedrelocation.container.slot.SlotUntouchable;
import com.dynious.refinedrelocation.grid.relocator.RelocatorModuleCrafting;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerModuleCrafting extends ContainerPhantom
{
    private RelocatorModuleCrafting module;
    private boolean initialUpdate = true;
    private boolean isStuffed = false;
    private int lastCraftStack = 64;
    private int lastCraftTick = 0;

    public ContainerModuleCrafting(EntityPlayer player, RelocatorModuleCrafting module)
    {
        this.module = module;
        allowStackSizes = false;

        this.addSlotToContainer(new SlotUntouchable(module.CRAFT_RESULT, 0, 106, 35));

        for (int y = 0; y < 3; y++)
        {
            for (int x = 0; x < 3; x++)
            {
                this.addSlotToContainer(new SlotGhost(module.CRAFT_MATRIX, y * 3 + x, 12 + x * 18, 17 + y * 18));
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

        if (module.getMaxCraftStack() != lastCraftStack || initialUpdate)
        {
            for (Object crafter : crafters)
            {
                ((ICrafting) crafter).sendProgressBarUpdate(this, 0, module.getMaxCraftStack());
            }
            lastCraftStack = module.getMaxCraftStack();
        }
        if ((module.outputStack == null) == isStuffed || initialUpdate)
        {
            for (Object crafter : crafters)
            {
                ((ICrafting) crafter).sendProgressBarUpdate(this, 1, (module.outputStack != null) ? 1 : 0);
            }
            isStuffed = (module.outputStack != null);
        }
        if (module.craftTick != lastCraftTick || initialUpdate)
        {
            for (Object crafter : crafters)
            {
                ((ICrafting) crafter).sendProgressBarUpdate(this, 2, module.craftTick);
            }
            lastCraftTick = module.craftTick;
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
                setMaxCraftStack(value);
                break;
            case 1:
                module.isStuffed = value == 1;
                break;
            case 2:
                module.craftTick = value;
                break;
        }
    }

    public void setMaxCraftStack(int size)
    {
        module.setMaxCraftStack(size);
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

    @Override
    public void onMessageInteger(int messageId, int value, EntityPlayer player)
    {
        if (messageId == 0)
        {
            setMaxCraftStack(value);
        }
    }

}
