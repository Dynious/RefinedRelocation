package com.dynious.blex.gui.widget;

import java.util.List;
import com.dynious.blex.gui.IGuiParent;
import com.dynious.blex.tileentity.TileBlockExtender;

public class GuiRedstoneSignalStatus extends GuiBlExButton
{
    protected TileBlockExtender tile;
    protected boolean lastEnabled = false;
    protected boolean lastPowered = true;
    protected String tooltipText;
    protected static final int textureXBase = 128;

    public GuiRedstoneSignalStatus(IGuiParent parent, int x, int y, TileBlockExtender tile)
    {
        super(parent, x, y, 16, 16, textureXBase, 80, null);
        this.tile = tile;
        update();
    }

    @Override
    public List<String> getTooltip(int mouseX, int mouseY)
    {
        List<String> tooltip = super.getTooltip(mouseX, mouseY);

        if (isMouseInsideBounds(mouseX, mouseY))
        {
            String colorCode = "\u00A7";
            String grayColor = colorCode+"7";
            String redColor = colorCode+"4";

            tooltip.add("Redstone signal transmission");
            if (tile.isRedstoneEnabled)
            {
                tooltip.add(grayColor+"Enabled");
                
                if (tile.isRedstonePowered)
                    tooltip.add(redColor+"Active");
                else
                    tooltip.add(redColor+"Inactive");
            }
            else
            {
                tooltip.add(grayColor+"Disabled");
            }
        }

        return tooltip;
    }
    
    @Override
    public void update()
    {
        super.update();
        
        this.textureX = tile.isRedstoneEnabled ? (tile.isRedstonePowered ? textureXBase : textureXBase + w) : textureXBase + w * 2;
    }
}
