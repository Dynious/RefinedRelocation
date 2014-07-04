package com.dynious.refinedrelocation.gui;

import com.dynious.refinedrelocation.api.relocator.IItemRelocator;
import com.dynious.refinedrelocation.api.relocator.IRelocatorModule;
import com.dynious.refinedrelocation.grid.relocator.RelocatorMultiModule;
import com.dynious.refinedrelocation.gui.container.ContainerMultiModule;
import com.dynious.refinedrelocation.network.NetworkHandler;
import com.dynious.refinedrelocation.network.packet.MessageHomeButtonClicked;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;

import java.util.List;

public class GuiHome extends GuiModular
{
    private RelocatorMultiModule multiModule;
    private List<IRelocatorModule> modules;
    private IItemRelocator relocator;
    private int side;

    public GuiHome(RelocatorMultiModule multiModule, List<IRelocatorModule> iRelocatorModules, IItemRelocator relocator, EntityPlayer player, int side)
    {
        super(new ContainerMultiModule(multiModule, relocator, player, side));
        this.multiModule = multiModule;
        this.modules = iRelocatorModules;
        this.relocator = relocator;
        this.side = side;
    }

    @Override
    public void initGui()
    {
        super.initGui();

        for (int i = 0; i < modules.size(); i++)
        {
            IRelocatorModule module = modules.get(i);
            new GuiButtonOpenModuleGUI(this, i, module.getDrops(relocator, side).get(0).getDisplayName());
        }
    }

    public void onButtonClicked(int index)
    {
        ((ContainerMultiModule)inventorySlots).openOrActive(index);
        NetworkHandler.INSTANCE.sendToServer(new MessageHomeButtonClicked(index));
    }
}
