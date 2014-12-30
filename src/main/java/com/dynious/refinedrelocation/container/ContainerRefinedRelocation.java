package com.dynious.refinedrelocation.container;

import com.dynious.refinedrelocation.helper.LogHelper;
import com.dynious.refinedrelocation.network.NetworkHandler;
import com.dynious.refinedrelocation.network.packet.gui.MessageGUIBoolean;
import com.dynious.refinedrelocation.network.packet.gui.MessageGUIByte;
import com.dynious.refinedrelocation.network.packet.gui.MessageGUIInteger;
import net.minecraft.inventory.Container;

public abstract class ContainerRefinedRelocation extends Container
{
    /**
     * @param messageId The ID of this message. MAX SIZE BYTE!
     * @param message   The message (boolean, byte or integer)
     */
    public void sendMessage(int messageId, Object message)
    {
        if (message instanceof Boolean)
        {
            NetworkHandler.INSTANCE.sendToServer(new MessageGUIBoolean(messageId, (Boolean) message));
        }
        else if (message instanceof Byte)
        {
            NetworkHandler.INSTANCE.sendToServer(new MessageGUIByte(messageId, (Byte) message));
        }
        else if (message instanceof Integer)
        {
            NetworkHandler.INSTANCE.sendToServer(new MessageGUIInteger(messageId, (Integer) message));
        }
        else
        {
            LogHelper.warning("ContainerRefinedRelocation#sendMessage was called for an unsupported class: " + message);
        }
    }

    public void onMessage(int messageID, Object message)
    {
    }
}
