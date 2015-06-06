package com.dynious.refinedrelocation.client.gui.widget;

import com.dynious.refinedrelocation.api.filter.IMultiFilterChild;
import com.dynious.refinedrelocation.client.gui.IGuiParent;
import com.dynious.refinedrelocation.lib.Resources;
import com.dynious.refinedrelocation.network.NetworkHandler;
import com.dynious.refinedrelocation.network.packet.filter.MessageSetFilterType;
import net.minecraft.client.gui.Gui;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

import java.util.ArrayList;
import java.util.List;

public class GuiButtonFilterType extends GuiWidgetBase
{

    private static final int ICON_WIDTH = 18;
    private static final int ICON_HEIGHT = 18;

    private IMultiFilterChild filter;
    private final GuiLabel descLabel;
    private final GuiLabel descLabel2;

    public GuiButtonFilterType(IGuiParent parent, int x, int y)
    {
        super(parent, x, y, 151, 25);

        descLabel = new GuiLabel(this, x + 5 + ICON_WIDTH + 10, y + 5, "");
        descLabel.drawCentered = false;

        descLabel2 = new GuiLabel(this, x + 5 + ICON_WIDTH + 10, y + 15, "");
        descLabel2.drawCentered = false;
    }

    public void setFilter(IMultiFilterChild filter)
    {
        this.filter = filter;
        if (filter != null)
        {
            String[] localDesc = StatCollector.translateToLocal(filter.getDescriptionLangKey()).split("\\\\n", 2);
            descLabel.text = localDesc[0];
            descLabel2.text = localDesc.length > 1 ? localDesc[1] : "";
        }
    }

    @Override
    public List<String> getTooltip(int mouseX, int mouseY)
    {
        if (filter != null && isInsideBounds(mouseX, mouseY))
        {
            List<String> tooltip = new ArrayList<String>();
            tooltip.add("\u00a7a" + StatCollector.translateToLocal(filter.getNameLangKey()));
            String[] tooltipLines = StatCollector.translateToLocal(filter.getDescriptionLangKey()).split("\\\\n");
            for (String tooltipLine : tooltipLines)
            {
                tooltip.add(tooltipLine);
            }
            return tooltip;
        }
        return super.getTooltip(mouseX, mouseY);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int type, boolean isShiftKeyDown)
    {
        if (filter != null && isInsideBounds(mouseX, mouseY))
        {
            NetworkHandler.INSTANCE.sendToServer(new MessageSetFilterType(-1, filter.getTypeName()));
        }
        super.mouseClicked(mouseX, mouseY, type, isShiftKeyDown);
    }

    @Override
    public void drawBackground(int mouseX, int mouseY)
    {
        mc.getTextureManager().bindTexture(Resources.GUI_MODULAR_FILTER);
        drawTexturedModalRect(x, y, 0, 198, 151, 34);

        if (filter == null || !isInsideBounds(mouseX, mouseY))
        {
            Gui.drawRect(x, y + 1, x + w, y + 1 + h, 0x44ffffff);
        }

        super.drawBackground(mouseX, mouseY);

        if (filter != null)
        {
            mc.getTextureManager().bindTexture(filter.getIconSheet());
            drawTexturedModalRect(x + 5, y + h / 2 - ICON_HEIGHT / 2, filter.getIconX(), filter.getIconY(), ICON_WIDTH, ICON_HEIGHT);
        }
    }
}
