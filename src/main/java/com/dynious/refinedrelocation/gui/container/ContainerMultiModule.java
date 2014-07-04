package com.dynious.refinedrelocation.gui.container;

import com.dynious.refinedrelocation.api.APIUtils;
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
        if (multiModule.getCurrentModule().getContainer(relocator, side, player) == null)
        {
            multiModule.getCurrentModule().onActivated(relocator, player, side, player.getCurrentEquippedItem());
        }
        else
        {
            APIUtils.openRelocatorModuleGUI(relocator, player, side);
        }
    }
}
