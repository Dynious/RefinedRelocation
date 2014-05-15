package com.dynious.refinedrelocation.event;

import com.dynious.refinedrelocation.network.PacketTypeHandler;
import com.dynious.refinedrelocation.network.packet.PacketTabSync;
import com.dynious.refinedrelocation.grid.FilterStandard;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.IPlayerTracker;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;
import net.minecraft.entity.player.EntityPlayer;

public class PlayerTracker implements IPlayerTracker
{
    @Override
    public void onPlayerLogin(EntityPlayer player)
    {
        if (FMLCommonHandler.instance().getEffectiveSide().isServer())
        {
            PacketDispatcher.sendPacketToPlayer(PacketTypeHandler.populatePacket(new PacketTabSync(FilterStandard.getLabels())), (Player) player);
        }
    }

    @Override
    public void onPlayerLogout(EntityPlayer player)
    {
    }

    @Override
    public void onPlayerChangedDimension(EntityPlayer player)
    {
    }

    @Override
    public void onPlayerRespawn(EntityPlayer player)
    {
    }
}
