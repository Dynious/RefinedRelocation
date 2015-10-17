package com.dynious.refinedrelocation.tileentity;

import cofh.api.transport.IItemDuct;
import com.dynious.refinedrelocation.api.relocator.IItemRelocator;
import com.dynious.refinedrelocation.grid.relocator.TravellingItem;
import com.dynious.refinedrelocation.lib.Mods;
import cpw.mods.fml.common.Optional;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;

import java.util.List;

@Optional.Interface(iface = "cofh.api.transport.IItemDuct", modid = Mods.COFH_TRANSPORT_API_ID)
public interface IRelocator extends IItemRelocator, IItemDuct
{
    IRelocator[] getConnectedRelocators();

    boolean canConnectOnSide(int side);

    boolean passesFilter(ItemStack itemStack, int side, boolean input, boolean simulate);

    void receiveTravellingItem(TravellingItem item);

    List<TravellingItem> getItems(boolean includeItemsToAdd);

    @SideOnly(Side.CLIENT)
    GuiScreen getGUI(int side, EntityPlayer player);

    Container getContainer(int side, EntityPlayer player);

    byte getRenderType();

    ItemStack getItemStackWithId(byte id);
}
