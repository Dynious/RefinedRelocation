package com.dynious.refinedrelocation.mods;

import com.dynious.refinedrelocation.api.SortingInventoryHandler;
import com.dynious.refinedrelocation.tileentity.TileSortingBarrel;
import mcp.mobius.betterbarrels.network.BarrelPacketHandler;
import mcp.mobius.betterbarrels.network.Message0x01ContentUpdate;
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
            System.out.println(added + " : " + itemStack);
            if (added != 0)
            {
                itemStack.stackSize -= added;
                BarrelPacketHandler.INSTANCE.sendToDimension(new Message0x01ContentUpdate(tile), owner.getWorldObj().provider.dimensionId);
                if (itemStack.stackSize == 0)
                    return null;
            }
        }
        return itemStack;
    }
}
