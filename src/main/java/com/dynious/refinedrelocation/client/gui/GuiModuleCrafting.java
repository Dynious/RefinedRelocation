package com.dynious.refinedrelocation.client.gui;

import com.dynious.refinedrelocation.container.ContainerModuleCrafting;
import com.dynious.refinedrelocation.container.ContainerModuleStock;
import com.dynious.refinedrelocation.grid.relocator.RelocatorModuleCrafting;
import com.dynious.refinedrelocation.grid.relocator.RelocatorModuleStock;
import com.dynious.refinedrelocation.lib.Resources;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.opengl.GL11;

public class GuiModuleCrafting extends GuiContainer
{
    public GuiModuleCrafting(EntityPlayer player, RelocatorModuleCrafting tile)
    {
        super(new ContainerModuleCrafting(player, tile));
        this.xSize = 176;
        this.ySize = 166;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int i, int j)
    {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(Resources.GUI_MODULE_STOCK);
        int xPos = (width - xSize) / 2;
        int yPos = (height - ySize) / 2;
        drawTexturedModalRect(xPos, yPos, 0, 0, xSize, ySize);
    }
}
