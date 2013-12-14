package com.dynious.blex.renderer;

import com.dynious.blex.tileentity.TileBlockExtender;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;

public class RendererBlockExtender extends TileEntitySpecialRenderer
{
    @Override
    public void renderTileEntityAt(TileEntity tileEntity, double v, double v2, double v3, float v4)
    {
        if (tileEntity != null && tileEntity instanceof TileBlockExtender)
        {
            TileBlockExtender tile = (TileBlockExtender)tileEntity;
            
        }
    }
}
