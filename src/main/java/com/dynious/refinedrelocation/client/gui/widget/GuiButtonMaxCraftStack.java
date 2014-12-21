package com.dynious.refinedrelocation.client.gui.widget;

import com.dynious.refinedrelocation.client.gui.IGuiParent;
import com.dynious.refinedrelocation.grid.relocator.RelocatorModuleCrafting;
import com.dynious.refinedrelocation.lib.Strings;
import com.dynious.refinedrelocation.network.NetworkHandler;
import com.dynious.refinedrelocation.network.packet.MessageMaxCraftStack;
import net.minecraft.util.StatCollector;

import java.util.ArrayList;
import java.util.List;

public class GuiButtonMaxCraftStack extends GuiButtonCounter
{
    protected RelocatorModuleCrafting tile;

    public GuiButtonMaxCraftStack(IGuiParent parent, int x, int y, RelocatorModuleCrafting tile)
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

        tile.setMaxCraftStack((byte) newValue);
        NetworkHandler.INSTANCE.sendToServer(new MessageMaxCraftStack((byte) newValue));
    }

    @Override
    public List<String> getTooltip(int mouseX, int mouseY)
    {
        List<String> subTooltip = super.getTooltip(mouseX, mouseY);
        if (isMouseInsideBounds(mouseX, mouseY))
        {
            List<String> tooltip = new ArrayList<String>();
            tooltip.add(StatCollector.translateToLocal(Strings.MAX_CRAFT_STACK));
            tooltip.addAll(subTooltip);
            return tooltip;
        }
        return subTooltip;
    }

    @Override
    public void update()
    {
        if (tile != null)
            setValue(tile.getMaxCraftStack());

        super.update();
    }
}
