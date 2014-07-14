package com.dynious.refinedrelocation.client.renderer;

import com.dynious.refinedrelocation.lib.Resources;
import com.dynious.refinedrelocation.client.model.ModelBuffer;
import com.dynious.refinedrelocation.client.model.ModelEnderPearl;
import cpw.mods.fml.client.FMLClientHandler;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.IItemRenderer;
import org.lwjgl.opengl.GL11;

public class ItemRendererBuffer implements IItemRenderer
{
    private ModelBuffer modelBlockExtender = new ModelBuffer();
    private ModelEnderPearl modelEnderPearl = new ModelEnderPearl();

    @Override
    public boolean handleRenderType(ItemStack item, ItemRenderType type)
    {
        return true;
    }

    @Override
    public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper)
    {
        return true;
    }

    @Override
    public void renderItem(ItemRenderType type, ItemStack item, Object... data)
    {
        switch (type)
        {
            case ENTITY:
            {
                render(-0.5F, 0.0F, -0.5F, item);
                return;
            }
            case EQUIPPED:
            {
                render(0.0F, 0.0F, 0.0F, item);
                return;
            }
            case EQUIPPED_FIRST_PERSON:
            {
                render(0.5F, 0.5F, 0.3F, item);
                return;
            }
            case INVENTORY:
            {
                render(0.5F, 0.3F, 0.5F, item);
                return;
            }
            default:
        }
    }

    public void render(float x, float y, float z, ItemStack itemStack)
    {
        GL11.glPushMatrix();

        GL11.glTranslated(x + 0.5F, y + 1.5F, z + 0.5F);
        GL11.glRotatef(180F, 1F, 0F, 0F);

        GL11.glPushMatrix();

        float xOffset = (float) Math.sin((float) (System.currentTimeMillis() / 2 % (Math.PI * 1000F)) / 100F) / 5F;
        float zOffset = (float) Math.cos((float) (System.currentTimeMillis() / 2 % (Math.PI * 1000F)) / 100F) / 5F;

        GL11.glTranslated(xOffset, 1, zOffset);
        GL11.glRotatef((float) (System.currentTimeMillis() % 36000) / 10F, 0F, 1F, 0F);
        GL11.glScalef(0.125F, 0.125F, 0.125F);

        FMLClientHandler.instance().getClient().renderEngine.bindTexture(Resources.MODEL_TEXTURE_ENDERPEARL);

        modelEnderPearl.render();

        GL11.glPopMatrix();

        if (itemStack.getItemDamage() == 0)
        {
            FMLClientHandler.instance().getClient().renderEngine.bindTexture(Resources.MODEL_TEXTURE_BLOCK_EXTENDER);
        }
        else if (itemStack.getItemDamage() == 1)
        {
            FMLClientHandler.instance().getClient().renderEngine.bindTexture(Resources.MODEL_TEXTURE_ADVANCED_BLOCK_EXTENDER);
        }
        else if (itemStack.getItemDamage() == 2)
        {
            FMLClientHandler.instance().getClient().renderEngine.bindTexture(Resources.MODEL_TEXTURE_FILTERED_BLOCK_EXTENDER);
        }

        modelBlockExtender.renderPilars();

        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        modelBlockExtender.renderSides();
        GL11.glDisable(GL11.GL_BLEND);

        GL11.glPopMatrix();
    }
}
