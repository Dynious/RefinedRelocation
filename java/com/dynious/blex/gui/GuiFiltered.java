package com.dynious.blex.gui;

import com.dynious.blex.gui.container.ContainerFiltered;
import com.dynious.blex.lib.Resources;
import com.dynious.blex.network.PacketTypeHandler;
import com.dynious.blex.network.packet.PacketBlacklist;
import com.dynious.blex.network.packet.PacketFilterOption;
import com.dynious.blex.network.packet.PacketUserFilter;
import com.dynious.blex.tileentity.IFilterTile;
import cpw.mods.fml.common.network.PacketDispatcher;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

public class GuiFiltered extends GuiContainer
{
    private IFilterTile filterTile;
    private GuiButton blacklist;
    private GuiTextField userFilter;
    private int index = 0;
    private int size;
    private static final int ITEMS_PER_SCREEN = 10;
    private static final int ITEM_SIZE = 14;

    public GuiFiltered(InventoryPlayer invPlayer, IFilterTile filterTile)
    {
        super(new ContainerFiltered(invPlayer, filterTile));
        this.filterTile = filterTile;
        size = filterTile.getFilter().getSize();
    }

    /**
     * Adds the buttons (and other controls) to the screen in question.
     */
    @SuppressWarnings({"unchecked"})
    public void initGui()
    {
        super.initGui();
        this.buttonList.clear();
        this.buttonList.add(blacklist = new GuiButton(0, width / 2 - 40, height / 2 - 100, 80, 20, filterTile.getBlackList() ? "Blacklist" : "Whitelist"));
        userFilter = new GuiTextField(fontRenderer, width / 2 - 88, height / 2 + 80, 176, 15);
        userFilter.setText(filterTile.getFilter().userFilter);
    }

    /**
     * Draws the screen and all the components in it.
     */
    @Override
    public void drawScreen(int h, int j, float f)
    {
        drawDefaultBackground();
        super.drawScreen(h, j, f);

        userFilter.drawTextBox();

        for (int i = 0; i < ITEMS_PER_SCREEN; i++)
        {
            int itemPlace = i + index;
            fontRenderer.drawString(filterTile.getFilter().getName(itemPlace), width / 2 - 60, height / 2 - 66 + i * ITEM_SIZE, 0);
        }
    }

    /**
     * Handles mouse input.
     */
    @Override
    public void handleMouseInput()
    {
        super.handleMouseInput();
        int i = Mouse.getEventDWheel();

        if (i != 0)
        {
            if (i > 0)
            {
                i = 1;
            }

            if (i < 0)
            {
                i = -1;
            }
            setIndex(index - i);
        }
    }

    public void setIndex(int index)
    {
        int i = index;
        if (i < 0)
        {
            i = 0;
        }
        if (i > size - ITEMS_PER_SCREEN)
        {
            i = size - ITEMS_PER_SCREEN;
        }
        this.index = i;
    }

    @Override
    public void updateScreen()
    {
        super.updateScreen();
        blacklist.displayString = filterTile.getBlackList() ? "Blacklist" : "Whitelist";
        userFilter.setText(filterTile.getFilter().userFilter);
    }

    @Override
    protected void keyTyped(char c, int i)
    {
        // allow esc but not inventory keybind
        if (i == 1 || (!userFilter.isFocused() && i != mc.gameSettings.keyBindInventory.keyCode))
        {
            super.keyTyped(c, i);
            return;
        }

        String oldUserFilter = userFilter.getText();
        userFilter.textboxKeyTyped(c, i);
        if (!oldUserFilter.equals(userFilter.getText()))
        {
            filterTile.getFilter().userFilter = userFilter.getText();
            PacketDispatcher.sendPacketToServer(PacketTypeHandler.populatePacket(new PacketUserFilter(userFilter.getText())));
        }
    }


    @Override
    protected void actionPerformed(GuiButton guibutton)
    {
        switch (guibutton.id)
        {
            case 0:
                filterTile.setBlackList(!filterTile.getBlackList());
                PacketDispatcher.sendPacketToServer(PacketTypeHandler.populatePacket(new PacketBlacklist(filterTile.getBlackList())));
                break;
        }
    }

    @Override
    protected void mouseClicked(int x, int y, int type)
    {
        super.mouseClicked(x, y, type);
        if (type == 0)
        {
            if (x >= width / 2 - 75 && x <= width / 2 + 75)
            {
                for (int i = 0; i < ITEMS_PER_SCREEN; i++)
                {
                    if (y >= height / 2 - 70 + i * ITEM_SIZE && y <= height / 2 - 70 + (1 + i) * ITEM_SIZE)
                    {
                        filterTile.getFilter().setValue(index + i, !filterTile.getFilter().getValue(index + i));
                        PacketDispatcher.sendPacketToServer(PacketTypeHandler.populatePacket(new PacketFilterOption((byte) (index + i))));
                    }
                }
            }
        }
        userFilter.mouseClicked(x, y, type);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3)
    {
        int xSize = 176;
        int ySize = 153;

        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        mc.renderEngine.bindTexture(Resources.GUI_FILTERED_BLOCK_EXTENDER);
        int xStart = (width - xSize) / 2;
        int yStart = (height - ySize) / 2;
        this.drawTexturedModalRect(xStart, yStart, 0, 0, xSize, ySize);
        for (int i = 0; i < ITEMS_PER_SCREEN; i++)
        {
            this.drawTexturedModalRect(width / 2 - 75, height / 2 - 70 + i * ITEM_SIZE, 0, 154, 150, 14);
            int itemPlace = i + index;
            if (filterTile.getFilter().getValue(itemPlace))
                this.drawTexturedModalRect(width / 2 - 75, height / 2 - 70 + i * ITEM_SIZE, 165, 154, 14, 14);
            else
                this.drawTexturedModalRect(width / 2 - 75, height / 2 - 70 + i * ITEM_SIZE, 151, 154, 14, 14);
        }

        int scrollBarTotalHeight = ySize - 10;
        int scrollBarWidth = 4;
        int scrollBarScaledHeight = (int) (scrollBarTotalHeight * ITEMS_PER_SCREEN / size);
        int scrollBarYPos = yStart + 5 + ((scrollBarTotalHeight - scrollBarScaledHeight) * index / (size - ITEMS_PER_SCREEN));
        int scrollBarXPos = xStart + xSize - 6;
        GuiContainer.drawRect(scrollBarXPos - scrollBarWidth, scrollBarYPos, scrollBarXPos, scrollBarYPos + scrollBarScaledHeight, 0xFF555555);
    }

    @Override
    public boolean doesGuiPauseGame()
    {
        return false;
    }
}
