package com.dynious.refinedrelocation.client.gui;

import com.dynious.refinedrelocation.container.ContainerSortingImporter;
import com.dynious.refinedrelocation.helper.GuiHelper;
import com.dynious.refinedrelocation.lib.Resources;
import com.dynious.refinedrelocation.lib.Strings;
import com.dynious.refinedrelocation.tileentity.TileSortingImporter;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.StatCollector;
import org.lwjgl.opengl.GL11;

public class GuiSortingImporter extends GuiContainer
{
    private TileSortingImporter tile;
    private GuiButton previousArrow;
    private GuiButton nextArrow;

    public GuiSortingImporter(EntityPlayer player, TileSortingImporter tile)
    {
        super(new ContainerSortingImporter(player, tile));
        this.tile = tile;
        this.xSize = 176;
        this.ySize = 166;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void initGui()
    {
        super.initGui();

        int x = (width - xSize) / 2;
        int y = (height - ySize) / 2;

        previousArrow = new GuiButton(0, x + 35, y + 38, 20, 20, "<");
        previousArrow.enabled = false;

        nextArrow = new GuiButton(1, x + 121, y + 38, 20, 20, ">");
        nextArrow.enabled = tile.getItemListSize() >= 9 + ((ContainerSortingImporter) inventorySlots).getPage() * 9;

        this.buttonList.add(previousArrow);
        this.buttonList.add(nextArrow);
    }

    @Override
    public void updateScreen()
    {
        super.updateScreen();
        nextArrow.enabled = tile.getItemListSize() >= 9 + ((ContainerSortingImporter) inventorySlots).getPage() * 9;

        previousArrow.enabled = ((ContainerSortingImporter) inventorySlots).getPage() > 0;
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int par1, int par2)
    {
        String s = StatCollector.translateToLocal(Strings.ORE_DICT_CON);
        fontRendererObj.drawString(s, xSize / 2 - fontRendererObj.getStringWidth(s) / 2, 8, 0x404040);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int i, int j)
    {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(Resources.GUI_SORTING_IMPORTER);
        int xPos = (width - xSize) / 2;
        int yPos = (height - ySize) / 2;
        drawTexturedModalRect(xPos, yPos, 0, 0, xSize, ySize);

        int validSlots = tile.getItemListSize() + 1 - ((ContainerSortingImporter) inventorySlots).getPage() * 9;
        int startX = validSlots % 3;
        for (int y = validSlots / 3; y < 3; y++)
        {
            for(int x = startX; x < 3; x++)
            {
                drawTexturedModalRect(xPos + 61 + x * 18, yPos + 22 + y * 18, 0, 167, 18, 18);
            }
            startX = 0;
        }
    }

    @Override
    protected void actionPerformed(GuiButton button)
    {
        switch (button.id)
        {
            case 0:
                ((ContainerSortingImporter) inventorySlots).previousPage();
                GuiHelper.sendBooleanMessage(ContainerSortingImporter.MESSAGE_SWITCH_PAGE, true);
                break;
            case 1:
                ((ContainerSortingImporter) inventorySlots).nextPage();
                GuiHelper.sendBooleanMessage(ContainerSortingImporter.MESSAGE_SWITCH_PAGE, false);
                break;
        }
    }
}
