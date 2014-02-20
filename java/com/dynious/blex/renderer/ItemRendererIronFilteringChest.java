package com.dynious.blex.renderer;

import com.dynious.blex.lib.Resources;
import com.google.common.collect.ImmutableMap;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import cpw.mods.ironchest.IronChestType;
import net.minecraft.client.model.ModelChest;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.IItemRenderer;

import java.util.Map;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.glColor4f;

@SideOnly(Side.CLIENT)
public class ItemRendererIronFilteringChest implements IItemRenderer
{
    private static final ResourceLocation RES_NORMAL_SINGLE = new ResourceLocation("textures/entity/chest/normal.png");

    private static Map<IronChestType, ResourceLocation> locations;
    static {
        ImmutableMap.Builder<IronChestType, ResourceLocation> builder = ImmutableMap.builder();
        for (IronChestType typ : IronChestType.values()) {
            builder.put(typ, new ResourceLocation("ironchest","textures/model/" + typ.getModelTexture()));
        }
        locations = builder.build();
    }

    public ItemRendererIronFilteringChest()
    {
        model = new ModelChest();
    }

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
        render(item, 0);
        render(item, 1);
    }

    public void render(ItemStack itemStack, int renderPass)
    {
        if (itemStack == null) {
            return;
        }

        if (renderPass == 0)
        {
            if (itemStack.getItemDamage() > 0)
            {
                IronChestType type = IronChestType.values()[itemStack.getItemDamage() - 1];
                FMLClientHandler.instance().getClient().renderEngine.bindTexture(locations.get(type));
            }
            else
            {
                FMLClientHandler.instance().getClient().renderEngine.bindTexture(RES_NORMAL_SINGLE);
            }
        }
        else
        {
            FMLClientHandler.instance().getClient().renderEngine.bindTexture(Resources.MODEL_TEXTURE_OVERLAY_CHEST);
        }
        glPushMatrix();
        glEnable(32826 /* GL_RESCALE_NORMAL_EXT */);
        glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        glTranslatef(0.0F, 1.0F, 1.0F);
        glScalef(1.0F, -1F, -1F);
        glTranslatef(0.5F, 0.5F, 0.5F);

        glRotatef(-90F, 0.0F, 1.0F, 0.0F);

        glTranslatef(-0.5F, -0.5F, -0.5F);

        // Render the chest itself
        model.renderAll();
        glPopMatrix();
        glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    }

    private ModelChest model;
}
