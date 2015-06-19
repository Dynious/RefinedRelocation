package com.dynious.refinedrelocation.container;

import com.dynious.refinedrelocation.api.relocator.IItemRelocator;
import com.dynious.refinedrelocation.grid.relocator.RelocatorMultiModule;
import net.minecraft.entity.player.EntityPlayer;

public class ContainerMultiModule extends ContainerHierarchical
{
    private RelocatorMultiModule multiModule;
    private IItemRelocator relocator;
    private EntityPlayer player;
    private int side;

    public ContainerMultiModule(RelocatorMultiModule module, IItemRelocator relocator, EntityPlayer player, int side)
    {
        this.multiModule = module;
        this.relocator = relocator;
        this.player = player;
        this.side = side;
    }

    @Override
    public boolean canInteractWith(EntityPlayer player)
    {
        return true;
    }

    public void openOrActive(int index)
    {
        multiModule.setCurrentModule(index);
        multiModule.getCurrentModule().onActivated(relocator, player, side, player.getCurrentEquippedItem());
    }

    @Override
    public void onContainerClosed(EntityPlayer p_75134_1_)
    {
        multiModule.setCurrentModule(-1);
    }

    @Override
    public void onMessageAction(int messageId, EntityPlayer player)
    {
        openOrActive(messageId);
    }

}
