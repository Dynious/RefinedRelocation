package com.dynious.refinedrelocation.client.renderer;

import com.dynious.refinedrelocation.item.ModItems;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.client.MinecraftForgeClient;
import org.lwjgl.opengl.GL11;

public class ItemRendererToolBox implements IItemRenderer {
    @Override
    public boolean handleRenderType(ItemStack itemStack, ItemRenderType renderType) {
        return true;
    }

    @Override
    public boolean shouldUseRenderHelper(ItemRenderType renderType, ItemStack itemStack, ItemRendererHelper helper) {
        return helper.ordinal() < ItemRendererHelper.EQUIPPED_BLOCK.ordinal();
    }

    @Override
    public void renderItem(ItemRenderType renderType, ItemStack itemStack, Object... data) {
        render(renderType, itemStack);

        ItemStack wrenchStack = ModItems.toolBox.getCurrentWrench(itemStack);
        if(wrenchStack != null) {
            IItemRenderer itemRenderer = MinecraftForgeClient.getItemRenderer(wrenchStack, renderType);
            if(itemRenderer != null) {
                itemRenderer.renderItem(renderType, wrenchStack, data);
            } else {
                render(renderType, wrenchStack);
            }
        }
    }

    public void render(ItemRenderType renderType, ItemStack itemStack) {
        if(renderType == ItemRenderType.INVENTORY) {
            Tessellator tess = Tessellator.instance;
            GL11.glPushMatrix();
            GL11.glDisable(GL11.GL_LIGHTING);
            IIcon icon = itemStack.getIconIndex();
            tess.startDrawingQuads();
            tess.setColorOpaque_I(itemStack.getItem().getColorFromItemStack(itemStack, 0));
            tess.addVertexWithUV(0, 16, 0, icon.getMinU(), icon.getMaxV());
            tess.addVertexWithUV(16, 16, 0, icon.getMaxU(), icon.getMaxV());
            tess.addVertexWithUV(16, 0, 0, icon.getMaxU(), icon.getMinV());
            tess.addVertexWithUV(0, 0, 0, icon.getMinU(), icon.getMinV());
            tess.draw();
            GL11.glEnable(GL11.GL_LIGHTING);
            GL11.glPopMatrix();
        } else {
            Tessellator tess = Tessellator.instance;
            GL11.glPushMatrix();
            IIcon icon = itemStack.getIconIndex();
            tess.startDrawingQuads();
            tess.setColorOpaque_I(itemStack.getItem().getColorFromItemStack(itemStack, 0));
            tess.addVertexWithUV(0, 1, 0, icon.getMaxU(), icon.getMinV());
            tess.addVertexWithUV(1, 1, 0, icon.getMinU(), icon.getMinV());
            tess.addVertexWithUV(1, 0, 0, icon.getMinU(), icon.getMaxV());
            tess.addVertexWithUV(0, 0, 0, icon.getMaxU(), icon.getMaxV());
            tess.draw();
            GL11.glPopMatrix();
        }
    }
}
