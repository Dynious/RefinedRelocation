package com.dynious.refinedrelocation.mods;

import codechicken.multipart.BlockMultipart;
import com.dynious.refinedrelocation.block.BlockExtender;
import com.dynious.refinedrelocation.block.BlockRelocator;
import com.dynious.refinedrelocation.lib.Mods;
import com.dynious.refinedrelocation.mods.waila.BlockExtenderHUDHandler;
import com.dynious.refinedrelocation.mods.waila.RelocatorHUDHandler;
import mcp.mobius.waila.api.IWailaRegistrar;

public class WailaProvider
{
    public static void callbackRegister(IWailaRegistrar registrar)
    {
        registrar.registerBodyProvider(new BlockExtenderHUDHandler(), BlockExtender.class);
        registrar.registerStackProvider(new RelocatorHUDHandler(), BlockRelocator.class);
        registrar.registerBodyProvider(new RelocatorHUDHandler(), BlockRelocator.class);
        registrar.registerSyncedNBTKey("*", BlockRelocator.class);

        if (Mods.IS_FMP_LOADED)
        {
            registrar.registerStackProvider(new RelocatorHUDHandler(), BlockMultipart.class);
            registrar.registerBodyProvider(new RelocatorHUDHandler(), BlockMultipart.class);
            registrar.registerSyncedNBTKey("*", BlockMultipart.class);
        }
    }
}
