package com.dynious.refinedrelocation.tileentity;

import com.dynious.refinedrelocation.api.relocator.IItemRelocator;
import com.dynious.refinedrelocation.api.relocator.IRelocatorModule;
import com.dynious.refinedrelocation.grid.relocator.TravellingItem;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

import java.util.List;

public interface IRelocator extends IItemRelocator
{
    public IRelocator[] getConnectedRelocators();

    public boolean canConnectOnSide(int side);

    public boolean passesFilter(ItemStack itemStack, int side, boolean input, boolean simulate);

    public void receiveTravellingItem(TravellingItem item);

    public List<TravellingItem> getItems(boolean includeItemsToAdd);

    @SideOnly(Side.CLIENT)
    public GuiScreen getGUI(int side, EntityPlayer player);

    public Container getContainer(int side, EntityPlayer player);

    public byte getRenderType();

    public List<ItemStack>[] getStuffedItems();
}
