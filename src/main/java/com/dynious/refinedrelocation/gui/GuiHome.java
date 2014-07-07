package com.dynious.refinedrelocation.gui;

import com.dynious.refinedrelocation.api.relocator.IItemRelocator;
import com.dynious.refinedrelocation.api.relocator.IRelocatorModule;
import com.dynious.refinedrelocation.grid.relocator.RelocatorMultiModule;
import com.dynious.refinedrelocation.gui.container.ContainerMultiModule;
import com.dynious.refinedrelocation.gui.widget.GuiButtonOpenModuleGUI;
import com.dynious.refinedrelocation.network.PacketTypeHandler;
import com.dynious.refinedrelocation.network.packet.PacketHomeButtonClicked;
import cpw.mods.fml.common.network.PacketDispatcher;
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
            new GuiButtonOpenModuleGUI(this, i, module.getDisplayName());
        }
    }

    public void onButtonClicked(int index)
    {
        ((ContainerMultiModule)inventorySlots).openOrActive(index);
        PacketDispatcher.sendPacketToServer(PacketTypeHandler.populatePacket(new PacketHomeButtonClicked(index)));
    }
}
