package com.dynious.blex.gui;

import com.dynious.blex.gui.container.ContainerFilteringIronChest;
import cpw.mods.fml.relauncher.ReflectionHelper;
import cpw.mods.ironchest.IronChestType;
import cpw.mods.ironchest.client.GUIChest;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class GuiFilteringIronChest extends GuiContainer {

    private ResourceLocation resourceLocation;

    public GuiFilteringIronChest(GUIChest.GUI type, EntityPlayer player, IInventory chest)
    {
        super(makeContainer(type, player, chest));
        resourceLocation = ((GUIChest.ResourceList) ReflectionHelper.getPrivateValue(GUIChest.GUI.class, type, "guiResourceList")).location;
        this.xSize = ReflectionHelper.getPrivateValue(GUIChest.GUI.class, type, "xSize");
        this.ySize = ReflectionHelper.getPrivateValue(GUIChest.GUI.class, type, "ySize");
        this.allowUserInput = false;
    }

    public static Container makeContainer(GUIChest.GUI type, EntityPlayer player, IInventory chest)
    {
        int xSize = ReflectionHelper.getPrivateValue(GUIChest.GUI.class, type, "xSize");
        int ySize = ReflectionHelper.getPrivateValue(GUIChest.GUI.class, type, "ySize");
        return new ContainerFilteringIronChest(player, chest, IronChestType.values()[type.ordinal()], xSize, ySize);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int i, int j)
    {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        // new "bind tex"
        this.mc.getTextureManager().bindTexture(resourceLocation);
        int x = (width - xSize) / 2;
        int y = (height - ySize) / 2;
        drawTexturedModalRect(x, y, 0, 0, xSize, ySize);
    }
}
