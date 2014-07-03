package com.dynious.refinedrelocation.gui.widget;

import com.dynious.refinedrelocation.api.gui.IGuiParent;
import com.dynious.refinedrelocation.lib.Strings;
import com.dynious.refinedrelocation.network.NetworkHandler;
import com.dynious.refinedrelocation.network.packet.MessageMaxStackSize;
import com.dynious.refinedrelocation.tileentity.IAdvancedTile;
import net.minecraft.util.StatCollector;

import java.util.ArrayList;
import java.util.List;

public class GuiButtonMaxStackSize extends GuiButtonCounter
{
    protected IAdvancedTile tile;

    public GuiButtonMaxStackSize(IGuiParent parent, int x, int y, IAdvancedTile tile)
    {
        super(parent, x, y, 24, 20, 0, 0, 0, 64, 1, 16);
        this.tile = tile;
        update();
    }

    @Override
    protected void onValueChangedByUser(double newValue)
    {
        if (tile == null)
            return;

        tile.setMaxStackSize((byte) newValue);
        NetworkHandler.INSTANCE.sendToServer(new MessageMaxStackSize((byte) newValue));
    }

    @Override
    public List<String> getTooltip(int mouseX, int mouseY)
    {
        List<String> subTooltip = super.getTooltip(mouseX, mouseY);
        if (isMouseInsideBounds(mouseX, mouseY))
        {
            List<String> tooltip = new ArrayList<String>();
            tooltip.add(StatCollector.translateToLocal(Strings.MAX_STACK_SIZE));
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
