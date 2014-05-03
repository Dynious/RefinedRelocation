package com.dynious.refinedrelocation.grid.relocator;

import com.dynious.refinedrelocation.api.tileentity.IRelocator;
import com.dynious.refinedrelocation.grid.Grid;
import com.dynious.refinedrelocation.helper.IOHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;

import java.util.ArrayList;
import java.util.List;

public class RelocatorGrid extends Grid
{
    public TravellingItem findOutput(ItemStack itemStack, IRelocator relocator, int direction)
    {
        List<IRelocator> checkedRelocators = new ArrayList<IRelocator>();

        for (int i = 0; i < relocator.getConnectedInventories().length; i++)
        {
            if (relocator.passesFilter(itemStack, i))
            {
                TileEntity inventory = relocator.getConnectedInventories()[i];
                if (inventory != null)
                {
                    IOHelper.insert(inventory, itemStack, ForgeDirection.getOrientation(direction), true);
                }
            }
        }

        return null;
    }

    public void travelItem(TravellingItem item)
    {

    }
}
