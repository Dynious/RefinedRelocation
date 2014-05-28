package com.dynious.refinedrelocation.renderer;

import codechicken.lib.render.CCRenderState;
import codechicken.lib.render.TextureUtils;
import codechicken.lib.render.uv.IconTransformation;
import codechicken.lib.vec.Translation;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.IItemRenderer;
import org.lwjgl.opengl.GL11;

public class ItemRendererRelocator implements IItemRenderer
{
    public static IconTransformation iconTransformation = new IconTransformation(Blocks.stone.getIcon(0, 0));

    @Override
    public boolean handleRenderType(ItemStack item, ItemRenderType type)
    {
        return true;
    }

    @Override
    public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper)
    {
        return true;
    }

    @Override
    public void renderItem(ItemRenderType type, ItemStack item, Object... data)
    {
        switch (type)
        {
            case ENTITY:
            {
                render(0.0F, 0.5F, 0.0F, item);
                return;
            }
            case EQUIPPED:
            {
                render(0.5F, 0.5F, 0.5F, item);
                return;
            }
            case EQUIPPED_FIRST_PERSON:
            {
                render(0.5F, 0.5F, 0.3F, item);
                return;
            }
            case INVENTORY:
            {
                render(0.5F, 0.4F, 0.5F, item);
                return;
            }
            default:
                return;
        }
    }

    public void render(float x, float y, float z, ItemStack itemStack)
    {
        GL11.glPushMatrix();

        TextureUtils.bindAtlas(0);
        GL11.glColor3f(1.0F, 1.0F, 1.0F);
        RendererRelocator.resetRenderer();

        CCRenderState.startDrawing();

        Translation trans = new Translation(x, y, z);

        for (int side = 0; side < 6; side++)
        {
            if (side == 0 || side == 1)
            {
                iconTransformation.icon = RendererRelocator.iconSide;

                RendererRelocator.SIDE_MODELS[side].render(0, 48, trans, iconTransformation);
            }
            else
            {
                iconTransformation.icon = RendererRelocator.iconsCenter[1];

                RendererRelocator.CENTER_MODEL.render(side * 4, 4, trans, iconTransformation);
                RendererRelocator.CENTER_MODEL.render(24 + side * 4, 4, trans, iconTransformation);
            }
        }

        CCRenderState.draw();

        GL11.glPopMatrix();
    }
}
