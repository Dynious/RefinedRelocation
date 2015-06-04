package com.dynious.refinedrelocation.api.gui;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public interface IGuiWidgetWrapped {

	int getX();
	int getY();
	int getWidth();
	int getHeight();
	void update();
	void drawBackground(int mouseX, int mouseY);
	void drawForeground(int mouseX, int mouseY);
	boolean keyTyped(char c, int i);
	void mouseClicked(int mouseX, int mouseY, int type, boolean isShiftKeyDown);
	void mouseMovedOrUp(int mouseX, int mouseY, int type);
	void handleMouseInput();
}
