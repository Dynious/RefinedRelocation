package com.dynious.refinedrelocation.api;

import com.dynious.refinedrelocation.helper.DirectionHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;

import java.util.ArrayList;
import java.util.List;

public class SortingMemberHandler
{
    protected TileEntity owner;

    private SortingMemberHandler leader;
    private ArrayList<SortingMemberHandler> childs;
    private boolean canJoinGroup = true;

    public SortingMemberHandler(TileEntity owner)
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
            if (tile != null && tile instanceof ISortingMember)
            {
                SortingMemberHandler filteringMember = ((ISortingMember) tile).getSortingMemberHandler();
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
     * Get the leader of this SortingMember
     *
     * @return The leader of this SortingMember (can be itself)
     */
    public final SortingMemberHandler getLeader()
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
     * Sets the leader of a SortingMember
     *
     * @param newLeader The new Leader for this SortingMember
     */
    public final void setLeader(SortingMemberHandler newLeader)
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
    public final void addChild(SortingMemberHandler child)
    {
        if (childs == null)
        {
            childs = new ArrayList<SortingMemberHandler>();
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
    public final void removeChild(SortingMemberHandler child)
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
            ArrayList<SortingMemberHandler> tempChilds = new ArrayList<SortingMemberHandler>(childs);
            childs = null;
            for (SortingMemberHandler child : tempChilds)
            {
                child.setLeader(null);
            }
            for (SortingMemberHandler child : tempChilds)
            {
                child.searchForLeader();
            }
        }
    }

    /**
     * Demotes a SortingMember leader to a child of the given leader and sets the leader of its childs
     *
     * @param newLeader The new Leader for this SortingMember and it's childs
     */
    public final void demoteToChild(SortingMemberHandler newLeader)
    {
        setLeader(newLeader);
        if (childs != null && !childs.isEmpty())
        {
            for (SortingMemberHandler child : childs)
            {
                child.setLeader(getLeader());
            }
        }
        childs = null;
    }

    /**
     * @return Boolean if the SortingMember can join a group
     */
    public final boolean canJoinGroup()
    {
        return canJoinGroup;
    }

    /**
     * Filters an ItemStack to all members of the SortingMember group
     *
     * @param itemStack The ItemStack to be filtered to all childs and this SortingMember
     * @param requester The SortingInventoryHandler that requested the filtering
     * @return The ItemStack that was not able to fit in any ISortingInventory
     */
    public final ItemStack filterStackToGroup(ItemStack itemStack, SortingInventoryHandler requester)
    {
        if (childs != null && !childs.isEmpty())
        {
            List<Integer> blackListers = new ArrayList<Integer>();

            //Try to put the ItemStack in a child that passes (whitelisted) the filter
            for (int i = 0; i < childs.size(); i++)
            {
                SortingMemberHandler filteringMember = childs.get(i);
                if (filteringMember == requester)
                {
                    continue;
                }
                if (filteringMember.owner instanceof ISortingInventory)
                {
                    ISortingInventory filteringInventory = (ISortingInventory) filteringMember.owner;

                    if (filteringInventory.getFilter().blacklists)
                    {
                        blackListers.add(i);
                        continue;
                    }

                    if (filteringInventory.getFilter().passesFilter(itemStack))
                    {
                        itemStack = filteringInventory.getSortingInventoryHandler().putInInventory(itemStack);
                        if (itemStack == null || itemStack.stackSize == 0)
                        {
                            return null;
                        }
                    }
                }
            }

            //If this (leader) is an inventory try to put the ItemStack in the inventory if whitelisted
            if (this instanceof SortingInventoryHandler)
            {
                SortingInventoryHandler myInv = (SortingInventoryHandler) this;
                if (!((ISortingInventory)myInv.owner).getFilter().blacklists && ((ISortingInventory)myInv.owner).getFilter().passesFilter(itemStack))
                {
                    itemStack = myInv.putInInventory(itemStack);
                    if (itemStack == null || itemStack.stackSize == 0)
                    {
                        return null;
                    }
                }
            }

            //If the ItemStack can also be put in the requester inventory (it's a blackList Tile), prefer this blacklisted inventory
            if (((ISortingInventory) requester.owner).getFilter().blacklists && ((ISortingInventory) requester.owner).getFilter().passesFilter(itemStack))
            {
                return itemStack;
            }

            //Lastly, try to insert the item in a blacklisting child
            for (int i : blackListers)
            {
                ISortingInventory filteringInventory = (ISortingInventory) childs.get(i).owner;
                if (filteringInventory.getFilter().passesFilter(itemStack))
                {
                    itemStack = filteringInventory.getSortingInventoryHandler().putInInventory(itemStack);
                    if (itemStack == null || itemStack.stackSize == 0)
                    {
                        return null;
                    }
                }
            }

            //Lastly, if this (leader) is an inventory try to put the ItemStack in the inventory not blacklisted
            if (this instanceof SortingInventoryHandler)
            {
                SortingInventoryHandler myInv = (SortingInventoryHandler) this;
                if (((ISortingInventory)myInv.owner).getFilter().blacklists && ((ISortingInventory)myInv.owner).getFilter().passesFilter(itemStack))
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
