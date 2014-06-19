package com.dynious.refinedrelocation.grid.relocator;

import com.dynious.refinedrelocation.api.tileentity.IRelocator;
import com.dynious.refinedrelocation.helper.IOHelper;
import com.dynious.refinedrelocation.util.Vector3;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.*;

public class RelocatorGridLogic
{
    private static List<TileEntity> checkedRelocators = new ArrayList<TileEntity>();

    public static TravellingItem findOutput(ItemStack itemStack, IRelocator relocator, int side)
    {
        PathToRelocator path = new PathToRelocator(relocator, new ArrayList<Byte>(Arrays.asList((byte) side)));

        //Try to output
        ItemOrPath itemOrPath = tryOutputAndReturnConnections(itemStack, path, side);
        //If an output was found the to-be-checked list is null, now return the travellingItem
        if (itemOrPath.ITEM != null)
        {
            checkedRelocators.clear();
            return itemOrPath.ITEM;
        }

        //While to list of to-be-checked uncheckedRelocators is not empty go on
        while (!itemOrPath.PATHS.isEmpty())
        {
            for (ListIterator<PathToRelocator> iterator = itemOrPath.PATHS.listIterator(); iterator.hasNext(); )
            {
                PathToRelocator pathToRelocator = iterator.next();
                //Try to output
                ItemOrPath itemOrPath2 = tryOutputAndReturnConnections(itemStack, pathToRelocator, -1);
                //If an output was found the to-be-checked list is null, now return the travellingItem
                if (itemOrPath2.ITEM != null)
                {
                    checkedRelocators.clear();
                    return itemOrPath2.ITEM;
                }

                //Remove the current checked Relocator and Path to it
                iterator.remove();

                //Add all connected Relocators and the Path to it
                for (PathToRelocator path2 : itemOrPath2.PATHS)
                    iterator.add(path2);
            }
        }
        checkedRelocators.clear();
        return null;
    }

    @SuppressWarnings("unchecked")
    private static ItemOrPath tryOutputAndReturnConnections(ItemStack itemStack, PathToRelocator path, int excludedOutputSide)
    {
        //Try to output the stack to the connected Tiles
        TravellingItem item = tryToOutput(itemStack, path, excludedOutputSide);
        //If something could be outputted set the travellingItem to the found item and return null (stop searching)
        if (item != null)
        {
            return new ItemOrPath(item);
        }
        //Add the Relocator to the checked list, this Relocator will not be checked again if found
        checkedRelocators.add(path.RELOCATOR.getTileEntity());

        //Make a new list of the connected Relocators
        List<PathToRelocator> uncheckedRelocators =  new ArrayList<PathToRelocator>();
        for (int i = 0; i < path.RELOCATOR.getConnectedRelocators().length; i++)
        {
            IRelocator relocator1 = path.RELOCATOR.getConnectedRelocators()[i];
            if (relocator1 != null && !checkedRelocators.contains(relocator1.getTileEntity()) && path.RELOCATOR.passesFilter(itemStack, i, false, true) && relocator1.passesFilter(itemStack, ForgeDirection.OPPOSITES[i], true, true))
            {
                //Clone the path to the connected Relocator and add the new side to it
                ArrayList<Byte> newP = (ArrayList<Byte>) path.PATH.clone();
                newP.add((byte) i);
                //Add the path to the unchecked list
                uncheckedRelocators.add(new PathToRelocator(relocator1, newP));
            }
        }
        return new ItemOrPath(uncheckedRelocators);
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
                    if (path.RELOCATOR.passesFilter(itemStack, i, false, true))
                    {
                        ItemStack stack;
                        if (path.RELOCATOR.getRelocatorModule(i) != null)
                        {
                            stack = path.RELOCATOR.getRelocatorModule(i).outputToSide(path.RELOCATOR, i, inventory, itemStack.copy(), true);
                        }
                        else
                        {
                            stack = IOHelper.insert(inventory, itemStack.copy(), ForgeDirection.getOrientation(i).getOpposite(), true);
                        }
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
                            return new TravellingItem(stack, newPath);
                        }
                    }
                }
            }
        }
        return null;
    }
}
