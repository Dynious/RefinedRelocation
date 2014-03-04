package com.dynious.refinedrelocation.api;

import com.dynious.refinedrelocation.helper.DirectionHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;

import java.util.ArrayList;
import java.util.List;

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
            TileEntity tile = DirectionHelper.getTileAtSide(owner, direction);
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
     * @param requester The FilteringInventoryHandler that requested the filtering
     * @return The ItemStack that was not able to fit in any IFilteringInventory
     */
    public final ItemStack filterStackToGroup(ItemStack itemStack, FilteringInventoryHandler requester)
    {
        if (childs != null && !childs.isEmpty())
        {
            List<Integer> blackListers = new ArrayList<Integer>();

            //Try to put the ItemStack in a child that passes (whitelisted) the filter
            for (int i = 0; i < childs.size(); i++)
            {
                FilteringMemberHandler filteringMember = childs.get(i);
                if (filteringMember == requester)
                {
                    continue;
                }
                if (filteringMember.owner instanceof IFilteringInventory)
                {
                    IFilteringInventory filteringInventory = (IFilteringInventory) filteringMember.owner;

                    if (filteringInventory.getFilter().blacklists)
                    {
                        blackListers.add(i);
                        continue;
                    }

                    if (filteringInventory.getFilter().passesFilter(itemStack))
                    {
                        itemStack = filteringInventory.getFilteringInventoryHandler().putInInventory(itemStack);
                        if (itemStack == null || itemStack.stackSize == 0)
                        {
                            return null;
                        }
                    }
                }
            }

            //If this (leader) is an inventory try to put the ItemStack in the inventory if whitelisted
            if (this instanceof FilteringInventoryHandler)
            {
                FilteringInventoryHandler myInv = (FilteringInventoryHandler) this;
                if (!((IFilteringInventory)myInv.owner).getFilter().blacklists && ((IFilteringInventory)myInv.owner).getFilter().passesFilter(itemStack))
                {
                    itemStack = myInv.putInInventory(itemStack);
                    if (itemStack == null || itemStack.stackSize == 0)
                    {
                        return null;
                    }
                }
            }

            //If the ItemStack can also be put in the requester inventory (it's a blackList Tile), prefer this blacklisted inventory
            if (((IFilteringInventory) requester.owner).getFilter().blacklists && ((IFilteringInventory) requester.owner).getFilter().passesFilter(itemStack))
            {
                return itemStack;
            }

            //Lastly, try to insert the item in a blacklisting child
            for (int i : blackListers)
            {
                IFilteringInventory filteringInventory = (IFilteringInventory) childs.get(i).owner;
                if (filteringInventory.getFilter().passesFilter(itemStack))
                {
                    itemStack = filteringInventory.getFilteringInventoryHandler().putInInventory(itemStack);
                    if (itemStack == null || itemStack.stackSize == 0)
                    {
                        return null;
                    }
                }
            }

            //Lastly, if this (leader) is an inventory try to put the ItemStack in the inventory not blacklisted
            if (this instanceof FilteringInventoryHandler)
            {
                FilteringInventoryHandler myInv = (FilteringInventoryHandler) this;
                if (((IFilteringInventory)myInv.owner).getFilter().blacklists && ((IFilteringInventory)myInv.owner).getFilter().passesFilter(itemStack))
                {
                    itemStack = myInv.putInInventory(itemStack);
                    if (itemStack == null || itemStack.stackSize == 0)
                    {
                        return null;
                    }
                }
            }
        }
        return itemStack;
    }
}
