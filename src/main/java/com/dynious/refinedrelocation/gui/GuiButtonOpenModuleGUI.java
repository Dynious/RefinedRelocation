package com.dynious.refinedrelocation.gui;

import com.dynious.refinedrelocation.api.relocator.IItemRelocator;
import com.dynious.refinedrelocation.api.relocator.IRelocatorModule;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;

public class GuiButtonOpenModuleGUI extends GuiScalableButton
{
    private IRelocatorModule module;
    private IItemRelocator relocator;
    private EntityPlayer player;
    private int side;
    private int moduleID;
    private String text;

    public GuiButtonOpenModuleGUI(IGuiParent parent, IRelocatorModule module, IItemRelocator relocator, int side, EntityPlayer player, String buttonText)
    {
        super(parent, 0, 0, Minecraft.getMinecraft().fontRenderer.getStringWidth(buttonText) + 3*2, 20, 0, 0, buttonText);
        this.module = module;
        this.relocator = relocator;
        this.player = player;
        this.side = side;
//        this.moduleID = moduleID;
        this.text = buttonText;
        update();
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int type, boolean isShiftKeyDown)
    {
//        module.onActivated(relocator, player, side, player.getCurrentEquippedItem());
//        APIUtils.openRelocatorModuleGUI(relocator, player, moduleID);
//        player.openGui(RefinedRelocation.instance, GuiIds.RELOCATOR_FILTER_BASE + side);
        //super.mouseClicked(mouseX, mouseY, type, isShiftKeyDown);
    }
}
