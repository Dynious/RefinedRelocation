package com.dynious.refinedrelocation.client.gui;

import com.dynious.refinedrelocation.api.filter.IFilterGUI;
import com.dynious.refinedrelocation.api.filter.IMultiFilter;
import com.dynious.refinedrelocation.api.filter.IMultiFilterChild;
import com.dynious.refinedrelocation.api.tileentity.IMultiFilterTile;
import com.dynious.refinedrelocation.client.gui.widget.*;
import com.dynious.refinedrelocation.container.ContainerFiltered;
import com.dynious.refinedrelocation.helper.BlockHelper;
import com.dynious.refinedrelocation.lib.Resources;
import com.dynious.refinedrelocation.lib.Strings;
import com.dynious.refinedrelocation.tileentity.TileAdvancedFilteredBlockExtender;
import net.minecraft.util.StatCollector;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;

public class GuiFiltered extends GuiRefinedRelocationContainer {

    private IMultiFilterTile filterTile;
    private IFilterGUI filter;
    private GuiTabPanel panel;
    private GuiButtonDeleteFilter deleteFilterButton;
    private final List<GuiTabButton> tabButtons = new ArrayList<GuiTabButton>();
    private int lastFilterCount;

    public GuiFiltered(IMultiFilterTile filterTile) {
        super(new ContainerFiltered(filterTile));
        this.filterTile = filterTile;
        this.filter = filterTile.getFilter();
        lastFilterCount = filter.getFilterCount();
    }

    /**
     * Adds the buttons (and other controls) to the screen in question.
     */
    public void initGui() {
        super.initGui();

        Keyboard.enableRepeatEvents(true);

        String title = BlockHelper.getTileEntityDisplayName(filterTile.getTileEntity());
        if(filterTile.getClass() == TileAdvancedFilteredBlockExtender.class) {
            title = StatCollector.translateToLocal(Strings.ADV_FILTERED_BLOCK_EXTENDER);
        }
        new GuiLabel(this, width / 2, height / 2 - 76, title);

        rebuildTabPanel(false);
    }

    public void rebuildTabPanel(boolean focusLast)     {
        if(panel != null) {
            removeChild(panel);
        }
        if(deleteFilterButton != null) {
            removeChild(deleteFilterButton);
        }
        for(GuiTabButton tabButton : tabButtons) {
            removeChild(tabButton);
        }
        tabButtons.clear();

        panel = new GuiTabPanel(this, width / 2 - 80, height / 2 - 100, 160, 200);
        int tabButtonX = width / 2 - 118;
        int tabButtonY = height / 2 - 60;

        GuiTabButton settingsTabButton = new GuiTabButton(this, panel, tabButtonX, tabButtonY, new GuiFilterSettings(panel.getX(), panel.getY(), panel.getWidth(), panel.getHeight(), filterTile), tabButtons.size(), 134, 238);
        settingsTabButton.setPlainTexture();
        if(!focusLast) {
            panel.setActiveTabButton(settingsTabButton);
        }
        tabButtons.add(settingsTabButton);

        tabButtonY = height / 2 - 18;

        if(filter.getFilterCount() == 0) {
            GuiTabButton emptyTabButton = new GuiTabButton(this, panel, tabButtonX, tabButtonY, new GuiEmptyFilter(panel.getX(), panel.getY(), panel.getWidth(), panel.getHeight()), tabButtons.size(), 62, 238);
            if(focusLast) {
                emptyTabButton.setActive(true);
            }
            emptyTabButton.setPlainTexture();
            tabButtons.add(emptyTabButton);
        } else {
            for(int i = 0; i < filter.getFilterCount(); i++) {
                IMultiFilterChild filterChild = filter.getFilterAtIndex(i);
                int iconTextureX = filterChild.getIconX();
                int iconTextureY = filterChild.getIconY();
                IGuiWidgetBase page = new GuiWidgetWrapper(filterChild.getGuiWidget(panel.getX(), panel.getY(), panel.getWidth(), panel.getHeight()));
                GuiTabButton tabButton = new GuiTabButton(this, panel, tabButtonX, tabButtonY, page, tabButtons.size(), iconTextureX, iconTextureY);
                tabButtons.add(tabButton);
                tabButtonY += 25;
            }
            if(focusLast) {
                tabButtons.get(tabButtons.size() - 1).setActive(true);
            }
            if(filter.getFilterCount() < 4) {
                GuiTabButton emptyTabButton = new GuiTabButton(this, panel, tabButtonX, tabButtonY, new GuiEmptyFilter(panel.getX(), panel.getY(), panel.getWidth(), panel.getHeight()), tabButtons.size(), 62, 238);
                emptyTabButton.setPlainTexture();
                tabButtons.add(emptyTabButton);
            }
        }

        deleteFilterButton = new GuiButtonDeleteFilter(this, width / 2 + 65, height / 2 - 65);
    }

    @Override
    public void onGuiClosed() {
        super.onGuiClosed();
        Keyboard.enableRepeatEvents(false);
    }

    public void updateScreen()
    {
        super.updateScreen();
        if(lastFilterCount != filter.getFilterCount()) {
            rebuildTabPanel(true);
            lastFilterCount = filter.getFilterCount();
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

    public boolean hasFilterSelected() {
        GuiTabButton activeTabButton = panel.getActiveTabButton();
        return activeTabButton != null && activeTabButton.getTabIndex() > 0 && !(activeTabButton.getTabPage() instanceof GuiEmptyFilter);
    }

    public int getSelectedFilterIndex() {
        return panel.getActiveTabButton().getTabIndex() - 1;
    }

    public IFilterGUI getFilter() {
        return filter;
    }
}
