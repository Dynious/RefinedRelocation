package com.dynious.refinedrelocation.proxy;

import com.dynious.refinedrelocation.event.EventHandlerClient;
import com.dynious.refinedrelocation.lib.BlockIds;
import com.dynious.refinedrelocation.mods.EE3Helper;
import com.dynious.refinedrelocation.mods.IronChestHelper;
import com.dynious.refinedrelocation.multiblock.TileMultiBlockBase;
import com.dynious.refinedrelocation.renderer.*;
import com.dynious.refinedrelocation.tileentity.*;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.Loader;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.common.MinecraftForge;

public class ClientProxy extends CommonProxy
{
    @Override
    public void initTileEntities()
    {
        super.initTileEntities();
        ClientRegistry.bindTileEntitySpecialRenderer(TileBlockExtender.class, new RendererBlockExtender());
        ClientRegistry.bindTileEntitySpecialRenderer(TileWirelessBlockExtender.class, new RendererWirelessBlockExtender());
        ClientRegistry.bindTileEntitySpecialRenderer(TileBuffer.class, new RendererBuffer());
        ClientRegistry.bindTileEntitySpecialRenderer(TileSortingChest.class, new RendererSortingChest());
        ClientRegistry.bindTileEntitySpecialRenderer(TileRelocationPortal.class, new RendererRelocationPortal());
        ClientRegistry.bindTileEntitySpecialRenderer(TileMultiBlockBase.class, new RendererMultiBlock());

        MinecraftForgeClient.registerItemRenderer(BlockIds.BLOCK_EXTENDER, new ItemRendererBlockExtender());
        MinecraftForgeClient.registerItemRenderer(BlockIds.BUFFER, new ItemRendererBuffer());
        MinecraftForgeClient.registerItemRenderer(BlockIds.SORTING_CHEST, new ItemRendererSortingChest());

        if (Loader.isModLoaded("IronChest"))
        {
            IronChestHelper.addIronChestRenders();
        }

        if (Loader.isModLoaded("EE3"))
        {
            EE3Helper.addEE3Renders();
        }
    }

    @Override
    public void registerEventHandlers()
    {
        super.registerEventHandlers();
        MinecraftForge.EVENT_BUS.register(new EventHandlerClient());
    }
}
