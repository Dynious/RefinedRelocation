package com.dynious.refinedrelocation.api.tileentity;

import com.dynious.refinedrelocation.api.filter.IRelocatorModule;
import com.dynious.refinedrelocation.grid.relocator.TravellingItem;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

import java.util.List;

public interface IRelocator
{
    public TileEntity[] getConnectedInventories();

    public IRelocator[] getConnectedRelocators();

    public boolean canConnectOnSide(int side);

    public boolean connectsToSide(int side);

    public boolean isStuffedOnSide(int side);

    public IRelocatorModule getRelocatorModule(int side);

    public boolean passesFilter(ItemStack itemStack, int side, boolean input);

    public ItemStack insert(ItemStack itemStack, int side, boolean simulate);

    public void receiveTravellingItem(TravellingItem item);

    public List<TravellingItem> getItems(boolean includeItemsToAdd);

    public TileEntity getTileEntity();

    public GuiScreen getGUI(int side, EntityPlayer player);

    public Container getContainer(int side, EntityPlayer player);

    public byte getRenderType();
}
