package com.dynious.refinedrelocation.api.tileentity.handlers;

import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

public interface ISortingMemberHandler
{
    /**
     * get the owner of this Handler
     *
     * @return The TileEntity this Handler is linked to
     */
    public TileEntity getOwner();

    /**
     * Should be called on first tick from its block
     */
    public void onTileAdded();

    /**
     * Should be called by breakBlock(...) from its block
     */
    public void onTileDestroyed();

    /**
     * Get the leader of this SortingMember
     *
     * @return The leader of this SortingMember (can be itself)
     */
    public ISortingMemberHandler getLeader();

    /**
     * Sets the leader of a SortingMember
     *
     * @param newLeader The new Leader for this SortingMember
     */
    public void setLeader(ISortingMemberHandler newLeader);

    /**
     * Adds a child to the list of childs
     *
     * @param child Child to be added
     */
    public void addChild(ISortingMemberHandler child);

    /**
     * Removes a child from the list of childs
     *
     * @param child Child to be removed
     */
    public void removeChild(ISortingMemberHandler child);

    /**
     * Sets leader of all childs to null and deletes the child list
     */
    public void resetChilds();

    /**
     * Demotes a SortingMember leader to a child of the given leader and sets the leader of its childs
     *
     * @param newLeader The new Leader for this SortingMember and it's childs
     */
    public void demoteToChild(ISortingMemberHandler newLeader);

    /**
     * @return Boolean if the SortingMember can join a group
     */
    public boolean canJoinGroup();

    /**
     * Filters an ItemStack to all members of the SortingMember group
     *
     * @param itemStack The ItemStack to be filtered to all childs and this SortingMember
     * @return The ItemStack that was not able to fit in any ISortingInventory
     */
    public ItemStack filterStackToGroup(ItemStack itemStack, TileEntity requester);
}
