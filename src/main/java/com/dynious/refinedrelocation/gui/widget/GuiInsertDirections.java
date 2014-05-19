package com.dynious.refinedrelocation.gui.widget;

import com.dynious.refinedrelocation.gui.IGuiParent;
import com.dynious.refinedrelocation.lib.Strings;
import com.dynious.refinedrelocation.tileentity.IAdvancedTile;
import com.dynious.refinedrelocation.tileentity.TileBlockExtender;
import com.dynious.refinedrelocation.tileentity.TileBuffer;
import com.dynious.refinedrelocation.tileentity.TileWirelessBlockExtender;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.ForgeDirection;

public class GuiInsertDirections extends GuiRefinedRelocationWidgetBase
{
    protected IAdvancedTile tile;

    public static int[][] facingRelativeSides = {
            {0, 1, 2, 3, 4, 5},  // down
            {1, 0, 3, 2, 4, 5},  // up
            {3, 2, 0, 1, 4, 5},  // north
            {2, 3, 1, 0, 4, 5},  // south
            {4, 5, 3, 2, 0, 1},  // west
            {5, 4, 3, 2, 1, 0},  // east
            {0, 1, 2, 3, 4, 5}   // unknown
    };

    protected GuiInsertDirection insertDirections[] = new GuiInsertDirection[ForgeDirection.VALID_DIRECTIONS.length];

    public GuiInsertDirections(IGuiParent parent, int x, int y, int w, int h, IAdvancedTile tile)
    {
        super(parent, x, y, w, h);
        this.tile = tile;

        ForgeDirection facingDir = ForgeDirection.UNKNOWN;
        if (tile instanceof TileBlockExtender)
            facingDir = ((TileBlockExtender) tile).getConnectedDirection();

        int insertDirectionSize = GuiInsertDirection.defaultSizeW;

        int colX[] = {x, x + w / 2 - insertDirectionSize / 2, x + w - insertDirectionSize};
        int colY[] = {y, y + h / 2 - insertDirectionSize / 2, y + h - insertDirectionSize};

        insertDirections[ForgeDirection.DOWN.ordinal()] = new GuiInsertDirection(this, colX[2], colY[2], tile, ForgeDirection.DOWN, ForgeDirection.getOrientation(facingRelativeSides[facingDir.ordinal()][ForgeDirection.DOWN.ordinal()]));
        insertDirections[ForgeDirection.UP.ordinal()] = new GuiInsertDirection(this, colX[1], colY[1], tile, ForgeDirection.UP, ForgeDirection.getOrientation(facingRelativeSides[facingDir.ordinal()][ForgeDirection.UP.ordinal()]));
        insertDirections[ForgeDirection.NORTH.ordinal()] = new GuiInsertDirection(this, colX[1], colY[0], tile, ForgeDirection.NORTH, ForgeDirection.getOrientation(facingRelativeSides[facingDir.ordinal()][ForgeDirection.NORTH.ordinal()]));
        insertDirections[ForgeDirection.SOUTH.ordinal()] = new GuiInsertDirection(this, colX[1], colY[2], tile, ForgeDirection.SOUTH, ForgeDirection.getOrientation(facingRelativeSides[facingDir.ordinal()][ForgeDirection.SOUTH.ordinal()]));
        insertDirections[ForgeDirection.WEST.ordinal()] = new GuiInsertDirection(this, colX[0], colY[1], tile, ForgeDirection.WEST, ForgeDirection.getOrientation(facingRelativeSides[facingDir.ordinal()][ForgeDirection.WEST.ordinal()]));
        insertDirections[ForgeDirection.EAST.ordinal()] = new GuiInsertDirection(this, colX[2], colY[1], tile, ForgeDirection.EAST, ForgeDirection.getOrientation(facingRelativeSides[facingDir.ordinal()][ForgeDirection.EAST.ordinal()]));

        GuiRefinedRelocationButton insertDirectionHelp = new GuiRefinedRelocationButton(this, x + w - 10, y, 10, 10, 0, 128, null);
        if (tile instanceof TileBuffer)
        {
            insertDirectionHelp.setTooltipString(StatCollector.translateToLocal(Strings.BUFFER_INSERTION_INFO).replaceAll("\\\\n", "\n\u00A77"));
        }
        else
        {
            insertDirectionHelp.setTooltipString(StatCollector.translateToLocal(Strings.BLOCK_EXTENDER_INSERTION_INFO).replaceAll("\\\\n", "\n\u00A77"));
        }

        if (tile instanceof TileWirelessBlockExtender)
        {
            new GuiWirelessLinkStatus(this, x, y, (TileWirelessBlockExtender) tile);
        }
        else if (tile instanceof TileBlockExtender)
        {
            new GuiDisguise(this, x, y, 16, 16, (TileBlockExtender) tile);
            new GuiRedstoneSignalStatus(this, colX[0], colY[2], (TileBlockExtender) tile);
        }
    }

}
