package com.dynious.refinedrelocation.grid.relocator;

import com.dynious.refinedrelocation.api.relocator.IItemRelocator;
import com.dynious.refinedrelocation.api.relocator.RelocatorModuleBase;
import com.dynious.refinedrelocation.helper.IOHelper;
import com.dynious.refinedrelocation.helper.ItemStackHelper;
import com.dynious.refinedrelocation.item.ModItems;
import com.dynious.refinedrelocation.lib.Names;
import com.dynious.refinedrelocation.lib.Resources;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RelocatorModuleSpread extends RelocatorModuleBase
{
    private static IIcon icon;

    @Override
    public String getDisplayName()
    {
        return StatCollector.translateToLocal(Names.relocatorModule + 8);
    }

    @Override
    public ItemStack outputToSide(IItemRelocator relocator, int side, TileEntity inventory, ItemStack stack, boolean simulate)
    {
        if (simulate || !(inventory instanceof IInventory))
        {
            return super.outputToSide(relocator, side, inventory, stack, simulate);
        }
        else
        {
            IInventory inv = (IInventory) inventory;
            int oppSide = ForgeDirection.OPPOSITES[side];
            List<SlotAndFreeSpace> sortedSlotList = new ArrayList<SlotAndFreeSpace>();
            if (inv instanceof ISidedInventory)
            {
                int[] accessible = ((ISidedInventory) inv).getAccessibleSlotsFromSide(oppSide);
                for (int slot = accessible.length - 1; slot >= 0; slot--)
                {
                    checkAndAddSlot(inv, accessible[slot], oppSide, stack, sortedSlotList);
                }
            }
            else
            {
                for (int slot = inv.getSizeInventory() - 1; slot >= 0; slot--)
                {
                    checkAndAddSlot(inv, slot, oppSide, stack, sortedSlotList);
                }
            }
            if (!sortedSlotList.isEmpty())
            {
                while (sortedSlotList.get(0).freeSpace != 0)
                {
                    for (int i = 0; i < sortedSlotList.size(); i++)
                    {
                        SlotAndFreeSpace slotAndFreeSpace = sortedSlotList.get(i);
                        for (int y = i; y >= 0; y--)
                        {
                            SlotAndFreeSpace slotAndFreeSpace1 = sortedSlotList.get(y);
                            if (slotAndFreeSpace == slotAndFreeSpace1)
                            {
                                if (slotAndFreeSpace.freeSpace != 0)
                                {
                                    addAmount(inv, slotAndFreeSpace1, 1, stack);
                                    if (stack.stackSize <= 0)
                                    {
                                        return null;
                                    }
                                }
                            }
                            else
                            {
                                if (slotAndFreeSpace1.freeSpace > slotAndFreeSpace.freeSpace)
                                {
                                    int added = Math.min(slotAndFreeSpace1.freeSpace - slotAndFreeSpace.freeSpace, stack.stackSize);
                                    addAmount(inv, slotAndFreeSpace1, added, stack);
                                    if (stack.stackSize <= 0)
                                    {
                                        return null;
                                    }
                                }
                            }
                        }
                    }
                }
            }
            return stack;
        }
    }

    public void addAmount(IInventory inv, SlotAndFreeSpace slotAndFreeSpace, int added, ItemStack stack)
    {
        ItemStack stack1 = inv.getStackInSlot(slotAndFreeSpace.SLOT);
        if (stack1 != null)
        {
            stack1.stackSize += added;
        }
        else
        {
            ItemStack newStack = stack.copy();
            newStack.stackSize = added;
            inv.setInventorySlotContents(slotAndFreeSpace.SLOT, newStack);
        }
        slotAndFreeSpace.freeSpace -= added;
        stack.stackSize -= added;
    }

    public void checkAndAddSlot(IInventory inventory, int slot, int side, ItemStack itemStack, List<SlotAndFreeSpace> list)
    {
        if (IOHelper.canInsertItemToInventory(inventory, itemStack, slot, side))
        {
            ItemStack contents = inventory.getStackInSlot(slot);
            if (contents == null)
            {
                list.add(0, new SlotAndFreeSpace(slot, inventory.getInventoryStackLimit()));
            }
            else if (ItemStackHelper.areItemStacksEqual(contents, itemStack))
            {
                int freeSpace = inventory.getInventoryStackLimit() - contents.stackSize;

                for (int i = 0; i < list.size(); i++)
                {
                    if (freeSpace >= list.get(i).freeSpace)
                    {
                        list.add(i, new SlotAndFreeSpace(slot, freeSpace));
                        return;
                    }
                }
                list.add(new SlotAndFreeSpace(slot, freeSpace));
            }
        }
    }


    @Override
    public List<ItemStack> getDrops(IItemRelocator relocator, int side)
    {
        return Arrays.asList(new ItemStack(ModItems.relocatorModule, 1, 8));
    }

    @Override
    public IIcon getIcon(IItemRelocator relocator, int side)
    {
        return icon;
    }

    @Override
    public void registerIcons(IIconRegister register)
    {
        icon = register.registerIcon(Resources.MOD_ID + ":" + "relocatorModuleSpread");
    }

    public static class SlotAndFreeSpace
    {
        public final int SLOT;
        public int freeSpace;

        public SlotAndFreeSpace(int slot, int freeSpace)
        {
            this.SLOT = slot;
            this.freeSpace = freeSpace;
        }
    }
}
