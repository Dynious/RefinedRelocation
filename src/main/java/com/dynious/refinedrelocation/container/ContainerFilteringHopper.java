package com.dynious.refinedrelocation.container;

import com.dynious.refinedrelocation.api.filter.IFilterGUI;
import com.dynious.refinedrelocation.api.filter.IMultiFilter;
import com.dynious.refinedrelocation.api.filter.IMultiFilterChild;
import com.dynious.refinedrelocation.api.tileentity.IMultiFilterTile;
import com.dynious.refinedrelocation.container.slot.SlotHopper;
import com.dynious.refinedrelocation.lib.Strings;
import com.dynious.refinedrelocation.network.NetworkHandler;
import com.dynious.refinedrelocation.network.packet.gui.MessageGUI;
import com.dynious.refinedrelocation.network.packet.gui.MessageGUIBoolean;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.*;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;

public class ContainerFilteringHopper extends ContainerHopper implements IContainerFiltered, IContainerNetworked
{
    protected final ISidedInventory inventory;
    protected IMultiFilterTile tile;
    protected IFilterGUI filter;

    private boolean lastBlacklist = true;
    private boolean initialUpdate = true;

    @SuppressWarnings("unchecked")
    public ContainerFilteringHopper(InventoryPlayer par1InventoryPlayer, IMultiFilterTile filterTile)
    {
        super(par1InventoryPlayer, (IInventory) filterTile);
        this.tile = filterTile;
        this.filter = filterTile.getFilter();
        this.inventory = (ISidedInventory) filterTile;

        for (int i = 0; i < inventory.getSizeInventory(); ++i)
        {
            Slot oldSlot = (Slot) this.inventorySlots.get(i);
            SlotHopper newSlot = new SlotHopper(inventory, oldSlot.getSlotIndex(), oldSlot.xDisplayPosition, oldSlot.yDisplayPosition);
            newSlot.slotNumber = oldSlot.slotNumber;
            this.inventorySlots.set(i, newSlot);
        }
    }

    /**
     * Called when a player shift-clicks on a slot. You must override this or you will crash when someone does that.
     */
    public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int par2)
    {
        ItemStack itemstack = null;
        Slot slot = (Slot) this.inventorySlots.get(par2);

        if (slot != null && slot.getHasStack())
        {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            if (par2 < this.inventory.getSizeInventory())
            {
                if (!this.mergeItemStack(itemstack1, this.inventory.getSizeInventory(), this.inventorySlots.size(), true))
                {
                    return null;
                }
            }
            else if (!tile.getFilter().passesFilter(itemstack1) || !this.mergeItemStack(itemstack1, 0, this.inventory.getSizeInventory(), false))
            {
                return null;
            }

            if (itemstack1.stackSize == 0)
            {
                slot.putStack(null);
            }
            else
            {
                slot.onSlotChanged();
            }
        }

        return itemstack;
    }

    // Need to get any filter changes while in the Hopper Gui so that the clientside slots can filter properly
    @Override
    public void detectAndSendChanges()
    {
        super.detectAndSendChanges();

        for(int i = 0; i < filter.getFilterCount(); i++) {
            IMultiFilterChild filterChild = filter.getFilterAtIndex(i);
            if(initialUpdate || filterChild.isDirty()) {
                for(Object crafter : crafters) {
                    if(crafter instanceof EntityPlayerMP) { // TODO <- is this really necessary? and if it is, it shouldn't be
                        filterChild.sendUpdate((EntityPlayerMP) crafter);
                    }
                }
                filterChild.markDirty(false);
            }
        }

        if(initialUpdate) {
            initialUpdate = false;
        }
    }

    @Override
    public void setPriority(int priority) {}

    @Override
    public IFilterGUI getFilter() {
        return tile.getFilter();
    }

    @Override
    public void onMessageBoolean(int messageId, boolean value, EntityPlayer player) {}

    @Override
    public void onMessageAction(int messageId, EntityPlayer player) {}

    @Override
    public void onMessageString(int messageId, String value, EntityPlayer player) {}

    @Override
    public void onMessageByte(int messageId, byte value, EntityPlayer player) {}

    @Override
    public void onMessageDouble(int messageId, double value, EntityPlayer player) {}

    @Override
    public void onMessageInteger(int messageId, int value, EntityPlayer player) {}

    @Override
    public void onMessageBooleanArray(int messageId, boolean[] values, EntityPlayer player) {}
}
