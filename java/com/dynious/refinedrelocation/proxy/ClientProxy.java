package com.dynious.refinedrelocation.proxy;

import com.dynious.refinedrelocation.block.ModBlocks;
import com.dynious.refinedrelocation.mods.IronChestHelper;
import com.dynious.refinedrelocation.multiblock.TileMultiBlockBase;
import com.dynious.refinedrelocation.renderer.*;
import com.dynious.refinedrelocation.tileentity.*;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.relauncher.Side;
import io.netty.channel.embedded.EmbeddedChannel;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.network.INetHandler;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraftforge.client.MinecraftForgeClient;

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

        MinecraftForgeClient.registerItemRenderer(ItemBlock.getItemFromBlock(ModBlocks.blockExtender), new ItemRendererBlockExtender());
        MinecraftForgeClient.registerItemRenderer(ItemBlock.getItemFromBlock(ModBlocks.buffer), new ItemRendererBuffer());
        MinecraftForgeClient.registerItemRenderer(ItemBlock.getItemFromBlock(ModBlocks.sortingChest), new ItemRendererSortingChest());

        if (Loader.isModLoaded("IronChest"))
        {
            IronChestHelper.addIronChestRenders();
        }
    }

    @Override
    public EmbeddedChannel getChannel()
    {
        return channels.get(Side.CLIENT);
    }

    @Override
    public EntityPlayer getPlayerFromNetHandler (INetHandler handler)
    {
        if (handler instanceof NetHandlerPlayServer)
        {
            return ((NetHandlerPlayServer) handler).playerEntity;
        }
        else
        {
            return Minecraft.getMinecraft().thePlayer;
        }
    }
}
