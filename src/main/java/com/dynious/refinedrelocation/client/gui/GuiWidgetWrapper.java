package com.dynious.refinedrelocation.client.gui;

import com.dynious.refinedrelocation.api.gui.IGuiWidgetWrapped;
import com.dynious.refinedrelocation.client.gui.widget.GuiWidgetBase;

import java.util.List;

public class GuiWidgetWrapper extends GuiWidgetBase {

	private final IGuiWidgetWrapped wrappedWidget;

	public GuiWidgetWrapper(IGuiWidgetWrapped wrappedWidget) {
		super(wrappedWidget.getX(), wrappedWidget.getY(), wrappedWidget.getWidth(), wrappedWidget.getHeight());
		this.wrappedWidget = wrappedWidget;
	}

	@Override
	public void update() {
		super.update();
		wrappedWidget.update();
	}

	@Override
	public void drawBackground(int mouseX, int mouseY) {
		super.drawBackground(mouseX, mouseY);
		wrappedWidget.drawBackground(mouseX, mouseY);
	}

	@Override
	public void drawForeground(int mouseX, int mouseY) {
		super.drawForeground(mouseX, mouseY);
		wrappedWidget.drawForeground(mouseX, mouseY);
	}

	@Override
	public boolean keyTyped(char c, int i) {
		if(wrappedWidget.keyTyped(c, i)) {
			return true;
		}
		return super.keyTyped(c, i);
	}

	@Override
	public void mouseClicked(int mouseX, int mouseY, int type, boolean isShiftKeyDown) {
		super.mouseClicked(mouseX, mouseY, type, isShiftKeyDown);
		wrappedWidget.mouseClicked(mouseX, mouseY, type, isShiftKeyDown);
	}

	@Override
	public void mouseMovedOrUp(int mouseX, int mouseY, int type) {
		super.mouseMovedOrUp(mouseX, mouseY, type);
		wrappedWidget.mouseMovedOrUp(mouseX, mouseY, type);
	}

	@Override
	public void handleMouseInput() {
		super.handleMouseInput();
		wrappedWidget.handleMouseInput();
	}

	@Override
	public List<String> getTooltip(int mouseX, int mouseY) {
		List<String> tooltipList = super.getTooltip(mouseX, mouseY);
		tooltipList.addAll(wrappedWidget.getTooltip(mouseX, mouseY));
		return tooltipList;
	}
}
