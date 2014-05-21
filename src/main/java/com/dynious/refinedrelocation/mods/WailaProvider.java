package com.dynious.refinedrelocation.mods;

import com.dynious.refinedrelocation.block.BlockExtender;
import com.dynious.refinedrelocation.helper.BlockHelper;
import com.dynious.refinedrelocation.lib.Strings;
import com.dynious.refinedrelocation.tileentity.TileBlockExtender;
import com.dynious.refinedrelocation.tileentity.TileWirelessBlockExtender;
import mcp.mobius.waila.api.*;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.List;

public class WailaProvider implements IWailaDataProvider
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
                    strings.add("Linked To : " /* + SpecialChars.TAB */ + BlockHelper.getBlockDisplayName(wirelessBlockExtender.getWorldObj(), wirelessBlockExtender.xConnected, wirelessBlockExtender.yConnected, wirelessBlockExtender.zConnected) + " (" + wirelessBlockExtender.xConnected + ":" + wirelessBlockExtender.yConnected + ":" + wirelessBlockExtender.zConnected + ")");
                }
                else
                {
                    strings.add("Unlinked");
                }
            }
            else
            {
                if (blockExtender.getConnectedTile() != null)
                {
                    strings.add("Connected To : " /* + SpecialChars.TAB */ + BlockHelper.getTileEntityDisplayName(blockExtender.getConnectedTile()));
                }
                else
                {
                    strings.add("Not Connected");
                }
            }

            if (blockExtender.getConnectedDirection() != ForgeDirection.UNKNOWN)
                strings.add("Facing : " /* + SpecialChars.TAB */ + blockExtender.getConnectedDirection().toString());

            if (!(blockExtender instanceof TileWirelessBlockExtender))
                strings.add("Redstone : " /* + SpecialChars.TAB */ + (!blockExtender.isRedstoneTransmissionEnabled() ? StatCollector.translateToLocal(Strings.DISABLED) : blockExtender.isRedstoneTransmissionActive() ? StatCollector.translateToLocal(Strings.ACTIVE) : StatCollector.translateToLocal(Strings.INACTIVE)));
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
        WailaProvider instance = new WailaProvider();
        registrar.registerBodyProvider(instance, BlockExtender.class);
    }
}
