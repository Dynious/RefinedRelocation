package com.dynious.blex.gui.widget;

import java.util.List;
import com.dynious.blex.gui.IGuiParent;
import com.dynious.blex.network.PacketTypeHandler;
import com.dynious.blex.network.packet.PacketSpread;
import com.dynious.blex.tileentity.IAdvancedTile;
import com.dynious.blex.tileentity.TileBuffer;
import cpw.mods.fml.common.network.PacketDispatcher;

public class GuiButtonSpread extends GuiButtonToggle
{
    protected IAdvancedTile tile;

    public GuiButtonSpread(IGuiParent parent, int x, int y, IAdvancedTile tile)
    {
        super(parent, x, y, 24, 20, 48, 0, null, null);
        this.tile = tile;
        update();
    }

    @Override
    protected void onStateChangedByUser(boolean newState)
    {
        if (tile == null)
            return;
        
        tile.setSpreadItems(newState);
        PacketDispatcher.sendPacketToServer(PacketTypeHandler.populatePacket(new PacketSpread(newState)));
    }

    @Override
    public List<String> getTooltip(int mouseX, int mouseY)
    {
        List<String> tooltip = super.getTooltip(mouseX, mouseY);
        if (isMouseInsideBounds(mouseX, mouseY))
        {
            if (tile instanceof TileBuffer)
            {
                if (getState())
                {
                    tooltip.add("Mode: Round-robin");
                    tooltip.add("\u00A77Spread output across");
                    tooltip.add("\u00A77all valid sides");
                }
                else
                {
                    tooltip.add("Mode: Greedy");
                    tooltip.add("\u00A77Only output to the valid side");
                    tooltip.add("\u00A77with the highest priority");
                }
            }
            else
                tooltip.add(getState() ? "Spread items" : "Stack items");
        }
        return tooltip;
    }
    
    @Override
    public void update()
    {
        if (tile != null)
            setState(tile.getSpreadItems());
        
        super.update();
    }
}
