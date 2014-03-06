package com.dynious.refinedrelocation.gui;

import com.dynious.refinedrelocation.gui.container.ContainerFilteringHopper;
import com.dynious.refinedrelocation.tileentity.TileFilteringHopper;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class GuiFilteringHopper extends GuiContainer
{
    private static final ResourceLocation hopperGuiTextures = new ResourceLocation("textures/gui/container/hopper.png");
    private IInventory playerInventory;
    private IInventory hopperInventory;

    public GuiFilteringHopper(InventoryPlayer par1InventoryPlayer, TileFilteringHopper filteringHopper)
    {
        super(new ContainerFilteringHopper(par1InventoryPlayer, filteringHopper));
        this.playerInventory = par1InventoryPlayer;
        this.hopperInventory = filteringHopper;
        this.allowUserInput = false;
        this.ySize = 133;
    }

    /**
     * Draw the foreground layer for the GuiContainer (everything in front of the items)
     */
    protected void drawGuiContainerForegroundLayer(int par1, int par2)
    {
        this.fontRenderer.drawString(this.hopperInventory.isInvNameLocalized() ? this.hopperInventory.getInvName() : I18n.getString(this.hopperInventory.getInvName()), 8, 6, 4210752);
        this.fontRenderer.drawString(this.playerInventory.isInvNameLocalized() ? this.playerInventory.getInvName() : I18n.getString(this.playerInventory.getInvName()), 8, this.ySize - 96 + 2, 4210752);
    }

    /**
     * Draw the background layer for the GuiContainer (everything behind the items)
     */
    protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3)
    {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(hopperGuiTextures);
        int k = (this.width - this.xSize) / 2;
        int l = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(k, l, 0, 0, this.xSize, this.ySize);
    }
}
