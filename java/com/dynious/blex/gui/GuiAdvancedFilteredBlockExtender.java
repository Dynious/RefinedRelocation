package com.dynious.blex.gui;

import com.dynious.blex.gui.container.ContainerAdvancedFiltered;
import com.dynious.blex.lib.Resources;
import com.dynious.blex.network.PacketTypeHandler;
import com.dynious.blex.network.packet.*;
import com.dynious.blex.tileentity.TileAdvancedFilteredBlockExtender;
import cpw.mods.fml.common.network.PacketDispatcher;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraftforge.common.ForgeDirection;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

public class GuiAdvancedFilteredBlockExtender extends GuiContainer
{
    private TileAdvancedFilteredBlockExtender blockExtender;
    private GuiButton spreadItems;
    private GuiButton blacklist;
    private GuiButton restrictExtraction;
    private int index = 0;
    private int size;
    private static final int ITEMS_PER_SCREEN = 10;
    private static final int ITEM_SIZE = 14;
    private GuiTextField stackSize;
    private GuiTextField userFilter;

    public GuiAdvancedFilteredBlockExtender(InventoryPlayer invPlayer, TileAdvancedFilteredBlockExtender blockExtender)
    {
        super(new ContainerAdvancedFiltered(invPlayer, blockExtender));
        this.blockExtender = blockExtender;
        size = blockExtender.getFilter().getSize();
    }

    /**
     * Adds the buttons (and other controls) to the screen in question.
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    public void initGui()
    {
        super.initGui();
        this.buttonList.clear();
        this.buttonList.add(blacklist = new GuiButton(1, width / 2 - 120, height / 2 - 65, 80, 20, blockExtender.getBlackList() ? "Blacklist" : "Whitelist"));
        this.buttonList.add(spreadItems = new GuiButton(0, width / 2 - 120, height / 2 - 40, 80, 20, blockExtender.getSpreadItems() ? "Spread on" : "Spread off"));
        this.buttonList.add(restrictExtraction = new GuiButton(2, width / 2 - 120, height / 2 - 15, 80, 20, blockExtender.restrictExtraction ? "Filter Extract" : "Free Extract"));
        stackSize = new GuiTextField(fontRenderer, width / 2 - 120, height / 2 + 33, 20, 15);
        stackSize.setMaxStringLength(2);
        stackSize.setText(Integer.toString(blockExtender.getInventoryStackLimit()));
        userFilter = new GuiTextField(fontRenderer, width / 2 - 88, height / 2 + 80, 176, 15);
        userFilter.setText(blockExtender.getFilter().userFilter);
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

    /**
     * Draws the screen and all the components in it.
     */
    @Override
    public void drawScreen(int h, int j, float f)
    {
        drawDefaultBackground();
        super.drawScreen(h, j, f);

        stackSize.drawTextBox();
        userFilter.drawTextBox();

        for (int i = 0; i < blockExtender.getInsertDirections().length; i++)
        {
            ForgeDirection direction = ForgeDirection.getOrientation(blockExtender.getInsertDirections()[i]);
            if (i != blockExtender.getConnectedDirection().ordinal())
            {
                String letter = Character.toString(direction.toString().charAt(0));
                switch (i)
                {
                    //Bottom
                    case 0:
                        fontRenderer.drawString(letter, width / 2 - 85 + 34 + 4, height / 2 + 40 + 10 + 4, 0);
                        break;
                    //Top
                    case 1:
                        fontRenderer.drawString(letter, width / 2 - 85 + 17 + 4, height / 2 + 40 - 7 + 4, 0);
                        break;
                    //North
                    case 2:
                        fontRenderer.drawString(letter, width / 2 - 85 + 17 + 4, height / 2 + 40 - 24 + 4, 0);
                        break;
                    //South
                    case 3:
                        fontRenderer.drawString(letter, width / 2 - 85 + 17 + 4, height / 2 + 40 + 10 + 4, 0);
                        break;
                    //West
                    case 4:
                        fontRenderer.drawString(letter, width / 2 - 85 + 4, height / 2 + 40 - 7 + 4, 0);
                        break;
                    //East
                    case 5:
                        fontRenderer.drawString(letter, width / 2 - 85 + 34 + 4, height / 2 + 40 - 7 + 4, 0);
                        break;
                }
            }
        }
        for (int i = 0; i < ITEMS_PER_SCREEN; i++)
        {
            int itemPlace = i + index;
            fontRenderer.drawString(blockExtender.getFilter().getName(itemPlace), width / 2 - 15, height / 2 - 66 + i * ITEM_SIZE, 0);
        }
    }

