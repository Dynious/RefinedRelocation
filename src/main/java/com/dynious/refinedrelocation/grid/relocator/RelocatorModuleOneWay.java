package com.dynious.refinedrelocation.grid.relocator;

import com.dynious.refinedrelocation.api.filter.IRelocatorModule;
import com.dynious.refinedrelocation.api.tileentity.IRelocator;
import com.dynious.refinedrelocation.item.ModItems;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import java.util.ArrayList;
import java.util.List;

public class RelocatorModuleOneWay implements IRelocatorModule
{
    private boolean inputAllowed = true;

    @Override
    public boolean onActivated(IRelocator relocator, EntityPlayer player, int side, ItemStack stack)
    {
        inputAllowed = !inputAllowed;
        return true;
    }

    @Override
    public void onUpdate(IRelocator relocator, int side)
    {
        //NO-OP
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
        return inputAllowed == input;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound)
    {
        inputAllowed = compound.getBoolean("inputAllowed");
    }

    @Override
    public void writeToNBT(NBTTagCompound compound)
    {
        compound.setBoolean("inputAllowed", inputAllowed);
    }

    @Override
    public List<ItemStack> getDrops(IRelocator relocator, int side)
    {
        List<ItemStack> list = new ArrayList<ItemStack>();
        list.add(new ItemStack(ModItems.relocatorModule, 1, 2));
        return list;
    }
}
