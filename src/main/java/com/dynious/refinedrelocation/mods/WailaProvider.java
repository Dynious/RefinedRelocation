package com.dynious.refinedrelocation.mods;

import com.dynious.refinedrelocation.block.BlockExtender;
import com.dynious.refinedrelocation.block.BlockRelocator;

import com.dynious.refinedrelocation.lib.Mods;
import com.dynious.refinedrelocation.mods.waila.*;
import codechicken.multipart.BlockMultipart;
import mcp.mobius.waila.api.*;

public class WailaProvider
{
    public static void callbackRegister(IWailaRegistrar registrar)
    {
        registrar.registerBodyProvider(new BlockExtenderHUDHandler(), BlockExtender.class);
        registrar.registerBodyProvider(new RelocatorHUDHandler(), BlockRelocator.class);
        registrar.registerSyncedNBTKey("*", BlockRelocator.class);

        if (Mods.IS_FMP_LOADED)
        {
            registrar.registerBodyProvider(new RelocatorHUDHandler(), BlockMultipart.class);
            registrar.registerSyncedNBTKey("*", BlockMultipart.class);
        }
    }
}
