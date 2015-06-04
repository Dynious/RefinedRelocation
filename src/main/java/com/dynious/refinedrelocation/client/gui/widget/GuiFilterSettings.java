package com.dynious.refinedrelocation.client.gui.widget;

import com.dynious.refinedrelocation.api.gui.IGuiWidgetWrapped;
import com.dynious.refinedrelocation.api.tileentity.IMultiFilterTile;
import com.dynious.refinedrelocation.api.tileentity.ISortingInventory;
import com.dynious.refinedrelocation.lib.Strings;
import com.dynious.refinedrelocation.network.packet.gui.MessageGUI;
import com.dynious.refinedrelocation.tileentity.IAdvancedFilteredTile;
import com.dynious.refinedrelocation.tileentity.IAdvancedTile;
import com.dynious.refinedrelocation.tileentity.TileBlockExtender;
import net.minecraft.util.StatCollector;

public class GuiFilterSettings extends GuiWidgetBase implements IGuiWidgetWrapped {

	public GuiFilterSettings(int x, int y, int w, int h, IMultiFilterTile filterTile) {
		super(x, y, w, h);

		GuiLabel headerLabel = new GuiLabel(this, x, y + 40, StatCollector.translateToLocal(Strings.FILTER_SETTINGS));
		headerLabel.drawCentered = false;

		int curX = x + w / 2 - 75;
		int curY = y + h / 2 - 45;

		if(filterTile instanceof IAdvancedTile)
		{
			new GuiButtonMaxStackSize(this, curX, curY, (IAdvancedTile) filterTile, MessageGUI.MAX_STACK_SIZE);
			curX += 27;
			new GuiButtonSpread(this, curX, curY, (IAdvancedTile) filterTile, MessageGUI.SPREAD_ITEMS);
			curX += 27;
			new GuiButtonFilterExtraction(this, curX, curY, (IAdvancedFilteredTile) filterTile, MessageGUI.RESTRICT_EXTRACTION);

			new GuiInsertDirections(this, x + w / 2 + 25, y + h / 2 - 47, 50, 50, (IAdvancedTile) filterTile);

			curY += 24;
		}
		curX = x + w / 2 - 75;

		new GuiButtonBlacklist(this, curX, curY, filterTile, MessageGUI.BLACKLIST);
		curX += 27;

		if (filterTile instanceof TileBlockExtender)
		{
			new GuiButtonRedstoneSignalStatus(this, curX, curY, (TileBlockExtender) filterTile, MessageGUI.REDSTONE_ENABLED);
			curX += 27;
		}

		if (filterTile instanceof ISortingInventory)
		{
			new GuiButtonPriority(this, curX, curY, 24, 20, (ISortingInventory) filterTile, MessageGUI.PRIORITY);
		}
	}

}
