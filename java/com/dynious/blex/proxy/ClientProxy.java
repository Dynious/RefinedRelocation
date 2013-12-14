package com.dynious.blex.proxy;

import com.dynious.blex.renderer.RendererBlockExtender;
import com.dynious.blex.tileentity.TileBlockExtender;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;

public class ClientProxy extends CommonProxy
{
    @Override
    public void initTileEntities()
    {
        super.initTileEntities();
        ClientRegistry.bindTileEntitySpecialRenderer(TileBlockExtender.class, new RendererBlockExtender());
    }
}
