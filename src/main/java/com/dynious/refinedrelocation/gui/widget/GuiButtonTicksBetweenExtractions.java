package com.dynious.refinedrelocation.gui.widget;

import com.dynious.refinedrelocation.grid.relocator.RelocatorModuleExtraction;
import com.dynious.refinedrelocation.gui.IGuiParent;
import com.dynious.refinedrelocation.lib.Settings;
import com.dynious.refinedrelocation.lib.Strings;
import com.dynious.refinedrelocation.network.NetworkHandler;
import com.dynious.refinedrelocation.network.packet.MessageTicksBetweenExtraction;
import net.minecraft.util.StatCollector;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class GuiButtonTicksBetweenExtractions extends GuiButtonCounter
{
    private static final DecimalFormat decimalFormat = new DecimalFormat("##.##");
    private RelocatorModuleExtraction module;

    public GuiButtonTicksBetweenExtractions(IGuiParent parent, int x, int y, RelocatorModuleExtraction module)
    {
        super(parent, x, y, 24, 20, 0, 0, Settings.RELOCATOR_MIN_TICKS_BETWEEN_EXTRACTION, Integer.MAX_VALUE, 1, 10);
        this.module = module;
        update();
    }

    @Override
    protected void onValueChangedByUser(double newValue)
    {
        if (module == null)
            return;

        module.setTicksBetweenExtraction((int) newValue);
        NetworkHandler.INSTANCE.sendToServer(new MessageTicksBetweenExtraction((int) newValue));
    }

    @Override
    public List<String> getTooltip(int mouseX, int mouseY)
    {
        List<String> subTooltip = super.getTooltip(mouseX, mouseY);
        if (isMouseInsideBounds(mouseX, mouseY))
        {
            float seconds = (float) module.getTicksBetweenExtraction() / 20;

            List<String> tooltip = new ArrayList<String>();
            tooltip.add(StatCollector.translateToLocal(Strings.TICKS_BETWEEN_EXT));
            tooltip.add(decimalFormat.format(seconds) + " " + StatCollector.translateToLocal(Strings.SECONDS_BETWEEN_EXT));
            tooltip.addAll(subTooltip);
            return tooltip;
        }
        return subTooltip;
    }

    @Override
    public void update()
    {
        if (module != null)
            setValue(module.getTicksBetweenExtraction());

        super.update();
    }
}
