package com.dynious.refinedrelocation.client.gui.widget.button;

import com.dynious.refinedrelocation.client.gui.IGuiParent;
import com.dynious.refinedrelocation.grid.filter.SameItemFilter;
import com.dynious.refinedrelocation.lib.Strings;
import com.dynious.refinedrelocation.network.packet.gui.MessageGUI;
import net.minecraft.util.StatCollector;

import java.util.List;

public class GuiButtonCheckMetadata extends GuiButtonToggle {
    protected SameItemFilter filter;

    public GuiButtonCheckMetadata(IGuiParent parent, int x, int y, SameItemFilter filter) {
        super(parent, x, y, 24, 20, "button_metadata_inactive", "button_metadata_active", null, null);
        this.filter = filter;
        update();
    }

    @Override
    protected void onStateChangedByUser(boolean newState) {
        filter.checkMetadata = newState;
        filter.getParentFilter().sendBooleanToServer(filter, MessageGUI.CHECK_METADATA, newState);
    }

    @Override
    public void getTooltip(List<String> tooltip, int mouseX, int mouseY) {
        super.getTooltip(tooltip, mouseX, mouseY);
        if (isInsideBounds(mouseX, mouseY)) {
            tooltip.add(StatCollector.translateToLocal(getState() ? Strings.CHECK_META : Strings.DONT_CHECK_META));
            tooltip.add("\u00a7e" + StatCollector.translateToLocal(Strings.CLICK_TO_TOGGLE));
        }
    }

    @Override
    public void update() {
        if (filter != null) {
            setState(filter.checkMetadata);
        }
        super.update();
    }
}
