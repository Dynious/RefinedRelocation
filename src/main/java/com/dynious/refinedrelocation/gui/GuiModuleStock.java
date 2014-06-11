package com.dynious.refinedrelocation.gui;

import com.dynious.refinedrelocation.grid.relocator.RelocatorModuleStock;
import com.dynious.refinedrelocation.gui.container.ContainerModuleStock;
import com.dynious.refinedrelocation.lib.Resources;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.opengl.GL11;

public class GuiModuleStock extends GuiContainer
{
    private RelocatorModuleStock tile;

    public GuiModuleStock(EntityPlayer player, RelocatorModuleStock tile)
    {
        super(new ContainerModuleStock(player, tile));
        this.tile = tile;
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
