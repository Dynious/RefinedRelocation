package com.dynious.blex.gui;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.tileentity.TileEntity;
import org.lwjgl.opengl.GL11;
import com.dynious.blex.gui.container.ContainerFiltered;
import com.dynious.blex.gui.widget.GuiButtonBlacklist;
import com.dynious.blex.gui.widget.GuiFilterList;
import com.dynious.blex.gui.widget.GuiLabel;
import com.dynious.blex.gui.widget.GuiRedstoneSignalStatus;
import com.dynious.blex.gui.widget.GuiUserFilter;
import com.dynious.blex.helper.BlockHelper;
import com.dynious.blex.lib.Resources;
import com.dynious.blex.tileentity.IFilterTile;
import com.dynious.blex.tileentity.IRedstoneTransmitter;

public class GuiFiltered extends GuiBlExContainer
{
    private IFilterTile filterTile;

    public GuiFiltered(InventoryPlayer invPlayer, IFilterTile filterTile)
    {
        super(new ContainerFiltered(filterTile));
        this.filterTile = filterTile;
    }

    /**
     * Adds the buttons (and other controls) to the screen in question.
     */
    public void initGui()
    {
        super.initGui();

        new GuiLabel(this, width / 2, height / 2 - 76, BlockHelper.getTileEntityDisplayName((TileEntity) filterTile));

        new GuiButtonBlacklist(this, width / 2 + 57, height / 2 - 67, filterTile);

        new GuiUserFilter(this, width / 2 - 80, height / 2 - 56, 160, 30, true, filterTile);

        new GuiFilterList(this, width / 2 - 80, height / 2 - 18, 160, 97, filterTile);

        if (filterTile instanceof IRedstoneTransmitter)
        {
            new GuiRedstoneSignalStatus(this, width / 2 + 35, height / 2 - 63, (IRedstoneTransmitter) filterTile);
        }
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3)
    {
        int xSize = 176;
        int ySize = 174;

        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        mc.renderEngine.bindTexture(Resources.GUI_FILTERED_BLOCK_EXTENDER);
        int xStart = (width - xSize) / 2;
        int yStart = (height - ySize) / 2;
        this.drawTexturedModalRect(xStart, yStart, 0, 0, xSize, ySize);

        super.drawGuiContainerBackgroundLayer(par1, par2, par3);
    }

    @Override
    public boolean doesGuiPauseGame()
    {
        return false;
    }
}
