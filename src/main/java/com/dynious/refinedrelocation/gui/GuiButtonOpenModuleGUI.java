package com.dynious.refinedrelocation.gui;

import com.dynious.refinedrelocation.RefinedRelocation;
import com.dynious.refinedrelocation.api.APIUtils;
import com.dynious.refinedrelocation.api.relocator.IItemRelocator;
import com.dynious.refinedrelocation.api.relocator.IRelocatorModule;
import com.dynious.refinedrelocation.grid.relocator.RelocatorMultiModule;
import com.dynious.refinedrelocation.lib.GuiIds;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;

public class GuiButtonOpenModuleGUI extends GuiScalableButton
{
    private IRelocatorModule module;
    private RelocatorMultiModule multiModule;
    private IItemRelocator relocator;
    private EntityPlayer player;
    private int side;
    private int moduleID;
    private String text;

    public GuiButtonOpenModuleGUI(IGuiParent parent, RelocatorMultiModule multiModule, IRelocatorModule module, IItemRelocator relocator, int side, EntityPlayer player, String buttonText)
    {
        super(parent, 0, 0, Minecraft.getMinecraft().fontRenderer.getStringWidth(buttonText) + 3*2, 20, 0, 0, buttonText);
        this.multiModule = multiModule;
        this.module = module;
        this.relocator = relocator;
        this.player = player;
        this.side = side;
        this.text = buttonText;
        update();
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int type, boolean isShiftKeyDown)
    {
        if (isMouseInsideBounds(mouseX, mouseY))
        {
            multiModule.setCurrentModule(module);
            if (module.getGUI(relocator, side, player) == null)
            {
                module.onActivated(relocator, player, side, player.getCurrentEquippedItem());
            }
            else
            {
                APIUtils.openRelocatorModuleGUI(relocator, player, side);
            }
        }
    }
}
