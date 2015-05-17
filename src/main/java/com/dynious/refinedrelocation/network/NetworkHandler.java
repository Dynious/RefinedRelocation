package com.dynious.refinedrelocation.network;

import com.dynious.refinedrelocation.lib.Reference;
import com.dynious.refinedrelocation.network.packet.*;
import com.dynious.refinedrelocation.network.packet.filter.MessageAddFilter;
import com.dynious.refinedrelocation.network.packet.filter.MessageSetFilterBoolean;
import com.dynious.refinedrelocation.network.packet.filter.MessageSetFilterString;
import com.dynious.refinedrelocation.network.packet.gui.*;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;

public class NetworkHandler
{
    public static final SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(Reference.MOD_ID.toLowerCase());

    public static void init()
    {
        INSTANCE.registerMessage(MessageSetFilterBoolean.class, MessageSetFilterBoolean.class, 0, Side.SERVER);
        INSTANCE.registerMessage(MessageSetFilterBoolean.class, MessageSetFilterBoolean.class, 1, Side.CLIENT);
        INSTANCE.registerMessage(MessageSetFilterString.class, MessageSetFilterString.class, 2, Side.SERVER);
        INSTANCE.registerMessage(MessageSetFilterString.class, MessageSetFilterString.class, 3, Side.CLIENT);
        INSTANCE.registerMessage(MessageAddFilter.class, MessageAddFilter.class, 4, Side.SERVER);

        INSTANCE.registerMessage(MessageInsertDirection.class, MessageInsertDirection.class, 5, Side.SERVER);
        INSTANCE.registerMessage(MessageTabSync.class, MessageTabSync.class, 6, Side.CLIENT);
        INSTANCE.registerMessage(MessageItemList.class, MessageItemList.class, 7, Side.CLIENT);
        INSTANCE.registerMessage(MessageKonga.class, MessageKonga.class, 8, Side.CLIENT);
        INSTANCE.registerMessage(MessageItemRequest.class, MessageItemRequest.class, 9, Side.SERVER);
        INSTANCE.registerMessage(MessageItemAnswer.class, MessageItemAnswer.class, 10, Side.CLIENT);

        INSTANCE.registerMessage(MessageGUIBoolean.class, MessageGUIBoolean.class, 11, Side.SERVER);
        INSTANCE.registerMessage(MessageGUIBoolean.class, MessageGUIBoolean.class, 12, Side.CLIENT);
        INSTANCE.registerMessage(MessageGUIByte.class, MessageGUIByte.class, 13, Side.SERVER);
        INSTANCE.registerMessage(MessageGUIByte.class, MessageGUIByte.class, 14, Side.CLIENT);
        INSTANCE.registerMessage(MessageGUIInteger.class, MessageGUIInteger.class, 15, Side.SERVER);
        INSTANCE.registerMessage(MessageGUIInteger.class, MessageGUIInteger.class, 16, Side.CLIENT);
        INSTANCE.registerMessage(MessageGUIString.class, MessageGUIString.class, 17, Side.SERVER);
        INSTANCE.registerMessage(MessageGUIString.class, MessageGUIString.class, 18, Side.CLIENT);
        INSTANCE.registerMessage(MessageGUIAction.class, MessageGUIAction.class, 19, Side.SERVER);
        INSTANCE.registerMessage(MessageGUIAction.class, MessageGUIAction.class, 20, Side.CLIENT);
        INSTANCE.registerMessage(MessageGUIDouble.class, MessageGUIDouble.class, 21, Side.SERVER);
        INSTANCE.registerMessage(MessageGUIDouble.class, MessageGUIDouble.class, 22, Side.CLIENT);
    }
}
