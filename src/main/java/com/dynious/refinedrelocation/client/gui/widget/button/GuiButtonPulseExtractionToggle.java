package com.dynious.refinedrelocation.client.gui.widget.button;

import com.dynious.refinedrelocation.client.graphics.TextureRegion;
import com.dynious.refinedrelocation.client.gui.IGuiParent;
import com.dynious.refinedrelocation.client.gui.SharedAtlas;
import com.dynious.refinedrelocation.grid.relocator.RelocatorModuleExtraction;
import com.dynious.refinedrelocation.helper.GuiHelper;
import com.dynious.refinedrelocation.lib.Strings;
import net.minecraft.util.StatCollector;

import java.util.List;

public class GuiButtonPulseExtractionToggle extends GuiButton {
    private final RelocatorModuleExtraction module;
    private final TextureRegion[] textureAlwaysActive = new TextureRegion[2];
    private final TextureRegion[] textureRedstoneLow = new TextureRegion[2];
    private final TextureRegion[] textureRedstoneHigh = new TextureRegion[2];
    private final TextureRegion[] textureRedstonePulse = new TextureRegion[2];

    public GuiButtonPulseExtractionToggle(IGuiParent parent, int x, int y, RelocatorModuleExtraction module) {
        super(parent, x, y, 24, 20, null, null);
        this.module = module;

        textureAlwaysActive[0] = SharedAtlas.findRegion("button_always_active");
        textureAlwaysActive[1] = SharedAtlas.findRegion("button_always_active_hover");
        textureRedstoneLow[0] = SharedAtlas.findRegion("button_redstone_low");
        textureRedstoneLow[1] = SharedAtlas.findRegion("button_redstone_low_hover");
        textureRedstoneHigh[0] = SharedAtlas.findRegion("button_redstone_high");
        textureRedstoneHigh[1] = SharedAtlas.findRegion("button_redstone_high_hover");
        textureRedstonePulse[0] = SharedAtlas.findRegion("button_redstone_pulse");
        textureRedstonePulse[1] = SharedAtlas.findRegion("button_redstone_pulse_hover");
        setButtonTextures(textureAlwaysActive);

        update();
        setAdventureModeRestriction(true);
    }

    @Override
    public void update() {
        if (module != null)
            setNewState(module.redstoneControlState, false);
        super.update();
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int type, boolean isShiftKeyDown) {
        if (isInsideBounds(mouseX, mouseY)) {
            if (type == 0)
                setNewState(getNextControlState(), true);
            else if (type == 1)
                setNewState(getPreviousControlState(), true);
        }

        super.mouseClicked(mouseX, mouseY, type, isShiftKeyDown);
    }

    private int getNextControlState() {
        return module.redstoneControlState == 3 ? 0 : module.redstoneControlState + 1;
    }

    private int getPreviousControlState() {
        return module.redstoneControlState - 1 >= 0 ? module.redstoneControlState - 1 : 3;
    }

    protected void setNewState(int newState, boolean sendUpdate) {
        if (module == null)
            return;

        module.redstoneControlState = newState;
        if (sendUpdate) {
            GuiHelper.sendIntMessage(1, newState);
        }

        switch (module.redstoneControlState) {
            case 0:
                setButtonTextures(textureAlwaysActive);
                break;
            case 1:
                setButtonTextures(textureRedstoneLow);
                break;
            case 2:
                setButtonTextures(textureRedstoneHigh);
                break;
            case 3:
                setButtonTextures(textureRedstonePulse);
                break;
        }
    }

    @Override
    public List<String> getTooltip(int mouseX, int mouseY) {
        List tooltip = super.getTooltip(mouseX, mouseY);
        if (isInsideBounds(mouseX, mouseY)) {
            tooltip.add(StatCollector.translateToLocal(Strings.MODULE_REDSTONE_CONTROL + module.redstoneControlState));
        }
        return tooltip;
    }
}
