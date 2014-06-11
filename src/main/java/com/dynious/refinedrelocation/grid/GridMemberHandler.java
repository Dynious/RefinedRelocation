package com.dynious.refinedrelocation.grid;

import com.dynious.refinedrelocation.api.tileentity.ISortingMember;
import com.dynious.refinedrelocation.api.tileentity.grid.IGrid;
import com.dynious.refinedrelocation.api.tileentity.handlers.IGridMemberHandler;
import com.dynious.refinedrelocation.grid.Grid;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;

public class GridMemberHandler implements IGridMemberHandler
{
    protected TileEntity owner;

    protected IGrid grid;
    private boolean canJoinGroup = true;

    public GridMemberHandler(TileEntity owner)
    {
        this.owner = owner;
    }

    /**
     * get the owner of this Handler
     *
     * @return The TileEntity this Handler is linked to
     */
    public TileEntity getOwner()
    {
        return owner;
    }

    /**
     * Should be called on first tick from its block
     */
    public void onTileAdded()
    {
        if (owner.getWorldObj().isRemote)
        {
            return;
        }
        searchForLeader();
    }

    /**
     * Should be called by breakBlock(...) from its block
     */
    public void onTileRemoved()
    {
        if (owner.getWorldObj().isRemote)
        {
            return;
        }
        canJoinGroup = false;
        if (getGrid() != null)
        {
            getGrid().removeMember(this);
            getGrid().resetMembers();
        }
    }

    /**
     * Searches for leaders around the TileEntity and demotes leaders if it already has a grid
     */
    private void searchForLeader()
    {
        for (ForgeDirection direction : ForgeDirection.VALID_DIRECTIONS)
        {
            TileEntity tile = owner.getWorldObj().getBlockTileEntity(owner.xCoord + direction.offsetX, owner.yCoord + direction.offsetY, owner.zCoord + direction.offsetZ);
            if (tile != null && tile instanceof ISortingMember)
            {
                IGridMemberHandler filteringMember = ((ISortingMember) tile).getHandler();
                if (filteringMember.canJoinGroup() && filteringMember.getGrid() != null)
                {
                    if (grid == null)
                    {
                        setGrid(filteringMember.getGrid());
                    }
                    else if (filteringMember.getGrid() != getGrid())
                    {
                        filteringMember.getGrid().mergeToGrid(getGrid());
                    }
                }
            }
        }
        if (getGrid() == null)
        {
            setGrid(createNewGrid());
        }
    }

    /**
     * Creates a new Grid
     *
     * @return A new Grid
     */
    protected IGrid createNewGrid()
    {
        return new Grid();
    }

    /**
     * Get the grid of this SortingMember
     *
     * @return The grid of this SortingMember (can be itself)
     */
    public IGrid getGrid()
    {
        return grid;
    }

    /**
     * Sets the grid of a SortingMember
     *
     * @param grid The new Leader for this SortingMember
     */
    public void setGrid(IGrid grid)
    {
        if (grid == getGrid())
        {
            return;
        }
        this.grid = grid;

        if (this.grid != null)
        {
            this.grid.addMember(this);
        }
    }

    /**
     * @return Boolean if the SortingMember can join a group
     */
    public boolean canJoinGroup()
    {
        return canJoinGroup;
    }
}
