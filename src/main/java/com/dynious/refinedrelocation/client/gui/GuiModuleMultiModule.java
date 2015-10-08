package com.dynious.refinedrelocation.client.gui;

import com.dynious.refinedrelocation.api.relocator.IItemRelocator;
import com.dynious.refinedrelocation.api.relocator.IRelocatorModule;
import com.dynious.refinedrelocation.client.gui.widget.button.GuiButtonOpenModuleGUI;
import com.dynious.refinedrelocation.container.ContainerMultiModule;
import com.dynious.refinedrelocation.grid.relocator.RelocatorMultiModule;
import com.dynious.refinedrelocation.helper.GuiHelper;
import com.dynious.refinedrelocation.lib.Resources;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.opengl.GL11;

import java.util.List;

public class GuiModuleMultiModule extends GuiRefinedRelocationContainer {
    private final List<IRelocatorModule> modules;

    public GuiModuleMultiModule(RelocatorMultiModule multiModule, List<IRelocatorModule> modules, IItemRelocator relocator, EntityPlayer player, int side) {
        super(new ContainerMultiModule(multiModule, relocator, player, side));
        this.modules = modules;
        xSize = 176;
        ySize = 174;
    }

    @Override
    public void initGui() {
        super.initGui();

        for (int i = 0; i < modules.size(); i++) {
            IRelocatorModule module = modules.get(i);
            new GuiButtonOpenModuleGUI(this, i, module.getDisplayName());
        }
    }

    public void onButtonClicked(int index) {
        ((ContainerMultiModule) inventorySlots).openOrActive(index);
        GuiHelper.sendActionMessage(index);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3) {
        GL11.glColor4f(1f, 1f, 1f, 1f);
        drawWindow(guiLeft, guiTop, xSize, ySize);

        super.drawGuiContainerBackgroundLayer(par1, par2, par3);
    }

}
