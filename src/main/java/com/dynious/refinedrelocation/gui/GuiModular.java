package com.dynious.refinedrelocation.gui;

import com.dynious.refinedrelocation.api.gui.IGuiWidgetBase;
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
    public void addChild(IGuiWidgetBase child)
    {
        super.addChild(child);
        recalculateAndSetChildPositions();
    }

    public void recalculateAndSetChildPositions()
    {
        int maxWidth = (this.width / 4)*3;
        int childTotalWidth = SPACE_BETWEEN_MODULES;
        boolean useMaxWidth = false;

        int childTotalHeight = SPACE_BETWEEN_MODULES;

        for (IGuiRefinedRelocationWidgetBase widget : children) // Get total width and height
        {
            int widthDelta = widget.getWidth() + SPACE_BETWEEN_MODULES;
            int heightDelta = widget.getHeight() + SPACE_BETWEEN_MODULES;

            if (childTotalWidth + widthDelta >= maxWidth) // If at maximum right side of GUI
            {
                useMaxWidth = true;
                childTotalWidth = SPACE_BETWEEN_MODULES;
                childTotalHeight += heightDelta;
            }
            else
            {
                childTotalWidth += widthDelta;

                if (childTotalHeight == SPACE_BETWEEN_MODULES)
                    childTotalHeight += heightDelta;
            }
        }

        if (useMaxWidth)
        {
            childTotalWidth = maxWidth;
        }

        int startX = (this.width - childTotalWidth) / 2;
        int startY = (this.height - childTotalHeight) / 2;

        int lastX = (this.width + childTotalWidth) / 2;
        int lastY = (this.height + childTotalHeight) / 2;

        int currentX = startX + SPACE_BETWEEN_MODULES;
        int currentY = startY + SPACE_BETWEEN_MODULES;

        for (IGuiRefinedRelocationWidgetBase widget : children)
        {
            widget.setPos(currentX, currentY);
            int deltaX = widget.getWidth() + SPACE_BETWEEN_MODULES;
            int deltaY = widget.getHeight() + SPACE_BETWEEN_MODULES;

            if (currentX > maxWidth)
            {
                currentX = startX + SPACE_BETWEEN_MODULES;
                currentY += deltaY;
            }
            else
            {
                currentX += deltaX;
            }
        }

        this.guiLeft = startX;
        this.guiTop = startY;

        this.xSize = lastX - startX;
        this.ySize = lastY - startY;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int mouseX, int mouseY)
    {
        int xPos = guiLeft;
        int yPos = guiTop;
        int barWidth = 3;
        drawRect(xPos, yPos, xPos + xSize, yPos + ySize, 0xffc6c6c6);

        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(Resources.GUI_MODULAR);
        drawTexturedModalRect(xPos, yPos - barWidth, 0, 0, xSize, barWidth); // top
        drawTexturedModalRect(xPos - barWidth, yPos - barWidth, 0, barWidth*2, barWidth, ySize + barWidth*2); // left
        drawTexturedModalRect(xPos + xSize, yPos - barWidth, barWidth, barWidth*2, barWidth, ySize + barWidth); // right
        drawTexturedModalRect(xPos - barWidth, yPos + ySize, 0, barWidth, xSize + barWidth, barWidth); // bottom

        super.drawGuiContainerBackgroundLayer(f, mouseX, mouseY);
    }
}
