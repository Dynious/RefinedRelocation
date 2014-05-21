package com.dynious.refinedrelocation.helper;

import net.minecraft.world.World;
import net.minecraftforge.common.ForgeChunkManager;

import java.util.List;

public class LoadingCallbackHelper implements ForgeChunkManager.LoadingCallback
{
    @Override
    public void ticketsLoaded(List<ForgeChunkManager.Ticket> tickets, World world)
    {
        //NOOP (We don't want to resubmit, max register length is just 3 seconds)
    }
}
