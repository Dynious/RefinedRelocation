package com.dynious.refinedrelocation.client.gui.widget.button;

import com.dynious.refinedrelocation.api.relocator.IRelocatorModule;
import com.dynious.refinedrelocation.client.gui.IGuiParent;
import com.dynious.refinedrelocation.client.gui.widget.GuiLabel;
import com.dynious.refinedrelocation.client.gui.widget.GuiWidgetBase;
import com.dynious.refinedrelocation.grid.relocator.RelocatorMultiModule;
import com.dynious.refinedrelocation.helper.GuiHelper;
import com.dynious.refinedrelocation.lib.Resources;
import com.dynious.refinedrelocation.network.packet.gui.MessageGUI;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import org.lwjgl.opengl.GL11;

import java.util.List;

public class GuiButtonOpenModuleGUI extends GuiWidgetBase {

    private final GuiLabel descLabel;
    private int index;
    private RelocatorMultiModule multiModule;
    private IRelocatorModule module;
    private IIcon moduleIcon;

    public GuiButtonOpenModuleGUI(IGuiParent parent, int x, int y, RelocatorMultiModule multiModule) {
        super(parent, x, y, 151, 25);

        this.multiModule = multiModule;

        descLabel = new GuiLabel(this, x + 33, y + h / 2 - mc.fontRenderer.FONT_HEIGHT / 2, "");
        descLabel.drawCentered = false;
    }

    public void setModule(int index, IRelocatorModule module) {
        this.index = index;
        this.module = module;
        if (module != null) {
            descLabel.setText(module.getDisplayName());
            moduleIcon = module.getModuleItemStack().getIconIndex();
        }
    }

    @Override
    public void getTooltip(List<String> tooltip, int mouseX, int mouseY) {
        super.getTooltip(tooltip, mouseX, mouseY);
        if (module != null && isInsideBounds(mouseX, mouseY)) {
            tooltip.add("\u00a7a" + StatCollector.translateToLocal(module.getDisplayName()));
        }
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int type, boolean isShiftKeyDown) {
        if (module != null && isInsideBounds(mouseX, mouseY)) {
            final int slotX = x + 5;
            final int slotY = y + 4;
            if(mouseX >= slotX && mouseX < slotX + 18 && mouseY >= slotY && mouseY < slotY + 18) {
                multiModule.setCurrentModule(-1);
                GuiHelper.sendByteMessage(MessageGUI.REMOVE_MODULE, (byte) index);
            } else {
                multiModule.setCurrentModule(index);
                GuiHelper.sendByteMessage(MessageGUI.OPEN_MODULE, (byte) index);
            }
        }
        super.mouseClicked(mouseX, mouseY, type, isShiftKeyDown);
    }

    @Override
    public void drawBackground(int mouseX, int mouseY) {
        GL11.glColor4f(1f, 1f, 1f, 1f);
        mc.getTextureManager().bindTexture(Resources.GUI_MODULAR_FILTER);
        drawTexturedModalRect(x, y, 0, 198, 151, 27);

        if (module == null || !isInsideBounds(mouseX, mouseY)) {
            Gui.drawRect(x, y + 1, x + w, y + 1 + h, 0x44ffffff);
        }

        super.drawBackground(mouseX, mouseY);

        final int slotX = x + 5;
        final int slotY = y + 4;
        drawTexturedModalRect(slotX, slotY, 0, 225, 18, 18);
        if (module != null) {
            mc.getTextureManager().bindTexture(TextureMap.locationItemsTexture);
            drawTexturedModelRectFromIcon(slotX + 1, slotY + 1, moduleIcon, moduleIcon.getIconWidth(), moduleIcon.getIconHeight());
            if(mouseX >= slotX && mouseX < slotX + 18 && mouseY >= slotY && mouseY < slotY + 18) {
                drawRect(slotX + 1, slotY + 1, slotX + 17, slotY + 17, 0x44ff0000);
            }
        }
    }

}
