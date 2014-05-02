package com.dynious.refinedrelocation.sorting;

import com.dynious.refinedrelocation.api.tileentity.ISortingInventory;
import com.dynious.refinedrelocation.api.tileentity.handlers.ISortingInventoryHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.ContainerPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;

import java.util.ArrayList;
import java.util.List;

public class SortingInventoryHandler extends SortingMemberHandler implements ISortingInventoryHandler
{
    protected ISortingInventory inventory;

    public SortingInventoryHandler(TileEntity owner)
    {
        super(owner);
        this.inventory = (ISortingInventory) owner;
    }

    /**
     * Sets the given item stack to the specified slot in the inventory (can be crafting or armor sections).
     */
    public final void setInventorySlotContents(int par1, ItemStack itemStack)
    {
        inventory.putStackInSlot(itemStack, par1);
        inventory.markDirty();

        if (itemStack == null || owner.getWorldObj().isRemote)
            return;

        itemStack = getLeader().filterStackToGroup(itemStack.copy(), this.owner, par1);

        if (itemStack == null || itemStack.stackSize == 0)
        {
            inventory.putStackInSlot(null, par1);
            syncInventory(par1);
        }
    }

    private void syncInventory(int slot)
    {
        float checkSize = 5.0F;
        List list = this.owner.getWorldObj().getEntitiesWithinAABB(EntityPlayer.class, AxisAlignedBB.getAABBPool().getAABB((float) this.owner.xCoord - checkSize, (float) this.owner.yCoord - checkSize, (float) this.owner.zCoord - checkSize, (float) this.owner.xCoord + 1 + checkSize, (float) this.owner.yCoord + 1 + checkSize, (float) this.owner.zCoord + 1 + checkSize));

        for (Object aList : list)
        {
            EntityPlayer player = (EntityPlayer) aList;
            if (!(player.openContainer instanceof ContainerPlayer) && slot < player.openContainer.inventoryItemStacks.size() && player.openContainer.inventoryItemStacks.get(slot) == null)
                ((EntityPlayerMP) player).sendSlotContents(player.openContainer, slot, null);
        }
    }
}
