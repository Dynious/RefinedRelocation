package com.dynious.refinedrelocation.client.gui;

import com.dynious.refinedrelocation.container.ContainerSortingImporter;
import com.dynious.refinedrelocation.lib.Resources;
import com.dynious.refinedrelocation.lib.Strings;
import com.dynious.refinedrelocation.network.NetworkHandler;
import com.dynious.refinedrelocation.network.packet.MessageSwitchPage;
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
        buttonList.clear();

        int x = (width - xSize) / 2;
        int y = (height - ySize) / 2;
        this.previousArrow = new GuiButton(0, x + 57, y + 33, 20, 20, "<");
        previousArrow.enabled = false;
        this.nextArrow = new GuiButton(1, x + 135, y + 33, 20, 20, ">");
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
        this.fontRendererObj.drawString(StatCollector.translateToLocal(Strings.ORE_DICT_CON), 106 - fontRendererObj.getStringWidth(StatCollector.translateToLocal(Strings.ORE_DICT_CON)) / 2, 6, 4210752);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int i, int j)
    {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(Resources.GUI_SORTING_IMPORTER);
        int xPos = (width - xSize) / 2;
        int yPos = (height - ySize) / 2;
        drawTexturedModalRect(xPos, yPos, 0, 0, xSize, ySize);

        int validSlots = tile.getItemListSize() + 1 - ((ContainerSortingImporter) inventorySlots).getPage()*9;
        int x = validSlots % 3;
        for (int y = validSlots / 3; y < 3; y++)
        {
            while (x < 3)
            {
                drawTexturedModalRect(xPos + 79 + x * 18, yPos + 17 + y * 18, 0, 167, 18, 18);
                x++;
            }
            x = 0;
        }
    }

    @Override
    protected void actionPerformed(GuiButton button)
    {
        switch (button.id)
        {
            case 0:
                ((ContainerSortingImporter) inventorySlots).previousPage();
                NetworkHandler.INSTANCE.sendToServer(new MessageSwitchPage(true));
                break;
            case 1:
                ((ContainerSortingImporter) inventorySlots).nextPage();
                NetworkHandler.INSTANCE.sendToServer(new MessageSwitchPage(false));
                break;
        }
    }
}
