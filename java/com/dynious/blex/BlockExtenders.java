package com.dynious.blex;

import com.dynious.blex.block.ModBlocks;
import com.dynious.blex.config.ConfigHandler;
import com.dynious.blex.creativetab.CreativeTabBlEx;
import com.dynious.blex.item.ModItems;
import com.dynious.blex.lib.Reference;
import com.dynious.blex.network.PacketHandler;
import com.dynious.blex.proxy.CommonProxy;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLInterModComms;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import net.minecraft.creativetab.CreativeTabs;

@Mod(modid = Reference.modid, name = Reference.name, version = Reference.version, dependencies = Reference.dependencies)
@NetworkMod(channels = {Reference.cannelName}, clientSideRequired = true, serverSideRequired = false, packetHandler = PacketHandler.class)
public class BlockExtenders
{
    @Mod.Instance(Reference.modid)
    public static BlockExtenders instance;

    @SidedProxy(clientSide = Reference.clientProxy, serverSide = Reference.commonProxy)
    public static CommonProxy proxy;

    public static CreativeTabs tabBlEx = new CreativeTabBlEx(CreativeTabs.getNextID(), Reference.modid);

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        ConfigHandler.init(event.getSuggestedConfigurationFile());

        ModBlocks.init();

        ModItems.init();
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event)
    {
        proxy.initTileEntities();

        FMLInterModComms.sendMessage("Waila", "register", "com.dynious.blex.mods.WailaProvider.callbackRegister");
    }
}
