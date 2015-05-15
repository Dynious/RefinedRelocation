package com.dynious.refinedrelocation.client.gui.widget;

import com.dynious.refinedrelocation.client.gui.IGuiParent;
import com.dynious.refinedrelocation.grid.filter.IChecklistFilter;
import com.dynious.refinedrelocation.network.NetworkHandler;
import com.dynious.refinedrelocation.network.packet.filter.MessageSetFilterBoolean;
import net.minecraft.client.Minecraft;

public class GuiCheckboxFilter extends GuiCheckbox {

    protected IChecklistFilter filter;
    protected int index;

    public GuiCheckboxFilter(IGuiParent parent, int x, int y, int w, int h, int index, IChecklistFilter filter) {
        super(parent, x, y, w, h, null);
        this.filter = filter;
        setIndex(index);
        update();
    }

    public void setIndex(int index) {
        this.index = index;
        this.label.setText(Minecraft.getMinecraft().fontRenderer.trimStringToWidth(filter.getName(index), w - (textureW + 6)));
    }

    @Override
    protected void onStateChangedByUser(boolean newState) {
        filter.setValue(index, newState);
        NetworkHandler.INSTANCE.sendToServer(new MessageSetFilterBoolean(filter.getFilterIndex(), index, newState));
    }

    @Override
    public void update() {
        setChecked(filter.getValue(index));

        super.update();
    }
}
