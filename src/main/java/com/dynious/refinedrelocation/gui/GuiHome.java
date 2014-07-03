package com.dynious.refinedrelocation.gui;

import com.dynious.refinedrelocation.api.relocator.IItemRelocator;
import com.dynious.refinedrelocation.api.relocator.IRelocatorModule;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;

public class GuiHome extends GuiModular
{
    private IRelocatorModule[] modules;
    private IItemRelocator relocator;
    private EntityPlayer player;
    private int side;

    public GuiHome(IRelocatorModule[] iRelocatorModules, IItemRelocator relocator, EntityPlayer player, int side)
    {
        super(new Container()
        {
            @Override
            public boolean canInteractWith(EntityPlayer p_75145_1_)
            {
                return true;
            }
        });
        this.modules = iRelocatorModules;
        this.relocator = relocator;
        this.player = player;
        this.side = side;
    }

    @Override
    public void initGui()
    {
        super.initGui();

        for (int i = 0; i < modules.length; i++)
        {
            new GuiButtonOpenModuleGUI(this, modules[i], relocator, side, player, modules[i].getDrops(relocator, side).get(0).getDisplayName());
        }
    }
}
