package com.dynious.refinedrelocation.client.renderer;

import com.dynious.refinedrelocation.lib.Resources;
import com.pahimar.ee3.client.renderer.item.ItemRendererAlchemicalChest;
import cpw.mods.fml.client.FMLClientHandler;
import net.minecraft.client.model.ModelChest;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.IItemRenderer;
import org.lwjgl.opengl.GL11;

public class ItemRendererSortingAlchemicalChest extends ItemRendererAlchemicalChest
{
    private final ModelChest modelChest;

    public ItemRendererSortingAlchemicalChest()
    {
        super();
        modelChest = new ModelChest();
    }

    @Override
    public void renderItem(IItemRenderer.ItemRenderType type, ItemStack item, Object... data)
    {
        super.renderItem(type, item, data);
        switch (type)
        {
            case ENTITY:
            {
                renderAlchemicalChestOverlay(0.5F, 0.5F, 0.5F, item.getItemDamage());
                break;
            }
            case EQUIPPED:
            {
                renderAlchemicalChestOverlay(1.0F, 1.0F, 1.0F, item.getItemDamage());
                break;
            }
            case EQUIPPED_FIRST_PERSON:
            {
                renderAlchemicalChestOverlay(1.0F, 1.0F, 1.0F, item.getItemDamage());
                break;
            }
            case INVENTORY:
            {
                renderAlchemicalChestOverlay(0.0F, 0.075F, 0.0F, item.getItemDamage());
                break;
            }
            default:
                break;
        }
    }

    private void renderAlchemicalChestOverlay(float x, float y, float z, int metaData)
    {
        FMLClientHandler.instance().getClient().renderEngine.bindTexture(Resources.MODEL_TEXTURE_OVERLAY_ALCHEMICAL_CHEST);

        GL11.glPushMatrix(); //start
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        GL11.glTranslatef(x, y, z); //size
        GL11.glRotatef(180, 1, 0, 0);
        GL11.glRotatef(-90, 0, 1, 0);
        modelChest.renderAll();
        GL11.glDisable(GL11.GL_ALPHA_TEST);
        GL11.glPopMatrix(); //end
    }
}
