package com.dynious.refinedrelocation.mods.WailaProviders;

import com.dynious.refinedrelocation.block.BlockExtender;
import com.dynious.refinedrelocation.block.BlockRelocator;
import com.dynious.refinedrelocation.helper.BlockHelper;
import com.dynious.refinedrelocation.lib.Strings;
import com.dynious.refinedrelocation.lib.Names;
import com.dynious.refinedrelocation.lib.BlockIds;
import com.dynious.refinedrelocation.tileentity.TileBlockExtender;
import com.dynious.refinedrelocation.tileentity.TileRelocator;
import com.dynious.refinedrelocation.tileentity.IRelocator;
import com.dynious.refinedrelocation.tileentity.TileWirelessBlockExtender;
import mcp.mobius.waila.api.*;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.ForgeDirection;
import com.dynious.refinedrelocation.api.relocator.IRelocatorModule;
import com.dynious.refinedrelocation.api.item.IItemRelocatorModule;
import com.dynious.refinedrelocation.grid.relocator.RelocatorModuleRegistry;
import com.dynious.refinedrelocation.part.*;

import codechicken.multipart.BlockMultipart;

import java.util.List;
import java.util.ListIterator;
import java.util.ArrayList;

public class RelocatorHUDHandler implements IWailaDataProvider {
    @Override
    public ItemStack getWailaStack(IWailaDataAccessor iWailaDataAccessor, IWailaConfigHandler iWailaConfigHandler)
    {
        return null;
    }

    @Override
    public List<String> getWailaHead(ItemStack itemStack, List<String> strings, IWailaDataAccessor iWailaDataAccessor, IWailaConfigHandler iWailaConfigHandler)
    {
        return strings;
    }

    @Override
    public List<String> getWailaBody(ItemStack itemStack, List<String> strings, IWailaDataAccessor iWailaDataAccessor, IWailaConfigHandler iWailaConfigHandler)
    {
        if (iWailaDataAccessor.getTileEntity() instanceof TileRelocator)
        {
            TileRelocator relocator = (TileRelocator) iWailaDataAccessor.getTileEntity();

            for (int side = 0; side < ForgeDirection.VALID_DIRECTIONS.length; side++)
            {
                String orientation = ForgeDirection.getOrientation(side).toString();
                String[] lines = new String[2];

                IRelocatorModule module = (IRelocatorModule) relocator.getRelocatorModule(side);
                if (module != null)
                {
                    String moduleName = module.getDrops(relocator, side).get(0).getDisplayName();
                    lines[0] = moduleName;
                }

                List<ItemStack> relocatorStuffedItems = relocator.getStuffedItems()[side];
                for (int itemStackNumber = 0; itemStackNumber < relocatorStuffedItems.size(); itemStackNumber++)
                {
                    ItemStack stuffedItems = relocatorStuffedItems.get(itemStackNumber);
                    lines[1] = stuffedItems.getDisplayName();
                }

                for (int i = 0; i < lines.length; i++) // Display lines
                {
                    String line = lines[i];
                    if (line == "" || line == null) continue;
                    String lineStart = (i == 0 ? orientation + ":" : "") + SpecialChars.TAB;
                    strings.add(lineStart + line);
                }
            }
        }
        return strings;
    }

    @Override
    public List<String> getWailaTail(ItemStack itemStack, List<String> strings, IWailaDataAccessor iWailaDataAccessor, IWailaConfigHandler iWailaConfigHandler)
    {
        return strings;
    }
}
