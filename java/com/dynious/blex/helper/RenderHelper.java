package com.dynious.blex.helper;

import com.dynious.blex.renderer.ItemRendererIronFilteringChest;
import net.minecraft.client.renderer.ChestItemRenderHelper;

public class RenderHelper
{
    public static void insertChestRenderReplacement()
    {
        ChestItemRenderHelper.instance = new ItemRendererIronFilteringChest();
    }
}
