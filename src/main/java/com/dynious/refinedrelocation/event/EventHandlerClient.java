package com.dynious.refinedrelocation.event;

import codechicken.lib.raytracer.RayTracer;
import com.dynious.refinedrelocation.block.ModBlocks;
import com.dynious.refinedrelocation.grid.relocator.RelocatorModuleRegistry;
import com.dynious.refinedrelocation.gui.container.IContainerFiltered;
import com.dynious.refinedrelocation.helper.LogHelper;
import com.dynious.refinedrelocation.item.ModItems;
import com.dynious.refinedrelocation.lib.Sounds;
import com.dynious.refinedrelocation.lib.Strings;
import com.dynious.refinedrelocation.renderer.RendererRelocator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.util.EnumMovingObjectType;
import net.minecraft.util.StatCollector;
import net.minecraftforge.client.event.*;
import net.minecraftforge.client.event.sound.SoundLoadEvent;
import net.minecraftforge.event.EventPriority;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraft.item.ItemStack;
import org.lwjgl.input.Keyboard;

public class EventHandlerClient
{
    @ForgeSubscribe
    public void FOVEvent(FOVUpdateEvent event)
    {
        ItemStack itemInUse = Minecraft.getMinecraft().thePlayer.getItemInUse();
        if (itemInUse != null && itemInUse.getItem().itemID == ModItems.playerRelocator.itemID)
        {
            ModItems.playerRelocator.shiftFOV(itemInUse, event);
        }
    }

    @ForgeSubscribe
    public void onSoundLoad(SoundLoadEvent event)
    {
        for (String soundFile : Sounds.sounds)
        {
            try
            {
                event.manager.addSound(soundFile);
            }
            catch (Exception e)
            {
                LogHelper.warning("Failed loading sound file: " + soundFile);
            }
        }
    }

    @ForgeSubscribe
    public void overlayEvent(RenderGameOverlayEvent event)
    {
        if (event.type == RenderGameOverlayEvent.ElementType.HELMET)
        {
            ItemStack itemInUse = Minecraft.getMinecraft().thePlayer.getItemInUse();
            if (itemInUse != null && itemInUse.getItem().itemID == ModItems.playerRelocator.itemID)
            {
                ModItems.playerRelocator.renderBlur(itemInUse, event.resolution);
            }
        }
    }

    @ForgeSubscribe
    public void onTextureStitch(TextureStitchEvent.Pre event)
    {
        if (event.map.getTextureType() == 0)
        {
            RendererRelocator.loadIcons(event.map);
            RelocatorModuleRegistry.registerIcons(event.map);
        }
    }

    @ForgeSubscribe(priority = EventPriority.LOWEST)
    public void tooltipEvent(ItemTooltipEvent event)
    {
        if (Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) || Keyboard.isKeyDown(Keyboard.KEY_RCONTROL) || (event.entityPlayer != null && event.entityPlayer.openContainer instanceof IContainerFiltered))
        {
            CreativeTabs tab = event.itemStack.getItem().getCreativeTab();
            if (tab != null)
                event.toolTip.add(StatCollector.translateToLocal(Strings.TAB) + ": " + I18n.getString(tab.getTranslatedTabLabel()));
        }
    }

    @ForgeSubscribe
    public void onBlockHighlight(DrawBlockHighlightEvent event)
    {
        if(ModBlocks.relocator != null && event.target.typeOfHit == EnumMovingObjectType.TILE && event.player.worldObj.getBlockId(event.target.blockX, event.target.blockY, event.target.blockZ) == ModBlocks.relocator.blockID)
        {
            RayTracer.retraceBlock(event.player.worldObj, event.player, event.target.blockX, event.target.blockY, event.target.blockZ);
        }
    }
}
