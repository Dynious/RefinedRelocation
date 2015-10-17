package com.dynious.refinedrelocation.container;

import com.dynious.refinedrelocation.lib.Settings;
import com.dynious.refinedrelocation.lib.Strings;
import com.dynious.refinedrelocation.network.NetworkHandler;
import com.dynious.refinedrelocation.network.packet.gui.MessageGUI;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.Container;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;

public abstract class ContainerRefinedRelocation extends Container implements IContainerNetworked {

    @Override
    public void onMessageAction(int messageId, EntityPlayer player, Side side) {}

    @Override
    public void onMessageString(int messageId, String value, EntityPlayer player, Side side) {}

    @Override
    public void onMessageByte(int messageId, byte value, EntityPlayer player, Side side) {}

    @Override
    public void onMessageDouble(int messageId, double value, EntityPlayer player, Side side) {}

    @Override
    public void onMessageInteger(int messageId, int value, EntityPlayer player, Side side) {}

    @Override
    public void onMessageBoolean(int messageId, boolean value, EntityPlayer player, Side side) {}

    @Override
    public void onMessageBooleanArray(int messageId, boolean[] values, EntityPlayer player, Side side) {}

    public void sendSyncMessage(MessageGUI message) {
        for(Object crafter : crafters) {
            NetworkHandler.INSTANCE.sendTo(message, (EntityPlayerMP) crafter);
        }
    }

    public static boolean isRestrictedAccessWithError(EntityPlayer player) {
        if(Settings.ENABLE_ADVENTURE_MODE_RESTRICTION && player instanceof EntityPlayerMP && ((EntityPlayerMP) player).theItemInWorldManager.getGameType().isAdventure()) {
            ChatComponentText chatComponent = new ChatComponentText(StatCollector.translateToLocal(Strings.ADVENTURE_MODE));
            chatComponent.getChatStyle().setColor(EnumChatFormatting.DARK_RED);
            player.addChatMessage(chatComponent);
            return true;
        }
        return false;
    }

}
