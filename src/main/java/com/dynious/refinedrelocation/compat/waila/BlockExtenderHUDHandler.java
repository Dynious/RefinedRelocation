package com.dynious.refinedrelocation.compat.waila;

import com.dynious.refinedrelocation.helper.BlockHelper;
import com.dynious.refinedrelocation.helper.StringHelper;
import com.dynious.refinedrelocation.lib.Strings;
import com.dynious.refinedrelocation.tileentity.TileBlockExtender;
import com.dynious.refinedrelocation.tileentity.TileWirelessBlockExtender;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import mcp.mobius.waila.api.IWailaDataProvider;
import mcp.mobius.waila.api.SpecialChars;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.List;

public class BlockExtenderHUDHandler implements IWailaDataProvider
{
    private static String getLocalizedRedstoneState(TileBlockExtender blockExtender)
    {
        if (blockExtender.isRedstoneTransmissionEnabled())
        {
            return blockExtender.isRedstoneTransmissionActive() ? StatCollector.translateToLocal(Strings.ACTIVE) : StatCollector.translateToLocal(Strings.INACTIVE);
        }
        else
        {
            return StatCollector.translateToLocal(Strings.DISABLED);
        }
    }

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
                    strings.add(StatCollector.translateToLocal(Strings.LINKED_TO) + SpecialChars.TAB +
                            BlockHelper.getBlockDisplayName(wirelessBlockExtender.getWorldObj(), wirelessBlockExtender.xConnected, wirelessBlockExtender.yConnected, wirelessBlockExtender.zConnected) +
                            " " + StringHelper.formatCoordinates(wirelessBlockExtender.xConnected, wirelessBlockExtender.yConnected, wirelessBlockExtender.zConnected));
                }
                else
                {
                    strings.add(StatCollector.translateToLocal(Strings.UNLINKED));
                }
            }
            else
            {
                if (blockExtender.hasConnection())
                {
                    strings.add(StatCollector.translateToLocal(Strings.CONNECTED_TO) + SpecialChars.TAB + BlockHelper.getTileEntityDisplayName(blockExtender.getConnectedTile()));
                }
                else
                {
                    strings.add(StatCollector.translateToLocal(Strings.NOT_CONNECTED));
                }

                if (blockExtender.getConnectedDirection() != ForgeDirection.UNKNOWN)
                    strings.add(StatCollector.translateToLocal(Strings.FACING) + SpecialChars.TAB + StringHelper.getLocalizedDirection(blockExtender.getConnectedDirection()));

                strings.add(StatCollector.translateToLocal(Strings.REDSTONE) + SpecialChars.TAB + getLocalizedRedstoneState(blockExtender));
            }

        }
        return strings;
    }

    @Override
    public List<String> getWailaTail(ItemStack itemStack, List<String> strings, IWailaDataAccessor iWailaDataAccessor, IWailaConfigHandler iWailaConfigHandler)
    {
        return strings;
    }

    @Override
    public NBTTagCompound getNBTData(EntityPlayerMP entityPlayerMP, TileEntity tileEntity, NBTTagCompound nbtTagCompound, World world, int i, int i1, int i2)
    {
        if (tileEntity != null)
            tileEntity.writeToNBT(nbtTagCompound);
        return nbtTagCompound;
    }
}
