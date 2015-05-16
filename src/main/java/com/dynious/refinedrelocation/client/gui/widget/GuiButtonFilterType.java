package com.dynious.refinedrelocation.client.gui.widget;

import com.dynious.refinedrelocation.api.filter.IFilterGUI;
import com.dynious.refinedrelocation.client.gui.GuiFiltered;
import com.dynious.refinedrelocation.client.gui.IGuiParent;
import com.dynious.refinedrelocation.network.NetworkHandler;
import com.dynious.refinedrelocation.network.packet.filter.MessageAddFilter;

public class GuiButtonFilterType extends GuiButton {

    private final int typeId;
    private final GuiFiltered parentGui;
    private final IFilterGUI filter;

    public GuiButtonFilterType(IGuiParent parent, int x, int y, String labelText, GuiFiltered parentGui, IFilterGUI filter, int typeId) {
        super(parent, x, y, 24, 20, 0, 0, labelText);
        this.typeId = typeId;
        this.parentGui = parentGui;
        this.filter = filter;
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int type, boolean isShiftKeyDown) {
        if(isInsideBounds(mouseX, mouseY)) {
            NetworkHandler.INSTANCE.sendToServer(new MessageAddFilter(typeId));
        }
        super.mouseClicked(mouseX, mouseY, type, isShiftKeyDown);
    }
}
