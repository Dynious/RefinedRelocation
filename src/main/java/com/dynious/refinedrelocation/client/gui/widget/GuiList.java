package com.dynious.refinedrelocation.client.gui.widget;

import com.dynious.refinedrelocation.client.gui.IGuiParent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

public abstract class GuiList extends GuiWidgetBase
{
    public int numFiltersPerScreen;
    public int filterRowHeight = 10;
    public int rowSpacing = 1;
    public int scrollBarAreaWidth = 10;
    public int scrollBarWidth = 7;
    public int scrollBarScaledHeight;
    public int scrollBarYPos;
    public int scrollBarXPos;
    public int scrollBarColor = 0xFFAAAAAA;
    public int mouseClickY = -1;
    public int indexWhenClicked;
    public int lastNumberOfMoves;
    protected int currentIndex = 0;
    protected GuiListCheckbox checkboxes[];

    public GuiList(IGuiParent parent, int x, int y, int w, int h)
    {
        super(parent, x, y, w, h);

        numFiltersPerScreen = (int) Math.floor(((h + 2 * rowSpacing) / (filterRowHeight + rowSpacing)));
    }

    public void init()
    {
        checkboxes = new GuiListCheckbox[numFiltersPerScreen];
        for (int i = 0; i < numFiltersPerScreen && i < getListSize(); i++)
        {
            checkboxes[i] = new GuiListCheckbox(this, x, y + i * (filterRowHeight + rowSpacing), isScrollable() ? w - scrollBarAreaWidth : w, filterRowHeight, i, this);
        }
        recalculateScrollBar();
    }

    public abstract int getListSize();

    public abstract String getString(int index);

    public abstract void onClicked(int index, boolean newState);

    public abstract boolean getValue(int index);

    public int getCurrentIndex()
    {
        return this.currentIndex;
    }

    public void setCurrentIndex(int index)
    {
        if (!isScrollable())
            return;

        index = Math.min(getListSize() - numFiltersPerScreen, Math.max(0, index));

        this.currentIndex = index;
        for (int i = 0; i < numFiltersPerScreen; i++)
        {
            checkboxes[i].setIndex(i + currentIndex);
        }
        recalculateScrollBar();
    }

    public void recalculateScrollBar()
    {
        if (!isScrollable())
            return;

        int scrollBarTotalHeight = h - 2;
        this.scrollBarScaledHeight = scrollBarTotalHeight * numFiltersPerScreen / getListSize() + 1;
        this.scrollBarYPos = y + ((scrollBarTotalHeight - scrollBarScaledHeight) * currentIndex / (getListSize() - numFiltersPerScreen));
        this.scrollBarXPos = x + w - scrollBarAreaWidth / 2 + scrollBarWidth / 2 + 1;
    }

    public boolean isScrollable()
    {
        return (getListSize() - numFiltersPerScreen) > 0;
    }

    @Override
    public void drawBackground(int mouseX, int mouseY)
    {
        if (mouseClickY != -1)
        {
            float pixelsPerFilter = ((float) h - 2 - scrollBarScaledHeight) / (getListSize() - numFiltersPerScreen);
            if (pixelsPerFilter != 0)
            {
                int numberOfFiltersMoved = (int) ((mouseY - mouseClickY) / pixelsPerFilter);
                if (numberOfFiltersMoved != lastNumberOfMoves)
                {
                    setCurrentIndex(indexWhenClicked + numberOfFiltersMoved);
                    lastNumberOfMoves = numberOfFiltersMoved;
                }
            }
        }

        GL11.glColor4f(1F, 1F, 1F, 1F);

        GuiContainer.drawRect(x - 1, y - 1, x + w, y + h, 0xFF373737);
        GuiContainer.drawRect(x, y, isScrollable() ? x + w - scrollBarAreaWidth : x + w, y + h, 0xFF5F5F5F);
        drawHorizontalGradient(x, y + h - 1, x + w, y + h, 0xFF373737, 0xFFFFFFFF);
        drawGradientRect(x + w, y - 1, x + w + 1, y + h, 0xFF373737, 0xFFFFFFFF);
        if (isScrollable())
            drawGradientRect(x + w - scrollBarAreaWidth, y, x + w - scrollBarAreaWidth + 1, y + h, 0xFF373737, 0xFFFFFFFF);

        for (GuiCheckbox checkbox : checkboxes)
        {
            if (checkbox != null)
                GuiContainer.drawRect(checkbox.x, checkbox.y, checkbox.x + checkbox.w, checkbox.y + checkbox.h, 0xFF8b8b8b);
        }

        if (isScrollable())
            GuiContainer.drawRect(scrollBarXPos - scrollBarWidth, scrollBarYPos, scrollBarXPos, scrollBarYPos + scrollBarScaledHeight, scrollBarColor);

        super.drawBackground(mouseX, mouseY);
    }

