package com.dynious.refinedrelocation.client.gui;

import com.dynious.refinedrelocation.container.ContainerModuleCrafting;
import com.dynious.refinedrelocation.container.ContainerModuleStock;
import com.dynious.refinedrelocation.grid.relocator.RelocatorModuleCrafting;
import com.dynious.refinedrelocation.grid.relocator.RelocatorModuleStock;
import com.dynious.refinedrelocation.lib.Resources;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import org.lwjgl.opengl.GL11;

import java.util.List;

public class GuiModuleCrafting extends GuiContainer
{
    public GuiModuleCrafting(EntityPlayer player, RelocatorModuleCrafting tile)
    {
        super(new ContainerModuleCrafting(player, tile));
        this.xSize = 176;
        this.ySize = 166;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int i, int j)
    {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(Resources.GUI_MODULE_STOCK);
        int xPos = (width - xSize) / 2;
        int yPos = (height - ySize) / 2;
        drawTexturedModalRect(xPos, yPos, 0, 0, xSize, ySize);
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
                this.drawGradientRect(slot.xDisplayPosition, slot.yDisplayPosition, slot.xDisplayPosition + 16, slot.yDisplayPosition + 16, -2130706433, -2130706433);
                GL11.glColorMask(true, true, true, true);
                GL11.glEnable(GL11.GL_LIGHTING);
                GL11.glEnable(GL11.GL_DEPTH_TEST);
            }
        }
    }
}
