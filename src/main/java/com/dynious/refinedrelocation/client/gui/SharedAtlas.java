package com.dynious.refinedrelocation.client.gui;

import com.dynious.refinedrelocation.client.graphics.TextureAtlas;
import com.dynious.refinedrelocation.client.graphics.TextureRegion;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;

import java.io.IOException;

@SideOnly(Side.CLIENT)
public class SharedAtlas {

    public static TextureAtlas atlas;

    public static void init(IResourceManager resourceManager) {
        try {
            atlas = new TextureAtlas(resourceManager, new ResourceLocation("refinedrelocation", "textures/gui/shared.pack"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static TextureRegion findRegion(String name) {
        return atlas.findRegion(name);
    }

}
