package com.dynious.blex.mods;

import com.dynious.blex.api.IFilteringInventory;
import com.dynious.blex.block.BlockExtender;
import com.dynious.blex.helper.BlockHelper;
import com.dynious.blex.tileentity.TileBlockExtender;
import mcp.mobius.waila.api.*;
import net.minecraft.item.ItemStack;

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
            strings.add("Connection Direction: " + SpecialChars.TAB + SpecialChars.ALIGNRIGHT + blockExtender.getConnectedDirection().toString());
            if (blockExtender.getConnectedTile() != null)
            {
                strings.add("Connected TileEntity: " + SpecialChars.TAB + SpecialChars.ALIGNRIGHT + BlockHelper.getTileEntityDisplayName(blockExtender.getConnectedTile()));
            }
            else
            {
                strings.add("Not Connected");
            }
            strings.add("Redstone Transmission: " + SpecialChars.TAB + SpecialChars.ALIGNRIGHT + (blockExtender.isRedstoneTransmissionEnabled() ? "Enabled" : "Disabled"));
            List<String> connectionTypes = blockExtender.getConnectionTypes();
            if (!connectionTypes.isEmpty())
            {
                strings.add("Connection Types: " + SpecialChars.TAB + SpecialChars.ALIGNRIGHT + connectionTypes.get(0));
                for (int i = 1; i < connectionTypes.size(); i++)
                {
                    strings.add(SpecialChars.TAB + SpecialChars.ALIGNRIGHT + connectionTypes.get(i));
                }
            }
        }
        if (iWailaDataAccessor.getTileEntity() instanceof IFilteringInventory)
        {
            strings.add((((IFilteringInventory) iWailaDataAccessor.getTileEntity()).getBlackList() ? "Blacklists" : "Whitelists"));
        }
        return strings;
    }

    @Override
    public List<String> getWailaTail(ItemStack itemStack, List<String> strings, IWailaDataAccessor iWailaDataAccessor, IWailaConfigHandler iWailaConfigHandler)
    {
        return strings;
    }

    public static void callbackRegister(IWailaRegistrar registrar){
        WailaProvider instance = new WailaProvider();
        registrar.registerBodyProvider(instance, BlockExtender.class);
        registrar.registerBodyProvider(instance, IFilteringInventory.class);
    }
}
