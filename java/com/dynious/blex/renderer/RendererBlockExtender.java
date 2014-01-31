package com.dynious.blex.renderer;

import com.dynious.blex.lib.Resources;
import com.dynious.blex.model.ModelBlockExtender;
import com.dynious.blex.model.ModelEnderPearl;
import com.dynious.blex.tileentity.*;
import cpw.mods.fml.client.FMLClientHandler;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import org.lwjgl.opengl.GL11;

public class RendererBlockExtender extends TileEntitySpecialRenderer
{
    private ModelBlockExtender modelBlockExtender = new ModelBlockExtender();
    private ModelEnderPearl modelEnderPearl = new ModelEnderPearl();

    @Override
    public void renderTileEntityAt(TileEntity tileEntity, double x, double y, double z, float timer)
    {
        if (tileEntity != null && tileEntity instanceof TileBlockExtender)
        {
            TileBlockExtender tile = (TileBlockExtender) tileEntity;

            GL11.glDisable(GL11.GL_LIGHTING);
            GL11.glDisable(GL11.GL_CULL_FACE);

            GL11.glPushMatrix();

            rotate(tile, x, y, z);

            GL11.glScalef(1F, 1F, 1F);

            GL11.glPushMatrix();

            float yOffset = (float) Math.sin((float) (System.currentTimeMillis() / 2 % (Math.PI * 1000F)) / 100F) / 10F;

            GL11.glTranslated(0, 1F + yOffset, 0);
            GL11.glRotatef((float) (System.currentTimeMillis() % 36000) / 10F, 0F, 1F, 0F);
            GL11.glScalef(0.125F, 0.125F, 0.125F);

            if (!tile.isRedstoneEnabled)
                GL11.glColor3f(0.5F, 0.5F, 0.5F);

            FMLClientHandler.instance().getClient().renderEngine.bindTexture(Resources.MODEL_TEXTURE_ENDERPEARL);

            modelEnderPearl.render();

            GL11.glPopMatrix();

            GL11.glColor3f(1, 1, 1);

            FMLClientHandler.instance().getClient().renderEngine.bindTexture(Resources.MODEL_TEXTURE_BLOCK_EXTENDER);

            if (tileEntity instanceof TileAdvancedBlockExtender)
            {
                FMLClientHandler.instance().getClient().renderEngine.bindTexture(Resources.MODEL_TEXTURE_ADVANCED_BLOCK_EXTENDER);
            }
            else if (tileEntity instanceof TileFilteredBlockExtender)
            {
                FMLClientHandler.instance().getClient().renderEngine.bindTexture(Resources.MODEL_TEXTURE_FILTERED_BLOCK_EXTENDER);
            }
            else if (tileEntity instanceof TileWirelessBlockExtender)
            {
                FMLClientHandler.instance().getClient().renderEngine.bindTexture(Resources.MODEL_TEXTURE_WIRELESS_BLOCK_EXTENDER);
            }
            else if (tileEntity instanceof TileAdvancedFilteredBlockExtender)
            {
                FMLClientHandler.instance().getClient().renderEngine.bindTexture(Resources.MODEL_TEXTURE_ADVANCED_FILTERED_BLOCK_EXTENDER);
            }

            modelBlockExtender.renderBase();
            modelBlockExtender.renderPilars();

            GL11.glPushMatrix();

            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
            GL11.glColor4f(1F, 1F, 1F, 0.15F + tile.getLightAmount());

            modelBlockExtender.renderSides();

            GL11.glDisable(GL11.GL_BLEND);

            GL11.glPopMatrix();

            GL11.glPopMatrix();

            GL11.glEnable(GL11.GL_LIGHTING);
            GL11.glEnable(GL11.GL_CULL_FACE);
        }
    }

    public void rotate(TileBlockExtender tile, double x, double y, double z)
    {
        switch (tile.getConnectedDirection())
        {
            case DOWN:
                GL11.glTranslated(x + 0.5F, y + 1.5F, z + 0.5F);
                GL11.glRotatef(180F, 1F, 0F, 0F);
                break;
            case UP:
                GL11.glTranslated(x + 0.5F, y - 0.5F, z + 0.5F);
                break;
            case NORTH:
                GL11.glTranslated(x + 0.5F, y + 0.5F, z + 1.5F);
                GL11.glRotatef(-90F, 1F, 0F, 0F);
                break;
            case SOUTH:
                GL11.glTranslated(x + 0.5F, y + 0.5, z - 0.5F);
                GL11.glRotatef(90F, 1F, 0F, 0F);
                break;
            case WEST:
                GL11.glTranslated(x + 1.5F, y + 0.5F, z + 0.5F);
                GL11.glRotatef(90F, 0F, 0F, 1F);
                break;
            case EAST:
                GL11.glTranslated(x - 0.5F, y + 0.5F, z + 0.5F);
                GL11.glRotatef(-90F, 0F, 0F, 1F);
                break;
            case UNKNOWN:
                GL11.glTranslated(x + 0.5F, y + 1.5F, z + 0.5F);
                GL11.glRotatef(180F, 1F, 0F, 0F);
        }
    }
}
