package com.dynious.blex.proxy;

import com.dynious.blex.lib.BlockIds;
import com.dynious.blex.renderer.ItemRendererBlockExtender;
import com.dynious.blex.renderer.RendererBlockExtender;
import com.dynious.blex.tileentity.TileBlockExtender;
import cpw.mods.fml.client.registry.ClientRegistry;
import net.minecraftforge.client.MinecraftForgeClient;

public class ClientProxy extends CommonProxy
{
    @Override
    public void initTileEntities()
    {
        super.initTileEntities();
        ClientRegistry.bindTileEntitySpecialRenderer(TileBlockExtender.class, new RendererBlockExtender());
        MinecraftForgeClient.registerItemRenderer(BlockIds.BLOCK_EXTENDER, new ItemRendererBlockExtender());
    }
}
