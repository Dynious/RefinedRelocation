package com.dynious.refinedrelocation.client.gui;

import com.dynious.refinedrelocation.container.ContainerFilteringHopper;
import com.dynious.refinedrelocation.helper.GuiHelper;
import com.dynious.refinedrelocation.network.NetworkHandler;
import com.dynious.refinedrelocation.network.packet.MessageOpenFilterGUI;
import com.dynious.refinedrelocation.tileentity.TileFilteringHopper;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class GuiFilteringHopper extends GuiContainer
{
    private static final ResourceLocation hopperGuiTextures = new ResourceLocation("textures/gui/container/hopper.png");

    private final TileFilteringHopper filteringHopper;
    private final IInventory playerInventory;
    private final GuiEditFilterButton editFilterButton;

    public GuiFilteringHopper(InventoryPlayer par1InventoryPlayer, TileFilteringHopper filteringHopper)
    {
        super(new ContainerFilteringHopper(par1InventoryPlayer, filteringHopper));
        this.playerInventory = par1InventoryPlayer;
        this.filteringHopper = filteringHopper;
        this.allowUserInput = false;
        this.ySize = 133;

        editFilterButton = new GuiEditFilterButton(guiLeft - GuiEditFilterButton.WIDTH, guiTop - GuiEditFilterButton.HEIGHT);
        buttonList.add(editFilterButton);
    }

    @Override
    protected void actionPerformed(GuiButton button)
    {
        super.actionPerformed(button);
        if (button == editFilterButton)
        {
            NetworkHandler.INSTANCE.sendToServer(new MessageOpenFilterGUI(filteringHopper.xCoord, filteringHopper.yCoord, filteringHopper.zCoord));
        }
    }

    /**
     * Draw the foreground layer for the GuiContainer (everything in front of the items)
     */
    @Override
    protected void drawGuiContainerForegroundLayer(int par1, int par2)
    {
        this.fontRendererObj.drawString(this.filteringHopper.hasCustomInventoryName() ? this.filteringHopper.getInventoryName() : I18n.format(this.filteringHopper.getInventoryName()), 8, 6, 4210752);
        this.fontRendererObj.drawString(this.playerInventory.hasCustomInventoryName() ? this.playerInventory.getInventoryName() : I18n.format(this.playerInventory.getInventoryName()), 8, this.ySize - 96 + 2, 4210752);
    }

    /**
     * Draw the background layer for the GuiContainer (everything behind the items)
     */
    @Override
    protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3)
    {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(hopperGuiTextures);
        int k = (this.width - this.xSize) / 2;
        int l = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(k, l, 0, 0, this.xSize, this.ySize);
    }
}
