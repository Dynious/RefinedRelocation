package com.dynious.refinedrelocation.mods;

import com.dynious.refinedrelocation.api.IFilter;
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
        return tile.getStorage().sameItem(itemStack);
    }
}
