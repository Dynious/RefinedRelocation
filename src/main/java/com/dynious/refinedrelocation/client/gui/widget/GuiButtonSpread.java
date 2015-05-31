package com.dynious.refinedrelocation.client.gui.widget;

import com.dynious.refinedrelocation.client.gui.IGuiParent;
import com.dynious.refinedrelocation.helper.GuiHelper;
import com.dynious.refinedrelocation.lib.Strings;
import com.dynious.refinedrelocation.tileentity.IAdvancedTile;
import com.dynious.refinedrelocation.tileentity.TileBuffer;
import net.minecraft.util.StatCollector;

import java.util.List;

public class GuiButtonSpread extends GuiButtonToggle
{
    private final int boundMessageId;
    protected IAdvancedTile tile;

    public GuiButtonSpread(IGuiParent parent, int x, int y, IAdvancedTile tile, int boundMessageId)
    {
        super(parent, x, y, 24, 20, 48, 0, null, null);
        this.tile = tile;
        this.boundMessageId = boundMessageId;
        update();
    }

    @Override
    protected void onStateChangedByUser(boolean newState)
    {
        if (tile == null)
            return;

        tile.setSpreadItems(newState);
        GuiHelper.sendBooleanMessage(boundMessageId, newState);
    }

    @Override
    public List<String> getTooltip(int mouseX, int mouseY)
    {
        List<String> tooltip = super.getTooltip(mouseX, mouseY);
        if (isInsideBounds(mouseX, mouseY))
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
