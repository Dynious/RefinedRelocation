package com.dynious.refinedrelocation.client.gui.widget.button;

import com.dynious.refinedrelocation.api.filter.IMultiFilterChild;
import com.dynious.refinedrelocation.client.gui.GuiRefinedRelocationContainer;
import com.dynious.refinedrelocation.client.gui.IGuiParent;
import com.dynious.refinedrelocation.client.gui.widget.GuiLabel;
import com.dynious.refinedrelocation.client.gui.widget.GuiWidgetBase;
import com.dynious.refinedrelocation.lib.Resources;
import com.dynious.refinedrelocation.network.NetworkHandler;
import com.dynious.refinedrelocation.network.packet.filter.MessageSetFilterType;
import net.minecraft.client.gui.Gui;
import net.minecraft.util.StatCollector;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GuiButtonFilterType extends GuiWidgetBase {
    private IMultiFilterChild filter;
    private final GuiLabel descLabel;
    private final GuiLabel descLabel2;

    public GuiButtonFilterType(IGuiParent parent, int x, int y) {
        super(parent, x, y, 151, 25);

        descLabel = new GuiLabel(this, x + 33, y + 5, "");
        descLabel.drawCentered = false;

        descLabel2 = new GuiLabel(this, x + 33, y + 15, "");
        descLabel2.drawCentered = false;
    }

    public void setFilter(IMultiFilterChild filter) {
        this.filter = filter;
        if (filter != null) {
            String[] localDesc = StatCollector.translateToLocal(filter.getDescriptionLangKey()).split("\\\\n", 2);
            descLabel.setText(localDesc[0]);
            descLabel2.setText(localDesc.length > 1 ? localDesc[1] : "");
        }
    }

    @Override
    public void getTooltip(List<String> tooltip, int mouseX, int mouseY) {
        super.getTooltip(tooltip, mouseX, mouseY);
        if (filter != null && isInsideBounds(mouseX, mouseY)) {
            tooltip.add("\u00a7a" + StatCollector.translateToLocal(filter.getNameLangKey()));
            String[] tooltipLines = StatCollector.translateToLocal(filter.getDescriptionLangKey()).split("\\\\n");
            Collections.addAll(tooltip, tooltipLines);
        }
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int type, boolean isShiftKeyDown) {
        if (filter != null && isInsideBounds(mouseX, mouseY)) {
            if (!GuiRefinedRelocationContainer.isRestrictedAccessWithError()) {
                NetworkHandler.INSTANCE.sendToServer(new MessageSetFilterType(-1, filter.getTypeName()));
            }
        }
        super.mouseClicked(mouseX, mouseY, type, isShiftKeyDown);
    }

    @Override
    public void drawBackground(int mouseX, int mouseY) {
        mc.getTextureManager().bindTexture(Resources.GUI_MODULAR_FILTER);
        drawTexturedModalRect(x, y, 0, 198, 151, 27);

        if (filter == null || !isInsideBounds(mouseX, mouseY)) {
            Gui.drawRect(x, y + 1, x + w, y + 1 + h, 0x44ffffff);
        }

        super.drawBackground(mouseX, mouseY);

        if (filter != null) {
            mc.getTextureManager().bindTexture(filter.getIconSheet());
            drawTexturedModalRect(x + 5 + 18 / 2 - filter.getIconWidth() / 2, y + h / 2 - filter.getIconHeight() / 2, filter.getIconX(), filter.getIconY(), filter.getIconWidth(), filter.getIconHeight());
        }
    }
}
