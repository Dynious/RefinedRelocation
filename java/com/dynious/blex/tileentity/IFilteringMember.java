package com.dynious.blex.tileentity;

import net.minecraft.item.ItemStack;

public interface IFilteringMember
{
    public void searchForLeader();

    public IFilteringMember getLeader();

    public void setLeader(IFilteringMember newLeader);

    public void addChild(IFilteringMember child);

    public void removeChild(IFilteringMember child);

    public void resetChilds();

    public void demoteToChild(IFilteringMember newLeader);

    public ItemStack filterStackToGroup(ItemStack itemStack);

    public boolean canJoinGroup();
}
