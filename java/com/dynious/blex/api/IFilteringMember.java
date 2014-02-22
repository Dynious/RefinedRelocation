package com.dynious.blex.api;

/**
 * This is the interface that will make your TileEntity part of the Filtering Network.
 *
 * Make sure you call the onTileAdded() and onTileDestroyed() at the right time.
 */
public interface IFilteringMember
{
    /**
     * This should return the FilteringMemberHandler of this tile. It cannot be null.
     *
     * @return The FilteringMemberHandler of this tile
     */
    public FilteringMemberHandler getFilteringMemberHandler();
}
