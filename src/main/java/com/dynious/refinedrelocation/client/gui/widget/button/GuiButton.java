package com.dynious.refinedrelocation.client.gui.widget.button;

import com.dynious.refinedrelocation.client.graphics.TextureRegion;
import com.dynious.refinedrelocation.client.gui.IGuiParent;
import com.dynious.refinedrelocation.client.gui.IGuiWidgetBase;
import com.dynious.refinedrelocation.client.gui.SharedAtlas;
import com.dynious.refinedrelocation.client.gui.widget.GuiLabel;
import com.dynious.refinedrelocation.client.gui.widget.GuiWidgetBase;
import com.dynious.refinedrelocation.lib.Resources;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class GuiButton extends GuiWidgetBase {

    private TextureRegion[] texture = new TextureRegion[2];
    protected GuiLabel label;
    private boolean adventureModeRestriction;

    public GuiButton(IGuiParent parent, int x, int y, String labelText) {
        super(parent);
        this.x = x;
        this.y = y;
        w = 24;
        h = 20;
        texture[0] = SharedAtlas.findRegion("button");
        texture[1] = SharedAtlas.findRegion("button_hover");
        this.label = new GuiLabel(this, x + w / 2, y + h / 2, labelText, 0xffffff, true);
    }

    public GuiButton(IGuiParent parent, int x, int y, int w, int h, String textureName, String labelText) {
        super(parent, x, y, w, h);
        if(textureName != null) {
            texture[0] = SharedAtlas.findRegion(textureName);
            texture[1] = SharedAtlas.findRegion(textureName + "_hover");
        }
        this.label = new GuiLabel(this, this.x + this.w / 2, this.y + this.h / 2, labelText, 0xffffff, true);
    }

    @Override
    public void drawBackground(int mouseX, int mouseY) {
        boolean isHovering = isInsideBounds(mouseX, mouseY);
        GL11.glColor4f(1f, 1f, 1f, 1f);
        texture[isHovering ? 1 : 0].draw(x, y);

        for (IGuiWidgetBase child : this.children) {
            if (child instanceof GuiLabel) {
                GuiLabel childLabel = (GuiLabel) child;
                childLabel.setColor(isInsideBounds(mouseX, mouseY) ? 0xffffa0 : 0xffffff);
            }
        }

        super.drawBackground(mouseX, mouseY);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int type, boolean isShiftKeyDown) {
        super.mouseClicked(mouseX, mouseY, type, isShiftKeyDown);
        if (isInsideBounds(mouseX, mouseY)) {
            this.mc.getSoundHandler().playSound(PositionedSoundRecord.func_147674_a(new ResourceLocation("gui.button.press"), 1.0F));
        }
    }

    public final boolean isAdventureModeRestriction() {
        return adventureModeRestriction;
    }

    public final void setAdventureModeRestriction(boolean adventureModeRestriction) {
        this.adventureModeRestriction = adventureModeRestriction;
    }

    public void setButtonTextures(TextureRegion[] textures) {
        if(textures.length != 2) {
            throw new RuntimeException("Expected an array of size two for the button textures.");
        }
        this.texture = textures;
    }
}
