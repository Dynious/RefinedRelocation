package com.dynious.refinedrelocation.event;

import com.dynious.refinedrelocation.repack.codechicken.lib.raytracer.RayTracer;
import com.dynious.refinedrelocation.api.tileentity.ISortingInventory;
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
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.resources.I18n;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.StatCollector;
import net.minecraftforge.client.event.*;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import java.util.List;

public class EventHandlerClient
{
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
                } else
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
    public void initGui(GuiScreenEvent.InitGuiEvent.Post event)
    {
        if (event.gui instanceof GuiContainer && !(event.gui instanceof GuiFiltered))
        {
            GuiContainer container = (GuiContainer) event.gui;
            for (Slot slot : (List<Slot>) container.inventorySlots.inventorySlots)
            {
                if (slot.inventory instanceof ISortingInventory)
                {
                    editFilterButton = new GuiEditFilterButton(container.guiLeft - GuiEditFilterButton.WIDTH, container.guiTop + GuiEditFilterButton.HEIGHT);
                    event.buttonList.add(editFilterButton);
                    return;
                }
            }
        }
    }

    @SubscribeEvent
    public void actionPerformed(GuiScreenEvent.ActionPerformedEvent.Pre event)
    {
        if (event.button == editFilterButton)
        {
            TileEntity tile = null;
            if (event.gui instanceof GuiContainer && !(event.gui instanceof GuiFiltered))
            {
                GuiContainer container = (GuiContainer) event.gui;
                for (Slot slot : (List<Slot>) container.inventorySlots.inventorySlots)
                {
                    if (slot.inventory instanceof ISortingInventory && ((ISortingInventory) slot.inventory).getHandler() != null)
                    {
                        tile = ((ISortingInventory) slot.inventory).getHandler().getOwner();
                        break;
                    }
                }
            }
            if (tile != null)
            {
                NetworkHandler.INSTANCE.sendToServer(new MessageOpenFilterGUI(tile.xCoord, tile.yCoord, tile.zCoord));
                event.setCanceled(true);
            }
        }
    }

}
