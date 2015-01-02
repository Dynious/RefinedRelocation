package com.dynious.refinedrelocation.client.gui.widget;

import com.dynious.refinedrelocation.client.gui.IGuiParent;
import net.minecraft.client.Minecraft;

public class GuiListCheckbox extends GuiCheckbox
{
    protected GuiList list;
    protected int index;

    public GuiListCheckbox(IGuiParent parent, int x, int y, int w, int h, int index, GuiList list)
    {
        super(parent, x, y, w, h, null);
        this.list = list;
        setIndex(index);
        update();
    }

    public void setIndex(int index)
    {
        this.index = index;
        if (list != null)
            this.label.setText(Minecraft.getMinecraft().fontRenderer.trimStringToWidth(list.getString(index), w - (textureW + 6)));
    }

    @Override
    protected void onStateChangedByUser(boolean newState)
    {
        if (list == null)
            return;

        list.onClicked(index, newState);
    }

    @Override
    public void update()
    {
        if (list != null)
            setChecked(list.getValue(index));

        super.update();
    }
}
