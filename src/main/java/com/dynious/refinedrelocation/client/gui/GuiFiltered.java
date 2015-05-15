package com.dynious.refinedrelocation.client.gui;

import com.dynious.refinedrelocation.api.tileentity.IFilterTileGUI;
import com.dynious.refinedrelocation.api.tileentity.ISortingInventory;
import com.dynious.refinedrelocation.client.gui.widget.*;
import com.dynious.refinedrelocation.container.ContainerFiltered;
import com.dynious.refinedrelocation.grid.filter.*;
import com.dynious.refinedrelocation.helper.BlockHelper;
import com.dynious.refinedrelocation.lib.Resources;
import com.dynious.refinedrelocation.network.packet.gui.MessageGUI;
import com.dynious.refinedrelocation.tileentity.TileBlockExtender;
import org.lwjgl.opengl.GL11;

public class GuiFiltered extends GuiRefinedRelocationContainer {

    private IFilterTileGUI filterTile;
    private GuiTabPanel panel;

    public GuiFiltered(IFilterTileGUI filterTile) {
        super(new ContainerFiltered(filterTile));
        this.filterTile = filterTile;
    }

    /**
     * Adds the buttons (and other controls) to the screen in question.
     */
    public void initGui() {
        super.initGui();

        new GuiLabel(this, width / 2, height / 2 - 76, BlockHelper.getTileEntityDisplayName(filterTile.getTileEntity()));

        panel = new GuiTabPanel(this, width / 2 - 80, height / 2 - 18, 160, 97);

        int tabButtonX = width / 2 - 118;
        int pageX = width / 2 - 80;
        int pageY = height / 2 - 18;
        if(filterTile.getFilter().getFilterCount() == 0) {
            GuiTabButton emptyTabButton = new GuiTabButton(panel, tabButtonX, height / 2 - 18, new GuiEmptyFilter(pageX, pageY, 160, 97));
            panel.setActiveTabButton(emptyTabButton);
        } else {
            GuiTabButton firstTabButton = null;
            int curY = pageY;
            for(int i = 0; i < filterTile.getFilter().getFilterCount(); i++) {
                AbstractFilter filter = filterTile.getFilter().getFilterAtIndex(i);
                IGuiWidgetBase page = null;
                switch(filter.getTypeId()) {
                    case AbstractFilter.TYPE_CUSTOM: page = new GuiUserFilter(pageX, pageY, 160, 97, true, (CustomUserFilter) filter); break;
                    case AbstractFilter.TYPE_PRESET: page = new GuiFilterList(pageX, pageY, 160, 97, (IChecklistFilter) filter); break;
                    case AbstractFilter.TYPE_CREATIVETAB: page = new GuiFilterList(pageX, pageY, 160, 97, (IChecklistFilter) filter); break;
                }
                GuiTabButton tabButton = new GuiTabButton(panel, tabButtonX, curY, page);
                if(firstTabButton == null) {
                    firstTabButton = tabButton;
                }
                curY += 25;
                if(filterTile.getFilter().getFilterCount() < 4) {
                    new GuiTabButton(panel, tabButtonX, curY, new GuiEmptyFilter(pageX, pageY, 160, 97));
                }
            }
            panel.setActiveTabButton(firstTabButton);
        }

        new GuiButtonBlacklist(this, width / 2 + 57, height / 2 - 67, filterTile, MessageGUI.BLACKLIST);

        if (filterTile instanceof TileBlockExtender)
        {
            new GuiRedstoneSignalStatus(this, width / 2 + 35, height / 2 - 63, (TileBlockExtender) filterTile);
            new GuiDisguise(this, width / 2 + 15, height / 2 - 63, 16, 16, (TileBlockExtender) filterTile);
        }
        if (filterTile instanceof ISortingInventory)
        {
            new GuiButtonPriority(this, width / 2 + 30, height / 2 - 67, 24, 20, (ISortingInventory) filterTile, MessageGUI.PRIORITY);
        }
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3) {
        int xSize = 176;
        int ySize = 174;

        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        mc.renderEngine.bindTexture(Resources.GUI_MULTIFILTER);
        int xStart = (width - xSize) / 2;
        int yStart = (height - ySize) / 2;
        this.drawTexturedModalRect(xStart, yStart, 0, 0, xSize, ySize);

        super.drawGuiContainerBackgroundLayer(par1, par2, par3);
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
}
