package com.dynious.refinedrelocation.gui.widget;

import com.dynious.refinedrelocation.api.tileentity.IFilterTileGUI;
import com.dynious.refinedrelocation.gui.IGuiParent;
import net.minecraft.client.gui.inventory.GuiContainer;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

public class GuiFilterList extends GuiRefinedRelocationWidgetBase
{
    protected IFilterTileGUI tile;
    public int numFiltersPerScreen;
    public int filterRowHeight = 10;
    protected int currentIndex = 0;
    public int rowSpacing = 1;

    protected GuiCheckboxFilter filters[];

    public int scrollBarAreaWidth = 10;
    public int scrollBarWidth = 7;
    public int scrollBarScaledHeight;
    public int scrollBarYPos;
    public int scrollBarXPos;
    public int scrollBarColor = 0xFFAAAAAA;

    public int mouseClickY = -1;
    public int indexWhenClicked;
    public int lastNumberOfMoves;

    public GuiFilterList(IGuiParent parent, int x, int y, int w, int h, IFilterTileGUI tile)
    {
        super(parent, x, y, w, h);
        this.tile = tile;

        numFiltersPerScreen = (int) Math.floor(((h + 2 * rowSpacing) / (filterRowHeight + rowSpacing)));

        filters = new GuiCheckboxFilter[numFiltersPerScreen];
        for (int i = 0; i < numFiltersPerScreen; i++)
        {
            filters[i] = new GuiCheckboxFilter(this, x, y + i * (filterRowHeight + rowSpacing), w - scrollBarAreaWidth, filterRowHeight, i, tile);
        }
        recalculateScrollBar();
    }

    public int getCurrentIndex()
    {
        return this.currentIndex;
    }

    public void setCurrentIndex(int index)
    {
        index = Math.min(tile.getFilter().getSize() - numFiltersPerScreen, Math.max(0, index));

        this.currentIndex = index;
        for (int i = 0; i < numFiltersPerScreen; i++)
        {
            filters[i].setIndex(i + currentIndex);
        }
        recalculateScrollBar();
    }

    public void recalculateScrollBar()
    {
        int scrollBarTotalHeight = h - 2;
        this.scrollBarScaledHeight = (int) (scrollBarTotalHeight * numFiltersPerScreen / tile.getFilter().getSize()) + 1;
        this.scrollBarYPos = y + ((scrollBarTotalHeight - scrollBarScaledHeight) * currentIndex / (tile.getFilter().getSize() - numFiltersPerScreen));
        this.scrollBarXPos = x + w - scrollBarAreaWidth / 2 + scrollBarWidth / 2 + 1;
    }

    @Override
    public void drawBackground(int mouseX, int mouseY)
    {
        if (mouseClickY != -1)
        {
            int pixelsPerFilter = (h - 2 - scrollBarScaledHeight) / (tile.getFilter().getSize() - numFiltersPerScreen);
            int numberOfFiltersMoved = (mouseY - mouseClickY) / pixelsPerFilter;
            if (numberOfFiltersMoved != lastNumberOfMoves)
            {
                setCurrentIndex(indexWhenClicked + numberOfFiltersMoved);
                lastNumberOfMoves = numberOfFiltersMoved;
            }
        }

        GL11.glColor4f(1F, 1F, 1F, 1F);

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
