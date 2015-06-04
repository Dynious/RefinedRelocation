package com.dynious.refinedrelocation.api.tileentity.handlers;

import com.dynious.refinedrelocation.api.tileentity.grid.IGrid;
import net.minecraft.tileentity.TileEntity;

public interface IGridMemberHandler
{
    /**
     * Get the owner of this Handler
     *
     * @return The TileEntity this Handler is linked to
     */
    TileEntity getOwner();

    /**
     * Should be called on first tick from its TileEntity
     */
    void onTileAdded();

    /**
     * Should be called by invalidate() and onChunkUnload() from its TileEntity
     */
    void onTileRemoved();

    /**
     * Get the Grid of this GridMember
     *
     * @return The Grid of this GridMember
     */
    IGrid getGrid();

    /**
     * Sets the Grid of a GridMember
     *
     * @param newLeader The new Grid for this GridMember
     */
    void setGrid(IGrid newLeader);

    /**
     * @return Boolean if the GridMember can join a group
     */
    boolean canJoinGroup();
}
