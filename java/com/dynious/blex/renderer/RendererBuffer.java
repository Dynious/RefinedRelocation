package com.dynious.blex.renderer;

import com.dynious.blex.lib.Resources;
import com.dynious.blex.model.ModelBuffer;
import com.dynious.blex.model.ModelEnderPearl;
import com.dynious.blex.tileentity.TileAdvancedBuffer;
import com.dynious.blex.tileentity.TileBuffer;
import com.dynious.blex.tileentity.TileFilteredBuffer;
import cpw.mods.fml.client.FMLClientHandler;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import org.lwjgl.opengl.GL11;

public class RendererBuffer extends TileEntitySpecialRenderer
{
    private ModelBuffer modelBlockExtender = new ModelBuffer();
    private ModelEnderPearl modelEnderPearl = new ModelEnderPearl();

    @Override
    public void renderTileEntityAt(TileEntity tileEntity, double x, double y, double z, float timer)
    {
        if (tileEntity != null && tileEntity instanceof TileBuffer)
        {

            GL11.glDisable(GL11.GL_LIGHTING);
            GL11.glDisable(GL11.GL_CULL_FACE);

            GL11.glPushMatrix();

            GL11.glTranslated(x + 0.5F, y - 0.5F, z + 0.5F);

            GL11.glScalef(1F, 1F, 1F);

            GL11.glPushMatrix();

            float yOffset = (float) Math.sin((float) (System.currentTimeMillis() / 2 % (Math.PI * 1000F)) / 100F) / 10F;

            GL11.glTranslated(0, 1F + yOffset, 0);
            GL11.glRotatef((float) (System.currentTimeMillis() % 36000) / 10F, 0F, 1F, 0F);
            GL11.glScalef(0.125F, 0.125F, 0.125F);

            FMLClientHandler.instance().getClient().renderEngine.bindTexture(Resources.MODEL_TEXTURE_ENDERPEARL);

            modelEnderPearl.render();

            GL11.glPopMatrix();

            FMLClientHandler.instance().getClient().renderEngine.bindTexture(Resources.MODEL_TEXTURE_BLOCK_EXTENDER);

            if (tileEntity instanceof TileAdvancedBuffer)
            {
                FMLClientHandler.instance().getClient().renderEngine.bindTexture(Resources.MODEL_TEXTURE_ADVANCED_BLOCK_EXTENDER);
            }
            if (tileEntity instanceof TileFilteredBuffer)
            {
                FMLClientHandler.instance().getClient().renderEngine.bindTexture(Resources.MODEL_TEXTURE_FILTERED_BLOCK_EXTENDER);
            }

            modelBlockExtender.renderPilars();

            GL11.glPushMatrix();

            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
            GL11.glColor4f(1F, 1F, 1F, 0.15F);

            modelBlockExtender.renderSides();

            GL11.glDisable(GL11.GL_BLEND);

            GL11.glPopMatrix();

            GL11.glPopMatrix();

            GL11.glEnable(GL11.GL_LIGHTING);
            GL11.glEnable(GL11.GL_CULL_FACE);
        }
    }
}
