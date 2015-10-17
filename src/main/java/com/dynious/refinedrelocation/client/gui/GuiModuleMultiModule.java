package com.dynious.refinedrelocation.client.gui;

import com.dynious.refinedrelocation.api.relocator.IItemRelocator;
import com.dynious.refinedrelocation.client.gui.widget.GuiLabel;
import com.dynious.refinedrelocation.client.gui.widget.GuiModuleList;
import com.dynious.refinedrelocation.container.ContainerMultiModule;
import com.dynious.refinedrelocation.grid.relocator.RelocatorMultiModule;
import com.dynious.refinedrelocation.lib.Strings;
import net.minecraft.util.StatCollector;
import org.lwjgl.opengl.GL11;

public class GuiModuleMultiModule extends GuiRefinedRelocationContainer {

    private final RelocatorMultiModule multiModule;
    private int lastModuleCount;

    public GuiModuleMultiModule(RelocatorMultiModule multiModule, IItemRelocator relocator, int side) {
        super(new ContainerMultiModule(multiModule, relocator, side));
        this.multiModule = multiModule;
        lastModuleCount = multiModule.getModuleCount();

        xSize = 176;
        ySize = 174;
    }

    @Override
    public void initGui() {
        super.initGui();

        GuiLabel headerLabel = new GuiLabel(this, width / 2 - 80, height / 2 - 75, StatCollector.translateToLocal(Strings.MULTI_MODULE));
        headerLabel.drawCentered = false;

        new GuiModuleList(this, multiModule, width / 2 - 80, height / 2 - 70 + headerLabel.h, 160, 113);

        int curX = width / 2 - 80;
        int curY = height / 2 + 57;
        String[] helpLines = StatCollector.translateToLocal(Strings.MULTI_MODULE_HELP).split("\\\\n");
        for (String helpLine : helpLines) {
            new GuiLabel(this, width / 2, curY, helpLine).drawCentered = true;
            curY += 12;
        }
    }

    @Override
    public void updateScreen() {
        super.updateScreen();

        if (lastModuleCount != multiModule.getModuleCount()) {
            initGui();
            lastModuleCount = multiModule.getModuleCount();
        }
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3) {
        GL11.glColor4f(1f, 1f, 1f, 1f);
        drawWindow(guiLeft, guiTop, xSize, ySize);

        super.drawGuiContainerBackgroundLayer(par1, par2, par3);
    }

}
