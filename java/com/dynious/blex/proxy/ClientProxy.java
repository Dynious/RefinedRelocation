package com.dynious.blex.proxy;

import com.dynious.blex.lib.BlockIds;
import com.dynious.blex.mods.IronChestHelper;
import com.dynious.blex.renderer.*;
import com.dynious.blex.tileentity.TileBlockExtender;
import com.dynious.blex.tileentity.TileBuffer;
import com.dynious.blex.tileentity.TileFilteringChest;
import com.dynious.blex.tileentity.TileWirelessBlockExtender;
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
