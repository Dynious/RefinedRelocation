package com.dynious.refinedrelocation.renderer;

import com.dynious.refinedrelocation.api.tileentity.IRelocator;
import com.dynious.refinedrelocation.grid.relocator.TravellingItem;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.tileentity.TileEntity;
import org.lwjgl.opengl.GL11;

public class RendererRelocator extends TileEntitySpecialRenderer
{
    private static RenderItem renderer;
    private static EntityItem entityItem = new EntityItem(null);

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
    }

    @Override
    public void renderTileEntityAt(TileEntity tile, double x, double y, double z, float partialTick)
    {
        IRelocator relocator = (IRelocator) tile;
        for (TravellingItem item : relocator.getItems(false))
        {
            GL11.glPushMatrix();
            entityItem.setEntityItemStack(item.getItemStack());

            float progress = item.getClientSideProgress(partialTick);
            GL11.glTranslated(x + item.getX(progress), y + item.getY(progress), z + item.getZ(progress));
            GL11.glScalef(0.8F, 0.8F, 0.8F);

            renderer.doRenderItem(entityItem, 0.0D, 0.0D, 0.0D, 0.0F, 0.0F);

            GL11.glPopMatrix();
        }
    }
}
