package com.dynious.refinedrelocation.grid.relocator;

import com.dynious.refinedrelocation.api.tileentity.IRelocator;
import com.dynious.refinedrelocation.api.tileentity.grid.IRelocatorGrid;
import com.dynious.refinedrelocation.grid.Grid;
import com.dynious.refinedrelocation.helper.IOHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;

import java.util.ArrayList;
import java.util.List;

public class RelocatorGrid extends Grid implements IRelocatorGrid
{
    private TravellingItem item;

    public TravellingItem findOutput(ItemStack itemStack, IRelocator relocator)
    {
        List<IRelocator> checkedRelocators = new ArrayList<IRelocator>();
        List<Byte> decisions = new ArrayList<Byte>();

        List<IRelocator> relocators = tryOutputAndReturnConnections(itemStack, relocator, decisions, checkedRelocators);
        if (relocators == null)
        {
            return item;
        }
        List<IRelocator> uncheckedRelocators =  new ArrayList<IRelocator>();
        while (!uncheckedRelocators.isEmpty())
        {
            for (IRelocator relocator1 : new ArrayList<IRelocator>(uncheckedRelocators))
            {
                List<IRelocator> relocators1 = tryOutputAndReturnConnections(itemStack, relocator1, decisions, checkedRelocators);
                if (relocators1 == null)
                {
                    return item;
                }
                uncheckedRelocators.addAll(relocators1);
                uncheckedRelocators.remove(relocator1);
            }
        }
        return null;
    }

    public List<IRelocator> tryOutputAndReturnConnections(ItemStack itemStack, IRelocator relocator, List<Byte> decisions, List<IRelocator> checkedRelocators)
    {
        TravellingItem travellingItem = tryToOutput(itemStack, relocator, decisions);
        if (travellingItem != null)
        {
            item = travellingItem;
            return null;
        }
        checkedRelocators.add(relocator);
        List<IRelocator> uncheckedRelocators =  new ArrayList<IRelocator>();

        for (int i = 0; i < relocator.getConnectedRelocators().length; i++)
        {
            IRelocator relocator1 = relocator.getConnectedRelocators()[i];
            if (relocator1 != null && !checkedRelocators.contains(relocator1))
            {
                uncheckedRelocators.add(relocator1);
            }
        }
        return uncheckedRelocators;
    }

    public TravellingItem tryToOutput(ItemStack itemStack, IRelocator relocator, List<Byte> decisions)
    {
        for (int i = 0; i < relocator.getConnectedInventories().length; i++)
        {
            if (relocator.passesFilter(itemStack, i))
            {
                TileEntity inventory = relocator.getConnectedInventories()[i];
                if (inventory != null)
                {
                    ItemStack stack = IOHelper.insert(inventory, itemStack.copy(), ForgeDirection.getOrientation(i).getOpposite(), true);
                    if (stack == null || stack.stackSize <= itemStack.stackSize)
                    {
                        decisions.add((byte) i);
                        if (stack != null)
                        {
                            stack.stackSize = itemStack.stackSize - stack.stackSize;
                        }
                        else
                        {
                            stack = itemStack.copy();
                        }
                        return new TravellingItem(stack, relocator, relocator, decisions);
                    }
                }
            }
        }
        return null;
    }

    public void travelItem(TravellingItem item, int side)
    {

    }
}
