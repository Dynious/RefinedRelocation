package com.dynious.refinedrelocation.mods;

import codechicken.multipart.BlockMultipart;
import com.dynious.refinedrelocation.api.tileentity.IFilterTile;
import com.dynious.refinedrelocation.block.*;
import com.dynious.refinedrelocation.lib.Mods;
import com.dynious.refinedrelocation.mods.waila.*;
import com.dynious.refinedrelocation.tileentity.IAdvancedTile;
import com.dynious.refinedrelocation.tileentity.IDisguisable;
import mcp.mobius.waila.api.IWailaRegistrar;

public class WailaProvider
{
    public static void callbackRegister(IWailaRegistrar registrar)
    {
        registrar.registerBodyProvider(new BlockExtenderHUDHandler(), BlockExtender.class);
        registrar.registerBodyProvider(new PowerLimiterHUDHandler(), BlockPowerLimiter.class);
        registrar.registerBodyProvider(new SortingInterfaceHUDHandler(), BlockSortingConnector.class);
        registrar.registerBodyProvider(new RelocationControllerHUDHandler(), BlockRelocationController.class);

        registrar.registerStackProvider(new RelocatorHUDHandler(), BlockRelocator.class);
        registrar.registerBodyProvider(new RelocatorHUDHandler(), BlockRelocator.class);

        registrar.registerBodyProvider(new ITileHUDHandler(), IFilterTile.class);
        registrar.registerBodyProvider(new ITileHUDHandler(), IAdvancedTile.class);
        registrar.registerBodyProvider(new ITileHUDHandler(), IDisguisable.class);

        registrar.registerSyncedNBTKey("*", BlockRelocator.class);
        registrar.registerSyncedNBTKey("*", BlockPowerLimiter.class);
        registrar.registerSyncedNBTKey("*", IFilterTile.class);
        registrar.registerSyncedNBTKey("*", IAdvancedTile.class);
        registrar.registerSyncedNBTKey("*", BlockRelocationController.class);

        if (Mods.IS_FMP_LOADED)
        {
            registrar.registerStackProvider(new RelocatorHUDHandler(), BlockMultipart.class);
            registrar.registerBodyProvider(new RelocatorHUDHandler(), BlockMultipart.class);
            registrar.registerSyncedNBTKey("*", BlockMultipart.class);
        }
    }
}
