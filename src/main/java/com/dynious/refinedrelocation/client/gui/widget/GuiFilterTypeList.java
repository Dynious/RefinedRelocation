package com.dynious.refinedrelocation.client.gui.widget;

import com.dynious.refinedrelocation.api.filter.IMultiFilterChild;
import com.dynious.refinedrelocation.api.gui.IGuiWidgetWrapped;
import com.dynious.refinedrelocation.client.gui.GuiFiltered;
import com.dynious.refinedrelocation.client.gui.widget.button.GuiButtonFilterType;
import com.dynious.refinedrelocation.grid.filter.MultiFilterRegistry;
import com.dynious.refinedrelocation.lib.Resources;
import com.dynious.refinedrelocation.lib.Strings;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.util.StatCollector;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;

public class GuiFilterTypeList extends GuiWidgetBase implements IGuiWidgetWrapped
{
    private static final int ROW_HEIGHT = 27;
    private static final int SCROLLBAR_COLOR = 0xFFAAAAAA;
    private static final int SCROLLBAR_AREA_WIDTH = 10;
    private static final int SCROLLBAR_WIDTH = 7;

    private final int numFiltersPerScreen;
    private final int listOffsetY;

    private int scrollBarScaledHeight;
    private int scrollBarYPos;
    private int scrollBarXPos;

    public int mouseClickY = -1;
    public int indexWhenClicked;
    public int lastNumberOfMoves;

    protected int currentIndex = 0;

    protected final GuiButtonFilterType[] filterTypes;
    protected final List<IMultiFilterChild> availableFilters = new ArrayList<IMultiFilterChild>();

    public GuiFilterTypeList(GuiFiltered parent, int x, int y, int w, int h)
    {
        super(x, y, w, h);

        for (Class<? extends IMultiFilterChild> entry : MultiFilterRegistry.getFilters())
        {
            try
            {
                IMultiFilterChild filterChild = entry.newInstance();
                if (filterChild.canFilterBeUsedOnTile(parent.getFilter().getFilterTile().getTileEntity()))
                {
                    availableFilters.add(filterChild);
                }
            } catch (InstantiationException e)
            {
                e.printStackTrace();
            } catch (IllegalAccessException e)
            {
                e.printStackTrace();
            }
        }

        GuiLabel headerLabel = new GuiLabel(this, x, y, StatCollector.translateToLocal(Strings.SELECT_FILTER_TYPE));
        headerLabel.drawCentered = false;
        listOffsetY = headerLabel.h + 10;

        numFiltersPerScreen = (int) Math.floor(((h - listOffsetY) / ROW_HEIGHT));

        int curY = y + listOffsetY;
        filterTypes = new GuiButtonFilterType[numFiltersPerScreen];
        for (int i = 0; i < numFiltersPerScreen; i++)
        {
            filterTypes[i] = new GuiButtonFilterType(this, x, curY);
            filterTypes[i].setFilter((i + currentIndex < availableFilters.size()) ? availableFilters.get(i + currentIndex) : null);
            curY += ROW_HEIGHT - 1;
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
        int scrollBarTotalHeight = 26 * 4 - 2;
        this.scrollBarScaledHeight = (int) (scrollBarTotalHeight * Math.min(1, (float) numFiltersPerScreen / (float) availableFilters.size()));
        this.scrollBarYPos = y + listOffsetY + 1 + ((scrollBarTotalHeight - scrollBarScaledHeight) * currentIndex / Math.max(1, (availableFilters.size() - numFiltersPerScreen)));
        this.scrollBarXPos = x + 1 + w - SCROLLBAR_AREA_WIDTH / 2 + SCROLLBAR_WIDTH / 2 + 1;
    }

    @Override
    public void drawBackground(int mouseX, int mouseY)
    {
        GL11.glColor4f(1f, 1f, 1f, 1f);

        if (mouseClickY != -1)
        {
            float pixelsPerFilter = ((float) h - listOffsetY - 4 - scrollBarScaledHeight) / (availableFilters.size() - numFiltersPerScreen);
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

        GL11.glColor4f(1f, 1f, 1f, 1f);

        super.drawBackground(mouseX, mouseY);

        mc.getTextureManager().bindTexture(Resources.GUI_MODULAR_FILTER);
        drawTexturedModalRect(x + 8 + w - SCROLLBAR_AREA_WIDTH - SCROLLBAR_WIDTH, y + listOffsetY, 162, 54, 11, 105);
        GuiContainer.drawRect(scrollBarXPos - SCROLLBAR_WIDTH, scrollBarYPos, scrollBarXPos, scrollBarYPos + scrollBarScaledHeight, SCROLLBAR_COLOR);


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

        if (mouseX >= scrollBarXPos - SCROLLBAR_WIDTH && mouseX <= scrollBarXPos && mouseY >= scrollBarYPos && mouseY <= scrollBarYPos + scrollBarScaledHeight)
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
