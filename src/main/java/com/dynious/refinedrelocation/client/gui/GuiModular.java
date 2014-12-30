package com.dynious.refinedrelocation.client.gui;

import com.dynious.refinedrelocation.container.ContainerRefinedRelocation;
import com.dynious.refinedrelocation.lib.Resources;
import org.lwjgl.opengl.GL11;

public class GuiModular extends GuiRefinedRelocationContainer
{
    private static final int SPACE_BETWEEN_MODULES = 10;

    public GuiModular(ContainerRefinedRelocation container)
    {
        super(container);
        this.xSize = 0;
        this.ySize = 0;
    }

    @Override
    public void addChild(IGuiWidgetBase child)
    {
        super.addChild(child);
        recalculateAndSetChildPositions();
    }

    public void recalculateAndSetChildPositions()
    {
        int maxWidth = (this.width / 4) * 3;
        int childTotalWidthLine = SPACE_BETWEEN_MODULES;
        int childTotalWidth = SPACE_BETWEEN_MODULES;

        int childTotalHeight = SPACE_BETWEEN_MODULES;

        for (IGuiWidgetBase widget : children) // Get total width and height
        {
            int widthDelta = widget.getWidth() + SPACE_BETWEEN_MODULES;
            int heightDelta = widget.getHeight() + SPACE_BETWEEN_MODULES;

            if (childTotalWidthLine + widthDelta > maxWidth) // If at maximum right side of GUI
            {
                childTotalWidthLine = SPACE_BETWEEN_MODULES;
                childTotalHeight += heightDelta;
            }
            else
            {
                childTotalWidthLine += widthDelta;
                if (childTotalWidthLine > childTotalWidth)
                    childTotalWidth = childTotalWidthLine;

                if (childTotalHeight == SPACE_BETWEEN_MODULES)
                    childTotalHeight += heightDelta;
            }
        }

        int startX = (this.width - childTotalWidth) / 2;
        int startY = (this.height - childTotalHeight) / 2;

        int lastX = (this.width + childTotalWidth) / 2;
        int lastY = (this.height + childTotalHeight) / 2;

        int currentX = startX + SPACE_BETWEEN_MODULES;
        int currentY = startY + SPACE_BETWEEN_MODULES;

        for (IGuiWidgetBase widget : children)
        {
            int deltaX = widget.getWidth() + SPACE_BETWEEN_MODULES;
            int deltaY = widget.getHeight() + SPACE_BETWEEN_MODULES;

            if (currentX + deltaX > maxWidth + startX)
            {
                currentX = startX + SPACE_BETWEEN_MODULES;
                currentY += deltaY;
            }
            widget.setPos(currentX, currentY);
            currentX += deltaX;
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
        drawTexturedModalRect(xPos - barWidth, yPos - barWidth, 0, barWidth * 2, barWidth, ySize + barWidth * 2); // left
        drawTexturedModalRect(xPos + xSize, yPos - barWidth, barWidth, barWidth * 2, barWidth, ySize + barWidth); // right
        drawTexturedModalRect(xPos - barWidth, yPos + ySize, 0, barWidth, xSize + barWidth, barWidth); // bottom

        super.drawGuiContainerBackgroundLayer(f, mouseX, mouseY);
    }
}
