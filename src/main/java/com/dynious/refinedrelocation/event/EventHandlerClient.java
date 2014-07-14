package com.dynious.refinedrelocation.event;

import codechicken.lib.raytracer.RayTracer;
import com.dynious.refinedrelocation.block.ModBlocks;
import com.dynious.refinedrelocation.grid.relocator.RelocatorModuleRegistry;
import com.dynious.refinedrelocation.container.IContainerFiltered;
import com.dynious.refinedrelocation.item.ModItems;
import com.dynious.refinedrelocation.lib.Strings;
import com.dynious.refinedrelocation.client.renderer.RendererRelocator;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.StatCollector;
import net.minecraftforge.client.event.DrawBlockHighlightEvent;
import net.minecraftforge.client.event.FOVUpdateEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import org.lwjgl.input.Keyboard;

public class EventHandlerClient
{
    @SubscribeEvent
    public void FOVEvent(FOVUpdateEvent event)
    {
        ItemStack itemInUse = Minecraft.getMinecraft().thePlayer.getItemInUse();
        if (itemInUse != null && itemInUse.getItem() == ModItems.playerRelocator)
        {
            ModItems.playerRelocator.shiftFOV(itemInUse, event);
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
        if(ModBlocks.relocator != null && event.target.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK && event.player.worldObj.getBlock(event.target.blockX, event.target.blockY, event.target.blockZ) == ModBlocks.relocator)
        {
            RayTracer.retraceBlock(event.player.worldObj, event.player, event.target.blockX, event.target.blockY, event.target.blockZ);
        }
    }
}
