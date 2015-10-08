package com.dynious.refinedrelocation.client.gui.widget;

import com.dynious.refinedrelocation.api.gui.IGuiWidgetWrapped;
import com.dynious.refinedrelocation.api.tileentity.IFilterTileGUI;
import com.dynious.refinedrelocation.api.tileentity.ISortingInventory;
import com.dynious.refinedrelocation.client.gui.widget.button.*;
import com.dynious.refinedrelocation.lib.Strings;
import com.dynious.refinedrelocation.tileentity.IAdvancedFilteredTile;
import com.dynious.refinedrelocation.tileentity.IAdvancedTile;
import com.dynious.refinedrelocation.tileentity.TileBlockExtender;
import com.dynious.refinedrelocation.tileentity.TileWirelessBlockExtender;
import net.minecraft.util.StatCollector;

public class GuiFilterSettings extends GuiWidgetBase implements IGuiWidgetWrapped
{
    public GuiFilterSettings(int x, int y, int w, int h, IFilterTileGUI filterTile)
    {
        super(x, y, w, h);

        GuiLabel headerLabel = new GuiLabel(this, x, y, StatCollector.translateToLocal(Strings.FILTER_SETTINGS));
        headerLabel.drawCentered = false;

        int curX = x + w / 2 - 75;
        int curY = y + 15;

        if (filterTile instanceof IAdvancedTile)
        {
            new GuiButtonMaxStackSize(this, curX, curY, (IAdvancedTile) filterTile);
            curX += 27;
            new GuiButtonSpread(this, curX, curY, (IAdvancedTile) filterTile);
            curX += 27;
            new GuiButtonFilterExtraction(this, curX, curY, (IAdvancedFilteredTile) filterTile);

            new GuiInsertDirections(this, x + w / 2 + 25, y + h / 2 - 57, 50, 50, (IAdvancedTile) filterTile).setAdventureModeRestriction(true);

            curY += 24;
        }
        curX = x + w / 2 - 75;

        if (filterTile instanceof TileBlockExtender && !(filterTile instanceof TileWirelessBlockExtender))
        {
            new GuiButtonRedstoneSignalStatus(this, curX, curY, (TileBlockExtender) filterTile);
            curX += 27;
        }

        if (filterTile instanceof ISortingInventory)
        {
            new GuiButtonPriority(this, curX, curY, (ISortingInventory) filterTile);
        }

        curX = x + w / 2 - 80;
        curY += 30;

        String[] helpLines = StatCollector.translateToLocal(Strings.MULTIFILTER_HELP).split("\\\\n");
        for (String helpLine : helpLines)
        {
            new GuiLabel(this, curX, curY, helpLine).drawCentered = false;
            curY += 12;
        }
    }
}
