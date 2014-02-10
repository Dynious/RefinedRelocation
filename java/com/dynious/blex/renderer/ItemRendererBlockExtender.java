package com.dynious.blex.renderer;

import com.dynious.blex.lib.Resources;
import com.dynious.blex.model.ModelBlockExtender;
import com.dynious.blex.model.ModelEnderPearl;
import com.dynious.blex.model.ModelWirelessBlockExtender;
import cpw.mods.fml.client.FMLClientHandler;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.IItemRenderer;
import org.lwjgl.opengl.GL11;

public class ItemRendererBlockExtender implements IItemRenderer
{
    private ModelBlockExtender modelBlockExtender = new ModelBlockExtender();
    private ModelWirelessBlockExtender modelWirelessBlockExtender = new ModelWirelessBlockExtender();
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
                return;
        }
    }

    public void render(float x, float y, float z, ItemStack itemStack)
    {
        boolean isWireless = itemStack.getItemDamage() == 4;
        
        GL11.glPushMatrix();

        GL11.glTranslated(x + 0.5F, y + 1.5F, z + 0.5F);
        GL11.glRotatef(180F, 1F, 0F, 0F);

        GL11.glPushMatrix();

        float yOffset = (float) Math.sin((float) (System.currentTimeMillis() / 2 % (Math.PI * 1000F)) / 100F) / 10F;

        GL11.glTranslated(0, 1F + yOffset, 0);
        GL11.glRotatef((float) (System.currentTimeMillis() % 36000) / 10F, 0F, 1F, 0F);
        GL11.glScalef(0.125F, 0.125F, 0.125F);

        FMLClientHandler.instance().getClient().renderEngine.bindTexture(Resources.MODEL_TEXTURE_ENDERPEARL);

        modelEnderPearl.render();

        GL11.glPopMatrix();

        if (isWireless)
        {
            // render the inner block extender
            GL11.glPushMatrix();
            GL11.glScalef(0.6F, 0.6F, 0.6F);
            GL11.glTranslated(0, .7F, 0);
            GL11.glRotatef((float) (System.currentTimeMillis() % 36000) / 10F, 0F, 1F, 0F);
            FMLClientHandler.instance().getClient().renderEngine.bindTexture(Resources.MODEL_TEXTURE_BLOCK_EXTENDER);
            modelBlockExtender.renderBase();
            modelBlockExtender.renderPilars();
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            modelBlockExtender.renderOutsideGlass();
            GL11.glDisable(GL11.GL_BLEND);
            GL11.glPopMatrix();

            FMLClientHandler.instance().getClient().renderEngine.bindTexture(Resources.MODEL_TEXTURE_WIRELESS_BLOCK_EXTENDER);

            modelWirelessBlockExtender.renderPilars();

            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            modelWirelessBlockExtender.renderSides();
            GL11.glDisable(GL11.GL_BLEND);
        }
        else
        {
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
            else if (itemStack.getItemDamage() == 3)
            {
                FMLClientHandler.instance().getClient().renderEngine.bindTexture(Resources.MODEL_TEXTURE_ADVANCED_FILTERED_BLOCK_EXTENDER);
            }
            
            modelBlockExtender.renderBase();
            modelBlockExtender.renderPilars();
            
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            modelBlockExtender.renderOutsideGlass();
            GL11.glDisable(GL11.GL_BLEND);
        }

        GL11.glPopMatrix();
    }
}
