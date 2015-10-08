package com.dynious.refinedrelocation.client.gui;

import com.dynious.refinedrelocation.client.graphics.TextureRegion;
import com.dynious.refinedrelocation.lib.Resources;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;

public class GuiEditFilterButton extends GuiButton {
    public static final int WIDTH = 31;
    public static final int HEIGHT = 26;

    private final TextureRegion textureTab;
    private final TextureRegion textureTabHover;
    private final TextureRegion textureIcon;

    public GuiEditFilterButton(int x, int y) {
        super(-1, x, y, WIDTH, HEIGHT, "");
        textureTab = SharedAtlas.findRegion("tab_inactive");
        textureTabHover = SharedAtlas.findRegion("tab_active");
        textureIcon = SharedAtlas.findRegion("icon_edit_filter");
    }

    @Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY) {
        boolean isInside = mouseX >= xPosition && mouseY >= yPosition && mouseX < xPosition + width && mouseY < yPosition + height;
        if(isInside) {
            textureTabHover.draw(xPosition, yPosition);
        } else {
            textureTab.draw(xPosition, yPosition);
        }
        textureIcon.draw(xPosition + width / 2 - textureIcon.getRegionWidth() / 2 + 2, yPosition + height / 2 - textureIcon.getRegionHeight() / 2);
    }
}
