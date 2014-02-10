package com.dynious.blex.renderer;

import net.minecraft.tileentity.TileEntity;
import org.lwjgl.opengl.GL11;
import com.dynious.blex.lib.Resources;
import com.dynious.blex.model.ModelWirelessBlockExtender;
import com.dynious.blex.tileentity.TileWirelessBlockExtender;
import cpw.mods.fml.client.FMLClientHandler;

public class RendererWirelessBlockExtender extends RendererBlockExtender
{
    protected ModelWirelessBlockExtender modelWirelessBlockExtender = new ModelWirelessBlockExtender();
    
    @Override
    public void renderTileEntityAt(TileEntity tileEntity, double x, double y, double z, float timer)
    {
        if (tileEntity != null && tileEntity instanceof TileWirelessBlockExtender)
        {
            TileWirelessBlockExtender tile = (TileWirelessBlockExtender) tileEntity;

            GL11.glPushMatrix();

            rotate(tile, x, y, z);

            GL11.glPushMatrix();

            float yOffset = 0.0F;
            if (tile.xConnected != Integer.MAX_VALUE)
                yOffset = (float) Math.sin((float) (System.currentTimeMillis() / 2 % (Math.PI * 1000F)) / 100F) / 10F;

            GL11.glTranslated(0, 1F + yOffset, 0);
            GL11.glRotatef((float) (System.currentTimeMillis() % 36000) / 10F, 0F, 1F, 0F);
            GL11.glScalef(0.125F, 0.125F, 0.125F);

            if (!tile.isRedstoneTransmissionEnabled())
                GL11.glColor3f(.5F, .1F, .1F);

            FMLClientHandler.instance().getClient().renderEngine.bindTexture(Resources.MODEL_TEXTURE_ENDERPEARL);

            modelEnderPearl.render();

            GL11.glPopMatrix();

            GL11.glColor3f(1, 1, 1);
            
            // render the inner block extender
            GL11.glPushMatrix();
            GL11.glScalef(0.6F, 0.6F, 0.6F);
            GL11.glTranslated(0, .7F, 0);
            GL11.glRotatef((float) (System.currentTimeMillis() % 36000) / 10F, 0F, 1F, 0F);
            FMLClientHandler.instance().getClient().renderEngine.bindTexture(Resources.MODEL_TEXTURE_BLOCK_EXTENDER);
            modelBlockExtender.renderBase();
            modelBlockExtender.renderPilars();
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, tile.getLightAmount() > 0 ? GL11.GL_ONE : GL11.GL_ONE_MINUS_SRC_ALPHA);
            modelBlockExtender.renderOutsideGlass();
            GL11.glDisable(GL11.GL_BLEND);
            GL11.glPopMatrix();

            FMLClientHandler.instance().getClient().renderEngine.bindTexture(Resources.MODEL_TEXTURE_WIRELESS_BLOCK_EXTENDER);

            modelWirelessBlockExtender.renderPilars();

            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, tile.getLightAmount() > 0 ? GL11.GL_ONE : GL11.GL_ONE_MINUS_SRC_ALPHA);
            modelWirelessBlockExtender.renderSides();
            GL11.glDisable(GL11.GL_BLEND);

            GL11.glPopMatrix();
        }
    }
}
