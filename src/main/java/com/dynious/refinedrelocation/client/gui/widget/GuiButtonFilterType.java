package com.dynious.refinedrelocation.client.gui.widget;

import com.dynious.refinedrelocation.client.gui.IGuiParent;
import com.dynious.refinedrelocation.network.NetworkHandler;
import com.dynious.refinedrelocation.network.packet.filter.MessageSetFilterType;
import net.minecraft.util.ResourceLocation;

public class GuiButtonFilterType extends GuiButton {

    private static final int ICON_WIDTH = 18;
    private static final int ICON_HEIGHT = 18;

    private final ResourceLocation iconTexture;
    private final int iconTexCoordX;
    private final int iconTexCoordY;
    private final String typeName;

    public GuiButtonFilterType(IGuiParent parent, int x, int y, String typeName, ResourceLocation texture, int texCoordX, int texCoordY) {
        super(parent, x, y, 24, 20, 0, 0, "");
        this.iconTexture = texture;
        this.iconTexCoordX = texCoordX;
        this.iconTexCoordY = texCoordY;
        this.typeName = typeName;
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int type, boolean isShiftKeyDown) {
        if(isInsideBounds(mouseX, mouseY)) {
            NetworkHandler.INSTANCE.sendToServer(new MessageSetFilterType(-1, typeName));
        }
        super.mouseClicked(mouseX, mouseY, type, isShiftKeyDown);
    }

    @Override
    public void drawBackground(int mouseX, int mouseY) {
        super.drawBackground(mouseX, mouseY);

        mc.getTextureManager().bindTexture(iconTexture);
        drawTexturedModalRect(x + w / 2 - ICON_WIDTH / 2, y + h / 2 - ICON_HEIGHT / 2, iconTexCoordX, iconTexCoordY, ICON_WIDTH, ICON_HEIGHT);
    }
}
