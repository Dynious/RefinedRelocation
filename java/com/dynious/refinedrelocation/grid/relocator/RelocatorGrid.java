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
    private TravellingItem travellingItem;
    private IRelocator start;

    public TravellingItem findOutput(ItemStack itemStack, IRelocator relocator)
    {
        start = relocator;
        List<IRelocator> checkedRelocators = new ArrayList<IRelocator>();
        PathToRelocator path = new PathToRelocator(relocator, new ArrayList<Byte>());

        //Try to output
        List<PathToRelocator> unckeckedRelocators = tryOutputAndReturnConnections(itemStack, path, checkedRelocators);
        //If an output was found the to-be-checked list is null, now return the travellingItem
        if (unckeckedRelocators == null)
        {
            return travellingItem;
        }

        //While to list of to-be-checked unckeckedRelocators is not empty go on
        while (!unckeckedRelocators.isEmpty())
        {
            for (PathToRelocator pathToRelocator : new ArrayList<PathToRelocator>(unckeckedRelocators))
            {
                //Try to output
                List<PathToRelocator> pathList = tryOutputAndReturnConnections(itemStack, pathToRelocator, checkedRelocators);
                //If an output was found the to-be-checked list is null, now return the travellingItem
                if (pathList == null)
                {
                    return travellingItem;
                }
                //Add all connected Relocators and the Path to it
                unckeckedRelocators.addAll(pathList);
                //Remove the current checked Relocator and Path to it
                unckeckedRelocators.remove(pathToRelocator);
            }
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public List<PathToRelocator> tryOutputAndReturnConnections(ItemStack itemStack, PathToRelocator path, List<IRelocator> checkedRelocators)
    {
        //Try to output the stack to the connected Tiles
        TravellingItem travellingItem = tryToOutput(itemStack, path);
        //If something could be outputted set the travellingItem to the found item and return null (stop searching)
        if (travellingItem != null)
        {
            this.travellingItem = travellingItem;
            return null;
        }
        //Add the Relocator to the checked list, this Relocator will not be checked again if found
        checkedRelocators.add(path.RELOCATOR);

        //Make a new list of the connected Relocators
        List<PathToRelocator> uncheckedRelocators =  new ArrayList<PathToRelocator>();
        for (int i = 0; i < path.RELOCATOR.getConnectedRelocators().length; i++)
        {
            IRelocator relocator1 = path.RELOCATOR.getConnectedRelocators()[i];
            if (relocator1 != null && !checkedRelocators.contains(relocator1))
            {
                //Clone the path to the connected Relocator and add the new side to it
                ArrayList<Byte> newP = (ArrayList<Byte>) path.PATH.clone();
                newP.add((byte) i);
                //Add the path to the unchecked list
                uncheckedRelocators.add(new PathToRelocator(relocator1, newP));
            }
        }
        return uncheckedRelocators;
    }

    @SuppressWarnings("unchecked")
    public TravellingItem tryToOutput(ItemStack itemStack, PathToRelocator path)
    {
        for (int i = 0; i < path.RELOCATOR.getConnectedInventories().length; i++)
        {
            if (path.RELOCATOR.passesFilter(itemStack, i))
            {
                TileEntity inventory = path.RELOCATOR.getConnectedInventories()[i];
                if (inventory != null)
                {
                    //Try to insert
                    ItemStack stack = IOHelper.insert(inventory, itemStack.copy(), ForgeDirection.getOrientation(i).getOpposite(), true);
                    //If we managed to output everything or a part of the stack go on
                    if (stack == null || stack.stackSize <= itemStack.stackSize)
                    {
                        //Create a new path with the side we can output to
                        ArrayList<Byte> newP = (ArrayList<Byte>) path.PATH.clone();
                        newP.add((byte) i);
                        PathToRelocator newPath = new PathToRelocator(path.RELOCATOR, newP);
                        //Invert the stack size (we get back what didn't fit)
                        if (stack != null)
                        {
                            stack.stackSize = itemStack.stackSize - stack.stackSize;
                        }
                        else
                        {
                            stack = itemStack.copy();
                        }
                        return new TravellingItem(stack, start, newPath);
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
