package com.dynious.refinedrelocation.client.gui.widget;

import com.dynious.refinedrelocation.client.gui.IGuiParent;
import net.minecraft.client.gui.inventory.GuiContainer;
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
            checkboxes[i] = new GuiListCheckbox(this, x, y + i * (filterRowHeight + rowSpacing), w - scrollBarAreaWidth, filterRowHeight, i, this);
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

        if (isScrollable())
            GuiContainer.drawRect(scrollBarXPos - scrollBarWidth, scrollBarYPos, scrollBarXPos, scrollBarYPos + scrollBarScaledHeight, scrollBarColor);

        super.drawBackground(mouseX, mouseY);
    }

    @Override
    public void handleMouseInput()
    {
        super.handleMouseInput();

        int i = Mouse.getEventDWheel();

        if (i == 0)
            return;

        setCurrentIndex(i > 0 ? getCurrentIndex() - 1 : getCurrentIndex() + 1);
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
}
