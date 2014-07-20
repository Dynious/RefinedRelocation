package com.dynious.refinedrelocation.client.gui.widget;

import com.dynious.refinedrelocation.client.gui.IGuiParent;
import com.dynious.refinedrelocation.grid.relocator.RelocatorModuleExtraction;
import com.dynious.refinedrelocation.lib.Strings;
import com.dynious.refinedrelocation.network.NetworkHandler;
import com.dynious.refinedrelocation.network.packet.MessageModuleMaxStackSize;
import net.minecraft.util.StatCollector;

import java.util.List;

public class GuiButtonModuleMaxStackSize extends GuiButtonCounter
{
    RelocatorModuleExtraction module;

    public GuiButtonModuleMaxStackSize(IGuiParent parent, int x, int y, RelocatorModuleExtraction module)
    {
        super(parent, x, y, 24, 20, 0, 0, 0, 64, 1, 8);
        this.module = module;
        update();
    }

    @Override
    protected void onValueChangedByUser(double newValue)
    {
        if (module == null)
            return;

        module.maxExtractionStackSize = (int) newValue;
        NetworkHandler.INSTANCE.sendToServer(new MessageModuleMaxStackSize((int) newValue));
    }

    @Override
    public List<String> getTooltip(int mouseX, int mouseY)
    {
        List<String> tooltip = super.getTooltip(mouseX, mouseY);
        if (isMouseInsideBounds(mouseX, mouseY))
            tooltip.add(0, StatCollector.translateToLocal(Strings.MODULE_MAX_STACK_SIZE));
        return tooltip;
    }

    @Override
    public void update()
    {
        if (module != null)
            setValue((double) module.maxExtractionStackSize);

        super.update();
    }
}
