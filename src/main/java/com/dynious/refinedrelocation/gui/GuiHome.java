package com.dynious.refinedrelocation.gui;

import com.dynious.refinedrelocation.api.APIUtils;
import com.dynious.refinedrelocation.api.relocator.IItemRelocator;
import com.dynious.refinedrelocation.api.relocator.IRelocatorModule;
import com.dynious.refinedrelocation.grid.relocator.RelocatorMultiModule;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;

import java.util.List;

public class GuiHome extends GuiModular
{
    private RelocatorMultiModule multiModule;
    private List<IRelocatorModule> modules;
    private IItemRelocator relocator;
    private EntityPlayer player;
    private int side;

    public GuiHome(RelocatorMultiModule multiModule, List<IRelocatorModule> iRelocatorModules, IItemRelocator relocator, EntityPlayer player, int side)
    {
        super(new Container()
        {
            @Override
            public boolean canInteractWith(EntityPlayer p_75145_1_)
            {
                return true;
            }
        });
        this.multiModule = multiModule;
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
            new GuiButtonOpenModuleGUI(this, multiModule, module, relocator, side, player, module.getDrops(relocator, side).get(0).getDisplayName());
        }
    }

    @Override
    public void onGuiClosed()
    {
        multiModule.setCurrentModule(-1);
//        APIUtils.openRelocatorModuleGUI(relocator, player, side);
    }
}
