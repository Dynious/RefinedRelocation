package com.dynious.refinedrelocation.grid;

import com.dynious.refinedrelocation.api.tileentity.grid.IGrid;
import com.dynious.refinedrelocation.api.tileentity.handlers.IGridMemberHandler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class Grid implements IGrid
{
    protected ArrayList<IGridMemberHandler> members;

    /**
     * Adds a member to the list of members
     *
     * @param member Member to be added
     */
    public void addMember(IGridMemberHandler member)
    {
        if (members == null)
        {
            members = new ArrayList<IGridMemberHandler>();
        }
        if (!members.contains(member))
        {
            removeDuplicateMember(member);
            members.add(member);
        }
    }

    private void removeDuplicateMember(IGridMemberHandler member)
    {
        for (Iterator<IGridMemberHandler> iterator = members.iterator(); iterator.hasNext(); )
        {
            IGridMemberHandler member1 = iterator.next();
            if (member.getOwner().xCoord == member1.getOwner().xCoord && member.getOwner().yCoord == member1.getOwner().yCoord && member.getOwner().zCoord == member1.getOwner().zCoord)
            {
                iterator.remove();
                return;
            }
        }
    }

    /**
     * Removes a member from the list of members
     *
     * @param member Member to be removed
     */
    public void removeMember(IGridMemberHandler member)
    {
        if (members != null)
        {
            members.remove(member);
        }
    }

    /**
     * Sets grid of all members to null and deletes the member list
     */
    public void resetMembers()
    {
        if (members != null && !members.isEmpty())
        {
            ArrayList<IGridMemberHandler> tempMembers = new ArrayList<IGridMemberHandler>(members);
            members = null;
            for (IGridMemberHandler member : tempMembers)
            {
                member.setGrid(null);
            }
            for (IGridMemberHandler member : tempMembers)
            {
                member.onTileAdded();
            }
        }
    }

    @Override
    public List<IGridMemberHandler> getMembers()
    {
        return Collections.unmodifiableList(members != null ? members : Collections.<IGridMemberHandler>emptyList());
    }

    /**
     * Merges this grid into the give grid
     *
     * @param grid The new Grid for this Grid should merge with
     */
    public void mergeToGrid(IGrid grid)
    {
        if (members != null)
        {
            for (IGridMemberHandler child : members)
            {
                child.setGrid(grid);
            }
        }
        members = null;
    }
}
