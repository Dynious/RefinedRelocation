package com.dynious.refinedrelocation.client.gui;

import com.dynious.refinedrelocation.api.tileentity.IFilterTileGUI;
import com.dynious.refinedrelocation.container.ContainerFiltered;

public class GuiModularFiltered extends GuiRefinedRelocationContainer
{
    private IFilterTileGUI filterTile;

    public GuiModularFiltered(IFilterTileGUI filterTile)
    {
        super(new ContainerFiltered(filterTile));
        this.filterTile = filterTile;
        this.xSize = 305;
        this.ySize = 240;
    }

    @Override
    public void initGui()
    {
        super.initGui();

        //new GuiFilterList(this, guiLeft, guiTop + 35, 150, 100, filterTile);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int mouseX, int mouseY)
    {
        drawRect(guiLeft, guiTop, guiLeft + xSize, guiTop + 30, 0xffc6c6c6);
        drawRect(guiLeft, guiTop + 35, guiLeft + 150, guiTop + 135, 0xffc6c6c6);
        drawRect(guiLeft + 155, guiTop + 35, guiLeft + 305, guiTop + 135, 0xffc6c6c6);
        drawRect(guiLeft, guiTop + 140, guiLeft + 150, guiTop + 240, 0xffc6c6c6);
        drawRect(guiLeft + 155, guiTop + 140, guiLeft + 305, guiTop + 240, 0xffc6c6c6);

        super.drawGuiContainerBackgroundLayer(f, mouseX, mouseY);
    }
}
