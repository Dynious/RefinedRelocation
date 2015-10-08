package com.dynious.refinedrelocation.client.gui.widget.button;

import com.dynious.refinedrelocation.client.graphics.TextureRegion;
import com.dynious.refinedrelocation.client.gui.GuiRefinedRelocationContainer;
import com.dynious.refinedrelocation.client.gui.IGuiParent;
import com.dynious.refinedrelocation.client.gui.SharedAtlas;
import com.dynious.refinedrelocation.client.gui.widget.GuiLabel;

public class GuiButtonToggle extends GuiButton {

    private final TextureRegion[] textureActive = new TextureRegion[2];
    private final TextureRegion[] textureInactive = new TextureRegion[2];
    protected boolean state;
    protected GuiLabel labelTrue;
    protected GuiLabel labelFalse;

    public GuiButtonToggle(IGuiParent parent, int x, int y, int w, int h, String textureNameInactive, String textureNameActive, String labelTrueText, String labelFalseText) {
        super(parent, x, y, w, h, null, null);
        labelFalse = new GuiLabel(this, x + w / 2, y + h / 2, labelFalseText, 0xffffff, true);
        labelTrue = new GuiLabel(this, x + w / 2, y + h / 2, labelTrueText, 0xffffff, true);

        textureInactive[0] = SharedAtlas.findRegion(textureNameInactive);
        textureInactive[1] = SharedAtlas.findRegion(textureNameInactive + "_hover");
        textureActive[0] = SharedAtlas.findRegion(textureNameActive);
        textureActive[1] = SharedAtlas.findRegion(textureNameActive + "_hover");
        setButtonTextures(textureInactive);

        this.setState(false);
    }

    public boolean getState() {
        return this.state;
    }

    public void setState(boolean state) {
        this.state = state;

        if (state) {
            labelTrue.setVisible(true);
            labelFalse.setVisible(false);
            setButtonTextures(textureActive);
        } else {
            labelFalse.setVisible(true);
            labelTrue.setVisible(false);
            setButtonTextures(textureInactive);
        }
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int type, boolean isShiftKeyDown) {
        if (isInsideBounds(mouseX, mouseY) && (type == 0 || type == 1)) {
            if (!isAdventureModeRestriction() || !GuiRefinedRelocationContainer.isRestrictedAccessWithError()) {
                setState(!getState());
                onStateChangedByUser(getState());
            }
            return;
        }
        super.mouseClicked(mouseX, mouseY, type, isShiftKeyDown);
    }

    protected void onStateChangedByUser(boolean newState) {
    }

}
