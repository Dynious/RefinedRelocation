package com.dynious.blex.gui;

import com.dynious.blex.lib.Resources;
import com.dynious.blex.network.PacketHandler;
import com.dynious.blex.network.PacketTypeHandler;
import com.dynious.blex.network.packet.PacketInsertDirection;
import com.dynious.blex.network.packet.PacketMaxStackSize;
import com.dynious.blex.network.packet.PacketSpread;
import com.dynious.blex.tileentity.TileAdvancedBlockExtender;
import cpw.mods.fml.common.network.PacketDispatcher;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraftforge.common.ForgeDirection;
import org.lwjgl.opengl.GL11;

public class GuiAdvancedBlockExtender extends GuiScreen
{
    private TileAdvancedBlockExtender blockExtender;
    private GuiButton spreadItems;
    private GuiTextField stackSize;

    public GuiAdvancedBlockExtender(TileAdvancedBlockExtender blockExtender)
    {
        this.blockExtender = blockExtender;
    }

    /**
     * Adds the buttons (and other controls) to the screen in question.
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    public void initGui()
    {
        super.initGui();
        this.buttonList.clear();
        this.buttonList.add(spreadItems = new GuiButton(0, width / 2 - 80, height / 2 - 20, 80, 20, blockExtender.getSpreadItems() ? "Spread on" : "Spread off"));
        stackSize = new GuiTextField(fontRenderer, width / 2 - 50, height / 2 + 10, 20, 15);
        stackSize.setMaxStringLength(2);
        stackSize.setFocused(true);
        stackSize.setText(Integer.toString(blockExtender.getInventoryStackLimit()));
    }

    /**
     * Draws the screen and all the components in it.
     */
    @Override
    public void drawScreen(int h, int j, float f)
    {
        drawDefaultBackground();
        drawContainerBackground();
        super.drawScreen(h, j, f);

        stackSize.drawTextBox();

        for (int i = 0; i < blockExtender.getInsertDirection().length; i++)
        {
            ForgeDirection direction = ForgeDirection.getOrientation(blockExtender.getInsertDirection()[i]);
            if (i != blockExtender.getConnectedDirection().ordinal())
            {
                String letter = Character.toString(direction.toString().charAt(0));
                switch (i)
                {
                    //Bottom
                    case 0:
                        fontRenderer.drawString(letter, width / 2 + 28 + 34 + 4, height / 2 + 10 + 4, 0);
                        break;
                    //Top
                    case 1:
                        fontRenderer.drawString(letter, width / 2 + 28 + 17 + 4, height / 2 - 7 + 4, 0);
                        break;
                    //North
                    case 2:
                        fontRenderer.drawString(letter, width / 2 + 28 + 17 + 4, height / 2 - 24 + 4, 0);
                        break;
                    //South
                    case 3:
                        fontRenderer.drawString(letter, width / 2 + 28 + 17 + 4, height / 2 + 10 + 4, 0);
                        break;
                    //West
                    case 4:
                        fontRenderer.drawString(letter, width / 2 + 28 + 4, height / 2 - 7 + 4, 0);
                        break;
                    //East
                    case 5:
                        fontRenderer.drawString(letter, width / 2 + 28 + 34 + 4, height / 2 - 7 + 4, 0);
                        break;
                }
            }
        }
    }

    @Override
    public void updateScreen()
    {
        super.updateScreen();
        spreadItems.displayString = blockExtender.getSpreadItems() ? "Spread on" : "Spread off";
        if (blockExtender.getInventoryStackLimit() == 0)
        {
            stackSize.setText("");
            return;
        }
        stackSize.setText(Integer.toString(blockExtender.getInventoryStackLimit()));
    }

    @Override
    public void keyTyped(char c, int i)
    {
        super.keyTyped(c, i);
        if (Character.isDigit(c) || Character.getType(c) == 15)
        {
            stackSize.textboxKeyTyped(c, i);
            if (stackSize.getText().isEmpty())
            {
                blockExtender.setMaxStackSize((byte) 0);
                return;
            }
            blockExtender.setMaxStackSize(Byte.parseByte(stackSize.getText()));
            PacketDispatcher.sendPacketToServer(PacketTypeHandler.populatePacket(new PacketMaxStackSize(blockExtender, Byte.parseByte(stackSize.getText()))));
        }
    }


    @Override
    protected void actionPerformed(GuiButton guibutton)
    {
        switch (guibutton.id)
        {
            case 0:
                blockExtender.setSpreadItems(!blockExtender.getSpreadItems());
                PacketDispatcher.sendPacketToServer(PacketTypeHandler.populatePacket(new PacketSpread(blockExtender)));
        }
    }

