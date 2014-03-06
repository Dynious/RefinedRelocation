package com.dynious.refinedrelocation.api;

/**
 * This is the interface that will make your TileEntity part of the Sorting Network.
 *
 * Make sure you call the onTileAdded() and onTileDestroyed() at the right time.
 */
public interface ISortingMember
{
    /**
     * This should return the SortingMemberHandler of this tile. It cannot be null.
     *
     * @return The SortingMemberHandler of this tile
     */
    public SortingMemberHandler getSortingMemberHandler();
}
