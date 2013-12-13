package com.dynious.blex;

import com.dynious.blex.block.ModBlocks;
import com.dynious.blex.config.ConfigHandler;
import com.dynious.blex.lib.Reference;
import com.dynious.blex.proxy.CommonProxy;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;

@Mod(modid = Reference.modid, name = Reference.name, version = Reference.version)
public class BlockExtenders
{
    @Mod.Instance(Reference.modid)
    public static BlockExtenders instance;

    @SidedProxy(clientSide = Reference.clientProxy, serverSide = Reference.commonProxy)
    public static CommonProxy proxy;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        ConfigHandler.init(event.getSuggestedConfigurationFile());

        ModBlocks.init();
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event)
    {

    }
}
