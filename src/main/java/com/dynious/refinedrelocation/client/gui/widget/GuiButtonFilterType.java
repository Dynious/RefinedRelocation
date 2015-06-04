package com.dynious.refinedrelocation.client.gui.widget;

import com.dynious.refinedrelocation.client.gui.IGuiParent;
import com.dynious.refinedrelocation.network.NetworkHandler;
import com.dynious.refinedrelocation.network.packet.filter.MessageSetFilterType;
import net.minecraft.util.ResourceLocation;

public class GuiButtonFilterType extends GuiButton {

    private final String typeName;

    public GuiButtonFilterType(IGuiParent parent, int x, int y, String typeName, ResourceLocation texture, int texCoordX, int texCoordY) {
        super(parent, x, y, 24, 20, texCoordX, texCoordY, "");
        this.typeName = typeName;
        setTextureSheet(texture);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int type, boolean isShiftKeyDown) {
        if(isInsideBounds(mouseX, mouseY)) {
            NetworkHandler.INSTANCE.sendToServer(new MessageSetFilterType(-1, typeName));
        }
        super.mouseClicked(mouseX, mouseY, type, isShiftKeyDown);
    }
}
