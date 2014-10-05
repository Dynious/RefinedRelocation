package com.dynious.refinedrelocation.client.gui.widget;

import com.dynious.refinedrelocation.client.gui.IGuiParent;
import com.dynious.refinedrelocation.grid.relocator.RelocatorModuleExtraction;
import com.dynious.refinedrelocation.lib.Strings;
import com.dynious.refinedrelocation.network.NetworkHandler;
import com.dynious.refinedrelocation.network.packet.MessageExtractOnRedstoneSignal;
import net.minecraft.util.StatCollector;

import java.util.List;

public class GuiButtonPulseExtractionToggle extends GuiButton
{
    private RelocatorModuleExtraction module;

    public GuiButtonPulseExtractionToggle(IGuiParent parent, int x, int y, RelocatorModuleExtraction module)
    {
        super(parent, x, y, 24, 20, 96, 0, null);
        this.module = module;
        update();
    }

    @Override
    public void update()
    {
        if (module != null)
            setNewState(module.redstoneControlState);
        super.update();
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int type, boolean isShiftKeyDown)
    {
        if (isMouseInsideBounds(mouseX, mouseY))
        {
            if (type == 0)
                setNewState(getNextControlState());
            else if (type == 1)
                setNewState(getPreviousControlState());
        }

        super.mouseClicked(mouseX, mouseY, type, isShiftKeyDown);
    }

    private int getNextControlState()
    {
        return module.redstoneControlState == 3 ? 0 : module.redstoneControlState + 1;
    }

    private int getPreviousControlState()
    {
        return module.redstoneControlState - 1 >= 0 ? module.redstoneControlState - 1 : 3;
    }

    protected void setNewState(int newState)
    {
        if (module == null)
            return;

        module.redstoneControlState = newState;
        NetworkHandler.INSTANCE.sendToServer(new MessageExtractOnRedstoneSignal(newState));

        switch (module.redstoneControlState)
        {
            case 0:
                this.textureX = 178;
                this.textureY = 0;
                break;
            case 1:
                this.textureX = 154;
                this.textureY = 40;
                break;
            case 2:
                this.textureX = 96;
                this.textureY = 40;
                break;
            case 3:
                this.textureX = 96;
                this.textureY = 0;
                break;
        }
    }

    @Override
    public List<String> getTooltip(int mouseX, int mouseY)
    {
        List tooltip = super.getTooltip(mouseX, mouseY);
        if (isMouseInsideBounds(mouseX, mouseY))
        {
            tooltip.add(StatCollector.translateToLocal(Strings.MODULE_REDSTONE_CONTROL + module.redstoneControlState));
        }
        return tooltip;
    }
}