    @Override
    public void handleMouseInput()
    {
        super.handleMouseInput();

        int x = Mouse.getEventX() * Minecraft.getMinecraft().currentScreen.width / this.mc.displayWidth;
        int y = Minecraft.getMinecraft().currentScreen.height - Mouse.getEventY() * Minecraft.getMinecraft().currentScreen.height / this.mc.displayHeight - 1;

        if (isMouseInsideBounds(x, y))
        {
            int i = Mouse.getEventDWheel();

            if (i == 0)
                return;

            setCurrentIndex(i > 0 ? getCurrentIndex() - 1 : getCurrentIndex() + 1);
        }
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int type, boolean isShiftKeyDown)
    {
        super.mouseClicked(mouseX, mouseY, type, isShiftKeyDown);

        if (mouseX >= scrollBarXPos - scrollBarWidth && mouseX <= scrollBarXPos && mouseY >= scrollBarYPos && mouseY <= scrollBarYPos + scrollBarScaledHeight)
        {
            mouseClickY = mouseY;
            indexWhenClicked = getCurrentIndex();
        }
    }

    @Override
    public void mouseMovedOrUp(int mouseX, int mouseY, int type)
    {
        super.mouseMovedOrUp(mouseX, mouseY, type);
        if (type != -1 && mouseClickY != -1)
        {
            mouseClickY = -1;
            indexWhenClicked = 0;
            lastNumberOfMoves = 0;
        }
    }

    protected void drawHorizontalGradient(int p_73733_1_, int p_73733_2_, int p_73733_3_, int p_73733_4_, int p_73733_5_, int p_73733_6_)
    {
        float f = (float)(p_73733_5_ >> 24 & 255) / 255.0F;
        float f1 = (float)(p_73733_5_ >> 16 & 255) / 255.0F;
        float f2 = (float)(p_73733_5_ >> 8 & 255) / 255.0F;
        float f3 = (float)(p_73733_5_ & 255) / 255.0F;
        float f4 = (float)(p_73733_6_ >> 24 & 255) / 255.0F;
        float f5 = (float)(p_73733_6_ >> 16 & 255) / 255.0F;
        float f6 = (float)(p_73733_6_ >> 8 & 255) / 255.0F;
        float f7 = (float)(p_73733_6_ & 255) / 255.0F;
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_ALPHA_TEST);
        OpenGlHelper.glBlendFunc(770, 771, 1, 0);
        GL11.glShadeModel(GL11.GL_SMOOTH);
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.setColorRGBA_F(f1, f2, f3, f);
        tessellator.addVertex((double)p_73733_1_, (double)p_73733_2_, (double)this.zLevel);
        tessellator.addVertex((double)p_73733_1_, (double)p_73733_4_, (double)this.zLevel);
        tessellator.setColorRGBA_F(f5, f6, f7, f4);
        tessellator.addVertex((double)p_73733_3_, (double)p_73733_4_, (double)this.zLevel);
        tessellator.addVertex((double)p_73733_3_, (double)p_73733_2_, (double)this.zLevel);
        tessellator.draw();
        GL11.glShadeModel(GL11.GL_FLAT);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
    }
}
