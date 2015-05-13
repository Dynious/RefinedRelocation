package com.dynious.refinedrelocation.client.gui;

import com.dynious.refinedrelocation.api.tileentity.IFilterTileGUI;
import com.dynious.refinedrelocation.api.tileentity.ISortingInventory;
import com.dynious.refinedrelocation.client.gui.widget.*;
import com.dynious.refinedrelocation.container.ContainerFiltered;
import com.dynious.refinedrelocation.helper.BlockHelper;
import com.dynious.refinedrelocation.lib.Resources;
import com.dynious.refinedrelocation.tileentity.TileBlockExtender;
import org.lwjgl.opengl.GL11;

public class GuiFiltered extends GuiRefinedRelocationContainer
{
    private IFilterTileGUI filterTile;

    public GuiFiltered(IFilterTileGUI filterTile)
    {
        super(new ContainerFiltered(filterTile));
        this.filterTile = filterTile;
    }

    /**
     * Adds the buttons (and other controls) to the screen in question.
     */
    public void initGui()
    {
        super.initGui();

        new GuiLabel(this, width / 2, height / 2 - 76, BlockHelper.getTileEntityDisplayName(filterTile.getTileEntity()));

        new GuiButtonBlacklist(this, width / 2 + 57, height / 2 - 67, filterTile, ContainerFiltered.MESSAGE_BLACKLIST);

        new GuiUserFilter(this, width / 2 - 80, height / 2 - 56, 160, 30, true, filterTile);

        new GuiFilterList(this, width / 2 - 80, height / 2 - 18, 160, 97, filterTile, ContainerFiltered.MESSAGE_FILTER_OPTION);

        if (filterTile instanceof TileBlockExtender)
        {
            new GuiRedstoneSignalStatus(this, width / 2 + 35, height / 2 - 63, (TileBlockExtender) filterTile);
            new GuiDisguise(this, width / 2 + 15, height / 2 - 63, 16, 16, (TileBlockExtender) filterTile);
        }
        if (filterTile instanceof ISortingInventory)
        {
            new GuiButtonPriority(this, width / 2 + 30, height / 2 - 67, 24, 20, (ISortingInventory) filterTile, ContainerFiltered.MESSAGE_PRIORITY);
        }
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3)
    {
        int xSize = 176;
        int ySize = 174;

        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        mc.renderEngine.bindTexture(Resources.GUI_FILTERED_BLOCK_EXTENDER);
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
