package com.dynious.refinedrelocation.gui.widget;

import com.dynious.refinedrelocation.api.tileentity.ISortingInventory;
import com.dynious.refinedrelocation.gui.IGuiParent;
import com.dynious.refinedrelocation.network.NetworkHelper;
import com.dynious.refinedrelocation.network.packet.PacketPriority;
import net.minecraft.util.StatCollector;

import java.util.ArrayList;
import java.util.List;

public class GuiButtonPriority extends GuiRefinedRelocationButton
{
    private ISortingInventory tile;

    public GuiButtonPriority(IGuiParent parent, ISortingInventory tile)
    {
        super(parent, "");
        this.tile = tile;
    }

    public GuiButtonPriority(IGuiParent parent, int x, int y, int w, int h, ISortingInventory tile)
    {
        super(parent, x, y, w, h, 0, 0, "");
        this.tile = tile;
    }

    public void setValue(ISortingInventory.Priority priority)
    {
        String text = "";
        switch (priority)
        {
            case HIGH:
                text = "+";
                break;
            case NORMAL_HIGH:
                text = "0+";
                break;
            case NORMAL:
                text = "0";
                break;
            case NORMAL_LOW:
                text = "-0";
                break;
            case LOW:
                text = "-";
                break;
        }
        this.label.setText(text);
    }

    @Override
    public List<String> getTooltip(int mouseX, int mouseY)
    {
        List<String> subTooltip = super.getTooltip(mouseX, mouseY);
        if (isMouseInsideBounds(mouseX, mouseY))
        {
            List<String> tooltip = new ArrayList<String>();
            tooltip.add(StatCollector.translateToLocal(tile.getPriority().name().replace('_', '-')));
            tooltip.addAll(subTooltip);
            return tooltip;
        }
        return subTooltip;
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int type, boolean isShiftKeyDown)
    {
        if (isMouseInsideBounds(mouseX, mouseY) && (type == 0 || type == 1))
        {
            int amount = type == 0 ? -1 : 1;
            if (tile.getPriority().ordinal() + amount >= 0 && tile.getPriority().ordinal() + amount < ISortingInventory.Priority.values().length)
            {
                ISortingInventory.Priority newPriority = ISortingInventory.Priority.values()[tile.getPriority().ordinal() + amount];
                tile.setPriority(newPriority);
                NetworkHelper.sendToServer(new PacketPriority(newPriority.ordinal()));
                setValue(newPriority);
            }
        }
        super.mouseClicked(mouseX, mouseY, type, isShiftKeyDown);
    }

    @Override
    public void update()
    {
        if (tile != null)
            setValue(tile.getPriority());

        super.update();
    }
}
