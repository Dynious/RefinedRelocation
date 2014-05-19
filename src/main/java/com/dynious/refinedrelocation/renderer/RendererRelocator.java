package com.dynious.refinedrelocation.renderer;

import codechicken.lib.lighting.LightModel;
import codechicken.lib.render.*;
import codechicken.lib.vec.Cuboid6;
import codechicken.lib.vec.Translation;
import com.dynious.refinedrelocation.api.tileentity.IRelocator;
import com.dynious.refinedrelocation.grid.relocator.TravellingItem;
import com.dynious.refinedrelocation.lib.RelocatorData;
import com.dynious.refinedrelocation.lib.Resources;
import com.dynious.refinedrelocation.part.ItemPartRelocator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import org.lwjgl.opengl.GL11;

public class RendererRelocator extends TileEntitySpecialRenderer
{
    public static RendererRelocator instance = new RendererRelocator();

    private static RenderItem renderer;
    private static EntityItem entityItem = new EntityItem(null);

    private static CCModel model;
    private static IconTransformation iconTransformation;

    static
    {
        renderer = new RenderItem()
        {
            public boolean shouldBob()
            {
                return false;
            }

            public boolean shouldSpreadItems()
            {
                return false;
            }
        };
        renderer.setRenderManager(RenderManager.instance);
        entityItem.hoverStart = 0.0F;

        model = CCModel.quadModel(48).generateBox(0, -3.0D, -3.0D, -3.0D, 6.0D, 6.0D, 6.0D, 0.0D, 0.0D, 32.0D, 32.0D, 16.0D);
        CCModel.generateBackface(model, 0, model, 24, 24);
        model.computeNormals().computeLighting(LightModel.standardLightModel);
    }

    @Override
    public void renderTileEntityAt(TileEntity tile, double x, double y, double z, float partialTick)
    {
        IRelocator relocator = (IRelocator) tile;

        resetRenderer(tile.worldObj, (int) x, (int) y, (int) z);

        GL11.glPushMatrix();

        GL11.glPushMatrix();

        model.render(0, 24, new Translation(x, y, z), iconTransformation, null);

        GL11.glPopMatrix();

        GL11.glPushMatrix();

        GL11.glTranslated(x, y, z);
        GL11.glScalef(0.8F, 0.8F, 0.8F);

        for (TravellingItem item : relocator.getItems(false))
        {
            GL11.glPushMatrix();
            entityItem.setEntityItemStack(item.getItemStack());

            float progress = item.getClientSideProgress(partialTick);
            GL11.glTranslated(item.getX(progress), item.getY(progress), item.getZ(progress));

            renderer.doRenderItem(entityItem, 0.0D, 0.0D, 0.0D, 0.0F, 0.0F);

            GL11.glPopMatrix();
        }
        GL11.glPopMatrix();

        GL11.glPopMatrix();
    }

    public static void resetRenderer(IBlockAccess world, int x, int y, int z)
    {
        CCRenderState.reset();
        CCRenderState.setBrightness(world, x, y, z);
        CCRenderState.useModelColours(true);
    }

    public static void loadIcons(IconRegister register)
    {
        iconTransformation = new IconTransformation(register.registerIcon(Resources.MOD_ID + ":" + "relocatorMiddle"));
    }
}
