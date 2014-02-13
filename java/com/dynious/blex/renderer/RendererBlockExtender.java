package com.dynious.blex.renderer;

import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import org.lwjgl.opengl.GL11;
import com.dynious.blex.lib.Resources;
import com.dynious.blex.model.ModelBlockExtender;
import com.dynious.blex.model.ModelEnderPearl;
import com.dynious.blex.tileentity.TileAdvancedBlockExtender;
import com.dynious.blex.tileentity.TileAdvancedFilteredBlockExtender;
import com.dynious.blex.tileentity.TileBlockExtender;
import com.dynious.blex.tileentity.TileFilteredBlockExtender;
import cpw.mods.fml.client.FMLClientHandler;

public class RendererBlockExtender extends TileEntitySpecialRenderer
{
    protected ModelBlockExtender modelBlockExtender = new ModelBlockExtender();
    protected ModelEnderPearl modelEnderPearl = new ModelEnderPearl();

    @Override
    public void renderTileEntityAt(TileEntity tileEntity, double x, double y, double z, float timer)
    {
        if (tileEntity != null && tileEntity instanceof TileBlockExtender)
        {
            TileBlockExtender tile = (TileBlockExtender) tileEntity;

            GL11.glPushMatrix();

            rotate(tile, x, y, z);

            GL11.glPushMatrix();

            float yOffset = 0.0F;
            if (tile.hasConnection())
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

            if (tileEntity instanceof TileAdvancedBlockExtender)
            {
                FMLClientHandler.instance().getClient().renderEngine.bindTexture(Resources.MODEL_TEXTURE_ADVANCED_BLOCK_EXTENDER);
            }
            else if (tileEntity instanceof TileFilteredBlockExtender)
            {
                FMLClientHandler.instance().getClient().renderEngine.bindTexture(Resources.MODEL_TEXTURE_FILTERED_BLOCK_EXTENDER);
            }
            else if (tileEntity instanceof TileAdvancedFilteredBlockExtender)
            {
                FMLClientHandler.instance().getClient().renderEngine.bindTexture(Resources.MODEL_TEXTURE_ADVANCED_FILTERED_BLOCK_EXTENDER);
            }
            else
            {
                FMLClientHandler.instance().getClient().renderEngine.bindTexture(Resources.MODEL_TEXTURE_BLOCK_EXTENDER);
            }

            if (tile.getDisguise() != null)
                modelBlockExtender.renderBaseDisguised();
            else
                modelBlockExtender.renderBase();

            modelBlockExtender.renderPilars();

            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, tile.getLightAmount() > 0 ? GL11.GL_ONE : GL11.GL_ONE_MINUS_SRC_ALPHA);
            modelBlockExtender.renderOutsideGlass();
            GL11.glDisable(GL11.GL_BLEND);

            GL11.glPopMatrix();

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
