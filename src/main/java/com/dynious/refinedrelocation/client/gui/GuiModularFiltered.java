package com.dynious.refinedrelocation.client.gui;

import com.dynious.refinedrelocation.api.filter.IFilterModule;
import com.dynious.refinedrelocation.api.tileentity.INewFilterTile;
import com.dynious.refinedrelocation.client.gui.widget.GuiButtonOpenModuleSelection;
import com.dynious.refinedrelocation.client.gui.widget.GuiFilterModuleList;
import com.dynious.refinedrelocation.container.ContainerModularFiltered;

public class GuiModularFiltered extends GuiRefinedRelocationContainer
{
    private INewFilterTile filterTile;
    private IGuiWidgetBase[] filterList = new IGuiWidgetBase[4];
    private boolean listenForChange = false;

    public static final int X_SIZE = 150;
    public static final int Y_SIZE = 100;
    private static final int[] X_POSITIONS = new int[] { 0, 155 };
    private static final int[] Y_POSITIONS = new int[] { 35, 140 };

    public GuiModularFiltered(INewFilterTile filterTile)
    {
        super(new ContainerModularFiltered(filterTile));
        this.filterTile = filterTile;
        this.xSize = 305;
        this.ySize = 240;
    }

    @Override
    public void initGui()
    {
        super.initGui();

        IFilterModule[] filters = filterTile.getFilter().filters;
        for (int i = 0; i < filters.length; i++)
        {
            IFilterModule filter = filters[i];
            if (filter != null)
                filterList[i] = filter.getGUI(this, guiLeft + X_POSITIONS[i % 2], guiTop + Y_POSITIONS[i/2]);
            else
                filterList[i] = new GuiButtonOpenModuleSelection(this, guiLeft + X_POSITIONS[i % 2] + X_SIZE/2 - 12, guiTop + Y_POSITIONS[i/2] + Y_SIZE/2 - 10, i);
        }
    }

    public void onModuleChanged()
    {
        listenForChange = true;
    }

    public void checkModuleChanges()
    {
        IFilterModule[] filters = filterTile.getFilter().filters;
        for (int i = 0; i < filters.length; i++)
        {
            IFilterModule module = filters[i];
            if (module != null && isModuleSelector(filterList[i]))
            {
                removeChild(filterList[i]);
                filterList[i] = module.getGUI(this, guiLeft + X_POSITIONS[i % 2], guiTop + Y_POSITIONS[i / 2]);
                listenForChange = false;
            }
            else if (module == null && !isModuleSelector(filterList[i]))
            {
                removeChild(filterList[i]);
                filterList[i] = new GuiFilterModuleList(this, guiLeft + X_POSITIONS[i % 2], guiTop + Y_POSITIONS[i/2], filterTile, i);
                listenForChange = false;
            }
        }
    }

    private boolean isModuleSelector(Object o)
    {
        return o instanceof GuiFilterModuleList || o instanceof GuiButtonOpenModuleSelection;
    }

    public void onListButtonClicked(GuiButtonOpenModuleSelection button)
    {
        removeChild(button);
        filterList[button.getId()] = new GuiFilterModuleList(this, guiLeft + X_POSITIONS[button.getId() % 2], guiTop + Y_POSITIONS[button.getId()/2], filterTile, button.getId());
    }

    @Override
    public void updateScreen()
    {
        if (listenForChange)
        {
            checkModuleChanges();
            listenForChange = false;
        }
        super.updateScreen();
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int mouseX, int mouseY)
    {
        drawRect(guiLeft, guiTop, guiLeft + xSize, guiTop + 30, 0xffc6c6c6);

        super.drawGuiContainerBackgroundLayer(f, mouseX, mouseY);
    }
}
