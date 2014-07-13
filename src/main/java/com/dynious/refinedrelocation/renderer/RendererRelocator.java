package com.dynious.refinedrelocation.renderer;

import codechicken.lib.lighting.LightModel;
import codechicken.lib.render.CCModel;
import codechicken.lib.render.CCRenderState;
import codechicken.lib.render.TextureUtils;
import codechicken.lib.render.uv.IconTransformation;
import codechicken.lib.vec.Translation;
import codechicken.lib.vec.Vector3;
import com.dynious.refinedrelocation.api.relocator.IRelocatorModule;
import com.dynious.refinedrelocation.grid.relocator.TravellingItem;
import com.dynious.refinedrelocation.lib.Resources;
import com.dynious.refinedrelocation.tileentity.IRelocator;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import org.lwjgl.opengl.GL11;

public class RendererRelocator extends TileEntitySpecialRenderer
{
    public static RendererRelocator instance = new RendererRelocator();

    private static RenderItem renderer;
    private static EntityItem entityItem = new EntityItem(null);

    public static final CCModel CENTER_MODEL;
    public static final CCModel[] SIDE_MODELS = new CCModel[6];

    private static IconTransformation iconTransformation = new IconTransformation(Blocks.stone.getIcon(0, 0));
    public static IIcon[] iconsCenter = new IIcon[4];
    public static IIcon iconSide;
    public static IIcon iconSideStuffed;

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

        CENTER_MODEL = CCModel.quadModel(48).generateBox(0, -4.0D, -4.0D, -4.0D, 8.0D, 8.0D, 8.0D, 0.0D, 0.0D, 32.0D, 32.0D, 16.0D);
        //CENTER_MODEL = CCModel.quadModel(48).generateBlock(0, RelocatorData.middleCuboid);
        CCModel.generateBackface(CENTER_MODEL, 0, CENTER_MODEL, 24, 24);
        CENTER_MODEL.computeNormals().computeLighting(LightModel.standardLightModel);

        SIDE_MODELS[1] = CCModel.quadModel(48).generateBox(0, -4.0D, 4.0D, -4.0D, 8.0D, 4.0D, 8.0D, 0.0D, 0.0D, 32.0D, 32.0D, 16.0D);
        //SIDE_MODELS[0] = CCModel.quadModel(48).generateBlock(0, RelocatorData.sideCuboids[0]);
        CCModel.generateBackface(SIDE_MODELS[1], 0, SIDE_MODELS[1], 24, 24);
        CCModel.generateSidedModels(SIDE_MODELS, 1, new Vector3());
        for (CCModel model : SIDE_MODELS)
        {
            model.computeNormals().computeLighting(LightModel.standardLightModel);
        }
    }

    @Override
    public void renderTileEntityAt(TileEntity tile, double x, double y, double z, float partialTick)
    {
        IRelocator relocator = (IRelocator) tile;

        GL11.glPushMatrix();

        TextureUtils.bindAtlas(0);
        GL11.glColor3f(1.0F, 1.0F, 1.0F);

        resetRenderer();

        GL11.glPushMatrix();

        CCRenderState.startDrawing();

        Translation trans = new Translation(x + 0.5F, y + 0.5F, z + 0.5F);

        for (int side = 0; side < 6; side++)
        {
            if (relocator.connectsToSide(side))
            {
                //Render Module textures
                IRelocatorModule module = relocator.getRelocatorModule(side);
                if (module != null)
                {
                    iconTransformation.icon = module.getIcon(relocator, side);
                    SIDE_MODELS[side].render(0, 4, trans, iconTransformation);
                    SIDE_MODELS[side].render(8, 24, trans, iconTransformation);
                    SIDE_MODELS[side].render(24, 28, trans, iconTransformation);
                    SIDE_MODELS[side].render(32, 48, trans, iconTransformation);
                }

                //Render other parts
                if (relocator.isStuffedOnSide(side))
                {
                    iconTransformation.icon = iconSideStuffed;
                }
                else
                {
                    iconTransformation.icon = iconSide;
                }
                SIDE_MODELS[side].render(0, 4, trans, iconTransformation);
                SIDE_MODELS[side].render(8, 24, trans, iconTransformation);
                SIDE_MODELS[side].render(24, 28, trans, iconTransformation);
                SIDE_MODELS[side].render(32, 48, trans, iconTransformation);
            }
            else
            {
                switch(relocator.getRenderType())
                {
                    case 0:
                        iconTransformation.icon = iconsCenter[0];
                        break;
                    case 1:
                        iconTransformation.icon = iconsCenter[1];
                        break;
                    case 2:
                        iconTransformation.icon = iconsCenter[2];
                        break;
                    case 3:
                        iconTransformation.icon = iconsCenter[3];
                        break;
                }
                CENTER_MODEL.render(side * 4, side * 4 + 4, trans, iconTransformation);
                CENTER_MODEL.render(24 + side * 4, 24 + side * 4 + 4, trans, iconTransformation);
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

            renderer.doRender(entityItem, 0.0D, 0.0D, 0.0D, 0.0F, 0.0F);

            GL11.glPopMatrix();
        }
        GL11.glPopMatrix();

        GL11.glPopMatrix();
    }

    public static void resetRenderer()
    {
        CCRenderState.reset();
        CCRenderState.hasNormal = false;
        CCRenderState.hasColour = true;
    }

    public static void loadIcons(IIconRegister register)
    {
        iconsCenter[0] = register.registerIcon(Resources.MOD_ID + ":" + "relocatorCenter0");
        iconsCenter[1] = register.registerIcon(Resources.MOD_ID + ":" + "relocatorCenter1");
        iconsCenter[2] = register.registerIcon(Resources.MOD_ID + ":" + "relocatorCenter2");
        iconsCenter[3] = register.registerIcon(Resources.MOD_ID + ":" + "relocatorCenter3");
        iconSide = register.registerIcon(Resources.MOD_ID + ":" + "relocatorSide");
        iconSideStuffed = register.registerIcon(Resources.MOD_ID + ":" + "relocatorSideStuffed");
    }
}
