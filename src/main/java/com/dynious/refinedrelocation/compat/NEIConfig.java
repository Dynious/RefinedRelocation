package com.dynious.refinedrelocation.compat;

import codechicken.nei.api.API;
import codechicken.nei.api.IConfigureNEI;
import com.dynious.refinedrelocation.block.ModBlocks;
import com.dynious.refinedrelocation.lib.Reference;
import net.minecraft.item.ItemStack;

public class NEIConfig implements IConfigureNEI
{
    @Override
    public void loadConfig()
    {
        API.hideItem(new ItemStack(ModBlocks.relocationPortal));
    }

    @Override
    public String getName()
    {
        return Reference.NAME;
    }

    @Override
    public String getVersion()
    {
        return Reference.VERSION;
    }
}
