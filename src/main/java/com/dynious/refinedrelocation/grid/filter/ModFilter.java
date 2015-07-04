package com.dynious.refinedrelocation.grid.filter;

import com.dynious.refinedrelocation.api.filter.IChecklistFilter;
import com.dynious.refinedrelocation.api.gui.IGuiWidgetWrapped;
import com.dynious.refinedrelocation.client.gui.widget.GuiFilterList;
import com.dynious.refinedrelocation.event.InitialSyncHandler;
import com.dynious.refinedrelocation.lib.Resources;
import com.dynious.refinedrelocation.lib.Strings;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.ModContainer;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.ResourceLocation;

import java.util.List;

public class ModFilter extends MultiFilterChildBase implements IChecklistFilter
{
    public static final String TYPE_NAME = "mod";

    private static String[] serverSideModIDs;
    private static String[] modNames;

    public static void syncMods(String[] modIDs)
    {
        serverSideModIDs = modIDs;
        modNames = new String[modIDs.length];
        for (int i = 0; i < modIDs.length; i++)
        {
            ModContainer modContainer = Loader.instance().getIndexedModList().get(modIDs[i]);
            if(modContainer != null)
            {
                modNames[i] = modContainer.getName();
            } else {
                modNames[i] = modIDs[i];
            }
        }
    }

    private boolean[] modStates;

    public ModFilter()
    {
        if (serverSideModIDs == null)
        {
            serverSideModIDs = InitialSyncHandler.getModIDs();
        }
        modStates = new boolean[serverSideModIDs.length];
    }

    @Override
    public boolean isInFilter(ItemStack itemStack)
    {
        GameRegistry.UniqueIdentifier identifier = GameRegistry.findUniqueIdentifierFor(itemStack.getItem());
        if (identifier != null)
        {
            for (int i = 0; i < modStates.length; i++)
            {
                if (modStates[i] && serverSideModIDs[i].equals(identifier.modId))
                {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void writeToNBT(NBTTagCompound compound)
    {
        NBTTagList tagList = new NBTTagList();
        for (int i = 0; i < modStates.length; i++)
        {
            if (modStates[i])
            {
                tagList.appendTag(new NBTTagString(serverSideModIDs[i]));
            }
        }
        compound.setTag("modStates", tagList);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound)
    {
        NBTTagList tagList = compound.getTagList("modStates", 8);
        for (int i = 0; i < tagList.tagCount(); i++)
        {
            String tabLabel = tagList.getStringTagAt(i);
            for (int j = 0; j < serverSideModIDs.length; j++)
            {
                if (serverSideModIDs[j].equals(tabLabel))
                {
                    modStates[j] = true;
                    break;
                }
            }
        }
    }

    @Override
    public void sendUpdate(EntityPlayerMP playerMP)
    {
        getParentFilter().sendBooleanArrayToPlayer(this, playerMP, 0, modStates);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IGuiWidgetWrapped getGuiWidget(int x, int y, int width, int height)
    {
        return new GuiFilterList(x, y, width, height, this);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public ResourceLocation getIconSheet()
    {
        return Resources.GUI_SHARED;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int getIconX()
    {
        return 188;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int getIconY()
    {
        return 238;
    }

    @Override
    public void setFilterBooleanArray(int optionId, boolean[] values)
    {
        modStates = values;
    }

    @Override
    public void setFilterBoolean(int optionId, boolean value)
    {
        modStates[optionId] = value;
    }

    @Override
    public void setFilterString(int optionId, String value)
    {
    }

    @Override
    public String getName(int index)
    {
        return modNames[index];
    }

    @Override
    public void setValue(int optionIndex, boolean value)
    {
        modStates[optionIndex] = value;
        markDirty(true);
    }

    @Override
    public boolean getValue(int optionIndex)
    {
        return modStates[optionIndex];
    }

    @Override
    public int getOptionCount()
    {
        return modStates.length;
    }

    @Override
    public String getTypeName()
    {
        return TYPE_NAME;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public String getNameLangKey()
    {
        return Strings.MOD_FILTER;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public String getDescriptionLangKey()
    {
        return Strings.MOD_FILTER_DESCRIPTION;
    }
}
