package com.dynious.blex.gui;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;
import org.lwjgl.opengl.GL11;
import com.dynious.blex.gui.container.ContainerAdvanced;
import com.dynious.blex.gui.widget.GuiButtonMaxStackSize;
import com.dynious.blex.gui.widget.GuiButtonSpread;
import com.dynious.blex.gui.widget.GuiInsertDirection;
import com.dynious.blex.gui.widget.GuiInsertDirections;
import com.dynious.blex.gui.widget.GuiLabel;
import com.dynious.blex.helper.BlockHelper;
import com.dynious.blex.lib.Resources;
import com.dynious.blex.tileentity.TileAdvancedBlockExtender;

public class GuiAdvancedBlockExtender extends GuiBlExContainer
{
    private TileAdvancedBlockExtender blockExtender;

    public GuiAdvancedBlockExtender(InventoryPlayer invPlayer, TileAdvancedBlockExtender blockExtender)
    {
        super(new ContainerAdvanced(blockExtender));
        this.blockExtender = blockExtender;
    }

    /**
     * Adds the buttons (and other controls) to the screen in question.
     */
    public void initGui()
    {
        super.initGui();

        new GuiLabel(this, width / 2, height / 2 - 30, BlockHelper.getTileEntityDisplayName((TileEntity) blockExtender));
        
        new GuiButtonMaxStackSize(this, width / 2 - 66, height / 2 - 4, blockExtender);
        new GuiButtonSpread(this, width / 2 + 42, height / 2 - 4, blockExtender);
        
        new GuiInsertDirections(this, width / 2 - 25, height / 2 - 19, 50, 50, blockExtender);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float par1, int x, int y)
    {
        int xSize = 176;
        int ySize = 81;

        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        mc.renderEngine.bindTexture(Resources.GUI_ADVANCED_BLOCK_EXTENDER);
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
