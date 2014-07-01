package com.dynious.refinedrelocation;

import com.dynious.refinedrelocation.block.ModBlocks;
import com.dynious.refinedrelocation.command.CommandRefinedRelocation;
import com.dynious.refinedrelocation.creativetab.CreativeTabRefinedRelocation;
import com.dynious.refinedrelocation.event.TickEvent;
import com.dynious.refinedrelocation.grid.relocator.RelocatorModuleRegistry;
import com.dynious.refinedrelocation.helper.LoadingCallbackHelper;
import com.dynious.refinedrelocation.item.ModItems;
import com.dynious.refinedrelocation.lib.Mods;
import com.dynious.refinedrelocation.lib.Reference;
import com.dynious.refinedrelocation.lib.Settings;
import com.dynious.refinedrelocation.mods.FMPHelper;
import com.dynious.refinedrelocation.multiblock.ModMultiBlocks;
import com.dynious.refinedrelocation.network.NetworkHandler;
import com.dynious.refinedrelocation.proxy.CommonProxy;
import com.dynious.refinedrelocation.version.VersionChecker;
import com.dynious.refinedrelocation.version.VersionContainer;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLInterModComms;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.client.event.ConfigChangedEvent.OnConfigChangedEvent;
import cpw.mods.fml.client.event.ConfigChangedEvent;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.common.config.Configuration;
import com.dynious.refinedrelocation.config.ConfigHandler;

@Mod(modid = Reference.MOD_ID, name = Reference.NAME, version = Reference.VERSION, dependencies = Reference.DEPENDENCIES, guiFactory = "com.dynious.refinedrelocation.config.GuiFactory")
public class RefinedRelocation
{
    @Mod.Instance(Reference.MOD_ID)
    public static RefinedRelocation instance;

    @SidedProxy(clientSide = Reference.CLIENT_PROXY, serverSide = Reference.COMMON_PROXY)
    public static CommonProxy proxy;

    public static CreativeTabs tabRefinedRelocation = new CreativeTabRefinedRelocation(CreativeTabs.getNextID(), Reference.MOD_ID);

    @Mod.EventHandler
    public void serverStarting(FMLServerStartingEvent event)
    {
        event.registerServerCommand(new CommandRefinedRelocation());
    }

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        VersionChecker.execute();

        FMLCommonHandler.instance().bus().register(RefinedRelocation.instance);
        ConfigHandler.init(event);

        FMLCommonHandler.instance().bus().register(new TickEvent());

        ModBlocks.init();

        ModItems.init();

        ModMultiBlocks.init();

        NetworkHandler.init();

        RelocatorModuleRegistry.registerModules();

        proxy.registerEventHandlers();
        
        ForgeChunkManager.setForcedChunkLoadingCallback(this, new LoadingCallbackHelper());
    }

    @SubscribeEvent
    public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent eventArgs) {
        if(eventArgs.modID.equals(Reference.MOD_ID))
            ConfigHandler.syncConfig();
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event)
    {
        if (Mods.IS_FMP_LOADED)
        {
            FMPHelper.addFMPBlocks();
            FMPHelper.addFMPRecipes();
        }

        proxy.initTileEntities();

        FMLInterModComms.sendMessage("Waila", "register", "com.dynious.refinedrelocation.mods.WailaProvider.callbackRegister");
    }
}
