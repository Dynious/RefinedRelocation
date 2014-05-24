package com.dynious.refinedrelocation.renderer;

import codechicken.lib.lighting.LightModel;
import codechicken.lib.render.*;
import codechicken.lib.vec.Translation;
import codechicken.lib.vec.Vector3;
import com.dynious.refinedrelocation.api.tileentity.IRelocator;
import com.dynious.refinedrelocation.grid.relocator.TravellingItem;
import com.dynious.refinedrelocation.lib.RelocatorData;
import com.dynious.refinedrelocation.lib.Resources;
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

    private static CCModel centerModel;
    private static CCModel[] sideModels = new CCModel[6];
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

        //centerModel = CCModel.quadModel(48).generateBox(0, -3.0D, -3.0D, -3.0D, 6.0D, 6.0D, 6.0D, 0.0D, 0.0D, 32.0D, 32.0D, 16.0D);
        centerModel = CCModel.quadModel(48).generateBlock(0, RelocatorData.middleCuboid);
        CCModel.generateBackface(centerModel, 0, centerModel, 24, 24);
        centerModel.computeNormals().computeLighting(LightModel.standardLightModel);

        sideModels[0] = CCModel.quadModel(48).generateBlock(0, RelocatorData.sideCuboids[0]);
        CCModel.generateBackface(sideModels[0], 0, sideModels[0], 24, 24);
        sideModels[0].computeNormals().computeLighting(LightModel.standardLightModel);
        CCModel.generateSidedModels(sideModels, 0, new Vector3(0.5F, 0.5F, 0.5F));
    }

    @Override
    public void renderTileEntityAt(TileEntity tile, double x, double y, double z, float partialTick)
    {
        IRelocator relocator = (IRelocator) tile;

        GL11.glPushMatrix();

        TextureUtils.bindAtlas(0);

        resetRenderer(tile.worldObj, (int) x, (int) y, (int) z);

        GL11.glPushMatrix();

        CCRenderState.startDrawing(7);

        Translation trans = new Translation(x, y, z);

        for (int side = 0; side < 6; side++)
        {
            if (relocator.connectsToSide(side))
            {
                sideModels[side].render(0, 4, trans, iconTransformation, null);
                sideModels[side].render(8, 16, trans, iconTransformation, null);
                sideModels[side].render(24, 4, trans, iconTransformation, null);
                sideModels[side].render(32, 16, trans, iconTransformation, null);
            }
            else
            {
                centerModel.render(side * 4, 4, trans, iconTransformation, null);
                centerModel.render(24 + side * 4, 4, trans, iconTransformation, null);
            }
        }
        CCRenderState.draw();

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
        CCRenderState.useNormals(true);
        CCRenderState.useModelColours(true);
        CCRenderState.setBrightness(world, x, y, z);
    }

    public static void loadIcons(IconRegister register)
    {
        iconTransformation = new IconTransformation(register.registerIcon(Resources.MOD_ID + ":" + "relocatorMiddle"));
    }
}
