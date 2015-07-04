package com.dynious.refinedrelocation.network;

import com.dynious.refinedrelocation.lib.Reference;
import com.dynious.refinedrelocation.network.packet.*;
import com.dynious.refinedrelocation.network.packet.filter.MessageSetFilterBooleanArray;
import com.dynious.refinedrelocation.network.packet.filter.MessageSetFilterType;
import com.dynious.refinedrelocation.network.packet.filter.MessageSetFilterBoolean;
import com.dynious.refinedrelocation.network.packet.filter.MessageSetFilterString;
import com.dynious.refinedrelocation.network.packet.gui.*;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;

public class NetworkHandler
{
    public static final SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(Reference.MOD_ID.toLowerCase());

    public static void init()
    {
        INSTANCE.registerMessage(MessageSetFilterBoolean.class, MessageSetFilterBoolean.class, 0, Side.SERVER);
        INSTANCE.registerMessage(MessageSetFilterBoolean.class, MessageSetFilterBoolean.class, 1, Side.CLIENT);
        INSTANCE.registerMessage(MessageSetFilterString.class, MessageSetFilterString.class, 2, Side.SERVER);
        INSTANCE.registerMessage(MessageSetFilterString.class, MessageSetFilterString.class, 3, Side.CLIENT);
        INSTANCE.registerMessage(MessageSetFilterType.class, MessageSetFilterType.class, 4, Side.SERVER);
        INSTANCE.registerMessage(MessageSetFilterType.class, MessageSetFilterType.class, 5, Side.CLIENT);
        INSTANCE.registerMessage(MessageSetFilterBooleanArray.class, MessageSetFilterBooleanArray.class, 6, Side.SERVER);
        INSTANCE.registerMessage(MessageSetFilterBooleanArray.class, MessageSetFilterBooleanArray.class, 7, Side.CLIENT);

        INSTANCE.registerMessage(MessageInsertDirection.class, MessageInsertDirection.class, 8, Side.SERVER);
        INSTANCE.registerMessage(MessageTabSync.class, MessageTabSync.class, 9, Side.CLIENT);
        INSTANCE.registerMessage(MessageItemList.class, MessageItemList.class, 10, Side.CLIENT);
        INSTANCE.registerMessage(MessageKonga.class, MessageKonga.class, 11, Side.CLIENT);
        INSTANCE.registerMessage(MessageItemRequest.class, MessageItemRequest.class, 12, Side.SERVER);
        INSTANCE.registerMessage(MessageItemAnswer.class, MessageItemAnswer.class, 13, Side.CLIENT);

        INSTANCE.registerMessage(MessageGUIBoolean.class, MessageGUIBoolean.class, 14, Side.SERVER);
        INSTANCE.registerMessage(MessageGUIBoolean.class, MessageGUIBoolean.class, 15, Side.CLIENT);
        INSTANCE.registerMessage(MessageGUIByte.class, MessageGUIByte.class, 16, Side.SERVER);
        INSTANCE.registerMessage(MessageGUIByte.class, MessageGUIByte.class, 17, Side.CLIENT);
        INSTANCE.registerMessage(MessageGUIInteger.class, MessageGUIInteger.class, 18, Side.SERVER);
        INSTANCE.registerMessage(MessageGUIInteger.class, MessageGUIInteger.class, 19, Side.CLIENT);
        INSTANCE.registerMessage(MessageGUIString.class, MessageGUIString.class, 20, Side.SERVER);
        INSTANCE.registerMessage(MessageGUIString.class, MessageGUIString.class, 21, Side.CLIENT);
        INSTANCE.registerMessage(MessageGUIAction.class, MessageGUIAction.class, 22, Side.SERVER);
        INSTANCE.registerMessage(MessageGUIAction.class, MessageGUIAction.class, 23, Side.CLIENT);
        INSTANCE.registerMessage(MessageGUIDouble.class, MessageGUIDouble.class, 24, Side.SERVER);
        INSTANCE.registerMessage(MessageGUIDouble.class, MessageGUIDouble.class, 25, Side.CLIENT);
        INSTANCE.registerMessage(MessageGUIBooleanArray.class, MessageGUIBooleanArray.class, 26, Side.SERVER);
        INSTANCE.registerMessage(MessageGUIBooleanArray.class, MessageGUIBooleanArray.class, 27, Side.CLIENT);

        INSTANCE.registerMessage(MessageOpenFilterGUI.class, MessageOpenFilterGUI.class, 28, Side.SERVER);
        INSTANCE.registerMessage(MessageModSync.class, MessageModSync.class, 29, Side.CLIENT);
    }

    @SideOnly(Side.CLIENT)
    public static EntityPlayer getClientPlayerEntity() {
        return FMLClientHandler.instance().getClientPlayerEntity();
    }
}
