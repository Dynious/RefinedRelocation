package com.dynious.blex.gui.widget;

import com.dynious.blex.gui.IGuiParent;
import com.dynious.blex.tileentity.IFilterTile;
import net.minecraft.client.gui.inventory.GuiContainer;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

public class GuiFilterList extends GuiBlExWidgetBase
{
    protected IFilterTile tile;
    public int numFiltersPerScreen;
    public int filterRowHeight = 10;
    protected int currentIndex = 0;
    public int rowSpacing = 1;

    protected GuiCheckboxFilter filters[];

    public int scrollBarAreaWidth = 5;
    public int scrollBarWidth = 2;
    public int scrollBarScaledHeight;
    public int scrollBarYPos;
    public int scrollBarXPos;
    public int scrollBarColor = 0xFFAAAAAA;

    public GuiFilterList(IGuiParent parent, int x, int y, int w, int h, IFilterTile tile)
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
        this.scrollBarScaledHeight = (int) (scrollBarTotalHeight * numFiltersPerScreen / tile.getFilter().getSize());
        this.scrollBarYPos = y + 1 + ((scrollBarTotalHeight - scrollBarScaledHeight) * currentIndex / (tile.getFilter().getSize() - numFiltersPerScreen));
        this.scrollBarXPos = x + w - scrollBarAreaWidth / 2 + scrollBarWidth / 2;
    }

    @Override
    public void drawBackground(int mouseX, int mouseY)
    {
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

}
