package com.dynious.refinedrelocation.client.gui;

import com.dynious.refinedrelocation.client.gui.widget.GuiLabel;
import com.dynious.refinedrelocation.client.gui.widget.button.GuiButtonRedstoneSignalStatus;
import com.dynious.refinedrelocation.container.ContainerBlockExtender;
import com.dynious.refinedrelocation.helper.BlockHelper;
import com.dynious.refinedrelocation.lib.Resources;
import com.dynious.refinedrelocation.network.packet.gui.MessageGUI;
import com.dynious.refinedrelocation.tileentity.TileBlockExtender;
import net.minecraft.entity.player.InventoryPlayer;
import org.lwjgl.opengl.GL11;

public class GuiBlockExtender extends GuiRefinedRelocationContainer {
    private TileBlockExtender blockExtender;

    public GuiBlockExtender(InventoryPlayer invPlayer, TileBlockExtender blockExtender) {
        super(new ContainerBlockExtender(blockExtender));
        this.blockExtender = blockExtender;

        xSize = 92;
        ySize = 56;
    }

    /**
     * Adds the buttons (and other controls) to the screen in question.
     */
    public void initGui() {
        super.initGui();

        new GuiLabel(this, width / 2, height / 2 - 17, BlockHelper.getTileEntityDisplayName(blockExtender));

        new GuiButtonRedstoneSignalStatus(this, width / 2 - 12, height / 2 - 5, blockExtender);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float par1, int x, int y) {
        GL11.glColor4f(1f, 1f, 1f, 1f);
        drawWindow(guiLeft, guiTop, xSize, ySize);

        super.drawGuiContainerBackgroundLayer(par1, x, y);
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
}
