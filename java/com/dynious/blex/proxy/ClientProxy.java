package com.dynious.blex.proxy;

import com.dynious.blex.lib.BlockIds;
import com.dynious.blex.renderer.ItemRendererBlockExtender;
import com.dynious.blex.renderer.ItemRendererBuffer;
import com.dynious.blex.renderer.RendererBlockExtender;
import com.dynious.blex.renderer.RendererBuffer;
import com.dynious.blex.tileentity.TileBlockExtender;
import com.dynious.blex.tileentity.TileBuffer;
import cpw.mods.fml.client.registry.ClientRegistry;
import net.minecraftforge.client.MinecraftForgeClient;

public class ClientProxy extends CommonProxy
{
    @Override
    public void initTileEntities()
    {
        super.initTileEntities();
        ClientRegistry.bindTileEntitySpecialRenderer(TileBlockExtender.class, new RendererBlockExtender());
        ClientRegistry.bindTileEntitySpecialRenderer(TileBuffer.class, new RendererBuffer());

        MinecraftForgeClient.registerItemRenderer(BlockIds.BLOCK_EXTENDER, new ItemRendererBlockExtender());
        MinecraftForgeClient.registerItemRenderer(BlockIds.BUFFER, new ItemRendererBuffer());
    }
}
