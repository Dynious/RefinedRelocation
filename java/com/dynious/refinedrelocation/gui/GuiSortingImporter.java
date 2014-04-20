package com.dynious.refinedrelocation.gui;

import com.dynious.refinedrelocation.gui.container.ContainerSortingImporter;
import com.dynious.refinedrelocation.tileentity.TileSortingImporter;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;

public class GuiSortingImporter extends GuiContainer
{
    private TileSortingImporter tile;

    public GuiSortingImporter(EntityPlayer player, TileSortingImporter tile)
    {
        super(new ContainerSortingImporter(player, tile));
        this.tile = tile;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int i, int j)
    {

    }
}