    @Override
    protected void mouseClicked(int x, int y, int type)
    {
        super.mouseClicked(x, y, type);
        if (type == 0)
        {
            //Bottom
            if (x >= width / 2 + 28 + 34 && x <= width / 2 + 28 + 34 + 14 && y >= height / 2 + 10 && y <= height / 2 + 10 + 14)
            {
                blockExtender.setInsertDirection(0, blockExtender.getInsertDirection()[0] + 1);
                PacketDispatcher.sendPacketToServer(PacketTypeHandler.populatePacket(new PacketInsertDirection(blockExtender, (byte)0)));
            }
            //Top
            else if (x >= width / 2 + 28 + 17 && x <= width / 2 + 28 + 17 + 14 && y >= height / 2 - 7 && y <= height / 2 - 7 + 14)
            {
                blockExtender.setInsertDirection(1, blockExtender.getInsertDirection()[1] + 1);
                PacketDispatcher.sendPacketToServer(PacketTypeHandler.populatePacket(new PacketInsertDirection(blockExtender, (byte)1)));
            }
            //North
            else if (x >= width / 2 + 28 + 17 && x <= width / 2 + 28 + 17 + 14 && y >= height / 2 - 24 && y <= height / 2 - 24 + 14)
            {
                blockExtender.setInsertDirection(2, blockExtender.getInsertDirection()[2] + 1);
                PacketDispatcher.sendPacketToServer(PacketTypeHandler.populatePacket(new PacketInsertDirection(blockExtender, (byte)2)));
            }
            //South
            else if (x >= width / 2 + 28 + 17 && x <= width / 2 + 28 + 17 + 14 && y >= height / 2 + 10 && y <= height / 2 + 10 + 14)
            {
                blockExtender.setInsertDirection(3, blockExtender.getInsertDirection()[3] + 1);
                PacketDispatcher.sendPacketToServer(PacketTypeHandler.populatePacket(new PacketInsertDirection(blockExtender, (byte)3)));
            }
            //West
            else if (x >= width / 2 + 28 && x <= width / 2 + 28 + 14 && y >= height / 2 - 7 && y <= height / 2 - 7 + 14)
            {
                blockExtender.setInsertDirection(4, blockExtender.getInsertDirection()[4] + 1);
                PacketDispatcher.sendPacketToServer(PacketTypeHandler.populatePacket(new PacketInsertDirection(blockExtender, (byte)4)));
            }
            //East
            else if (x >= width / 2 + 28 + 34 && x <= width / 2 + 28 + 34 + 14 && y >= height / 2 - 7 && y <= height / 2 - 7 + 14)
            {
                blockExtender.setInsertDirection(5, blockExtender.getInsertDirection()[5] + 1);
                PacketDispatcher.sendPacketToServer(PacketTypeHandler.populatePacket(new PacketInsertDirection(blockExtender, (byte)5)));
            }
        }
    }

    private void drawContainerBackground()
    {
        int xSize = 176;
        int ySize = 80;

        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        mc.renderEngine.bindTexture(Resources.GUI_ADVANCED_BLOCK_EXTENDER);
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
                        this.drawTexturedModalRect(width / 2 + 28 + 34, height / 2 + 10, 176, 28, 14, 14);
                    else
                        this.drawTexturedModalRect(width / 2 + 28 + 34, height / 2 + 10, 176, hasTile ? 0 : 14, 14, 14);
                    break;
                //Top
                case 1:
                    if (connection)
                        this.drawTexturedModalRect(width / 2 + 28 + 17, height / 2 - 7, 176, 28, 14, 14);
                    else
                        this.drawTexturedModalRect(width / 2 + 28 + 17, height / 2 - 7, 176, hasTile ? 0 : 14, 14, 14);
                    break;
                //North
                case 2:
                    if (connection)
                        this.drawTexturedModalRect(width / 2 + 28 + 17, height / 2 - 24, 176, 28, 14, 14);
                    else
                        this.drawTexturedModalRect(width / 2 + 28 + 17, height / 2 - 24, 176, hasTile ? 0 : 14, 14, 14);
                    break;
                //South
                case 3:
                    if (connection)
                        this.drawTexturedModalRect(width / 2 + 28 + 17, height / 2 + 10, 176, 28, 14, 14);
                    else
                        this.drawTexturedModalRect(width / 2 + 28 + 17, height / 2 + 10, 176, hasTile ? 0 : 14, 14, 14);
                    break;
                //West
                case 4:
                    if (connection)
                        this.drawTexturedModalRect(width / 2 + 28, height / 2 - 7, 176, 28, 14, 14);
                    else
                        this.drawTexturedModalRect(width / 2 + 28, height / 2 - 7, 176, hasTile ? 0 : 14, 14, 14);
                    break;
                //East
                case 5:
                    if (connection)
                        this.drawTexturedModalRect(width / 2 + 28 + 34, height / 2 - 7, 176, 28, 14, 14);
                    else
                        this.drawTexturedModalRect(width / 2 + 28 + 34, height / 2 - 7, 176, hasTile ? 0 : 14, 14, 14);
                    break;
            }
        }
    }

    @Override
    public boolean doesGuiPauseGame()
    {
        return false;
    }
}
