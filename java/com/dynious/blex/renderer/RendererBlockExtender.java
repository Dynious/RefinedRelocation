package com.dynious.blex.renderer;

import com.dynious.blex.model.ModelBlockExtender;
import com.dynious.blex.tileentity.TileBlockExtender;
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

            GL11.glScalef(1F, 1F, 1F);
            GL11.glTranslated(x, y, z);

            modelBlockExtender.render();

            GL11.glPushMatrix();

            GL11.glEnable(GL11.GL_LIGHTING);
            GL11.glEnable(GL11.GL_CULL_FACE);
        }
    }
}