    @Override
    public void updateScreen()
    {
        super.updateScreen();
        spreadItems.displayString = blockExtender.getSpreadItems() ? "Spread on" : "Spread off";
        blacklist.displayString = blockExtender.getBlackList() ? "Blacklist" : "Whitelist";
        restrictExtraction.displayString = blockExtender.restrictExtraction ? "Filter Extract" : "Free Extract";
        if (blockExtender.getInventoryStackLimit() == 0)
        {
            stackSize.setText("");
            return;
        }
        stackSize.setText(Integer.toString(blockExtender.getInventoryStackLimit()));
        userFilter.setText(blockExtender.getFilter().userFilter);
    }

    @Override
    public void keyTyped(char c, int i)
    {
        // allow esc but not inventory keybind
        if (i == 1 || (!userFilter.isFocused() && !stackSize.isFocused() && i != mc.gameSettings.keyBindInventory.keyCode))
        {
            super.keyTyped(c, i);
            return;
        }
        
        String oldUserFilter = userFilter.getText();
        userFilter.textboxKeyTyped(c, i);
        if (!oldUserFilter.equals(userFilter.getText()))
        {
            blockExtender.getFilter().userFilter = userFilter.getText();
            PacketDispatcher.sendPacketToServer(PacketTypeHandler.populatePacket(new PacketUserFilter(userFilter.getText())));
        }
        
        if (Character.isDigit(c) || Character.getType(c) == 15)
        {
            String oldStackSize = stackSize.getText();
            stackSize.textboxKeyTyped(c, i);
            if (!oldStackSize.equals(stackSize.getText()))
            {
                byte newMaxStackSize = stackSize.getText().isEmpty() ? (byte) 0 : Byte.parseByte(stackSize.getText());
                blockExtender.setMaxStackSize(newMaxStackSize);
                PacketDispatcher.sendPacketToServer(PacketTypeHandler.populatePacket(new PacketMaxStackSize(newMaxStackSize)));
            }
        }
    }

    @Override
    protected void actionPerformed(GuiButton guibutton)
    {
        switch (guibutton.id)
        {
            case 0:
                blockExtender.setSpreadItems(!blockExtender.getSpreadItems());
                PacketDispatcher.sendPacketToServer(PacketTypeHandler.populatePacket(new PacketSpread(blockExtender.getSpreadItems())));
                break;
            case 1:
                blockExtender.setBlackList(!blockExtender.getBlackList());
                PacketDispatcher.sendPacketToServer(PacketTypeHandler.populatePacket(new PacketBlacklist(blockExtender.getBlackList())));
                break;
            case 2:
                blockExtender.restrictExtraction = !blockExtender.restrictExtraction;
                PacketDispatcher.sendPacketToServer(PacketTypeHandler.populatePacket(new PacketRestrictExtraction(blockExtender.restrictExtraction)));
                break;
        }
    }

