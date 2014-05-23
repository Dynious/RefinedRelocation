package com.dynious.refinedrelocation.renderer;

import com.dynious.refinedrelocation.lib.Resources;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelChest;
import net.minecraftforge.client.MinecraftForgeClient;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import rebelkeithy.mods.metallurgy.machines.chests.TileEntityPreciousChest;
import rebelkeithy.mods.metallurgy.machines.chests.TileEntityPreciousChestRenderer;

public class RendererSortingPreciousChest extends TileEntityPreciousChestRenderer
{
    private ModelChest chestModel = new ModelChest();

    @Override
    public void renderTileEntityChestAt(TileEntityPreciousChest par1TileEntityChest, double par2, double par4, double par6, float par8)
    {
        if (MinecraftForgeClient.getRenderPass() == 0)
        {
            super.renderTileEntityChestAt(par1TileEntityChest, par2, par4, par6, par8);
        }
        else
        {
            float offset = 0;

            int var9;

            if (!par1TileEntityChest.hasWorldObj())
            {
                var9 = 5;
                offset = 0.1f;
            }
            else
            {
                var9 = par1TileEntityChest.getDirection();
            }

            Minecraft.getMinecraft().getTextureManager().bindTexture(Resources.MODEL_TEXTURE_OVERLAY_CHEST);

            GL11.glPushMatrix();
            GL11.glEnable(GL12.GL_RESCALE_NORMAL);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            GL11.glTranslatef((float) par2, (float) par4 + 1.0F, (float) par6 + 1.0F);
            GL11.glScalef(1.0F, -1.0F, -1.0F);
            GL11.glTranslatef(0.5F, 0.5F, 0.5F);
            short var11 = 0;

            if (var9 == 2)
            {
                var11 = 180;
            }

            if (var9 == 3)
            {
                var11 = 0;
            }

            if (var9 == 4)
            {
                var11 = 90;
            }

            if (var9 == 5)
            {
                var11 = -90;
            }

            GL11.glRotatef(var11, 0.0F, 1.0F, 0.0F);
            GL11.glTranslatef(-0.5F, -0.5f + offset, -0.5F);
            float var12 = par1TileEntityChest.prevLidAngle + (par1TileEntityChest.lidAngle - par1TileEntityChest.prevLidAngle) * par8;
            var12 = 1.0F - var12;
            var12 = 1.0F - var12 * var12 * var12;
            chestModel.chestLid.rotateAngleX = -(var12 * (float) Math.PI / 2.0F);
            chestModel.renderAll();
            // GL11.glDisable(GL12.GL_RESCALE_NORMAL);
            GL11.glPopMatrix();
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        }
    }
}
