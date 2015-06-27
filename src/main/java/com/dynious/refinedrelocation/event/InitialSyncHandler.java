package com.dynious.refinedrelocation.event;

import com.dynious.refinedrelocation.network.NetworkHandler;
import com.dynious.refinedrelocation.network.packet.MessageModSync;
import com.dynious.refinedrelocation.network.packet.MessageTabSync;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.FMLModContainer;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.ModContainer;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayerMP;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class InitialSyncHandler
{
    private static String[] getCreativeTabLabels()
    {
        String[] labels = new String[CreativeTabs.creativeTabArray.length];
        CreativeTabs[] creativeTabArray = CreativeTabs.creativeTabArray;
        for (int i = 0; i < labels.length; i++)
        {
            labels[i] = creativeTabArray[i].tabLabel;
        }
        return labels;
    }

    public static String[] getModIDs()
    {
        List<ModContainer> modList = Loader.instance().getActiveModList();
        List<String> modIDs = new ArrayList<String>();
        for (ModContainer modContainer : modList)
        {
            if (modContainer instanceof FMLModContainer)
            {
                modIDs.add(modContainer.getModId());
            }
        }
        Collections.sort(modIDs, new Comparator<String>()
        {
            @Override
            public int compare(String o1, String o2)
            {
                return Loader.instance().getIndexedModList().get(o1).getName().compareTo(Loader.instance().getIndexedModList().get(o2).getName());
            }
        });
        return modIDs.toArray(new String[modIDs.size()]);
    }

    private static String[] creativeTabLabels;
    private static String[] modIDs;

    @SubscribeEvent
    public void loggedInEvent(PlayerEvent.PlayerLoggedInEvent event)
    {
        if (FMLCommonHandler.instance().getEffectiveSide().isServer())
        {
            if (creativeTabLabels == null)
            {
                creativeTabLabels = getCreativeTabLabels();
            }
            if (modIDs == null)
            {
                modIDs = getModIDs();
            }
            NetworkHandler.INSTANCE.sendTo(new MessageTabSync(creativeTabLabels), (EntityPlayerMP) event.player);
            NetworkHandler.INSTANCE.sendTo(new MessageModSync(modIDs), (EntityPlayerMP) event.player);
        }
    }
}
