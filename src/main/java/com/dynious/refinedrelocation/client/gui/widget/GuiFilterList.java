package com.dynious.refinedrelocation.client.gui.widget;

import com.dynious.refinedrelocation.api.filter.IChecklistFilter;
import com.dynious.refinedrelocation.api.gui.IGuiWidgetWrapped;
import com.dynious.refinedrelocation.lib.Resources;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.util.StatCollector;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

public class GuiFilterList extends GuiWidgetBase implements IGuiWidgetWrapped {

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
    protected IChecklistFilter filter;
    protected int currentIndex = 0;
    protected GuiCheckboxFilter filters[];

    public GuiFilterList(int x, int y, int w, int h, IChecklistFilter filter)
    {
        super(x, y, w, h);
        this.filter = filter;

        GuiLabel headerLabel = new GuiLabel(this, x, y, StatCollector.translateToLocal(filter.getFilterName()));
        headerLabel.drawCentered = false;

        numFiltersPerScreen = (int) Math.floor(((h - 12 + 2 * rowSpacing) / (filterRowHeight + rowSpacing)));

        filters = new GuiCheckboxFilter[numFiltersPerScreen];
        for (int i = 0; i < numFiltersPerScreen; i++)
        {
            filters[i] = new GuiCheckboxFilter(this, x, y + 12 + i * (filterRowHeight + rowSpacing), w - scrollBarAreaWidth, filterRowHeight, i, filter);
        }
        recalculateScrollBar();
    }

    public int getCurrentIndex()
    {
        return this.currentIndex;
    }

    public void setCurrentIndex(int index)
    {
        index = Math.min(filter.getOptionCount() - numFiltersPerScreen, Math.max(0, index));

        this.currentIndex = index;
        for (int i = 0; i < numFiltersPerScreen; i++)
        {
            filters[i].setIndex(i + currentIndex);
        }
        recalculateScrollBar();
    }

    public void recalculateScrollBar()
    {
        int scrollBarTotalHeight = h - 22;
        this.scrollBarScaledHeight = scrollBarTotalHeight * numFiltersPerScreen / filter.getOptionCount() + 1;
        this.scrollBarYPos = y + 13 + ((scrollBarTotalHeight - scrollBarScaledHeight) * currentIndex / (filter.getOptionCount() - numFiltersPerScreen));
        this.scrollBarXPos = x + 1 + w - scrollBarAreaWidth / 2 + scrollBarWidth / 2 + 1;
    }

    @Override
    public void drawBackground(int mouseX, int mouseY)
    {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        mc.renderEngine.bindTexture(Resources.GUI_MODULAR_FILTER);
        this.drawTexturedModalRect(x, y + 12, 0, 0, 162, 89 - 12);

        if (mouseClickY != -1)
        {
            float pixelsPerFilter = ((float) h - 2 - scrollBarScaledHeight) / (filter.getOptionCount() - numFiltersPerScreen);
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
