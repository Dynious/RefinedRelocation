package com.dynious.refinedrelocation.client.gui.widget.button;

import com.dynious.refinedrelocation.client.gui.IGuiParent;
import com.dynious.refinedrelocation.grid.relocator.RelocatorModuleCrafting;
import com.dynious.refinedrelocation.helper.GuiHelper;
import com.dynious.refinedrelocation.lib.Strings;
import net.minecraft.util.StatCollector;

import java.util.ArrayList;
import java.util.List;

public class GuiButtonMaxCraftStack extends GuiButtonCounter {
    protected RelocatorModuleCrafting tile;

    public GuiButtonMaxCraftStack(IGuiParent parent, int x, int y, RelocatorModuleCrafting tile) {
        super(parent, x, y, 24, 20, 0, 64, 1, 16);
        this.tile = tile;
        update();
        setAdventureModeRestriction(true);
    }

    @Override
    protected void onValueChangedByUser(double newValue) {
        if (tile == null)
            return;

        tile.setMaxCraftStack((int) newValue);
        GuiHelper.sendIntMessage(0, (int) newValue);
    }

    @Override
    public void getTooltip(List<String> tooltip, int mouseX, int mouseY) {
        super.getTooltip(tooltip, mouseX, mouseY);
        if (isInsideBounds(mouseX, mouseY)) {
            tooltip.add(StatCollector.translateToLocal(Strings.MAX_CRAFT_STACK));
        }
    }

    @Override
    public void update() {
        if (tile != null)
            setValue(tile.getMaxCraftStack());

        super.update();
    }
}
