package com.dynious.refinedrelocation.client.renderer;

import com.dynious.refinedrelocation.tileentity.TileRelocationPortal;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.nio.FloatBuffer;
import java.util.Random;

public class RendererRelocationPortal extends TileEntitySpecialRenderer
{
    private static final ResourceLocation enderPortalEndSkyTextures = new ResourceLocation("textures/environment/end_sky.png");
    private static final ResourceLocation endPortalTextures = new ResourceLocation("textures/entity/end_portal.png");

    FloatBuffer buffer = GLAllocation.createDirectFloatBuffer(16);
    Random random = new Random(31100L);

    public void renderEndPortalTileEntity(TileEntity tileEntity, double par2, double par4, double par6)
    {
        float f1 = (float) this.field_147501_a.field_147560_j;
        float f2 = (float) this.field_147501_a.field_147561_k;
        float f3 = (float) this.field_147501_a.field_147558_l;
        GL11.glDisable(GL11.GL_LIGHTING);
        float f4 = 0.99F;

        for (int i = 0; i < 16; ++i)
        {
            GL11.glPushMatrix();
            float f5 = (float) (16 - i);
            float f6 = 0.0625F;
            float f7 = 1.0F / (f5 + 1.0F);

            if (i == 0)
            {
                this.bindTexture(enderPortalEndSkyTextures);
                f7 = 0.1F;
                f5 = 65.0F;
                f6 = 0.125F;
                GL11.glEnable(GL11.GL_BLEND);
                GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            }

            if (i == 1)
            {
                this.bindTexture(endPortalTextures);
                GL11.glEnable(GL11.GL_BLEND);
                GL11.glBlendFunc(GL11.GL_ONE, GL11.GL_ONE);
                f6 = 0.5F;
            }

            float f8 = (float) (-(par4 + (double) f4));
            float f9 = f8 + ActiveRenderInfo.objectY;
            float f10 = f8 + f5 + ActiveRenderInfo.objectY;
            float f11 = f9 / f10;
            f11 += (float) (par4 + (double) f4);
            GL11.glTranslatef(f1, f11, f3);
            GL11.glTexGeni(GL11.GL_S, GL11.GL_TEXTURE_GEN_MODE, GL11.GL_OBJECT_LINEAR);
            GL11.glTexGeni(GL11.GL_T, GL11.GL_TEXTURE_GEN_MODE, GL11.GL_OBJECT_LINEAR);
            GL11.glTexGeni(GL11.GL_R, GL11.GL_TEXTURE_GEN_MODE, GL11.GL_OBJECT_LINEAR);
            GL11.glTexGeni(GL11.GL_Q, GL11.GL_TEXTURE_GEN_MODE, GL11.GL_EYE_LINEAR);
            GL11.glTexGen(GL11.GL_S, GL11.GL_OBJECT_PLANE, this.func_76907_a(1.0F, 0.0F, 0.0F, 0.0F));
            GL11.glTexGen(GL11.GL_T, GL11.GL_OBJECT_PLANE, this.func_76907_a(0.0F, 0.0F, 1.0F, 0.0F));
            GL11.glTexGen(GL11.GL_R, GL11.GL_OBJECT_PLANE, this.func_76907_a(0.0F, 0.0F, 0.0F, 1.0F));
            GL11.glTexGen(GL11.GL_Q, GL11.GL_EYE_PLANE, this.func_76907_a(0.0F, 1.0F, 0.0F, 0.0F));
            GL11.glEnable(GL11.GL_TEXTURE_GEN_S);
            GL11.glEnable(GL11.GL_TEXTURE_GEN_T);
            GL11.glEnable(GL11.GL_TEXTURE_GEN_R);
            GL11.glEnable(GL11.GL_TEXTURE_GEN_Q);
            GL11.glPopMatrix();
            GL11.glMatrixMode(GL11.GL_TEXTURE);
            GL11.glPushMatrix();
            GL11.glLoadIdentity();
            GL11.glTranslatef(0.0F, (float) (Minecraft.getSystemTime() % 700000L) / 700000.0F, 0.0F);
            GL11.glScalef(f6, f6, f6);
            GL11.glTranslatef(0.5F, 0.5F, 0.0F);
            GL11.glRotatef((float) (i * i * 4321 + i * 9) * 2.0F, 0.0F, 0.0F, 1.0F);
            GL11.glTranslatef(-0.5F, -0.5F, 0.0F);
            GL11.glTranslatef(-f1, -f3, -f2);
            f9 = f8 + ActiveRenderInfo.objectY;
            GL11.glTranslatef(ActiveRenderInfo.objectX * f5 / f9, ActiveRenderInfo.objectZ * f5 / f9, -f2);
            Tessellator tessellator = Tessellator.instance;

            float f12;
            float f13;
            TileEntity tile = tileEntity.getWorldObj().getTileEntity(tileEntity.xCoord, tileEntity.yCoord - 1, tileEntity.zCoord);
            if (tile == null || !(tile instanceof TileRelocationPortal))
            {
                //Draw lower
                tessellator.startDrawingQuads();
                f11 = random.nextFloat() * 0.5F + 0.1F;
                f12 = random.nextFloat() * 0.5F + 0.4F;
                f13 = random.nextFloat() * 0.5F + 0.5F;

                if (i == 0)
                {
                    f13 = 1.0F;
                    f12 = 1.0F;
                    f11 = 1.0F;
                }

                tessellator.setColorRGBA_F(f11 * f7, f12 * f7, f13 * f7, 1.0F);
                tessellator.addVertex(par2, par4 + (1 - f4), par6);
                tessellator.addVertex(par2, par4 + (1 - f4), par6 + 1.0D);
                tessellator.addVertex(par2 + 1.0D, par4 + (1 - f4), par6 + 1.0D);
                tessellator.addVertex(par2 + 1.0D, par4 + (1 - f4), par6);
                tessellator.draw();
            }

            //Draw -x
            tessellator.startDrawingQuads();
            f11 = random.nextFloat() * 0.5F + 0.1F;
            f12 = random.nextFloat() * 0.5F + 0.4F;
            f13 = random.nextFloat() * 0.5F + 0.5F;

            if (i == 0)
            {
                f13 = 1.0F;
                f12 = 1.0F;
                f11 = 1.0F;
            }

            tessellator.setColorRGBA_F(f11 * f7, f12 * f7, f13 * f7, 1.0F);
            tessellator.addVertex(par2 + (1 - f4), par4 + 1.0D, par6);
            tessellator.addVertex(par2 + (1 - f4), par4 + 1.0D, par6 + 1.0D);
            tessellator.addVertex(par2 + (1 - f4), par4, par6 + 1.0D);
            tessellator.addVertex(par2 + (1 - f4), par4, par6);
            tessellator.draw();

            //Draw +x
            tessellator.startDrawingQuads();
            f11 = random.nextFloat() * 0.5F + 0.1F;
            f12 = random.nextFloat() * 0.5F + 0.4F;
            f13 = random.nextFloat() * 0.5F + 0.5F;

            if (i == 0)
            {
                f13 = 1.0F;
                f12 = 1.0F;
                f11 = 1.0F;
            }

            tessellator.setColorRGBA_F(f11 * f7, f12 * f7, f13 * f7, 1.0F);
            tessellator.addVertex(par2 + f4, par4, par6);
            tessellator.addVertex(par2 + f4, par4, par6 + 1.0D);
            tessellator.addVertex(par2 + f4, par4 + 1.0D, par6 + 1.0D);
            tessellator.addVertex(par2 + f4, par4 + 1.0D, par6);
            tessellator.draw();

            //Draw -y
            tessellator.startDrawingQuads();
            f11 = random.nextFloat() * 0.5F + 0.1F;
            f12 = random.nextFloat() * 0.5F + 0.4F;
            f13 = random.nextFloat() * 0.5F + 0.5F;

            if (i == 0)
            {
                f13 = 1.0F;
                f12 = 1.0F;
                f11 = 1.0F;
            }

            tessellator.setColorRGBA_F(f11 * f7, f12 * f7, f13 * f7, 1.0F);
            tessellator.addVertex(par2, par4, par6 + (1 - f4));
            tessellator.addVertex(par2 + 1.0D, par4, par6 + (1 - f4));
            tessellator.addVertex(par2 + 1.0D, par4 + 1.0D, par6 + (1 - f4));
            tessellator.addVertex(par2, par4 + 1.0D, par6 + (1 - f4));
            tessellator.draw();

            //Draw +y
            tessellator.startDrawingQuads();
            f11 = random.nextFloat() * 0.5F + 0.1F;
            f12 = random.nextFloat() * 0.5F + 0.4F;
            f13 = random.nextFloat() * 0.5F + 0.5F;

            if (i == 0)
            {
                f13 = 1.0F;
                f12 = 1.0F;
                f11 = 1.0F;
            }

            tessellator.setColorRGBA_F(f11 * f7, f12 * f7, f13 * f7, 1.0F);
            tessellator.addVertex(par2, par4 + 1.0D, par6 + f4);
            tessellator.addVertex(par2 + 1.0D, par4 + 1.0D, par6 + f4);
            tessellator.addVertex(par2 + 1.0D, par4, par6 + f4);
            tessellator.addVertex(par2, par4, par6 + f4);
            tessellator.draw();

            GL11.glPopMatrix();
            GL11.glMatrixMode(GL11.GL_MODELVIEW);
        }

        GL11.glDisable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_TEXTURE_GEN_S);
        GL11.glDisable(GL11.GL_TEXTURE_GEN_T);
        GL11.glDisable(GL11.GL_TEXTURE_GEN_R);
        GL11.glDisable(GL11.GL_TEXTURE_GEN_Q);
        GL11.glEnable(GL11.GL_LIGHTING);
    }

    private FloatBuffer func_76907_a(float par1, float par2, float par3, float par4)
    {
        this.buffer.clear();
        this.buffer.put(par1).put(par2).put(par3).put(par4);
        this.buffer.flip();
        return this.buffer;
    }

    @Override
    public void renderTileEntityAt(TileEntity tileentity, double d0, double d1, double d2, float f)
    {
        this.renderEndPortalTileEntity(tileentity, d0, d1, d2);
    }
}
