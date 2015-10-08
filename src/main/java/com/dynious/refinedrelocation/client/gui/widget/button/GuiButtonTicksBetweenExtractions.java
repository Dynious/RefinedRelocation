package com.dynious.refinedrelocation.client.gui.widget.button;

import com.dynious.refinedrelocation.client.gui.IGuiParent;
import com.dynious.refinedrelocation.grid.relocator.RelocatorModuleExtraction;
import com.dynious.refinedrelocation.helper.GuiHelper;
import com.dynious.refinedrelocation.lib.Settings;
import com.dynious.refinedrelocation.lib.Strings;
import net.minecraft.util.StatCollector;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class GuiButtonTicksBetweenExtractions extends GuiButtonCounter {
    private static final DecimalFormat decimalFormat = new DecimalFormat("##.##");
    private RelocatorModuleExtraction module;

    public GuiButtonTicksBetweenExtractions(IGuiParent parent, int x, int y, RelocatorModuleExtraction module) {
        super(parent, x, y, 24, 20, Settings.RELOCATOR_MIN_TICKS_BETWEEN_EXTRACTION, Integer.MAX_VALUE, 1, 10);
        this.module = module;
        update();
        setAdventureModeRestriction(true);
    }

    @Override
    protected void onValueChangedByUser(double newValue) {
        if (module == null)
            return;

        module.setTicksBetweenExtraction((int) newValue);
        GuiHelper.sendIntMessage(0, (int) newValue);
    }

    @Override
    public void getTooltip(List<String> tooltip, int mouseX, int mouseY) {
        super.getTooltip(tooltip, mouseX, mouseY);
        if (isInsideBounds(mouseX, mouseY)) {
            float seconds = (float) module.getTicksBetweenExtraction() / 20;

            tooltip.add(StatCollector.translateToLocal(Strings.TICKS_BETWEEN_EXT));
            tooltip.add(decimalFormat.format(seconds) + " " + StatCollector.translateToLocal(Strings.SECONDS_BETWEEN_EXT));
        }
    }

    @Override
    public void update() {
        if (module != null)
            setValue(module.getTicksBetweenExtraction());

        super.update();
    }
}
