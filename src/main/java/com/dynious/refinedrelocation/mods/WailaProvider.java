package com.dynious.refinedrelocation.mods;

import com.dynious.refinedrelocation.block.BlockExtender;
import com.dynious.refinedrelocation.block.BlockRelocator;
// import com.dynious.refinedrelocation.helper.BlockHelper;
// import com.dynious.refinedrelocation.lib.Strings;
// import com.dynious.refinedrelocation.lib.Names;
// import com.dynious.refinedrelocation.lib.BlockIds;
// import com.dynious.refinedrelocation.tileentity.TileBlockExtender;
// import com.dynious.refinedrelocation.tileentity.TileRelocator;
// import com.dynious.refinedrelocation.tileentity.IRelocator;
// import com.dynious.refinedrelocation.tileentity.TileWirelessBlockExtender;
// import net.minecraft.item.ItemStack;
// import net.minecraft.util.StatCollector;
// import net.minecraftforge.common.ForgeDirection;
// import com.dynious.refinedrelocation.api.relocator.IRelocatorModule;
// import com.dynious.refinedrelocation.api.item.IItemRelocatorModule;
// import com.dynious.refinedrelocation.grid.relocator.RelocatorModuleRegistry;
// import com.dynious.refinedrelocation.part.*;

import com.dynious.refinedrelocation.mods.WailaProviders.*;
import codechicken.multipart.BlockMultipart;
import mcp.mobius.waila.api.*;

public class WailaProvider
{
    public static void callbackRegister(IWailaRegistrar registrar)
    {
        registrar.registerBodyProvider(new BlockExtenderHUDHandler(), BlockExtender.class);
        registrar.registerBodyProvider(new RelocatorHUDHandler(), BlockRelocator.class);
        registrar.registerBodyProvider(new RelocatorHUDHandler(), BlockMultipart.class);
        registrar.registerSyncedNBTKey("*", BlockMultipart.class);
    }
}
