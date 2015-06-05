package com.dynious.refinedrelocation.client.gui.widget;

import com.dynious.refinedrelocation.client.gui.IGuiParent;
import com.dynious.refinedrelocation.helper.GuiHelper;
import com.dynious.refinedrelocation.lib.Strings;
import com.dynious.refinedrelocation.tileentity.IAdvancedTile;
import net.minecraft.util.StatCollector;

import java.util.ArrayList;
import java.util.List;

public class GuiButtonMaxStackSize extends GuiButtonCounter
{
    private final int boundMessageId;
    protected IAdvancedTile tile;

    public GuiButtonMaxStackSize(IGuiParent parent, int x, int y, IAdvancedTile tile, int boundMessageId)
    {
        super(parent, x, y, 24, 20, 0, 0, 0, 64, 1, 16);
        this.tile = tile;
        this.boundMessageId = boundMessageId;
        update();
    }

    @Override
    protected void onValueChangedByUser(double newValue)
    {
        if (tile == null)
            return;

        tile.setMaxStackSize((byte) newValue);
        GuiHelper.sendByteMessage(boundMessageId, (byte) newValue);
    }

    @Override
    public List<String> getTooltip(int mouseX, int mouseY)
    {
        List<String> subTooltip = super.getTooltip(mouseX, mouseY);
        if (isInsideBounds(mouseX, mouseY))
        {
            List<String> tooltip = new ArrayList<String>();
            tooltip.add(StatCollector.translateToLocal(Strings.MAX_STACK_SIZE));
            tooltip.add("\u00a7e" + StatCollector.translateToLocal(Strings.CLICK_INCREASE));
            tooltip.add("\u00a7e" + StatCollector.translateToLocal(Strings.CLICK_DECREASE));
            tooltip.addAll(subTooltip);
            return tooltip;
        }
        return subTooltip;
    }

    @Override
    public void update()
    {
        if (tile != null)
            setValue(tile.getMaxStackSize());

        super.update();
    }
}
