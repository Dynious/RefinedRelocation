package com.dynious.refinedrelocation.mods.waila;

import com.dynious.refinedrelocation.helper.BlockHelper;
import com.dynious.refinedrelocation.lib.Strings;
import com.dynious.refinedrelocation.tileentity.TileBlockExtender;
import com.dynious.refinedrelocation.tileentity.TileWirelessBlockExtender;
import mcp.mobius.waila.api.*;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.ForgeDirection;

import java.util.List;

public class BlockExtenderHUDHandler implements IWailaDataProvider
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
}
