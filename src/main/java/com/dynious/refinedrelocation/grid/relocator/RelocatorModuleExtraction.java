package com.dynious.refinedrelocation.grid.relocator;

import com.dynious.refinedrelocation.api.filter.IRelocatorModule;
import com.dynious.refinedrelocation.api.tileentity.IRelocator;
import com.dynious.refinedrelocation.helper.IOHelper;
import com.dynious.refinedrelocation.item.ModItems;
import com.dynious.refinedrelocation.lib.Resources;
import com.dynious.refinedrelocation.lib.Settings;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraftforge.common.ForgeDirection;

import java.util.ArrayList;
import java.util.List;

public class RelocatorModuleExtraction implements IRelocatorModule
{
    private static Icon icon;
    private byte tick = 0;
    private int lastCheckedSlot = -1;

    @Override
    public boolean onActivated(IRelocator relocator, EntityPlayer player, int side, ItemStack stack)
    {
        //NO-OP
        return false;
    }

    @Override
    public void onUpdate(IRelocator relocator, int side)
    {
        tick++;
        if (tick >= Settings.RELOCATOR_MIN_TICKS_PER_EXTRACTION)
        {
            TileEntity tile = relocator.getConnectedInventories()[side];
            if (tile instanceof IInventory)
            {
                tryExtraction(relocator, (IInventory) tile, side, lastCheckedSlot);
            }
            tick = 0;
        }
    }

    public void tryExtraction(IRelocator relocator, IInventory inventory, int side, int firstChecked)
    {
        int slot = getNextSlot(inventory, ForgeDirection.getOrientation(side).getOpposite());
        ItemStack stack = inventory.getStackInSlot(slot);
        if (stack != null)
        {
            if (IOHelper.canExtractItemFromInventory(inventory, stack, slot, ForgeDirection.OPPOSITES[side]))
            {
                ItemStack returnedStack = relocator.insert(stack.copy(), side, false);
                if (returnedStack == null || stack.stackSize != returnedStack.stackSize)
                {
                    inventory.setInventorySlotContents(slot, returnedStack);
                }
            }
        }
        else if (firstChecked != lastCheckedSlot)
        {
            if (firstChecked < 0) firstChecked = 0;

            tryExtraction(relocator, inventory, side, firstChecked);
        }
    }

    public int getNextSlot(IInventory inventory, ForgeDirection direction)
    {
        if (inventory instanceof ISidedInventory)
        {
            ISidedInventory isidedinventory = (ISidedInventory)inventory;
            int[] accessibleSlotsFromSide = isidedinventory.getAccessibleSlotsFromSide(direction.ordinal());

            if (lastCheckedSlot < accessibleSlotsFromSide.length - 1)
            {
                lastCheckedSlot++;
            }
            else
            {
                lastCheckedSlot = 0;
            }
            return accessibleSlotsFromSide[lastCheckedSlot];
        }
        else
        {
            if (lastCheckedSlot < inventory.getSizeInventory() - 1)
            {
                lastCheckedSlot++;
            }
            else
            {
                lastCheckedSlot = 0;
            }
            return lastCheckedSlot;
        }
    }

    @Override
    public GuiScreen getGUI(IRelocator relocator)
    {
        return null;
    }

    @Override
    public Container getContainer(IRelocator relocator)
    {
        return null;
    }

    @Override
    public boolean passesFilter(ItemStack stack, boolean input)
    {
        return true;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound)
    {
    }

    @Override
    public void writeToNBT(NBTTagCompound compound)
    {
    }

    @Override
    public List<ItemStack> getDrops(IRelocator relocator, int side)
    {
        List<ItemStack> list = new ArrayList<ItemStack>();
        list.add(new ItemStack(ModItems.relocatorModule, 1, 3));
        return list;
    }

    @Override
    public Icon getIcon(IRelocator relocator, int side)
    {
        return icon;
    }

    @Override
    public void registerIcons(IconRegister register)
    {
        icon = register.registerIcon(Resources.MOD_ID + ":" + "relocatorModuleExtraction");
    }
}
