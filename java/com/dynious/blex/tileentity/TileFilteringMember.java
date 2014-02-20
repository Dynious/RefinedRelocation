package com.dynious.blex.tileentity;

import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;

import java.util.ArrayList;

public class TileFilteringMember extends TileEntity
{
    protected boolean isFirstRun = true;

    private TileFilteringMember leader;
    private ArrayList<TileFilteringMember> childs;
    private boolean canJoinGroup = true;

    /**
     * Allows the entity to update its state. Overridden in most subclasses, e.g. the mob spawner uses this to count
     * ticks and creates a new spawn inside its implementation.
     */
    public void updateEntity()
    {
        super.updateEntity();

        if (isFirstRun)
        {
            searchForLeader();
            isFirstRun = false;
        }
    }

    /**
     * Sould be called by OnBlockPlacedBy(...) from its block
     */
    public void onTileAdded()
    {
        searchForLeader();
    }

    /**
     * Sould be called by breakBlock(...) from its block
     */
    public void onTileDestroyed()
    {
        canJoinGroup = false;
        getLeader().removeChild(this);
        getLeader().resetChilds();
    }

    /**
     * Searches for leaders around the TileEntity and demotes leaders if it already has a leader
     */
    public void searchForLeader()
    {
        for (ForgeDirection direction : ForgeDirection.VALID_DIRECTIONS)
        {
            TileEntity tile = worldObj.getBlockTileEntity(xCoord + direction.offsetX, yCoord + direction.offsetY, zCoord + direction.offsetZ);
            if (tile != null && tile instanceof TileFilteringMember)
            {
                TileFilteringMember filteringMember = (TileFilteringMember) tile;
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
    public TileFilteringMember getLeader()
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
    public void setLeader(TileFilteringMember newLeader)
    {
        if (newLeader == this)
        {
            System.out.println("I set myself :(" + this.xCoord);
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
    public void addChild(TileFilteringMember child)
    {
        if (childs == null)
        {
            childs = new ArrayList<TileFilteringMember>();
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
    public void removeChild(TileFilteringMember child)
    {
        if (childs != null)
        {
            childs.remove(child);
        }
    }

    /**
     * Sets leader of all childs to null and deletes the child list
     */
    public void resetChilds()
    {
        if (childs != null && !childs.isEmpty())
        {
            ArrayList<TileFilteringMember> tempChilds = new ArrayList<TileFilteringMember>(childs);
            childs = null;
            for (TileFilteringMember child : tempChilds)
            {
                child.setLeader(null);
            }
            for (TileFilteringMember child : tempChilds)
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
    public void demoteToChild(TileFilteringMember newLeader)
    {
        setLeader(newLeader);
        if (childs != null && !childs.isEmpty())
        {
            for (TileFilteringMember child : childs)
            {
                child.setLeader(getLeader());
            }
        }
        childs = null;
    }

    /**
     * @return Boolean if the FilteringMember can join a group
     */
    public boolean canJoinGroup()
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
            for (TileFilteringMember filteringMember : childs)
            {
                if (filteringMember instanceof TileFilteringInventory)
                {
                    TileFilteringInventory filteringInventory = (TileFilteringInventory) filteringMember;
                    if (filteringInventory.getBlackList() ? !filteringInventory.getFilter().passesFilter(itemStack) : filteringInventory.getFilter().passesFilter(itemStack))
                    {
                        itemStack = filteringInventory.putInInventory(itemStack);
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
