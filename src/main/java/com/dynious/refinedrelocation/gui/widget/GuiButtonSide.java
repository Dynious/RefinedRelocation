package com.dynious.refinedrelocation.gui.widget;

import com.dynious.refinedrelocation.grid.relocator.RelocatorModuleSneaky;
import com.dynious.refinedrelocation.gui.IGuiParent;
import com.dynious.refinedrelocation.lib.Strings;
import com.dynious.refinedrelocation.network.NetworkHandler;
import com.dynious.refinedrelocation.network.packet.MessageSide;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.util.ForgeDirection;

public class GuiButtonSide extends GuiRefinedRelocationButton
{
    private RelocatorModuleSneaky module;

    public GuiButtonSide(IGuiParent parent, int x, int y, RelocatorModuleSneaky module)
    {
        super(parent, x, y, 34, 20, 120, 0, ForgeDirection.getOrientation(module.getSide()).toString());
        this.module = module;
        this.setTooltipString(StatCollector.translateToLocal(Strings.SNEAKY));
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int type, boolean isShiftKeyDown)
    {
        if (isMouseInsideBounds(mouseX, mouseY))
        {
            if (type == 0)
            {
                onValueChangedByUser((module.getSide() + 1) % ForgeDirection.VALID_DIRECTIONS.length);
            }
            else if (type == -1)
            {
                onValueChangedByUser((module.getSide() - 1) % ForgeDirection.VALID_DIRECTIONS.length);
            }
        }
    }

    protected void onValueChangedByUser(int side)
    {
        module.setSide(side);
        NetworkHandler.INSTANCE.sendToServer(new MessageSide(side));
    }

    public void update()
    {
        if (module != null)
            label.setText(ForgeDirection.getOrientation(module.getSide()).toString());

        super.update();
    }
}
