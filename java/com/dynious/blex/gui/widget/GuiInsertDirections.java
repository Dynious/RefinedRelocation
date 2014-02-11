package com.dynious.blex.gui.widget;

import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;
import com.dynious.blex.gui.IGuiParent;
import com.dynious.blex.tileentity.IAdvancedTile;
import com.dynious.blex.tileentity.TileBlockExtender;
import com.dynious.blex.tileentity.TileWirelessBlockExtender;

public class GuiInsertDirections extends GuiBlExWidgetBase
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
        
        int colX[] = { x, x + w / 2 - insertDirectionSize / 2, x + w - insertDirectionSize };
        int colY[] = { y, y + h / 2 - insertDirectionSize / 2, y + h - insertDirectionSize };

        insertDirections[ForgeDirection.DOWN.ordinal()] = new GuiInsertDirection(this, colX[2], colY[2], tile, ForgeDirection.DOWN, ForgeDirection.getOrientation(facingRelativeSides[facingDir.ordinal()][ForgeDirection.DOWN.ordinal()]));
        insertDirections[ForgeDirection.UP.ordinal()] = new GuiInsertDirection(this, colX[1], colY[1], tile, ForgeDirection.UP, ForgeDirection.getOrientation(facingRelativeSides[facingDir.ordinal()][ForgeDirection.UP.ordinal()]));
        insertDirections[ForgeDirection.NORTH.ordinal()] = new GuiInsertDirection(this, colX[1], colY[0], tile, ForgeDirection.NORTH, ForgeDirection.getOrientation(facingRelativeSides[facingDir.ordinal()][ForgeDirection.NORTH.ordinal()]));
        insertDirections[ForgeDirection.SOUTH.ordinal()] = new GuiInsertDirection(this, colX[1], colY[2], tile, ForgeDirection.SOUTH, ForgeDirection.getOrientation(facingRelativeSides[facingDir.ordinal()][ForgeDirection.SOUTH.ordinal()]));
        insertDirections[ForgeDirection.WEST.ordinal()] = new GuiInsertDirection(this, colX[0], colY[1], tile, ForgeDirection.WEST, ForgeDirection.getOrientation(facingRelativeSides[facingDir.ordinal()][ForgeDirection.WEST.ordinal()]));
        insertDirections[ForgeDirection.EAST.ordinal()] = new GuiInsertDirection(this, colX[2], colY[1], tile, ForgeDirection.EAST, ForgeDirection.getOrientation(facingRelativeSides[facingDir.ordinal()][ForgeDirection.EAST.ordinal()]));

        GuiBlExButton insertDirectionHelp = new GuiBlExButton(this, x+w-10, y, 10, 10, 0, 128, null);
        insertDirectionHelp.setTooltipString("Insert directions\n\u00A77Items will get inserted\n\u00A77into the chosen side\n\u00A77of the connected inventory\n\u00A77whenever they are inserted\n\u00A77into the relevant side of\n\u00A77this block extender");
        
        if (tile instanceof TileWirelessBlockExtender)
        {
            new GuiWirelessLinkStatus(this, x, y, (TileWirelessBlockExtender) tile);
        }
        if (((TileEntity) tile).getBlockType().canProvidePower())
        {
            new GuiRedstoneSignalStatus(this, colX[0], colY[2], (TileBlockExtender) tile);
        }
    }

}
