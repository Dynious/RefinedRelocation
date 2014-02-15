package com.dynious.blex.gui;

import com.dynious.blex.gui.container.ContainerAdvanced;
import com.dynious.blex.gui.widget.GuiButtonSpread;
import com.dynious.blex.gui.widget.GuiInsertDirections;
import com.dynious.blex.gui.widget.GuiLabel;
import com.dynious.blex.helper.BlockHelper;
import com.dynious.blex.lib.Resources;
import com.dynious.blex.tileentity.TileAdvancedBuffer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.tileentity.TileEntity;
import org.lwjgl.opengl.GL11;

public class GuiAdvancedBuffer extends GuiBlExContainer
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

        new GuiLabel(this, width / 2, height / 2 - 30, BlockHelper.getTileEntityDisplayName((TileEntity) buffer).replaceAll("Advanced", "Adv."));

        new GuiButtonSpread(this, width / 2 + 17, height / 2 - 4, buffer);

        new GuiInsertDirections(this, width / 2 - 39, height / 2 - 19, 50, 50, buffer);
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
