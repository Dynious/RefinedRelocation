package com.dynious.refinedrelocation.gui;

import com.dynious.refinedrelocation.gui.container.ContainerSortingImporter;
import com.dynious.refinedrelocation.lib.Resources;
import com.dynious.refinedrelocation.tileentity.TileSortingImporter;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.opengl.GL11;

public class GuiSortingImporter extends GuiContainer
{
    private TileSortingImporter tile;

    public GuiSortingImporter(EntityPlayer player, TileSortingImporter tile)
    {
        super(new ContainerSortingImporter(player, tile));
        this.tile = tile;
        this.xSize = 176;
        this.ySize = 166;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int i, int j)
    {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(Resources.GUI_SORTING_IMPORTER);
        int x = (width - xSize) / 2;
        int y = (height - ySize) / 2;
        drawTexturedModalRect(x, y, 0, 0, xSize, ySize);
    }
}
