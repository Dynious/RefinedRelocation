package com.dynious.refinedrelocation.api.tileentity;

import buildcraft.api.transport.IPipeTile;
import com.dynious.refinedrelocation.api.filter.IRelocatorModule;
import com.dynious.refinedrelocation.grid.relocator.TravellingItem;
import cpw.mods.fml.common.Optional;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

import java.util.List;

@Optional.Interface(iface = "buildcraft.api.transport.IPipeTile", modid = "BuildCraftAPI|transport")
public interface IRelocator extends IPipeTile
{
    public TileEntity[] getConnectedInventories();

    public IRelocator[] getConnectedRelocators();

    public boolean canConnectOnSide(int side);

    public boolean connectsToSide(int side);

    public boolean isStuffedOnSide(int side);

    public IRelocatorModule getRelocatorModule(int side);

    public boolean getRedstoneState();

    public boolean passesFilter(ItemStack itemStack, int side, boolean input);

    public ItemStack insert(ItemStack itemStack, int side, boolean simulate);

    public void receiveTravellingItem(TravellingItem item);

    public List<TravellingItem> getItems(boolean includeItemsToAdd);

    public TileEntity getTileEntity();

    @SideOnly(Side.CLIENT)
    public GuiScreen getGUI(int side, EntityPlayer player);

    public Container getContainer(int side, EntityPlayer player);

    public byte getRenderType();
}
