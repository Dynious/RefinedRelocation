package com.dynious.refinedrelocation.gui.widget;

import com.dynious.refinedrelocation.api.IFilterTile;
import com.dynious.refinedrelocation.gui.IGuiParent;
import com.dynious.refinedrelocation.lib.Strings;
import com.dynious.refinedrelocation.network.NetworkHelper;
import com.dynious.refinedrelocation.network.packet.PacketBlacklist;
import net.minecraft.util.StatCollector;

import java.util.List;

public class GuiButtonBlacklist extends GuiButtonToggle
{
    protected IFilterTile tile;

    public GuiButtonBlacklist(IGuiParent parent, int x, int y, IFilterTile tile)
    {
        super(parent, x, y, 24, 20, 24, 0, null, null);
        this.tile = tile;
        update();
    }

    @Override
    protected void onStateChangedByUser(boolean newState)
    {
        if (tile == null)
            return;

        tile.getFilter().blacklists = newState;
        NetworkHelper.sendToServer(new PacketBlacklist(newState));
    }

    @Override
    public List<String> getTooltip(int mouseX, int mouseY)
    {
        List<String> tooltip = super.getTooltip(mouseX, mouseY);
        if (isMouseInsideBounds(mouseX, mouseY))
            tooltip.add(StatCollector.translateToLocal(getState() ? Strings.BLACKLIST : Strings.WHITELIST));
        return tooltip;
    }

    @Override
    public void update()
    {
        if (tile != null)
            setState(tile.getFilter().blacklists);

        super.update();
    }
}
