package com.dynious.blex.renderer;

import com.dynious.blex.block.ModBlocks;
import cpw.mods.fml.common.ObfuscationReflectionHelper;
import cpw.mods.ironchest.TileEntityIronChest;
import cpw.mods.ironchest.client.IronChestRenderHelper;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;

import java.util.Map;

public class ItemRendererIronFilteringChest extends IronChestRenderHelper
{
    @Override
    public void renderChest(Block block, int i, float f)
    {
        if (block == ModBlocks.filteringChest && i > 0)
        {
            Map<Integer, TileEntityIronChest> itemRenders = ObfuscationReflectionHelper.getPrivateValue(IronChestRenderHelper.class, this, "itemRenders");
            TileEntityRenderer.instance.renderTileEntityAt(itemRenders.get(i - 1), 0.0D, 0.0D, 0.0D, 0.0F);
        }
        else
        {
            super.renderChest(block, i, f);
        }
    }
}
