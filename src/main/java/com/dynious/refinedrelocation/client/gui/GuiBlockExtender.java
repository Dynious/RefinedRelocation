package com.dynious.refinedrelocation.client.gui;

import com.dynious.refinedrelocation.client.gui.widget.GuiButtonRedstoneSignalStatus;
import com.dynious.refinedrelocation.client.gui.widget.GuiLabel;
import com.dynious.refinedrelocation.container.ContainerBlockExtender;
import com.dynious.refinedrelocation.helper.BlockHelper;
import com.dynious.refinedrelocation.lib.Resources;
import com.dynious.refinedrelocation.network.packet.gui.MessageGUI;
import com.dynious.refinedrelocation.tileentity.TileBlockExtender;
import net.minecraft.entity.player.InventoryPlayer;
import org.lwjgl.opengl.GL11;

public class GuiBlockExtender extends GuiRefinedRelocationContainer
{
    private TileBlockExtender blockExtender;

    public GuiBlockExtender(InventoryPlayer invPlayer, TileBlockExtender blockExtender)
    {
        super(new ContainerBlockExtender(blockExtender));
        this.blockExtender = blockExtender;
    }

    /**
     * Adds the buttons (and other controls) to the screen in question.
     */
    public void initGui()
    {
        super.initGui();

        new GuiLabel(this, width / 2, height / 2 - 17, BlockHelper.getTileEntityDisplayName(blockExtender));

        new GuiButtonRedstoneSignalStatus(this, width / 2 - 12, height / 2 - 5, blockExtender, MessageGUI.REDSTONE_ENABLED);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float par1, int x, int y)
    {
        int xSize = 92;
        int ySize = 56;

        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        mc.renderEngine.bindTexture(Resources.GUI_BLOCK_EXTENDER);
        int xStart = (width - xSize) / 2;
        int yStart = (height - ySize) / 2;
        this.drawTexturedModalRect(xStart, yStart, 0, 0, xSize, ySize);

        super.drawGuiContainerBackgroundLayer(par1, x, y);
    }

    @Override
    public boolean doesGuiPauseGame()
    {
        return false;
    }
}
