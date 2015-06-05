package com.dynious.refinedrelocation.client.gui.widget;

import com.dynious.refinedrelocation.api.filter.IMultiFilterChild;
import com.dynious.refinedrelocation.api.gui.IGuiWidgetWrapped;
import com.dynious.refinedrelocation.client.gui.IGuiParent;
import com.dynious.refinedrelocation.grid.filter.MultiFilterRegistry;
import com.dynious.refinedrelocation.lib.Resources;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.util.MathHelper;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;

public class GuiFilterTypeList extends GuiWidgetBase implements IGuiWidgetWrapped {

    public int numFiltersPerScreen;
    public int rowHeight = 27;
    public int rowSpacing = 0;
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

    protected final GuiButtonFilterType[] filterTypes;
    protected final List<IMultiFilterChild> availableFilters = new ArrayList<IMultiFilterChild>();

    public GuiFilterTypeList(IGuiParent parent, int x, int y, int w, int h)
    {
        super(parent, x, y, w, h);

        for(Class<? extends IMultiFilterChild> entry : MultiFilterRegistry.getFilters())
        {
            try
            {
                IMultiFilterChild filterChild = entry.newInstance();
                availableFilters.add(filterChild);
            }
            catch (InstantiationException e)
            {
                e.printStackTrace();
            }
            catch (IllegalAccessException e)
            {
                e.printStackTrace();
            }
        }

        numFiltersPerScreen = (int) Math.floor(((h - 73 + 2 * rowSpacing) / (rowHeight + rowSpacing)));

        int curY = y;
        filterTypes = new GuiButtonFilterType[numFiltersPerScreen];
        for (int i = 0; i < numFiltersPerScreen; i++)
        {
            filterTypes[i] = new GuiButtonFilterType(this, x, curY);
            filterTypes[i].setFilter((i + currentIndex < availableFilters.size()) ? availableFilters.get(i + currentIndex) : null);
            curY += rowHeight - 1;
        }

        recalculateScrollBar();
    }

    public int getCurrentIndex()
    {
        return this.currentIndex;
    }

    public void setCurrentIndex(int index)
    {
        index = Math.min(Math.max(availableFilters.size() - numFiltersPerScreen, 0), Math.max(0, index));

        this.currentIndex = index;
        for (int i = 0; i < numFiltersPerScreen; i++)
        {
            filterTypes[i].setFilter((i + currentIndex < availableFilters.size()) ? availableFilters.get(i + currentIndex) : null);
        }
        recalculateScrollBar();
    }

    public void recalculateScrollBar()
    {
        int scrollBarTotalHeight = h - 124;
        this.scrollBarScaledHeight = scrollBarTotalHeight * numFiltersPerScreen / availableFilters.size() + 1;
        this.scrollBarYPos = y + 1 + ((scrollBarTotalHeight - scrollBarScaledHeight) * currentIndex / (availableFilters.size() - numFiltersPerScreen));
        this.scrollBarXPos = x + 1 + w - scrollBarAreaWidth / 2 + scrollBarWidth / 2 + 1;
    }

    @Override
    public void drawBackground(int mouseX, int mouseY)
    {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

        if (mouseClickY != -1)
        {
            float pixelsPerFilter = ((float) h - 73 - scrollBarScaledHeight) / (availableFilters.size() - numFiltersPerScreen);
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

        super.drawBackground(mouseX, mouseY);

        mc.getTextureManager().bindTexture(Resources.GUI_MODULAR_FILTER);
        drawTexturedModalRect(x + 8 + w - scrollBarAreaWidth - scrollBarWidth, y, 162, 54, 11, 105);
        GuiContainer.drawRect(scrollBarXPos - scrollBarWidth, scrollBarYPos, scrollBarXPos, scrollBarYPos + scrollBarScaledHeight, scrollBarColor);


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