    @Override
    protected void mouseClicked(int x, int y, int type)
    {
        super.mouseClicked(x, y, type);
        if (type == 0)
        {
            //Bottom
            if (x >= width / 2 - 85 + 34 && x <= width / 2 - 85 + 34 + 14 && y >= height / 2 + 40 + 10 && y <= height / 2 + 40 + 10 + 14)
            {
                blockExtender.setInsertDirection(0, blockExtender.getInsertDirections()[0] + 1);
                PacketDispatcher.sendPacketToServer(PacketTypeHandler.populatePacket(new PacketInsertDirection((byte) 0, blockExtender.getInsertDirections()[0])));
            }
            //Top
            else if (x >= width / 2 - 85 + 17 && x <= width / 2 - 85 + 17 + 14 && y >= height / 2 + 40 - 7 && y <= height / 2 + 40 - 7 + 14)
            {
                blockExtender.setInsertDirection(1, blockExtender.getInsertDirections()[1] + 1);
                PacketDispatcher.sendPacketToServer(PacketTypeHandler.populatePacket(new PacketInsertDirection((byte) 1, blockExtender.getInsertDirections()[1])));
            }
            //North
            else if (x >= width / 2 - 85 + 17 && x <= width / 2 - 85 + 17 + 14 && y >= height / 2 + 40 - 24 && y <= height / 2 + 40 - 24 + 14)
            {
                blockExtender.setInsertDirection(2, blockExtender.getInsertDirections()[2] + 1);
                PacketDispatcher.sendPacketToServer(PacketTypeHandler.populatePacket(new PacketInsertDirection((byte) 2, blockExtender.getInsertDirections()[2])));
            }
            //South
            else if (x >= width / 2 - 85 + 17 && x <= width / 2 - 85 + 17 + 14 && y >= height / 2 + 40 + 10 && y <= height / 2 + 40 + 10 + 14)
            {
                blockExtender.setInsertDirection(3, blockExtender.getInsertDirections()[3] + 1);
                PacketDispatcher.sendPacketToServer(PacketTypeHandler.populatePacket(new PacketInsertDirection((byte) 3, blockExtender.getInsertDirections()[3])));
            }
            //West
            else if (x >= width / 2 - 85 && x <= width / 2 - 85 + 14 && y >= height / 2 + 40 - 7 && y <= height / 2 + 40 - 7 + 14)
            {
                blockExtender.setInsertDirection(4, blockExtender.getInsertDirections()[4] + 1);
                PacketDispatcher.sendPacketToServer(PacketTypeHandler.populatePacket(new PacketInsertDirection((byte) 4, blockExtender.getInsertDirections()[4])));
            }
            //East
            else if (x >= width / 2 - 85 + 34 && x <= width / 2 - 85 + 34 + 14 && y >= height / 2 + 40 - 7 && y <= height / 2 + 40 - 7 + 14)
            {
                blockExtender.setInsertDirection(5, blockExtender.getInsertDirections()[5] + 1);
                PacketDispatcher.sendPacketToServer(PacketTypeHandler.populatePacket(new PacketInsertDirection((byte) 5, blockExtender.getInsertDirections()[5])));
            }
            if (x >= width / 2 - 30 && x <= width / 2 + 120)
            {
                for (int i = 0; i < ITEMS_PER_SCREEN; i++)
                {
                    if (y >= height / 2 - 70 + i * ITEM_SIZE && y <= height / 2 - 70 + (1 + i) * ITEM_SIZE)
                    {
                        blockExtender.getFilter().setValue(index + i, !blockExtender.getFilter().getValue(index + i));
                        PacketDispatcher.sendPacketToServer(PacketTypeHandler.populatePacket(new PacketFilterOption((byte) (index + i))));
                    }
                }
            }
        }
        userFilter.mouseClicked(x, y, type);
        stackSize.mouseClicked(x, y, type);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3)
    {
        int xSize = 256;
        int ySize = 153;

        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        mc.renderEngine.bindTexture(Resources.GUI_ADVANCED_FILTERED_BLOCK_EXTENDER);
        int xStart = (width - xSize) / 2;
        int yStart = (height - ySize) / 2;
        this.drawTexturedModalRect(xStart, yStart, 0, 0, xSize, ySize);
        for (int i = 0; i < ForgeDirection.values().length; i++)
        {
            boolean connection = false;
            if (ForgeDirection.getOrientation(i) == blockExtender.getConnectedDirection())
            {
                connection = true;
            }
            boolean hasTile = blockExtender.getTiles()[i] != null;
            switch (i)
            {
                //Bottom
                case 0:
                    if (connection)
                        this.drawTexturedModalRect(width / 2 - 85 + 34, height / 2 + 40 + 10, 207, 154, 14, 14);
                    else
                        this.drawTexturedModalRect(width / 2 - 85 + 34, height / 2 + 40 + 10, hasTile ? 179 : 193, 154, 14, 14);
                    break;
                //Top
                case 1:
                    if (connection)
                        this.drawTexturedModalRect(width / 2 - 85 + 17, height / 2 + 40 - 7, 207, 154, 14, 14);
                    else
                        this.drawTexturedModalRect(width / 2 - 85 + 17, height / 2 + 40 - 7, hasTile ? 179 : 193, 154, 14, 14);
                    break;
                //North
                case 2:
                    if (connection)
                        this.drawTexturedModalRect(width / 2 - 85 + 17, height / 2 + 40 - 24, 207, 154, 14, 14);
                    else
                        this.drawTexturedModalRect(width / 2 - 85 + 17, height / 2 + 40 - 24, hasTile ? 179 : 193, 154, 14, 14);
                    break;
                //South
                case 3:
                    if (connection)
                        this.drawTexturedModalRect(width / 2 - 85 + 17, height / 2 + 40 + 10, 207, 154, 14, 14);
                    else
                        this.drawTexturedModalRect(width / 2 - 85 + 17, height / 2 + 40 + 10, hasTile ? 179 : 193, 154, 14, 14);
                    break;
                //West
                case 4:
                    if (connection)
                        this.drawTexturedModalRect(width / 2 - 85, height / 2 + 40 - 7, 207, 154, 14, 14);
                    else
                        this.drawTexturedModalRect(width / 2 - 85, height / 2 + 40 - 7, hasTile ? 179 : 193, 154, 14, 14);
                    break;
                //East
                case 5:
                    if (connection)
                        this.drawTexturedModalRect(width / 2 - 85 + 34, height / 2 + 40 - 7, 207, 154, 14, 14);
                    else
                        this.drawTexturedModalRect(width / 2 - 85 + 34, height / 2 + 40 - 7, hasTile ? 179 : 193, 154, 14, 14);
                    break;
            }
        }
        for (int i = 0; i < ITEMS_PER_SCREEN; i++)
        {
            this.drawTexturedModalRect(width / 2 - 30, height / 2 - 70 + i * ITEM_SIZE, 0, 154, 150, 14);
            int itemPlace = i + index;
            if (blockExtender.getFilter().getValue(itemPlace))
                this.drawTexturedModalRect(width / 2 - 30, height / 2 - 70 + i * ITEM_SIZE, 165, 154, 14, 14);
            else
                this.drawTexturedModalRect(width / 2 - 30, height / 2 - 70 + i * ITEM_SIZE, 151, 154, 14, 14);
        }

        int scrollBarTotalHeight = ySize-10;
        int scrollBarWidth = 3;
        int scrollBarScaledHeight = (int)(scrollBarTotalHeight * ITEMS_PER_SCREEN / size);
        int scrollBarYPos = yStart + 5 + ((scrollBarTotalHeight-scrollBarScaledHeight) * index / (size - ITEMS_PER_SCREEN));
        int scrollBarXPos = xStart + xSize - 4;
        GuiContainer.drawRect(scrollBarXPos-scrollBarWidth, scrollBarYPos, scrollBarXPos, scrollBarYPos+scrollBarScaledHeight, 0xFF555555);
    }

    @Override
    public boolean doesGuiPauseGame()
    {
        return false;
    }
}
