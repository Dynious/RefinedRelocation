package com.dynious.refinedrelocation.client.gui.widget;

import com.dynious.refinedrelocation.api.filter.IMultiFilter;
import com.dynious.refinedrelocation.client.gui.GuiFiltered;
import com.dynious.refinedrelocation.client.gui.IGuiParent;
import com.dynious.refinedrelocation.network.NetworkHandler;
import com.dynious.refinedrelocation.network.packet.filter.MessageSetFilterType;

public class GuiButtonFilterType extends GuiButton {

    private final String typeName;
    private final GuiFiltered parentGui;
    private final IMultiFilter filter;

    public GuiButtonFilterType(IGuiParent parent, int x, int y, String labelText, GuiFiltered parentGui, IMultiFilter filter, String typeName) {
        super(parent, x, y, 24, 20, 0, 0, labelText);
        this.typeName = typeName;
        this.parentGui = parentGui;
        this.filter = filter;
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int type, boolean isShiftKeyDown) {
        if(isInsideBounds(mouseX, mouseY)) {
            NetworkHandler.INSTANCE.sendToServer(new MessageSetFilterType(-1, typeName));
        }
        super.mouseClicked(mouseX, mouseY, type, isShiftKeyDown);
    }
}
