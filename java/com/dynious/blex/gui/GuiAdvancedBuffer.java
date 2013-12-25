package com.dynious.blex.gui;

import com.dynious.blex.tileentity.TileAdvancedBuffer;
import net.minecraft.client.gui.GuiScreen;

public class GuiAdvancedBuffer extends GuiScreen
{
    TileAdvancedBuffer buffer;

    public GuiAdvancedBuffer(TileAdvancedBuffer buffer)
    {
        this.buffer = buffer;
    }
}
