package com.dynious.refinedrelocation.mods;

import com.dynious.refinedrelocation.api.SortingInventoryHandler;
import com.dynious.refinedrelocation.tileentity.TileSortingBarrel;
import cpw.mods.fml.common.network.PacketDispatcher;
import mcp.mobius.betterbarrels.network.Packet0x01ContentUpdate;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

public class BarrelSortingInventoryHandler extends SortingInventoryHandler
{
    public BarrelSortingInventoryHandler(TileEntity owner)
    {
        super(owner);
    }

    @Override
    public ItemStack putInInventory(ItemStack itemStack)
    {
        if (owner instanceof TileSortingBarrel)
        {
            TileSortingBarrel tile = (TileSortingBarrel) owner;
            int added = tile.getStorage().addStack(itemStack.copy());
            if (added != 0)
            {
                itemStack.stackSize -= added;
                PacketDispatcher.sendPacketToAllInDimension(Packet0x01ContentUpdate.create(tile), tile.worldObj.provider.dimensionId);
                if (itemStack.stackSize == 0)
                    return null;
            }
        }
        return itemStack;
    }
}
