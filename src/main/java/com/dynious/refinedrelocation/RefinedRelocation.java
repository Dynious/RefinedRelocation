package com.dynious.refinedrelocation;

import com.dynious.refinedrelocation.block.ModBlocks;
import com.dynious.refinedrelocation.command.CommandRefinedRelocation;
import com.dynious.refinedrelocation.config.ConfigHandler;
import com.dynious.refinedrelocation.creativetab.CreativeTabRefinedRelocation;
import com.dynious.refinedrelocation.event.TickEvent;
import com.dynious.refinedrelocation.grid.filter.MultiFilterRegistry;
import com.dynious.refinedrelocation.grid.relocator.RelocatorModuleRegistry;
import com.dynious.refinedrelocation.helper.LoadingCallbackHelper;
import com.dynious.refinedrelocation.item.ModItems;
import com.dynious.refinedrelocation.lib.Mods;
import com.dynious.refinedrelocation.lib.Reference;
import com.dynious.refinedrelocation.mods.BuildcraftIntegration;
import com.dynious.refinedrelocation.mods.FMPHelper;
import com.dynious.refinedrelocation.mods.JabbaHelper;
import com.dynious.refinedrelocation.multiblock.ModMultiBlocks;
import com.dynious.refinedrelocation.network.NetworkHandler;
import com.dynious.refinedrelocation.proxy.CommonProxy;
import com.dynious.refinedrelocation.version.VersionChecker;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.*;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraftforge.common.ForgeChunkManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(modid = Reference.MOD_ID, name = Reference.NAME, version = Reference.VERSION, dependencies = Reference.DEPENDENCIES, guiFactory = "com.dynious.refinedrelocation.config.GuiFactory")
public class RefinedRelocation {

    public static Logger logger = LogManager.getLogger();

    @Mod.Instance(Reference.MOD_ID)
    public static RefinedRelocation instance;

    @SidedProxy(clientSide = Reference.CLIENT_PROXY, serverSide = Reference.COMMON_PROXY)
    public static CommonProxy proxy;

    public static CreativeTabs tabRefinedRelocation = new CreativeTabRefinedRelocation(CreativeTabs.getNextID(), Reference.MOD_ID);

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        VersionChecker.execute();

        ConfigHandler.init(event);

        FMLCommonHandler.instance().bus().register(new TickEvent());

        ModBlocks.init();

        ModItems.init();

        ModMultiBlocks.init();

        NetworkHandler.init();

        RelocatorModuleRegistry.registerModules();
        MultiFilterRegistry.registerFilters();

        proxy.registerEventHandlers();

        ForgeChunkManager.setForcedChunkLoadingCallback(this, new LoadingCallbackHelper());
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        if (Mods.IS_FMP_LOADED) {
            FMPHelper.addFMPBlocks();
            FMPHelper.addFMPRecipes();
        }

        BuildcraftIntegration.init();

        proxy.init(event);
        proxy.initTileEntities();

        FMLInterModComms.sendMessage("Waila", "register", "com.dynious.refinedrelocation.mods.WailaProvider.callbackRegister");

    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        if (Mods.IS_JABBA_LOADED) {
            JabbaHelper.enableDolly();
        }
    }

    @Mod.EventHandler
    public void serverStarting(FMLServerStartingEvent event) {
        event.registerServerCommand(new CommandRefinedRelocation());
    }
}
