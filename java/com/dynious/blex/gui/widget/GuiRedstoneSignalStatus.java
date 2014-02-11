package com.dynious.blex.gui.widget;

import java.util.List;
import com.dynious.blex.gui.IGuiParent;
import com.dynious.blex.network.PacketTypeHandler;
import com.dynious.blex.network.packet.PacketRedstoneEnabled;
import com.dynious.blex.tileentity.IRedstoneTransmitter;
import cpw.mods.fml.common.network.PacketDispatcher;

public class GuiRedstoneSignalStatus extends GuiButtonToggle
{
    protected IRedstoneTransmitter tile;
    protected boolean lastEnabled = false;
    protected boolean lastPowered = true;
    protected String tooltipText;
    protected static final int textureXBase = 128;

    public GuiRedstoneSignalStatus(IGuiParent parent, int x, int y, IRedstoneTransmitter tile)
    {
        super(parent, x, y, 16, 16, textureXBase, 80, null, null);
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
            if (tile.isRedstoneTransmissionEnabled())
            {
                tooltip.add(grayColor+"Enabled");
                
                if (tile.isRedstoneTransmissionActive())
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
    protected void onStateChangedByUser(boolean newState)
    {
        if (tile == null)
            return;
        
        tile.setRedstoneTransmissionEnabled(newState);
        PacketDispatcher.sendPacketToServer(PacketTypeHandler.populatePacket(new PacketRedstoneEnabled(newState)));
    }
    
    @Override
    public void update()
    {
        super.update();

        if (tile != null)
        {
            setState(tile.isRedstoneTransmissionEnabled());
            this.textureX = tile.isRedstoneTransmissionActive() ? textureXBase + w : textureXBase;
        }
    }
}
