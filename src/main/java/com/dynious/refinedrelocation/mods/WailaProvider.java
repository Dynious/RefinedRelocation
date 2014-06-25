package com.dynious.refinedrelocation.mods;

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

import java.util.List;
import java.util.ListIterator;
import java.util.ArrayList;

public class WailaProvider implements IWailaDataProvider// IWailaFMPProvider
{
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
        // System.out.println("Block@WailaProvider");
        if (iWailaDataAccessor.getTileEntity() instanceof TileRelocator)
        {
            TileRelocator relocator = (TileRelocator) iWailaDataAccessor.getTileEntity();

            for (int side = 0; side < ForgeDirection.VALID_DIRECTIONS.length; side++)
            {
                private static String orientation = ForgeDirection.getOrientation(side).toString();
                private String[] lines = new String[2];

                IRelocatorModule module = (IRelocatorModule) relocator.getRelocatorModule(side);
                if (module != null)
                {
                    String moduleName = module.getDrops(relocator, side).get(0).getDisplayName();
                    lines[0] = moduleName;

                }

                private final List<ItemStack>[] relocatorStuffedItems = relocator.getStuffedItems();
                for (ListIterator<ItemStack> iterator = relocatorStuffedItems[side].listIterator(); iterator.hasNext();)
                {
                    if (relocatorStuffedItems[i].isEmpty())
                        continue;

                    lines[1] = relocatorStuffedItems[i].get(0).getDisplayName();
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

        if (iWailaDataAccessor.getTileEntity() instanceof TileBlockExtender)
        {
            TileBlockExtender blockExtender = (TileBlockExtender) iWailaDataAccessor.getTileEntity();

            if (blockExtender instanceof TileWirelessBlockExtender)
            {
                TileWirelessBlockExtender wirelessBlockExtender = (TileWirelessBlockExtender) blockExtender;

                if (wirelessBlockExtender.isLinked())
                {
                    strings.add(StatCollector.translateToLocal(Strings.LINKED_TO) + SpecialChars.TAB + BlockHelper.getBlockDisplayName(wirelessBlockExtender.worldObj, wirelessBlockExtender.xConnected, wirelessBlockExtender.yConnected, wirelessBlockExtender.zConnected) + " (" + wirelessBlockExtender.xConnected + ", " + wirelessBlockExtender.yConnected + ", " + wirelessBlockExtender.zConnected + ")");
                }
                else
                {
                    strings.add(StatCollector.translateToLocal(Strings.UNLINKED));
                }
            }
            else
            {
                if (blockExtender.getConnectedTile() != null)
                {
                    strings.add(StatCollector.translateToLocal(Strings.CONNECTED_TO) + SpecialChars.TAB + BlockHelper.getTileEntityDisplayName(blockExtender.getConnectedTile()));
                }
                else
                {
                    strings.add(StatCollector.translateToLocal(Strings.NOT_CONNECTED));
                }
            }

            if (blockExtender.getConnectedDirection() != ForgeDirection.UNKNOWN)
                strings.add(StatCollector.translateToLocal(Strings.FACING) + SpecialChars.TAB + blockExtender.getConnectedDirection().toString());

            if (!(blockExtender instanceof TileWirelessBlockExtender))
                strings.add(StatCollector.translateToLocal(Strings.REDSTONE) + SpecialChars.TAB + (!blockExtender.isRedstoneTransmissionEnabled() ? StatCollector.translateToLocal(Strings.DISABLED) : blockExtender.isRedstoneTransmissionActive() ? StatCollector.translateToLocal(Strings.ACTIVE) : StatCollector.translateToLocal(Strings.INACTIVE)));
        }
        return strings;
    }

    @Override
    public List<String> getWailaTail(ItemStack itemStack, List<String> strings, IWailaDataAccessor iWailaDataAccessor, IWailaConfigHandler iWailaConfigHandler)
    {
        return strings;
    }

    public static void callbackRegister(IWailaRegistrar registrar)
    {
        // WailaProvider instance = new WailaProvider();
        registrar.registerBodyProvider(new WailaProvider(), BlockExtender.class);
        registrar.registerBodyProvider(new WailaProvider(), BlockRelocator.class);
        // registrar.registerBodyProvider(new WailaProvider(), TileRelocator.class);
        // registrar.registerBodyProvider(new WailaProvider(), PartRelocator.class);
    }
}
