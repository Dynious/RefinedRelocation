package com.dynious.refinedrelocation.gui.widget;

import com.dynious.refinedrelocation.grid.relocator.RelocatorModuleExtraction;
import com.dynious.refinedrelocation.api.gui.IGuiParent;
import com.dynious.refinedrelocation.lib.Settings;
import com.dynious.refinedrelocation.lib.Strings;
import com.dynious.refinedrelocation.network.PacketTypeHandler;
import com.dynious.refinedrelocation.network.packet.PacketTicksBetweenExtraction;
import cpw.mods.fml.common.network.PacketDispatcher;
import net.minecraft.util.StatCollector;

import java.util.ArrayList;
import java.util.List;
import java.text.DecimalFormat;

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
        PacketDispatcher.sendPacketToServer(PacketTypeHandler.populatePacket(new PacketTicksBetweenExtraction((int) newValue)));
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
