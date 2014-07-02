package com.dynious.refinedrelocation.gui;

import com.dynious.refinedrelocation.gui.widget.IGuiRefinedRelocationWidgetBase;
import com.dynious.refinedrelocation.lib.Resources;
import net.minecraft.inventory.Container;
import org.lwjgl.opengl.GL11;

public class GuiModular extends GuiRefinedRelocationContainer
{
    private static final int SPACE_BETWEEN_MODULES = 10;

    public GuiModular(Container container)
    {
        super(container);
    }

    @Override
    public void addChild(IGuiRefinedRelocationWidgetBase child)
    {
        super.addChild(child);
        recalculateAndSetChildPositions();
    }

    public void recalculateAndSetChildPositions()
    {
        int width = 0;
        int height = 0;
        System.out.println(children.size());
        for (IGuiRefinedRelocationWidgetBase widget : children)
        {
            int space = (width == 0 && height == 0) ? 0 : SPACE_BETWEEN_MODULES;
            System.out.println("Width: " + width);
            System.out.println("Height: " + height);
            System.out.println(widget);

            System.out.println(widget.getX() + " : " + widget.getY());

            if (width + widget.getWidth() <= height + widget.getHeight())
            {
                widget.setPos(this.width/2 + width/2 + space, this.height/2 - widget.getHeight()/2);
                for (IGuiRefinedRelocationWidgetBase widget1 : children)
                {
                    widget1.moveX(-(widget.getWidth() + space)/2);
                }
                width += widget.getWidth() + space;
                if (height < widget.getHeight())
                    height = widget.getHeight();
            }
            else
            {
                widget.setPos(this.width/2 - widget.getWidth()/2, this.height/2 + height/2 + space);
                for (IGuiRefinedRelocationWidgetBase widget1 : children)
                {
                    widget1.moveY(-(widget.getHeight() + space) / 2);
                }
                height += widget.getHeight() + space;
                if (width < widget.getWidth())
                    width = widget.getWidth();
            }
            System.out.println(widget.getX() + " : " + widget.getY());
        }
        this.xSize = width;
        this.ySize = height;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int mouseX, int mouseY)
    {
        int xPos = (width - xSize) / 2;
        int yPos = (height - ySize) / 2;
        drawRect(xPos - SPACE_BETWEEN_MODULES/2, yPos - SPACE_BETWEEN_MODULES/2, xPos + xSize + SPACE_BETWEEN_MODULES/2, yPos + ySize + SPACE_BETWEEN_MODULES/2, 0xffc6c6c6);

        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(Resources.GUI_MODULAR);
        drawTexturedModalRect(xPos - SPACE_BETWEEN_MODULES/2, yPos - SPACE_BETWEEN_MODULES/2, 0, 0, xSize + SPACE_BETWEEN_MODULES, 3);
        drawTexturedModalRect(xPos - SPACE_BETWEEN_MODULES/2, yPos + ySize + SPACE_BETWEEN_MODULES/2 - 2, 0, 3, xSize + SPACE_BETWEEN_MODULES, 3);
        drawTexturedModalRect(xPos - SPACE_BETWEEN_MODULES/2, yPos - SPACE_BETWEEN_MODULES/2, 0, 6, 3, ySize + SPACE_BETWEEN_MODULES);
        drawTexturedModalRect(xPos + xSize + SPACE_BETWEEN_MODULES/2 - 2, yPos - SPACE_BETWEEN_MODULES/2, 3, 6, 3, ySize + SPACE_BETWEEN_MODULES);

        super.drawGuiContainerBackgroundLayer(f, mouseX, mouseY);
    }
}
