package com.dynious.refinedrelocation.client.gui.widget.button;

import com.dynious.refinedrelocation.client.gui.IGuiParent;
import com.dynious.refinedrelocation.helper.GuiHelper;
import com.dynious.refinedrelocation.lib.Strings;
import com.dynious.refinedrelocation.network.packet.gui.MessageGUI;
import com.dynious.refinedrelocation.tileentity.IAdvancedTile;
import net.minecraft.util.StatCollector;

import java.util.ArrayList;
import java.util.List;

public class GuiButtonMaxStackSize extends GuiButtonCounter {
    protected IAdvancedTile tile;

    public GuiButtonMaxStackSize(IGuiParent parent, int x, int y, IAdvancedTile tile) {
        super(parent, x, y, 24, 20, 0, 64, 1, 16);
        this.tile = tile;
        update();
        setAdventureModeRestriction(true);
    }

    @Override
    protected void onValueChangedByUser(double newValue) {
        if (tile == null)
            return;

        tile.setMaxStackSize((byte) newValue);
        GuiHelper.sendByteMessage(MessageGUI.MAX_STACK_SIZE, (byte) newValue);
    }

    @Override
    public void getTooltip(List<String> tooltip, int mouseX, int mouseY) {
        super.getTooltip(tooltip, mouseX, mouseY);
        if (isInsideBounds(mouseX, mouseY)) {
            tooltip.add(StatCollector.translateToLocal(Strings.MAX_STACK_SIZE));
            tooltip.add("\u00a7e" + StatCollector.translateToLocal(Strings.CLICK_INCREASE));
            tooltip.add("\u00a7e" + StatCollector.translateToLocal(Strings.CLICK_DECREASE));
        }
    }

    @Override
    public void update() {
        if (tile != null)
            setValue(tile.getMaxStackSize());

        super.update();
    }
}
