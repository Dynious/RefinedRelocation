package com.dynious.refinedrelocation.client.renderer;

import com.dynious.refinedrelocation.lib.Resources;
import com.dynious.refinedrelocation.tileentity.TileSortingAlchemicalChest;
import com.pahimar.ee3.client.renderer.tileentity.TileEntityRendererAlchemicalChest;
import cpw.mods.fml.client.FMLClientHandler;
import net.minecraft.client.model.ModelChest;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.common.util.ForgeDirection;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public class RendererSortingAlchemicalChest extends TileEntityRendererAlchemicalChest
{
    private final ModelChest modelChest = new ModelChest();

    @Override
    public void renderTileEntityAt(TileEntity tileEntity, double x, double y, double z, float tick)
    {
        if (MinecraftForgeClient.getRenderPass() == 0)
        {
            super.renderTileEntityAt(tileEntity, x, y, z, tick);
        }
        else if (tileEntity instanceof TileSortingAlchemicalChest)
        {
            TileSortingAlchemicalChest tileAlchemicalChest = (TileSortingAlchemicalChest) tileEntity;
            ForgeDirection direction = null;

            if (tileAlchemicalChest.getWorldObj() != null)
            {
                direction = tileAlchemicalChest.getOrientation();
            }

            FMLClientHandler.instance().getClient().renderEngine.bindTexture(Resources.MODEL_TEXTURE_OVERLAY_ALCHEMICAL_CHEST);

            GL11.glPushMatrix();
            GL11.glEnable(GL12.GL_RESCALE_NORMAL);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            GL11.glTranslatef((float) x, (float) y + 1.0F, (float) z + 1.0F);
            GL11.glScalef(1.0F, -1.0F, -1.0F);
            GL11.glTranslatef(0.5F, 0.5F, 0.5F);
            short angle = 0;

            if (direction != null)
            {
                if (direction == ForgeDirection.NORTH)
                {
                    angle = 180;
                }
                else if (direction == ForgeDirection.SOUTH)
                {
                    angle = 0;
                }
                else if (direction == ForgeDirection.WEST)
                {
                    angle = 90;
                }
                else if (direction == ForgeDirection.EAST)
                {
                    angle = -90;
                }
            }

            GL11.glRotatef(angle, 0.0F, 1.0F, 0.0F);
            GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
            float adjustedLidAngle = tileAlchemicalChest.prevLidAngle + (tileAlchemicalChest.lidAngle - tileAlchemicalChest.prevLidAngle) * tick;
            adjustedLidAngle = 1.0F - adjustedLidAngle;
            adjustedLidAngle = 1.0F - adjustedLidAngle * adjustedLidAngle * adjustedLidAngle;
            modelChest.chestLid.rotateAngleX = -(adjustedLidAngle * (float) Math.PI / 2.0F);
            modelChest.renderAll();
            GL11.glDisable(GL12.GL_RESCALE_NORMAL);
            GL11.glPopMatrix();
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        }
    }
}
