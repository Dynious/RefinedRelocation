package com.dynious.refinedrelocation.client;

import com.dynious.refinedrelocation.CommonProxy;
import com.dynious.refinedrelocation.block.ModBlocks;
import com.dynious.refinedrelocation.client.gui.SharedAtlas;
import com.dynious.refinedrelocation.client.renderer.*;
import com.dynious.refinedrelocation.event.EventHandlerClient;
import com.dynious.refinedrelocation.lib.Mods;
import com.dynious.refinedrelocation.compat.EE3Helper;
import com.dynious.refinedrelocation.compat.FMPHelper;
import com.dynious.refinedrelocation.compat.IronChestHelper;
import com.dynious.refinedrelocation.multiblock.TileMultiBlockBase;
import com.dynious.refinedrelocation.tileentity.*;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.common.MinecraftForge;

public class ClientProxy extends CommonProxy
{
    @Override
    public void initTileEntities()
    {
        super.initTileEntities();

        DirectionalRenderer.renderId = RenderingRegistry.getNextAvailableRenderId();
        RenderingRegistry.registerBlockHandler(DirectionalRenderer.renderId, new DirectionalRenderer());

        ClientRegistry.bindTileEntitySpecialRenderer(TileBlockExtender.class, new RendererBlockExtender());
        ClientRegistry.bindTileEntitySpecialRenderer(TileWirelessBlockExtender.class, new RendererWirelessBlockExtender());
        ClientRegistry.bindTileEntitySpecialRenderer(TileBuffer.class, new RendererBuffer());
        ClientRegistry.bindTileEntitySpecialRenderer(TileSortingChest.class, new RendererSortingChest());
        ClientRegistry.bindTileEntitySpecialRenderer(TileRelocationPortal.class, new RendererRelocationPortal());
        ClientRegistry.bindTileEntitySpecialRenderer(TileMultiBlockBase.class, new RendererMultiBlock());
        ClientRegistry.bindTileEntitySpecialRenderer(TileRelocator.class, new RendererRelocator());

        MinecraftForgeClient.registerItemRenderer(ItemBlock.getItemFromBlock(ModBlocks.blockExtender), new ItemRendererBlockExtender());
        MinecraftForgeClient.registerItemRenderer(ItemBlock.getItemFromBlock(ModBlocks.buffer), new ItemRendererBuffer());
        MinecraftForgeClient.registerItemRenderer(ItemBlock.getItemFromBlock(ModBlocks.sortingChest), new ItemRendererSortingChest());
        MinecraftForgeClient.registerItemRenderer(ModBlocks.relocator != null ? ItemBlock.getItemFromBlock(ModBlocks.relocator) : FMPHelper.partRelocator, new ItemRendererRelocator());

        if (Mods.IS_IRON_CHEST_LOADED)
        {
            IronChestHelper.addIronChestRenders();
        }

        if (Mods.IS_EE3_LOADED)
        {
            EE3Helper.addEE3Renders();
        }
    }

    @Override
    public void init(FMLInitializationEvent event) {
        super.init(event);

        SharedAtlas.init(Minecraft.getMinecraft().getResourceManager());
    }

    @Override
    public void registerEventHandlers()
    {
        super.registerEventHandlers();
        EventHandlerClient ev = new EventHandlerClient();
        FMLCommonHandler.instance().bus().register(ev);
        MinecraftForge.EVENT_BUS.register(ev);
    }
}
