package com.dynious.refinedrelocation.client.gui.widget;

import com.dynious.refinedrelocation.client.gui.IGuiParent;
import com.dynious.refinedrelocation.grid.filter.CustomUserFilter;
import com.dynious.refinedrelocation.network.NetworkHandler;
import com.dynious.refinedrelocation.network.packet.filter.MessageSetFilterString;

public class GuiTextInputUserFilter extends GuiTextInput {

    protected CustomUserFilter filter;

    public GuiTextInputUserFilter(IGuiParent parent, int x, int y, int w, int h, CustomUserFilter filter) {
        super(parent, x, y, w, h);
        this.filter = filter;
        setMaxStringLength(256);
        update();
    }

    @Override
    protected void onTextChangedByUser(String newFilter) {
        filter.setValue(newFilter);
        NetworkHandler.INSTANCE.sendToServer(new MessageSetFilterString(filter.getFilterIndex(), 0, newFilter));
    }

    @Override
    public void update() {
        if(!filter.getValue().equals(textField.getText())) {
            setText(filter.getValue());
        }
        super.update();
    }

}
