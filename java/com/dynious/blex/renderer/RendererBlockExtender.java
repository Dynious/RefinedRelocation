package com.dynious.blex.renderer;

import com.dynious.blex.lib.Resources;
import com.dynious.blex.model.ModelBlockExtender;
import com.dynious.blex.tileentity.TileBlockExtender;
import cpw.mods.fml.client.FMLClientHandler;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import org.lwjgl.opengl.GL11;

public class RendererBlockExtender extends TileEntitySpecialRenderer
{
    private ModelBlockExtender modelBlockExtender = new ModelBlockExtender();

    @Override
    public void renderTileEntityAt(TileEntity tileEntity, double x, double y, double z, float timer)
    {
        if (tileEntity != null && tileEntity instanceof TileBlockExtender)
        {
            TileBlockExtender tile = (TileBlockExtender)tileEntity;

            GL11.glDisable(GL11.GL_LIGHTING);
            GL11.glDisable(GL11.GL_CULL_FACE);

            GL11.glPushMatrix();

            FMLClientHandler.instance().getClient().renderEngine.bindTexture(Resources.MODEL_TEXTURE_ADVANCED_FILTERED_BLOCK_EXTENDER);

            GL11.glTranslated(x + 0.5F, y, z + 0.5F);
            GL11.glScalef(0.5F, 0.5F, 0.5F);

            modelBlockExtender.render();

            GL11.glPopMatrix();

            GL11.glEnable(GL11.GL_LIGHTING);
            GL11.glEnable(GL11.GL_CULL_FACE);
        }
    }
}
