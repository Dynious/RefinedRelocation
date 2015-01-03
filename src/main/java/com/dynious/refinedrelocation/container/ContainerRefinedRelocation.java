package com.dynious.refinedrelocation.container;

import com.dynious.refinedrelocation.helper.LogHelper;
import com.dynious.refinedrelocation.network.NetworkHandler;
import com.dynious.refinedrelocation.network.packet.gui.*;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.Container;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public abstract class ContainerRefinedRelocation extends Container
{
    private List<SyncedVariable> syncList = new ArrayList<SyncedVariable>();
    /**
     * @param messageId The ID of this message. MAX SIZE BYTE!
     * @param message   The message (boolean, byte or integer)
     */
    public void sendMessage(int messageId, Object message)
    {
        if (message instanceof Boolean)
        {
            NetworkHandler.INSTANCE.sendToServer(new MessageGUIBooleanServer(messageId, (Boolean) message));
        }
        else if (message instanceof Byte)
        {
            NetworkHandler.INSTANCE.sendToServer(new MessageGUIByteServer(messageId, (Byte) message));
        }
        else if (message instanceof Integer)
        {
            NetworkHandler.INSTANCE.sendToServer(new MessageGUIIntegerServer(messageId, (Integer) message));
        }
        else
        {
            LogHelper.warning("ContainerRefinedRelocation#sendMessage was called for an unsupported class: " + message);
        }
    }

    public void onMessage(int messageID, Object message)
    {
    }

    /**
     * Syncs the given field from the server to all clients when value is changed.
     *
     * @param fieldName The name of the field that needs to be synced
     * @param owner The Object that contains the field
     */
    public void registerFieldSync(String fieldName, Object owner)
    {
        Field field;
        try
        {
            field = owner.getClass().getDeclaredField(fieldName);
        } catch (NoSuchFieldException e)
        {
            e.printStackTrace();
            return;
        }

        field.setAccessible(true);
        try
        {
            Object currentValue = field.get(owner);
            if (!(currentValue instanceof Boolean || currentValue instanceof Byte || currentValue instanceof Integer))
                throw new IllegalArgumentException("Only Booleans, Bytes and Integers are supported for syncing!");

            syncList.add(new SyncedVariable(field, owner, currentValue));
        } catch (Exception e)
        {
            e.printStackTrace();
            LogHelper.warning("Error when finding value of: " + field.toString() + ".\nThis value will NOT be synced");
        }
    }

    @Override
    public void detectAndSendChanges()
    {
        super.detectAndSendChanges();
        for (int i = 0; i < syncList.size(); i++)
        {
            SyncedVariable variable = syncList.get(i);
            try
            {
                Object newValue = variable.FIELD.get(variable.OWNER);
                if (variable.variable == null)
                {
                    if (newValue != null)
                    {
                        sync(variable, i, newValue);
                    }
                }
                else if (!variable.variable.equals(newValue))
                {
                    sync(variable, i, newValue);
                }
            } catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    private void sync(SyncedVariable variable, int index, Object newValue)
    {
        for (EntityPlayerMP crafter : (List<EntityPlayerMP>) crafters)
        {
            if (newValue instanceof Boolean)
            {
                NetworkHandler.INSTANCE.sendTo(new MessageGUIBooleanClient(index, (Boolean) newValue), crafter);
            }
            else if (newValue instanceof Byte)
            {
                NetworkHandler.INSTANCE.sendTo(new MessageGUIByteClient(index, (Byte) newValue), crafter);
            }
            else if (newValue instanceof Integer)
            {
                NetworkHandler.INSTANCE.sendTo(new MessageGUIIntegerClient(index, (Integer) newValue), crafter);
            }
        }
        variable.variable = newValue;
    }

    public final void onSyncMessage(int id, Object value)
    {
        if (id < syncList.size())
        {
            SyncedVariable variable = syncList.get(id);
            try
            {
                variable.FIELD.set(variable.OWNER, value);
            } catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    private static class SyncedVariable
    {
        public final Field FIELD;
        public final Object OWNER;
        public Object variable;

        public SyncedVariable(Field field, Object owner, Object variable)
        {
            this.FIELD = field;
            this.OWNER = owner;
            this.variable = variable;
        }
    }
}
