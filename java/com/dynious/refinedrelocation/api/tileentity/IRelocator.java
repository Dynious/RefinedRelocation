package com.dynious.refinedrelocation.api.tileentity;

import com.dynious.refinedrelocation.grid.relocator.TravellingItem;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

import java.util.List;

public interface IRelocator
{
    public TileEntity[] getConnectedInventories();

    public IRelocator[] getConnectedRelocators();

    public boolean passesFilter(ItemStack itemStack, int side);

    public ItemStack insert(ItemStack itemStack, int side, boolean simulate);

    public void receiveTravellingItem(TravellingItem item);

    public List<TravellingItem> getItems(boolean includeItemsToAdd);

    public TileEntity getTileEntity();

    public GuiScreen getGUI(int side);

    public Container getContainer(int side);
}
