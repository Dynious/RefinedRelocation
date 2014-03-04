package com.dynious.refinedrelocation.proxy;

import com.dynious.refinedrelocation.lib.BlockIds;
import com.dynious.refinedrelocation.mods.IronChestHelper;
import com.dynious.refinedrelocation.renderer.*;
import com.dynious.refinedrelocation.tileentity.TileBlockExtender;
import com.dynious.refinedrelocation.tileentity.TileBuffer;
import com.dynious.refinedrelocation.tileentity.TileFilteringChest;
import com.dynious.refinedrelocation.tileentity.TileWirelessBlockExtender;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.Loader;
import net.minecraftforge.client.MinecraftForgeClient;

public class ClientProxy extends CommonProxy
{
    @Override
    public void initTileEntities()
    {
        super.initTileEntities();
        ClientRegistry.bindTileEntitySpecialRenderer(TileBlockExtender.class, new RendererBlockExtender());
        ClientRegistry.bindTileEntitySpecialRenderer(TileWirelessBlockExtender.class, new RendererWirelessBlockExtender());
        ClientRegistry.bindTileEntitySpecialRenderer(TileBuffer.class, new RendererBuffer());
        ClientRegistry.bindTileEntitySpecialRenderer(TileFilteringChest.class, new RendererFilteringChest());

        MinecraftForgeClient.registerItemRenderer(BlockIds.BLOCK_EXTENDER, new ItemRendererBlockExtender());
        MinecraftForgeClient.registerItemRenderer(BlockIds.BUFFER, new ItemRendererBuffer());
        MinecraftForgeClient.registerItemRenderer(BlockIds.FILTERING_CHEST, new ItemRendererFilteringChest());

        if (Loader.isModLoaded("IronChest"))
        {
            IronChestHelper.addIronChestRenders();
        }
    }
}
