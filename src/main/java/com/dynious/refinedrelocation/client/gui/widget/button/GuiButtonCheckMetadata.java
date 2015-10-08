package com.dynious.refinedrelocation.client.gui.widget.button;

import com.dynious.refinedrelocation.client.gui.IGuiParent;
import com.dynious.refinedrelocation.grid.filter.SameItemFilter;
import com.dynious.refinedrelocation.lib.Strings;
import net.minecraft.util.StatCollector;

import java.util.List;

public class GuiButtonCheckMetadata extends GuiButtonToggle {
    private final int boundMessageId;
    protected SameItemFilter filter;

    public GuiButtonCheckMetadata(IGuiParent parent, int x, int y, SameItemFilter filter, int boundMessageId) {
        super(parent, x, y, 24, 20, "button_metadata_inactive", "button_metadata_active", null, null);
        this.boundMessageId = boundMessageId;
        this.filter = filter;
        update();
    }

    @Override
    protected void onStateChangedByUser(boolean newState) {
        filter.checkMetadata = newState;
        filter.getParentFilter().sendBooleanToServer(filter, boundMessageId, newState);
    }

    @Override
    public List<String> getTooltip(int mouseX, int mouseY) {
        List<String> tooltip = super.getTooltip(mouseX, mouseY);
        if (isInsideBounds(mouseX, mouseY)) {
            tooltip.add(StatCollector.translateToLocal(getState() ? Strings.CHECK_META : Strings.DONT_CHECK_META));
            tooltip.add("\u00a7e" + StatCollector.translateToLocal(Strings.CLICK_TO_TOGGLE));
        }
        return tooltip;
    }

    @Override
    public void update() {
        if (filter != null) {
            setState(filter.checkMetadata);
        }
        super.update();
    }
}
