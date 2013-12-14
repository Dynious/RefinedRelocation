package com.dynious.blex.tileentity;

import com.dynious.blex.config.Filter;
import com.dynious.blex.helper.ItemStackHelper;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.oredict.OreDictionary;

public class TileAdvancedFilteredBlockExtender extends TileBlockExtender
{
    public boolean spreadItems = false;
    private byte[] insertDirection;
    private int bestSlot;
    private boolean shouldUpdateBestSlot = true;
    private int lastSlotSide;
    private ItemStack lastStack;
    public boolean blacklist = false;
    public Filter filter = new Filter();

    public TileAdvancedFilteredBlockExtender()
    {
        insertDirection = new byte[ForgeDirection.values().length];
        for (byte i = 0; i < insertDirection.length; i++)
        {
            insertDirection[i] = i;
        }
    }

    public byte[] getInsertDirection()
    {
        return insertDirection;
    }

    public void setInsertDirection(int from, int value)
    {
        if (value > 5) value = 0;
        insertDirection[from] = (byte)value;
    }

    @Override
    public boolean canInsertItem(int i, ItemStack itemStack, int i2)
    {
        if (spreadItems)
        {
            if (lastSlotSide != i || !ItemStackHelper.areItemStacksEqual(itemStack, lastStack)  || shouldUpdateBestSlot)
            {
                updateBestSlot(i2, itemStack);
                shouldUpdateBestSlot = false;
            }
            if (i != bestSlot || !super.canInsertItem(bestSlot, itemStack, i2))
            {
                return false;
            }
            shouldUpdateBestSlot = true;
            return blacklist ? !doesItemStackPassFilter(itemStack): doesItemStackPassFilter(itemStack);
        }
        else
        {
            if (!super.isItemValidForSlot(i, itemStack))
            {
                return false;
            }
            return blacklist ? !doesItemStackPassFilter(itemStack): doesItemStackPassFilter(itemStack);
        }
    }

    private void updateBestSlot(int side, ItemStack itemStack)
    {
        int bestSize = Integer.MAX_VALUE;
        for (int slot = 0; slot < getSizeInventory(); slot++)
        {
            ItemStack stack = getStackInSlot(slot);
            if (!super.canInsertItem(slot, itemStack, side))
            {
                continue;
            }
            if (stack == null)
            {
                bestSlot = slot;
                break;
            }
            if (ItemStackHelper.areItemStacksEqual(itemStack, stack) && stack.stackSize < bestSize)
            {
                bestSlot = slot;
                bestSize = stack.stackSize;
            }
        }
        lastSlotSide = side;
        lastStack = itemStack;
    }

    private boolean doesItemStackPassFilter(ItemStack itemStack)
    {
        String oreName = OreDictionary.getOreName(OreDictionary.getOreID(itemStack));
        if (filter.ingots && oreName.toLowerCase().contains("ingot"))
            return true;
        if (filter.ores && oreName.toLowerCase().contains("ore"))
            return true;
        if(filter.wood && oreName.toLowerCase().contains("wood"))
            return true;
        if (filter.planks && oreName.toLowerCase().contains("plank"))
            return true;
        if (filter.dusts && (oreName.toLowerCase().contains("dust") || oreName.toLowerCase().contains("crushed")))
            return true;

        int index = itemStack.getItem().getCreativeTab().getTabIndex();
        for (int i = 0; i < filter.creativeTabs.length; i++)
        {
            if (filter.creativeTabs[i] && index == i)
            {
                return true;
            }
        }
        return false;
    }

    @Override
    public int[] getAccessibleSlotsFromSide(int i)
    {
        if (inventory != null)
        {
            if (inventory instanceof ISidedInventory)
            {
                return ((ISidedInventory)inventory).getAccessibleSlotsFromSide(insertDirection[i]);
            }
            return accessibleSlots;
        }
        return new int[0];
    }

    @Override
    public void readFromNBT(NBTTagCompound compound)
    {
        super.readFromNBT(compound);
        spreadItems = compound.getBoolean("spreadItems");
        insertDirection = compound.getByteArray("insertDirection");
        blacklist = compound.getBoolean("blacklist");
        filter.readFromNBT(compound);
    }

    @Override
    public void writeToNBT(NBTTagCompound compound)
    {
        super.writeToNBT(compound);
        compound.setBoolean("spreadItems", spreadItems);
        compound.setByteArray("insertDirection", insertDirection);
        compound.setBoolean("blacklist", blacklist);
        filter.writeToNBT(compound);
    }
}
