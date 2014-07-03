package com.dynious.refinedrelocation.gui;

import com.dynious.refinedrelocation.api.relocator.IItemRelocator;
import com.dynious.refinedrelocation.api.relocator.IRelocatorModule;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;

import java.util.List;

public class GuiHome extends GuiModular
{
    private List<IRelocatorModule> modules;
    private IItemRelocator relocator;
    private EntityPlayer player;
    private int side;

    public GuiHome(List<IRelocatorModule> iRelocatorModules, IItemRelocator relocator, EntityPlayer player, int side)
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

        for (IRelocatorModule module : modules)
        {
            new GuiButtonOpenModuleGUI(this, module, relocator, side, player, module.getDrops(relocator, side).get(0).getDisplayName());
        }
    }
}
