package com.dynious.refinedrelocation.gui.widget;

import com.dynious.refinedrelocation.gui.IGuiParent;
import com.dynious.refinedrelocation.gui.widget.GuiLabel;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class GuiScalableButton extends com.dynious.refinedrelocation.gui.widget.GuiButton
{
    protected int textureX = 0;
    protected int textureY = 0;
    protected GuiLabel label = null;
    protected static final ResourceLocation buttonTextures = new ResourceLocation("textures/gui/widgets.png");

    public GuiScalableButton(IGuiParent parent, String labelText)
    {
        super(parent, labelText);
        this.label = new GuiLabel(this, x + w / 2, y + h / 2, labelText, 0xffffff, true);
    }

    public GuiScalableButton(IGuiParent parent, int x, int y, int w, int h, int textureX, int textureY, String labelText)
    {
        super(parent, x, y, w, h, textureX, textureY, labelText);
        this.textureX = textureX;
        this.textureY = textureY;
        this.label = new GuiLabel(this, this.x + this.w / 2, this.y + this.h / 2, labelText, 0xffffff, true);
    }

    /**
     * FML METHODS
     */

    /**
     * Draws this button to the screen.
     */
    @Override
    public void drawBackground(int mouseX, int mouseY)
    {
        if (this.visible)
        {
            int k = isMouseInsideBounds(mouseX, mouseY) ? 40 : 20;
            drawContinuousTexturedBox(buttonTextures, this.x, this.y, 0, 46 + k, this.getWidth(), this.getHeight(), 200, 20, 2, 3, 2, 2, this.zLevel);
            int color = 14737632;

            if (isMouseInsideBounds(mouseX, mouseY))
            {
                color = 16777120;
            }
            label.setColor(color);
        }
    }

    /**
     * Draws a textured box of any size (smallest size is borderSize * 2 square) based on a fixed size textured box with continuous borders
     * and filler. The provided ResourceLocation object will be bound using
     * Minecraft.getMinecraft().getTextureManager().bindTexture(resourceLocation).
     *
     * @param res the ResourceLocation object that contains the desired image
     * @param x x axis offset
     * @param y y axis offset
     * @param u bound resource location image x offset
     * @param v bound resource location image y offset
     * @param width the desired box width
     * @param height the desired box height
     * @param textureWidth the width of the box texture in the resource location image
     * @param textureHeight the height of the box texture in the resource location image
     * @param topBorder the size of the box's top border
     * @param bottomBorder the size of the box's bottom border
     * @param leftBorder the size of the box's left border
     * @param rightBorder the size of the box's right border
     * @param zLevel the zLevel to draw at
     */
    public static void drawContinuousTexturedBox(ResourceLocation res, int x, int y, int u, int v, int width, int height, int textureWidth, int textureHeight,
                                                 int topBorder, int bottomBorder, int leftBorder, int rightBorder, float zLevel)
    {
        Minecraft.getMinecraft().getTextureManager().bindTexture(res);
        drawContinuousTexturedBox(x, y, u, v, width, height, textureWidth, textureHeight, topBorder, bottomBorder, leftBorder, rightBorder, zLevel);
    }

    /**
     * Draws a textured box of any size (smallest size is borderSize * 2 square) based on a fixed size textured box with continuous borders
     * and filler. It is assumed that the desired texture ResourceLocation object has been bound using
     * Minecraft.getMinecraft().getTextureManager().bindTexture(resourceLocation).
     *
     * @param x x axis offset
     * @param y y axis offset
     * @param u bound resource location image x offset
     * @param v bound resource location image y offset
     * @param width the desired box width
     * @param height the desired box height
     * @param textureWidth the width of the box texture in the resource location image
     * @param textureHeight the height of the box texture in the resource location image
     * @param topBorder the size of the box's top border
     * @param bottomBorder the size of the box's bottom border
     * @param leftBorder the size of the box's left border
     * @param rightBorder the size of the box's right border
     * @param zLevel the zLevel to draw at
     */
    public static void drawContinuousTexturedBox(int x, int y, int u, int v, int width, int height, int textureWidth, int textureHeight,
                                                 int topBorder, int bottomBorder, int leftBorder, int rightBorder, float zLevel)
    {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        int fillerWidth = textureWidth - leftBorder - rightBorder;
        int fillerHeight = textureHeight - topBorder - bottomBorder;
        int canvasWidth = width - leftBorder - rightBorder;
        int canvasHeight = height - topBorder - bottomBorder;
        int xPasses = canvasWidth / fillerWidth;
        int remainderWidth = canvasWidth % fillerWidth;
        int yPasses = canvasHeight / fillerHeight;
        int remainderHeight = canvasHeight % fillerHeight;

        // Draw Border
        // Top Left
        drawTexturedModalRect(x, y, u, v, leftBorder, topBorder, zLevel);
        // Top Right
        drawTexturedModalRect(x + leftBorder + canvasWidth, y, u + leftBorder + fillerWidth, v, rightBorder, topBorder, zLevel);
        // Bottom Left
        drawTexturedModalRect(x, y + topBorder + canvasHeight, u, v + topBorder + fillerHeight, leftBorder, bottomBorder, zLevel);
        // Bottom Right
        drawTexturedModalRect(x + leftBorder + canvasWidth, y + topBorder + canvasHeight, u + leftBorder + fillerWidth, v + topBorder + fillerHeight, rightBorder, bottomBorder, zLevel);

        for (int i = 0; i < xPasses + (remainderWidth > 0 ? 1 : 0); i++)
        {
            // Top Border
            drawTexturedModalRect(x + leftBorder + (i * fillerWidth), y, u + leftBorder, v, (i == xPasses ? remainderWidth : fillerWidth), topBorder, zLevel);
            // Bottom Border
            drawTexturedModalRect(x + leftBorder + (i * fillerWidth), y + topBorder + canvasHeight, u + leftBorder, v + topBorder + fillerHeight, (i == xPasses ? remainderWidth : fillerWidth), bottomBorder, zLevel);

            // Throw in some filler for good measure
            for (int j = 0; j < yPasses + (remainderHeight > 0 ? 1 : 0); j++)
                drawTexturedModalRect(x + leftBorder + (i * fillerWidth), y + topBorder + (j * fillerHeight), u + leftBorder, v + topBorder, (i == xPasses ? remainderWidth : fillerWidth), (j == yPasses ? remainderHeight : fillerHeight), zLevel);
        }

        // Side Borders
        for (int j = 0; j < yPasses + (remainderHeight > 0 ? 1 : 0); j++)
        {
            // Left Border
            drawTexturedModalRect(x, y + topBorder + (j * fillerHeight), u, v + topBorder, leftBorder, (j == yPasses ? remainderHeight : fillerHeight), zLevel);
            // Right Border
            drawTexturedModalRect(x + leftBorder + canvasWidth, y + topBorder + (j * fillerHeight), u + leftBorder + fillerWidth, v + topBorder, rightBorder, (j == yPasses ? remainderHeight : fillerHeight), zLevel);
        }
    }

    public static void drawTexturedModalRect(int x, int y, int u, int v, int width, int height, float zLevel)
    {
        float var7 = 0.00390625F;
        float var8 = 0.00390625F;
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.addVertexWithUV((x + 0), (y + height), zLevel, ((u + 0) * var7), ((v + height) * var8));
        tessellator.addVertexWithUV((x + width), (y + height), zLevel, ((u + width) * var7), ((v + height) * var8));
        tessellator.addVertexWithUV((x + width), (y + 0), zLevel, ((u + width) * var7), ((v + 0) * var8));
        tessellator.addVertexWithUV((x + 0), (y + 0), zLevel, ((u + 0) * var7), ((v + 0) * var8));
        tessellator.draw();
    }
}
