package com.dynious.refinedrelocation.client.gui;

import com.dynious.refinedrelocation.client.gui.widget.GuiInsertDirections;
import com.dynious.refinedrelocation.client.gui.widget.GuiLabel;
import com.dynious.refinedrelocation.client.gui.widget.button.GuiButtonSpread;
import com.dynious.refinedrelocation.container.ContainerAdvanced;
import com.dynious.refinedrelocation.helper.BlockHelper;
import com.dynious.refinedrelocation.lib.Resources;
import com.dynious.refinedrelocation.network.packet.gui.MessageGUI;
import com.dynious.refinedrelocation.tileentity.TileAdvancedBuffer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.InventoryPlayer;
import org.lwjgl.opengl.GL11;

public class GuiAdvancedBuffer extends GuiRefinedRelocationContainer
{
    TileAdvancedBuffer buffer;
    GuiButton[] buttons = new GuiButton[6];
    GuiButton spread;

    public GuiAdvancedBuffer(InventoryPlayer invPlayer, TileAdvancedBuffer buffer)
    {
        super(new ContainerAdvanced(buffer));
        this.buffer = buffer;
    }

    /**
     * Adds the buttons (and other controls) to the screen in question.
     */
    public void initGui()
    {
        super.initGui();

        new GuiLabel(this, width / 2, height / 2 - 30, BlockHelper.getTileEntityDisplayName(buffer).replace("Advanced", "Adv."));

        new GuiButtonSpread(this, width / 2 + 17, height / 2 - 4, buffer);
        new GuiInsertDirections(this, width / 2 - 39, height / 2 - 19, 50, 50, buffer).setAdventureModeRestriction(true);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3)
    {
        int xSize = 97;
        int ySize = 81;

        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        mc.renderEngine.bindTexture(Resources.GUI_ADVANCED_BUFFER);
        int xStart = (width - xSize) / 2;
        int yStart = (height - ySize) / 2;
        this.drawTexturedModalRect(xStart, yStart, 0, 0, xSize, ySize);

        super.drawGuiContainerBackgroundLayer(par1, par2, par3);
    }

    @Override
    public boolean doesGuiPauseGame()
    {
        return false;
    }
}
