package com.dynious.refinedrelocation.client.gui;

import com.dynious.refinedrelocation.client.gui.widget.*;
import com.dynious.refinedrelocation.container.ContainerAdvancedFiltered;
import com.dynious.refinedrelocation.helper.BlockHelper;
import com.dynious.refinedrelocation.lib.Resources;
import com.dynious.refinedrelocation.tileentity.TileAdvancedFilteredBlockExtender;
import net.minecraft.entity.player.InventoryPlayer;
import org.lwjgl.opengl.GL11;

public class GuiAdvancedFilteredBlockExtender extends GuiRefinedRelocationContainer
{
    private TileAdvancedFilteredBlockExtender blockExtender;

    public GuiAdvancedFilteredBlockExtender(InventoryPlayer invPlayer, TileAdvancedFilteredBlockExtender blockExtender)
    {
        super(new ContainerAdvancedFiltered(blockExtender));
        this.blockExtender = blockExtender;
    }

    /**
     * Adds the buttons (and other controls) to the screen in question.
     */
    public void initGui()
    {
        super.initGui();

        new GuiLabel(this, width / 2, height / 2 - 76, BlockHelper.getTileEntityDisplayName(blockExtender).replaceAll("Advanced", "Adv."));

        new GuiButtonMaxStackSize(this, width / 2 - 81, height / 2 - 67, blockExtender, ContainerAdvancedFiltered.MESSAGE_MAX_STACK_SIZE);

        new GuiButtonBlacklist(this, width / 2 - 54, height / 2 - 67, blockExtender, ContainerAdvancedFiltered.MESSAGE_BLACKLIST);
        new GuiButtonSpread(this, width / 2 - 27, height / 2 - 67, blockExtender, ContainerAdvancedFiltered.MESSAGE_SPREAD_ITEMS);
        new GuiButtonFilterExtraction(this, width / 2, height / 2 - 67, blockExtender, ContainerAdvancedFiltered.MESSAGE_RESTRICT_EXTRACTION);

        new GuiUserFilter(this, width / 2 - 80, height / 2 - 41, 103, 27, true, blockExtender);

        new GuiFilterList(this, width / 2 - 80, height / 2 - 8, 160, 87, blockExtender);

        new GuiInsertDirections(this, width / 2 + 29, height / 2 - 65, 50, 50, blockExtender);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float par1, int x, int y)
    {
        int xSize = 176;
        int ySize = 174;

        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        mc.renderEngine.bindTexture(Resources.GUI_ADVANCED_FILTERED_BLOCK_EXTENDER);
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
