package com.dynious.refinedrelocation.grid.relocator;

import com.dynious.refinedrelocation.api.tileentity.IRelocator;
import com.dynious.refinedrelocation.helper.IOHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;

import java.util.ArrayList;
import java.util.List;

public class RelocatorGridLogic
{
    private static TravellingItem travellingItem;
    private static IRelocator start;
    private static byte startSide;

    public static TravellingItem findOutput(ItemStack itemStack, IRelocator relocator, int side)
    {
        start = relocator;
        startSide = (byte) side;
        List<IRelocator> checkedRelocators = new ArrayList<IRelocator>();
        PathToRelocator path = new PathToRelocator(relocator, new ArrayList<Byte>());

        //Try to output
        List<PathToRelocator> uncheckedRelocators = tryOutputAndReturnConnections(itemStack, path, checkedRelocators, side);
        //If an output was found the to-be-checked list is null, now return the travellingItem
        if (uncheckedRelocators == null)
        {
            return travellingItem;
        }

        //While to list of to-be-checked uncheckedRelocators is not empty go on
        while (!uncheckedRelocators.isEmpty())
        {
            for (PathToRelocator pathToRelocator : new ArrayList<PathToRelocator>(uncheckedRelocators))
            {
                //Try to output
                List<PathToRelocator> pathList = tryOutputAndReturnConnections(itemStack, pathToRelocator, checkedRelocators, -1);
                //If an output was found the to-be-checked list is null, now return the travellingItem
                if (pathList == null)
                {
                    return travellingItem;
                }
                //Add all connected Relocators and the Path to it
                uncheckedRelocators.addAll(pathList);
                //Remove the current checked Relocator and Path to it
                uncheckedRelocators.remove(pathToRelocator);
            }
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    private static List<PathToRelocator> tryOutputAndReturnConnections(ItemStack itemStack, PathToRelocator path, List<IRelocator> checkedRelocators, int excludedOutputSide)
    {
        //Try to output the stack to the connected Tiles
        TravellingItem item = tryToOutput(itemStack, path, excludedOutputSide);
        //If something could be outputted set the travellingItem to the found item and return null (stop searching)
        if (item != null)
        {
            travellingItem = item;
            return null;
        }
        //Add the Relocator to the checked list, this Relocator will not be checked again if found
        checkedRelocators.add(path.RELOCATOR);

        //Make a new list of the connected Relocators
        List<PathToRelocator> uncheckedRelocators =  new ArrayList<PathToRelocator>();
        for (int i = 0; i < path.RELOCATOR.getConnectedRelocators().length; i++)
        {
            IRelocator relocator1 = path.RELOCATOR.getConnectedRelocators()[i];
            if (relocator1 != null && !checkedRelocators.contains(relocator1) && path.RELOCATOR.passesFilter(itemStack, i) && relocator1.passesFilter(itemStack, ForgeDirection.OPPOSITES[i]))
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
    private static TravellingItem tryToOutput(ItemStack itemStack, PathToRelocator path, int excludedSide)
    {
        for (int i = 0; i < path.RELOCATOR.getConnectedInventories().length; i++)
        {
            if (i != excludedSide)
            {
                TileEntity inventory = path.RELOCATOR.getConnectedInventories()[i];
                if (inventory != null)
                {
                    if (path.RELOCATOR.passesFilter(itemStack, i))
                    {
                        //Try to insert
                        ItemStack stack = IOHelper.insert(inventory, itemStack.copy(), ForgeDirection.getOrientation(i).getOpposite(), true);
                        //If we managed to output everything or a part of the stack go on
                        if (stack == null || stack.stackSize < itemStack.stackSize)
                        {
                            //Create a new path with the side we can output to
                            ArrayList<Byte> newPath = (ArrayList<Byte>) path.PATH.clone();
                            newPath.add((byte) i);
                            //Invert the stack size (we get back what didn't fit)
                            if (stack != null)
                            {
                                stack.stackSize = itemStack.stackSize - stack.stackSize;
                            }
                            else
                            {
                                stack = itemStack.copy();
                            }
                            return new TravellingItem(stack, start, newPath, startSide);
                        }
                    }
                }
            }
        }
        return null;
    }
}
