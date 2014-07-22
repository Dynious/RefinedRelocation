package com.dynious.refinedrelocation.client.renderer;

import com.dynious.refinedrelocation.lib.Resources;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.relauncher.ReflectionHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import cpw.mods.ironchest.IronChestType;
import cpw.mods.ironchest.client.TileEntityIronChestRenderer;
import net.minecraft.client.model.ModelChest;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.IItemRenderer;
import org.lwjgl.opengl.GL11;

import java.util.Map;

import static org.lwjgl.opengl.GL11.*;

@SideOnly(Side.CLIENT)
public class ItemRendererSortingIronChest implements IItemRenderer
{
    private static Map<IronChestType, ResourceLocation> locations;

    public ItemRendererSortingIronChest()
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
        if (locations == null)
        {
            locations = ReflectionHelper.getPrivateValue(TileEntityIronChestRenderer.class, null, "locations");
        }
        render(item, 0);
        render(item, 1);
    }

    public void render(ItemStack itemStack, int renderPass)
    {
        if (itemStack == null)
        {
            return;
        }

        if (renderPass == 0)
        {
            IronChestType type = IronChestType.values()[itemStack.getItemDamage()];
            ResourceLocation loc = locations.get(type);
            if (loc != null)
            {
                FMLClientHandler.instance().getClient().renderEngine.bindTexture(loc);
            }
        }
        else
        {
            FMLClientHandler.instance().getClient().renderEngine.bindTexture(Resources.MODEL_TEXTURE_OVERLAY_CHEST);
        }
        glPushMatrix();
        glEnable(32826 /* GL_RESCALE_NORMAL_EXT */);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        glTranslatef(0.0F, 1.0F, 1.0F);
        glScalef(1.0F, -1F, -1F);
        glTranslatef(0.5F, 0.5F, 0.5F);

        glRotatef(-90F, 0.0F, 1.0F, 0.0F);

        glTranslatef(-0.5F, -0.5F, -0.5F);

        // Render the chest itself
        model.renderAll();
        GL11.glDisable(GL11.GL_ALPHA_TEST);
        glPopMatrix();
        glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    }

    private ModelChest model;
}
