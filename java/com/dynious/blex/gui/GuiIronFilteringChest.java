package com.dynious.blex.gui;

import com.dynious.blex.gui.container.ContainerIronFilteringChest;
import cpw.mods.fml.common.ObfuscationReflectionHelper;
import cpw.mods.ironchest.IronChestType;
import cpw.mods.ironchest.client.GUIChest;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;

import org.lwjgl.opengl.GL11;

public class GuiIronFilteringChest extends GuiContainer {

    private GUIChest.GUI type;

    public GuiIronFilteringChest(GUIChest.GUI type, EntityPlayer player, IInventory chest)
    {
        super(makeContainer(type, player, chest));
        this.type = type;
        this.xSize = ObfuscationReflectionHelper.getPrivateValue(GUIChest.GUI.class, type, "xSize");
        this.ySize = ObfuscationReflectionHelper.getPrivateValue(GUIChest.GUI.class, type, "ySize");
        this.allowUserInput = false;
    }

    public static Container makeContainer(GUIChest.GUI type, EntityPlayer player, IInventory chest)
    {
        int xSize = ObfuscationReflectionHelper.getPrivateValue(GUIChest.GUI.class, type, "xSize");
        int ySize = ObfuscationReflectionHelper.getPrivateValue(GUIChest.GUI.class, type, "ySize");
        return new ContainerIronFilteringChest(player, chest, IronChestType.values()[type.ordinal()], xSize, ySize);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int i, int j)
    {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        // new "bind tex"
        this.mc.getTextureManager().bindTexture(((GUIChest.ResourceList)ObfuscationReflectionHelper.getPrivateValue(GUIChest.GUI.class, type, "guiResourceList")).location);
        int x = (width - xSize) / 2;
        int y = (height - ySize) / 2;
        drawTexturedModalRect(x, y, 0, 0, xSize, ySize);
    }
}
