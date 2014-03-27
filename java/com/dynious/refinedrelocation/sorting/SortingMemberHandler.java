package com.dynious.refinedrelocation.sorting;

import com.dynious.refinedrelocation.api.tileentity.ISortingInventory;
import com.dynious.refinedrelocation.api.tileentity.ISortingMember;
import com.dynious.refinedrelocation.api.tileentity.handlers.ISortingMemberHandler;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;

import java.util.ArrayList;
import java.util.List;

public class SortingMemberHandler implements ISortingMemberHandler
{
    protected TileEntity owner;

    private ISortingMemberHandler leader;
    private ArrayList<ISortingMemberHandler> childs;
    private boolean canJoinGroup = true;

    public SortingMemberHandler(TileEntity owner)
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
    public final void onTileAdded()
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
    public final void onTileDestroyed()
    {
        if (owner.getWorldObj().isRemote)
        {
            return;
        }
        canJoinGroup = false;
        getLeader().removeChild(this);
        getLeader().resetChilds();
    }

    /**
     * Searches for leaders around the TileEntity and demotes leaders if it already has a leader
     */
    private void searchForLeader()
    {
        for (ForgeDirection direction : ForgeDirection.VALID_DIRECTIONS)
        {
            TileEntity tile = owner.getWorldObj().getBlockTileEntity(owner.xCoord + direction.offsetX, owner.yCoord + direction.offsetY, owner.zCoord + direction.offsetZ);
            if (tile != null && tile instanceof ISortingMember)
            {
                ISortingMemberHandler filteringMember = ((ISortingMember) tile).getSortingHandler();
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
    public final ISortingMemberHandler getLeader()
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
    public final void setLeader(ISortingMemberHandler newLeader)
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
    public final void addChild(ISortingMemberHandler child)
    {
        if (childs == null)
        {
            childs = new ArrayList<ISortingMemberHandler>();
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
    public final void removeChild(ISortingMemberHandler child)
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
            ArrayList<ISortingMemberHandler> tempChilds = new ArrayList<ISortingMemberHandler>(childs);
            childs = null;
            for (ISortingMemberHandler child : tempChilds)
            {
                child.setLeader(null);
            }
            for (ISortingMemberHandler child : tempChilds)
            {
                child.onTileAdded();
            }
        }
    }

    /**
     * Demotes a SortingMember leader to a child of the given leader and sets the leader of its childs
     *
     * @param newLeader The new Leader for this SortingMember and it's childs
     */
    public final void demoteToChild(ISortingMemberHandler newLeader)
    {
        setLeader(newLeader);
        if (childs != null && !childs.isEmpty())
        {
            for (ISortingMemberHandler child : childs)
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
     * @return The ItemStack that was not able to fit in any ISortingInventory
     */
    public final ItemStack filterStackToGroup(ItemStack itemStack, TileEntity requester)
    {
        if (childs != null && !childs.isEmpty())
        {
            List<List<ISortingInventory>> sortingList = createSortingList(requester);
            for (List<ISortingInventory> list : sortingList)
            {
                for (ISortingInventory inventory : list)
                {
                    if (inventory.getFilter().passesFilter(itemStack))
                    {
                        itemStack = inventory.putInInventory(itemStack);
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

    private List<List<ISortingInventory>> createSortingList(TileEntity requester)
    {
        List<List<ISortingInventory>> list = new ArrayList<List<ISortingInventory>>();
        for (ISortingInventory.Priority ignored : ISortingInventory.Priority.values())
        {
            list.add(new ArrayList<ISortingInventory>());
        }

        for (ISortingMemberHandler filteringMember : childs)
        {
            if (filteringMember.getOwner() instanceof ISortingInventory)
            {
                ISortingInventory filteringInventory = (ISortingInventory) filteringMember.getOwner();

                if (filteringInventory == requester)
                {
                    list.get(filteringInventory.getPriority().ordinal()).add(0, filteringInventory);
                }
                else
                {
                    list.get(filteringInventory.getPriority().ordinal()).add(filteringInventory);
                }
            }
        }

        if (owner instanceof ISortingInventory)
        {
            ISortingInventory myInventory = (ISortingInventory) this.owner;

            if (myInventory == requester)
            {
                list.get(myInventory.getPriority().ordinal()).add(0, myInventory);
            }
            else
            {
                list.get(myInventory.getPriority().ordinal()).add(myInventory);
            }
        }

        return list;
    }
}
