package com.dynious.blex.gui.widget;

import com.dynious.blex.gui.IGuiParent;
import com.dynious.blex.lib.Strings;
import com.dynious.blex.network.PacketTypeHandler;
import com.dynious.blex.network.packet.PacketBlacklist;
import com.dynious.blex.tileentity.IFilterTile;
import cpw.mods.fml.common.network.PacketDispatcher;
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

        tile.setBlackList(newState);
        PacketDispatcher.sendPacketToServer(PacketTypeHandler.populatePacket(new PacketBlacklist(newState)));
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
            setState(tile.getBlackList());

        super.update();
    }
}
