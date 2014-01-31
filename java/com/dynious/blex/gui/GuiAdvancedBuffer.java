package com.dynious.blex.gui;

import com.dynious.blex.lib.Resources;
import com.dynious.blex.network.PacketTypeHandler;
import com.dynious.blex.network.packet.PacketInsertDirection;
import com.dynious.blex.network.packet.PacketSpread;
import com.dynious.blex.tileentity.TileAdvancedBuffer;
import cpw.mods.fml.common.network.PacketDispatcher;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.ForgeDirection;
import org.lwjgl.opengl.GL11;

public class GuiAdvancedBuffer extends GuiScreen
{
    TileAdvancedBuffer buffer;
    GuiButton[] buttons = new GuiButton[6];
    GuiButton spread;

    public GuiAdvancedBuffer(TileAdvancedBuffer buffer)
    {
        this.buffer = buffer;
    }
    /**
     * Adds the buttons (and other controls) to the screen in question.
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    public void initGui()
    {
        super.initGui();
        this.buttonList.clear();
        for (int i = 0; i < 3; i++)
        {
            this.buttonList.add(buttons[i] = new GuiButton(i, width/2 - 70 + (i*50), height/2 - 40, 40, 20, buffer.getInsertionName(i)));
        }
        for (int i = 0; i < 3; i++)
        {
            this.buttonList.add(buttons[i + 3] = new GuiButton(i + 3, width/2 - 70 + (i*50), height/2, 40, 20, buffer.getInsertionName(i + 3)));
        }

        this.buttonList.add(spread = new GuiButton(6, width/2 - 40, height/2 + 30, 80, 20, buffer.getSpreadItems() ? "Spread on" : "Spread off"));
    }

    @Override
    public void drawScreen(int h, int j, float f)
    {
        drawDefaultBackground();
        drawContainerBackground();
        super.drawScreen(h, j, f);
        for (int i = 0; i < 3; i++)
        {
            this.fontRenderer.drawString(Integer.toString(i), width/2 - 52 + (i*50), height/2 - 50, 4210752);
        }
        for (int i = 0; i < 3; i++)
        {
            this.fontRenderer.drawString(Integer.toString(i + 3), width/2 - 52 + (i*50), height/2 - 10, 4210752);
        }
    }

    @Override
    public void updateScreen()
    {
        super.updateScreen();
        for (int i = 0; i < buttons.length; i++)
        {
            buttons[i].displayString = buffer.getInsertionName(i);
        }
        spread.displayString = buffer.getSpreadItems() ? "Spread on" : "Spread off";
    }

    @Override
    protected void actionPerformed(GuiButton guibutton)
    {
        if (guibutton.id < 6)
        {
            buffer.setInsertDirection(guibutton.id, buffer.getInsertDirection()[guibutton.id] + 1);
            PacketDispatcher.sendPacketToServer(PacketTypeHandler.populatePacket(new PacketInsertDirection(buffer, (byte)guibutton.id)));
        }
        else
        {
            buffer.setSpreadItems(!buffer.getSpreadItems());
            PacketDispatcher.sendPacketToServer(PacketTypeHandler.populatePacket(new PacketSpread(buffer)));
        }
    }

    private void drawContainerBackground()
    {
        int xSize = 204;
        int ySize = 109;

        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        mc.renderEngine.bindTexture(Resources.GUI_ADVANCED_BUFFER);
        int xStart = (width - xSize) / 2;
        int yStart = (height - ySize) / 2;
        this.drawTexturedModalRect(xStart, yStart, 0, 0, xSize, ySize);
    }

    @Override
    public boolean doesGuiPauseGame()
    {
        return false;
    }
}
