package com.dynious.refinedrelocation.compat;

import com.dynious.refinedrelocation.api.filter.IFilter;
import com.dynious.refinedrelocation.tileentity.TileSortingBarrel;
import net.minecraft.item.ItemStack;

public class BarrelFilter implements IFilter
{
    private TileSortingBarrel tile;

    public BarrelFilter(TileSortingBarrel tile)
    {
        this.tile = tile;
    }

    @Override
    public boolean passesFilter(ItemStack itemStack)
    {
        return tile.getStorage().hasItem() && tile.getStorage().sameItem(itemStack);
    }
}
