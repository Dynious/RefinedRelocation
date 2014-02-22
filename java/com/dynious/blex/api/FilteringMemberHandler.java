package com.dynious.blex.api;

import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;

import java.util.ArrayList;

public class FilteringMemberHandler
{
    protected TileEntity owner;

    private FilteringMemberHandler leader;
    private ArrayList<FilteringMemberHandler> childs;
    private boolean canJoinGroup = true;

    public FilteringMemberHandler(TileEntity owner)
    {
        this.owner = owner;
    }

    /**
     * Should be called on first tick from its block
     */
    public final void onTileAdded()
    {
        searchForLeader();
    }

    /**
     * Should be called by breakBlock(...) from its block
     */
    public final void onTileDestroyed()
    {
        canJoinGroup = false;
        getLeader().removeChild(this);
        getLeader().resetChilds();
    }

    /**
     * Searches for leaders around the TileEntity and demotes leaders if it already has a leader
     */
    public final void searchForLeader()
    {
        for (ForgeDirection direction : ForgeDirection.VALID_DIRECTIONS)
        {
            TileEntity tile = owner.worldObj.getBlockTileEntity(owner.xCoord + direction.offsetX, owner.yCoord + direction.offsetY, owner.zCoord + direction.offsetZ);
            if (tile != null && tile instanceof IFilteringMember)
            {
                FilteringMemberHandler filteringMember = ((IFilteringMember) tile).getFilteringMemberHandler();
                if (filteringMember.canJoinGroup() && filteringMember.getLeader() != this)
                {
                    if (leader == null && childs == null)
                    {
                        setLeader(filteringMember.getLeader());
                    }
                    else if (filteringMember.getLeader() != getLeader())
                    {
                        filteringMember.getLeader().demoteToChild(getLeader());
                    }
                }
            }
        }
    }

    /**
     * Get the leader of this FilteringMember
     *
     * @return The leader of this FilteringMember (can be itself)
     */
    public final FilteringMemberHandler getLeader()
    {
        if (leader == null)
        {
            return this;
        }
        else
        {
            return leader;
        }
    }

    /**
     * Sets the leader of a FilteringMember
     *
     * @param newLeader The new Leader for this FilteringMember
     */
    public final void setLeader(FilteringMemberHandler newLeader)
    {
        if (newLeader == this)
        {
            return;
        }
        this.leader = newLeader;

        if (leader != null)
        {
            leader.addChild(this);
        }
    }

    /**
     * Adds a child to the list of childs
     *
     * @param child Child to be added
     */
    public final void addChild(FilteringMemberHandler child)
    {
        if (childs == null)
        {
            childs = new ArrayList<FilteringMemberHandler>();
        }
        if (!childs.contains(child))
        {
            childs.add(child);
        }
    }

    /**
     * Removes a child from the list of childs
     *
     * @param child Child to be removed
     */
    public final void removeChild(FilteringMemberHandler child)
    {
        if (childs != null)
        {
            childs.remove(child);
        }
    }

    /**
     * Sets leader of all childs to null and deletes the child list
     */
    public final void resetChilds()
    {
        if (childs != null && !childs.isEmpty())
        {
            ArrayList<FilteringMemberHandler> tempChilds = new ArrayList<FilteringMemberHandler>(childs);
            childs = null;
            for (FilteringMemberHandler child : tempChilds)
            {
                child.setLeader(null);
            }
            for (FilteringMemberHandler child : tempChilds)
            {
                child.searchForLeader();
            }
        }
    }

    /**
     * Demotes a FilteringMember leader to a child of the given leader and sets the leader of its childs
     *
     * @param newLeader The new Leader for this FilteringMember and it's childs
     */
    public final void demoteToChild(FilteringMemberHandler newLeader)
    {
        setLeader(newLeader);
        if (childs != null && !childs.isEmpty())
        {
            for (FilteringMemberHandler child : childs)
            {
                child.setLeader(getLeader());
            }
        }
        childs = null;
    }

    /**
     * @return Boolean if the FilteringMember can join a group
     */
    public final boolean canJoinGroup()
    {
        return canJoinGroup;
    }

    /**
     * Filters an ItemStack to all members of the FilteringMember group
     *
     * @param itemStack The ItemStack to be filtered to all childs and this FilteringMember
     * @return The ItemStack that was not able to fit in any FilteringMember
     */
    public ItemStack filterStackToGroup(ItemStack itemStack)
    {
        if (childs != null && !childs.isEmpty())
        {
            for (FilteringMemberHandler filteringMember : childs)
            {
                if (filteringMember.owner instanceof IFilteringInventory)
                {
                    IFilteringInventory filteringInventory = (IFilteringInventory) filteringMember.owner;
                    if (filteringInventory.getBlackList() ? !filteringInventory.getFilter().passesFilter(itemStack) : filteringInventory.getFilter().passesFilter(itemStack))
                    {
                        itemStack = filteringInventory.getFilteringInventoryHandler().putInInventory(itemStack);
                        if (itemStack == null || itemStack.stackSize == 0)
                        {
                            return null;
                        }
                    }
                }
            }
        }
        return itemStack;
    }
}
