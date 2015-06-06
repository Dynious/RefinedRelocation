package com.dynious.refinedrelocation.event;

import codechicken.lib.raytracer.RayTracer;
import com.dynious.refinedrelocation.api.APIUtils;
import com.dynious.refinedrelocation.api.tileentity.IFilterTileGUI;
import com.dynious.refinedrelocation.api.tileentity.ISortingMember;
import com.dynious.refinedrelocation.api.tileentity.handlers.IGridMemberHandler;
import com.dynious.refinedrelocation.block.ModBlocks;
import com.dynious.refinedrelocation.client.gui.GuiEditFilterButton;
import com.dynious.refinedrelocation.client.gui.GuiFiltered;
import com.dynious.refinedrelocation.client.renderer.RendererRelocator;
import com.dynious.refinedrelocation.container.IContainerFiltered;
import com.dynious.refinedrelocation.grid.relocator.RelocatorModuleRegistry;
import com.dynious.refinedrelocation.item.ModItems;
import com.dynious.refinedrelocation.lib.Strings;
import com.dynious.refinedrelocation.network.NetworkHandler;
import com.dynious.refinedrelocation.network.packet.MessageOpenFilterGUI;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiContainerCreative;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.resources.I18n;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.client.event.*;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

public class EventHandlerClient
{
    private World interactionWorld;
    private int interactionX;
    private int interactionY;
    private int interactionZ;
    private GuiButton editFilterButton;

    @SubscribeEvent
    public void FOVEvent(FOVUpdateEvent event)
    {
        EntityPlayer player = Minecraft.getMinecraft().thePlayer;
        if (player != null)
        {
            ItemStack itemInUse = player.getItemInUse();
            if (itemInUse != null && itemInUse.getItem() == ModItems.playerRelocator)
            {
                ModItems.playerRelocator.shiftFOV(itemInUse, event);
            }
        }
    }

    @SubscribeEvent
    public void overlayEvent(RenderGameOverlayEvent event)
    {
        if (event.type == RenderGameOverlayEvent.ElementType.HELMET)
        {
            ItemStack itemInUse = Minecraft.getMinecraft().thePlayer.getItemInUse();
            if (itemInUse != null && itemInUse.getItem() == ModItems.playerRelocator)
            {
                ModItems.playerRelocator.renderBlur(itemInUse, event.resolution);
            }
        }
    }

    @SubscribeEvent
    public void onTextureStitch(TextureStitchEvent.Pre event)
    {
        if (event.map.getTextureType() == 0)
        {
            RendererRelocator.loadIcons(event.map);
            RelocatorModuleRegistry.registerIcons(event.map);
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void tooltipEvent(ItemTooltipEvent event)
    {
        if (Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) || Keyboard.isKeyDown(Keyboard.KEY_RCONTROL) || (event.entityPlayer != null && event.entityPlayer.openContainer instanceof IContainerFiltered))
        {
            CreativeTabs tab = event.itemStack.getItem().getCreativeTab();
            if (tab != null)
                event.toolTip.add(StatCollector.translateToLocal(Strings.TAB) + ": " + I18n.format(tab.getTranslatedTabLabel()));
        }
    }

    @SubscribeEvent
    public void onBlockHighlight(DrawBlockHighlightEvent event)
    {
        if (ModBlocks.relocator != null && event.target.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK && event.player.worldObj.getBlock(event.target.blockX, event.target.blockY, event.target.blockZ) == ModBlocks.relocator)
        {
            RayTracer.retraceBlock(event.player.worldObj, event.player, event.target.blockX, event.target.blockY, event.target.blockZ);
        }
        if (event.target != null && event.target.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK)
        {
            TileEntity tile = event.player.worldObj.getTileEntity(event.target.blockX, event.target.blockY, event.target.blockZ);

            if (tile instanceof ISortingMember && ((ISortingMember) tile).getHandler().getGrid() != null)
            {
                GL11.glEnable(GL11.GL_BLEND);
                OpenGlHelper.glBlendFunc(770, 771, 1, 0);
                GL11.glColor4f(1.0F, 1.0F, 0.0F, 0.4F);
                GL11.glLineWidth(2.0F);
                GL11.glDisable(GL11.GL_TEXTURE_2D);
                GL11.glDepthMask(false);
                float expansion = 0.002F;
                double offsetX = event.player.lastTickPosX + (event.player.posX - event.player.lastTickPosX) * event.partialTicks;
                double offsetY = event.player.lastTickPosY + (event.player.posY - event.player.lastTickPosY) * event.partialTicks;
                double offsetZ = event.player.lastTickPosZ + (event.player.posZ - event.player.lastTickPosZ) * event.partialTicks;

                if (event.player.isSneaking())
                {
                    for (IGridMemberHandler member : ((ISortingMember) tile).getHandler().getGrid().getMembers())
                    {
                        renderOverlay(member.getOwner(), offsetX, offsetY, offsetZ, expansion);
                    }
                }
                else
                {
                    renderOverlay(tile, offsetX, offsetY, offsetZ, expansion);
                }

                GL11.glDepthMask(true);
                GL11.glEnable(GL11.GL_TEXTURE_2D);
                GL11.glDisable(GL11.GL_BLEND);

                event.setCanceled(true);
            }
        }
    }

    private void renderOverlay(TileEntity tile, double offsetX, double offsetY, double offsetZ, float expansion)
    {
        Block block = tile.getWorldObj().getBlock(tile.xCoord, tile.yCoord, tile.zCoord);
        block.setBlockBoundsBasedOnState(tile.getWorldObj(), tile.xCoord, tile.yCoord, tile.zCoord);
        RenderGlobal.drawOutlinedBoundingBox(block.getSelectedBoundingBoxFromPool(tile.getWorldObj(), tile.xCoord, tile.yCoord, tile.zCoord).expand(expansion, expansion, expansion).getOffsetBoundingBox(-offsetX, -offsetY, -offsetZ), -1);
    }

    @SubscribeEvent
    public void interactWith(PlayerInteractEvent event) {
        // Sadly the only solution I could think of for checking if a GuiContainer belongs to an IFilterTileGUI
        if(event.action == PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK) {
            interactionWorld = event.world;
            interactionX = event.x;
            interactionY = event.y;
            interactionZ = event.z;
            return;
        }
        interactionWorld = null;
    }

    @SubscribeEvent
    public void initGui(GuiScreenEvent.InitGuiEvent event) {
        if(interactionWorld != null) {
            TileEntity tileEntity = interactionWorld.getTileEntity(interactionX, interactionY, interactionZ);
            if(tileEntity instanceof IFilterTileGUI && event.gui instanceof GuiContainer) {
                // Make sure it's not already a filter GUI or an inventory GUI
                if (!(event.gui instanceof GuiFiltered) && !(event.gui instanceof GuiInventory) && !(event.gui instanceof GuiContainerCreative)) {
                    GuiContainer guiContainer = (GuiContainer) event.gui;
                    if (interactionWorld != null) {
                        editFilterButton = new GuiEditFilterButton(guiContainer.guiLeft - 31, guiContainer.guiTop + 25);
                        event.buttonList.add(editFilterButton);
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void actionPerformed(GuiScreenEvent.ActionPerformedEvent.Pre event) {
        if(event.button == editFilterButton) {
            NetworkHandler.INSTANCE.sendToServer(new MessageOpenFilterGUI(interactionX, interactionY, interactionZ));
            event.setCanceled(true);
        }
    }

}
