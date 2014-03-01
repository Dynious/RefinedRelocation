package com.dynious.blex.mods;

import com.dynious.blex.block.BlockExtender;
import com.dynious.blex.helper.BlockHelper;
import com.dynious.blex.tileentity.TileBlockExtender;
import com.dynious.blex.tileentity.TileWirelessBlockExtender;
import mcp.mobius.waila.api.*;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.ForgeDirection;

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

                // canConnect actually returns an isConnected equivalent
                if (wirelessBlockExtender.canConnect())
                {
                    strings.add("Linked To : " + SpecialChars.TAB + BlockHelper.getTileEntityDisplayName(wirelessBlockExtender.getConnectedTile()) + " (" + wirelessBlockExtender.xConnected + ":" + wirelessBlockExtender.yConnected + ":" + wirelessBlockExtender.zConnected + ")");
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
                    strings.add("Connected To : " + SpecialChars.TAB + BlockHelper.getTileEntityDisplayName(blockExtender.getConnectedTile()));
                }
                else
                {
                    strings.add("Not Connected");
                }
            }

            if (blockExtender.getConnectedDirection() != ForgeDirection.UNKNOWN)
                strings.add("Facing : " + SpecialChars.TAB + blockExtender.getConnectedDirection().toString());

            if (!(blockExtender instanceof TileWirelessBlockExtender))
                strings.add("Redstone : " + SpecialChars.TAB + (!blockExtender.isRedstoneTransmissionEnabled() ? "Disabled" : blockExtender.isRedstoneTransmissionActive() ? "Powered" : "Unpowered"));
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
