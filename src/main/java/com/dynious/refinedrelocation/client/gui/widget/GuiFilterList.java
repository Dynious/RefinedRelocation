package com.dynious.refinedrelocation.client.gui.widget;

import com.dynious.refinedrelocation.api.filter.IChecklistFilter;
import com.dynious.refinedrelocation.api.gui.IGuiWidgetWrapped;
import com.dynious.refinedrelocation.lib.Resources;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.util.StatCollector;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

public class GuiFilterList extends GuiWidgetBase implements IGuiWidgetWrapped
{

    private static final int ROW_SPACING = 1;
    private static final int ROW_HEIGHT = 10;
    private static final int SCROLLBAR_COLOR = 0xFFAAAAAA;
    private static final int SCROLLBAR_AREA_WIDTH = 10;
    private static final int SCROLLBAR_WIDTH = 7;

    private final int numFiltersPerScreen;
    private final int listOffsetY;

    private int scrollBarScaledHeight;
    private int scrollBarYPos;
    private int scrollBarXPos;

    private int mouseClickY = -1;
    private int indexWhenClicked;
    private int lastNumberOfMoves;

    protected IChecklistFilter filter;
    protected int currentIndex = 0;
    protected GuiCheckboxFilter filters[];

    public GuiFilterList(int x, int y, int w, int h, IChecklistFilter filter)
    {
        super(x, y, w, h);
        this.filter = filter;

        GuiLabel headerLabel = new GuiLabel(this, x, y, StatCollector.translateToLocal(filter.getNameLangKey()));
        headerLabel.drawCentered = false;
        listOffsetY = headerLabel.h + 8;

        numFiltersPerScreen = (int) Math.floor(((h - listOffsetY + 2 * ROW_SPACING) / (ROW_HEIGHT + ROW_SPACING)));

        filters = new GuiCheckboxFilter[numFiltersPerScreen];
        for (int i = 0; i < numFiltersPerScreen; i++)
        {
            filters[i] = new GuiCheckboxFilter(this, x, y + listOffsetY + i * (ROW_HEIGHT + ROW_SPACING), w - SCROLLBAR_AREA_WIDTH, ROW_HEIGHT, i, filter);
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
        int scrollBarTotalHeight =  h - listOffsetY - 4;
        this.scrollBarScaledHeight = scrollBarTotalHeight * Math.min(1, numFiltersPerScreen / filter.getOptionCount());
        this.scrollBarYPos = y + listOffsetY + 1 + ((scrollBarTotalHeight - scrollBarScaledHeight) * currentIndex / Math.max(1, filter.getOptionCount() - numFiltersPerScreen));
        this.scrollBarXPos = x + 1 + w - SCROLLBAR_AREA_WIDTH / 2 + SCROLLBAR_WIDTH / 2 + 1;
    }

    @Override
    public void drawBackground(int mouseX, int mouseY)
    {
        GL11.glColor4f(1f, 1f, 1f, 1f);
        mc.renderEngine.bindTexture(Resources.GUI_MODULAR_FILTER);
        drawTexturedModalRect(x, y + listOffsetY, 0, 0, 162, 121);

        if (mouseClickY != -1)
        {
            float pixelsPerFilter = ((float) h - listOffsetY - 4 - scrollBarScaledHeight) / (filter.getOptionCount() - numFiltersPerScreen);
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
        GuiContainer.drawRect(scrollBarXPos - SCROLLBAR_WIDTH, scrollBarYPos, scrollBarXPos, scrollBarYPos + scrollBarScaledHeight, SCROLLBAR_COLOR);

        super.drawBackground(mouseX, mouseY);
    }

    @Override
    public void handleMouseInput()
    {
        super.handleMouseInput();

        int i = Mouse.getEventDWheel();

        if (i == 0)
        {
            return;
        }

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
