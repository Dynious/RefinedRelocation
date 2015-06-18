package com.dynious.refinedrelocation.client.gui;

import com.dynious.refinedrelocation.client.gui.widget.button.GuiButtonMaxCraftStack;
import com.dynious.refinedrelocation.container.ContainerModuleCrafting;
import com.dynious.refinedrelocation.grid.relocator.RelocatorModuleCrafting;
import com.dynious.refinedrelocation.lib.Resources;
import com.dynious.refinedrelocation.lib.Settings;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import org.lwjgl.opengl.GL11;

import java.util.List;

public class GuiModuleCrafting extends GuiRefinedRelocationContainer
{
    private RelocatorModuleCrafting moduleCrafting;

    public GuiModuleCrafting(EntityPlayer player, RelocatorModuleCrafting tile)
    {
        super(new ContainerModuleCrafting(player, tile));
        moduleCrafting = tile;
        this.xSize = 176;
        this.ySize = 166;
    }

    @Override
    public void initGui()
    {
        super.initGui();

        new GuiButtonMaxCraftStack(this, (width - xSize) / 2 + 139, (height - ySize) / 2 + 33, moduleCrafting);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int i, int j)
    {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(Resources.GUI_MODULE_CRAFTING);
        int xPos = (width - xSize) / 2;
        int yPos = (height - ySize) / 2;
        drawTexturedModalRect(xPos, yPos, 0, 0, xSize, ySize);
        if (moduleCrafting.isStuffed)
        {
            drawTexturedModalRect(xPos + 101, yPos + 30, 176, 0, 26, 26);
        }
        if (moduleCrafting.craftTick > 0 && Settings.CRAFTING_MODULE_TICKS_BETWEEN_CRAFTING != 0)
        {
            drawTexturedModalRect(xPos + 70, yPos + 35, 176, 26, (int) ((((float) moduleCrafting.craftTick) / Settings.CRAFTING_MODULE_TICKS_BETWEEN_CRAFTING) * 24), 17);
        }
        super.drawGuiContainerBackgroundLayer(f, i, j);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void drawGuiContainerForegroundLayer(int p_146979_1_, int p_146979_2_)
    {
        super.drawGuiContainerForegroundLayer(p_146979_1_, p_146979_2_);
        for (Slot slot : (List<Slot>) inventorySlots.inventorySlots)
        {
            if (slot != null && slot.getHasStack() && slot.getStack().stackSize == 0)
            {
                GL11.glDisable(GL11.GL_LIGHTING);
                GL11.glDisable(GL11.GL_DEPTH_TEST);
                GL11.glColorMask(true, true, true, false);
                drawRect(slot.xDisplayPosition, slot.yDisplayPosition, slot.xDisplayPosition + 16, slot.yDisplayPosition + 16, 0x7F8B8B8B);
                GL11.glColorMask(true, true, true, true);
                GL11.glEnable(GL11.GL_LIGHTING);
                GL11.glEnable(GL11.GL_DEPTH_TEST);
            }
        }
    }
}
