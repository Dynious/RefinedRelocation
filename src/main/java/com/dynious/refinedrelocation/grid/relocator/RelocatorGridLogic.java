package com.dynious.refinedrelocation.grid.relocator;

import com.dynious.refinedrelocation.api.relocator.IRelocatorModule;
import com.dynious.refinedrelocation.helper.IOHelper;
import com.dynious.refinedrelocation.tileentity.IRelocator;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;

public class RelocatorGridLogic
{
    private static List<TileEntity> checkedRelocators = new ArrayList<TileEntity>();

    public static TravellingItem findOutput(ItemStack itemStack, IRelocator relocator, int side)
    {
        PathToRelocator path = new PathToRelocator(relocator, new ArrayList<Byte>(Arrays.asList((byte) side)));

        //Try to output
        ItemOrPaths itemOrPaths = tryOutputAndReturnConnections(itemStack, path, null, side);
        //If an output was found the to-be-checked list is null, now return the travellingItem
        if (itemOrPaths.ITEM != null)
        {
            checkedRelocators.clear();
            return itemOrPaths.ITEM;
        }

        //While to list of to-be-checked uncheckedRelocators is not empty go on
        while (!itemOrPaths.PATHS.isEmpty())
        {
            for (ListIterator<PathToRelocator> iterator = itemOrPaths.PATHS.listIterator(); iterator.hasNext(); )
            {
                PathToRelocator pathToRelocator = iterator.next();
                //Try to output
                ItemOrPaths itemOrPaths2 = tryOutputAndReturnConnections(itemStack, pathToRelocator, itemOrPaths, ForgeDirection.OPPOSITES[pathToRelocator.PATH.get(pathToRelocator.PATH.size() - 1)]);
                //If an output was found the to-be-checked list is null, now return the travellingItem
                if (itemOrPaths2.ITEM != null)
                {
                    checkedRelocators.clear();
                    return itemOrPaths2.ITEM;
                }

                //Remove the current checked Relocator and Path to it
                iterator.remove();

                //Add all connected Relocators and the Path to it
                for (PathToRelocator path2 : itemOrPaths2.PATHS)
                    iterator.add(path2);
            }
        }
        checkedRelocators.clear();
        return null;
    }

    @SuppressWarnings("unchecked")
    private static ItemOrPaths tryOutputAndReturnConnections(ItemStack itemStack, PathToRelocator path, ItemOrPaths currentPaths, int excludedOutputSide)
    {
        //Try to output the stack to the connected Tiles
        TravellingItem item = tryToOutput(itemStack, path, excludedOutputSide);
        //If something could be outputted set the travellingItem to the found item and return null (stop searching)
        if (item != null)
        {
            return new ItemOrPaths(item);
        }
        //Add the Relocator to the checked list, this Relocator will not be checked again if found
        checkedRelocators.add(path.RELOCATOR.getTileEntity());

        //Make a new list of the connected Relocators
        List<PathToRelocator> uncheckedRelocators = new ArrayList<PathToRelocator>();
        for (int i = 0; i < path.RELOCATOR.getConnectedRelocators().length; i++)
        {
            IRelocator relocator1 = path.RELOCATOR.getConnectedRelocators()[i];
            if (relocator1 != null && !checkedRelocators.contains(relocator1.getTileEntity()) && (currentPaths == null || !doesListContainTile(currentPaths.PATHS, relocator1.getTileEntity())) && path.RELOCATOR.passesFilter(itemStack, i, false, true))
            {
                IRelocatorModule module = path.RELOCATOR.getRelocatorModule(i);
                if (module != null && module.isItemDestination())
                {
                    ItemStack stack = module.receiveItemStack(path.RELOCATOR, i, itemStack.copy(), false, true);
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
                        return new ItemOrPaths(new TravellingItem(stack, newPath));
                    }
                    continue;
                }
                if (relocator1.passesFilter(itemStack, ForgeDirection.OPPOSITES[i], true, true))
                {
                    IRelocatorModule module1 = relocator1.getRelocatorModule(ForgeDirection.OPPOSITES[i]);
                    if (module1 != null && module1.isItemDestination())
                    {
                        ItemStack stack = module1.receiveItemStack(relocator1, ForgeDirection.OPPOSITES[i], itemStack.copy(), true, true);
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
                            return new ItemOrPaths(new TravellingItem(stack, newPath));
                        }
                        continue;
                    }
                    //Clone the path to the connected Relocator and add the new side to it
                    ArrayList<Byte> newP = (ArrayList<Byte>) path.PATH.clone();
                    newP.add((byte) i);
                    //Add the path to the unchecked list
                    uncheckedRelocators.add(new PathToRelocator(relocator1, newP));
                }
            }
        }
        return new ItemOrPaths(uncheckedRelocators);
    }

    @SuppressWarnings("unchecked")
    private static TravellingItem tryToOutput(ItemStack itemStack, PathToRelocator path, int excludedSide)
    {
        for (int i = 0; i < path.RELOCATOR.getConnectedInventories().length; i++)
        {
            if (i != excludedSide)
            {
                IRelocatorModule module = path.RELOCATOR.getRelocatorModule(i);
                if (module != null && module.isItemDestination())
                {
                    ItemStack stack = module.receiveItemStack(path.RELOCATOR, i, itemStack.copy(), false, true);
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
                    continue;
                }

                TileEntity inventory = path.RELOCATOR.getConnectedInventories()[i];
                if (inventory != null)
                {
                    if (path.RELOCATOR.passesFilter(itemStack, i, false, true))
                    {
                        ItemStack stack;
                        if (module != null)
                        {
                            stack = module.outputToSide(path.RELOCATOR, i, inventory, itemStack.copy(), true);
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

    /**
     * Checks if the list of Relocators to be checked doesn't already contain the Relocator given
     */
    public static boolean doesListContainTile(List<PathToRelocator> uncheckedRelocators, TileEntity tile)
    {
        for (PathToRelocator path : uncheckedRelocators)
        {
            if (path.RELOCATOR.getTileEntity() == tile)
                return true;
        }
        return false;
    }
}
