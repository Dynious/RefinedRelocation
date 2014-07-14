package com.dynious.refinedrelocation.client.gui.widget;

import com.dynious.refinedrelocation.client.gui.IGuiParent;
import com.dynious.refinedrelocation.lib.Strings;
import com.dynious.refinedrelocation.network.NetworkHandler;
import com.dynious.refinedrelocation.network.packet.MessageSpread;
import com.dynious.refinedrelocation.tileentity.IAdvancedTile;
import com.dynious.refinedrelocation.tileentity.TileBuffer;
import net.minecraft.util.StatCollector;

import java.util.List;

public class GuiButtonSpread extends GuiButtonToggle
{
    protected IAdvancedTile tile;

    public GuiButtonSpread(IGuiParent parent, int x, int y, IAdvancedTile tile)
    {
        super(parent, x, y, 24, 20, 48, 0, null, null);
        this.tile = tile;
        update();
    }

    @Override
    protected void onStateChangedByUser(boolean newState)
    {
        if (tile == null)
            return;

        tile.setSpreadItems(newState);
        NetworkHandler.INSTANCE.sendToServer(new MessageSpread(newState));
    }

    @Override
    public List<String> getTooltip(int mouseX, int mouseY)
    {
        List<String> tooltip = super.getTooltip(mouseX, mouseY);
        if (isMouseInsideBounds(mouseX, mouseY))
        {
            if (tile instanceof TileBuffer)
            {
                if (getState())
                {
                    tooltip.add(StatCollector.translateToLocal(Strings.MODE) + ": " + StatCollector.translateToLocal(Strings.ROUND_ROBIN));
                    for (String s : StatCollector.translateToLocal(Strings.ROUND_ROBIN_INFO).split("\\\\n"))
                    {
                        tooltip.add("\u00A77" + s);
                    }
                }
                else
                {
                    tooltip.add(StatCollector.translateToLocal(Strings.MODE) + ": " + StatCollector.translateToLocal(Strings.GREEDY));
                    for (String s : StatCollector.translateToLocal(Strings.GREEDY_INFO).split("\\\\n"))
                    {
                        tooltip.add("\u00A77" + s);
                    }
                }
            }
            else
                tooltip.add(StatCollector.translateToLocal(getState() ? Strings.SPREAD : Strings.STACK));
        }
        return tooltip;
    }

    @Override
    public void update()
    {
        if (tile != null)
            setState(tile.getSpreadItems());

        super.update();
    }
}
