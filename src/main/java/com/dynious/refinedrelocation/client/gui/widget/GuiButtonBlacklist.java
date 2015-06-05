package com.dynious.refinedrelocation.client.gui.widget;

import com.dynious.refinedrelocation.api.tileentity.IMultiFilterTile;
import com.dynious.refinedrelocation.client.gui.IGuiParent;
import com.dynious.refinedrelocation.helper.GuiHelper;
import com.dynious.refinedrelocation.lib.Strings;
import net.minecraft.util.StatCollector;

import java.util.List;

public class GuiButtonBlacklist extends GuiButtonToggle
{
    private final int boundMessageId;
    protected IMultiFilterTile tile;

    public GuiButtonBlacklist(IGuiParent parent, int x, int y, IMultiFilterTile tile, int boundMessageId)
    {
        super(parent, x, y, 24, 20, 24, 0, null, null);
        this.boundMessageId = boundMessageId;
        this.tile = tile;
        update();
    }

    @Override
    protected void onStateChangedByUser(boolean newState)
    {
        if (tile == null)
            return;

        tile.getFilter().setBlacklists(newState);
        GuiHelper.sendBooleanMessage(boundMessageId, newState);
    }

    @Override
    public List<String> getTooltip(int mouseX, int mouseY)
    {
        List<String> tooltip = super.getTooltip(mouseX, mouseY);
        if (isInsideBounds(mouseX, mouseY)) {
            tooltip.add(StatCollector.translateToLocal(getState() ? Strings.BLACKLIST : Strings.WHITELIST));
            tooltip.add("\u00a7e" + StatCollector.translateToLocal(Strings.CLICK_TO_TOGGLE));
        }
        return tooltip;
    }

    @Override
    public void update()
    {
        if (tile != null)
            setState(tile.getFilter().isBlacklisting());

        super.update();
    }
}
