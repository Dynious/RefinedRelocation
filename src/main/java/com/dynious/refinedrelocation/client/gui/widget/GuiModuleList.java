package com.dynious.refinedrelocation.client.gui.widget;

import com.dynious.refinedrelocation.api.gui.IGuiWidgetWrapped;
import com.dynious.refinedrelocation.api.relocator.IRelocatorModule;
import com.dynious.refinedrelocation.client.gui.IGuiParent;
import com.dynious.refinedrelocation.client.gui.widget.button.GuiButtonOpenModuleGUI;
import com.dynious.refinedrelocation.grid.relocator.RelocatorMultiModule;
import com.dynious.refinedrelocation.lib.Resources;
import com.dynious.refinedrelocation.lib.Strings;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.util.StatCollector;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import java.util.List;

public class GuiModuleList extends GuiWidgetBase implements IGuiWidgetWrapped {

    private static final int ROW_HEIGHT = 27;
    private static final int SCROLLBAR_COLOR = 0xFFAAAAAA;
    private static final int SCROLLBAR_AREA_WIDTH = 10;
    private static final int SCROLLBAR_WIDTH = 7;

    private final int numModulesPerScreen;

    private int scrollBarScaledHeight;
    private int scrollBarYPos;
    private int scrollBarXPos;

    public int mouseClickY = -1;
    public int indexWhenClicked;
    public int lastNumberOfMoves;

    protected int currentIndex = 0;

    protected final GuiButtonOpenModuleGUI[] moduleButtons;
    protected final List<IRelocatorModule> availableModules;

    public GuiModuleList(IGuiParent parent, RelocatorMultiModule multiModule, int x, int y, int w, int h) {
        super(parent, x, y, w, h);
        availableModules = multiModule.getModules();

        numModulesPerScreen = (int) Math.floor((h / ROW_HEIGHT));

        int curY = y;
        moduleButtons = new GuiButtonOpenModuleGUI[numModulesPerScreen];
        for (int i = 0; i < numModulesPerScreen; i++) {
            moduleButtons[i] = new GuiButtonOpenModuleGUI(this, x, curY, multiModule);
            moduleButtons[i].setModule(i + currentIndex, (i + currentIndex < availableModules.size()) ? availableModules.get(i + currentIndex) : null);
            curY += ROW_HEIGHT - 1;
        }

        recalculateScrollBar();
    }

    public int getCurrentIndex() {
        return this.currentIndex;
    }

    public void setCurrentIndex(int index) {
        index = Math.min(Math.max(availableModules.size() - numModulesPerScreen, 0), Math.max(0, index));

        this.currentIndex = index;
        for (int i = 0; i < numModulesPerScreen; i++) {
            moduleButtons[i].setModule(i + currentIndex, (i + currentIndex < availableModules.size()) ? availableModules.get(i + currentIndex) : null);
        }
        recalculateScrollBar();
    }

    public void recalculateScrollBar() {
        int scrollBarTotalHeight = 26 * 4 - 2;
        this.scrollBarScaledHeight = (int) (scrollBarTotalHeight * Math.min(1, (float) numModulesPerScreen / (float) availableModules.size()));
        this.scrollBarYPos = y + 1 + ((scrollBarTotalHeight - scrollBarScaledHeight) * currentIndex / Math.max(1, (availableModules.size() - numModulesPerScreen)));
        this.scrollBarXPos = x + 1 + w - SCROLLBAR_AREA_WIDTH / 2 + SCROLLBAR_WIDTH / 2 + 1;
    }

    @Override
    public void drawBackground(int mouseX, int mouseY) {
        GL11.glColor4f(1f, 1f, 1f, 1f);

        if (mouseClickY != -1) {
            float pixelsPerFilter = ((float) h - 4 - scrollBarScaledHeight) / (availableModules.size() - numModulesPerScreen);
            if (pixelsPerFilter != 0) {
                int numberOfFiltersMoved = (int) ((mouseY - mouseClickY) / pixelsPerFilter);
                if (numberOfFiltersMoved != lastNumberOfMoves) {
                    setCurrentIndex(indexWhenClicked + numberOfFiltersMoved);
                    lastNumberOfMoves = numberOfFiltersMoved;
                }
            }
        }

        GL11.glColor4f(1f, 1f, 1f, 1f);

        super.drawBackground(mouseX, mouseY);

        mc.getTextureManager().bindTexture(Resources.GUI_MODULAR_FILTER);
        drawTexturedModalRect(x + 8 + w - SCROLLBAR_AREA_WIDTH - SCROLLBAR_WIDTH, y, 162, 54, 11, 105);
        GuiContainer.drawRect(scrollBarXPos - SCROLLBAR_WIDTH, scrollBarYPos, scrollBarXPos, scrollBarYPos + scrollBarScaledHeight, SCROLLBAR_COLOR);

    }

    @Override
    public void handleMouseInput() {
        super.handleMouseInput();

        int i = Mouse.getEventDWheel();
        if (i == 0) {
            return;
        }

        setCurrentIndex(i > 0 ? getCurrentIndex() - 1 : getCurrentIndex() + 1);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int type, boolean isShiftKeyDown) {
        super.mouseClicked(mouseX, mouseY, type, isShiftKeyDown);

        if (mouseX >= scrollBarXPos - SCROLLBAR_WIDTH && mouseX <= scrollBarXPos && mouseY >= scrollBarYPos && mouseY <= scrollBarYPos + scrollBarScaledHeight) {
            mouseClickY = mouseY;
            indexWhenClicked = getCurrentIndex();
        }
    }

    @Override
    public void mouseMovedOrUp(int mouseX, int mouseY, int type) {
        super.mouseMovedOrUp(mouseX, mouseY, type);
        if (type != -1 && mouseClickY != -1) {
            mouseClickY = -1;
            indexWhenClicked = 0;
            lastNumberOfMoves = 0;
        }
    }
}
