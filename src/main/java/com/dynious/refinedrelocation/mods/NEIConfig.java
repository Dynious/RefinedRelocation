package com.dynious.refinedrelocation.mods;

import com.dynious.refinedrelocation.api.ModObjects;
import com.dynious.refinedrelocation.lib.BlockIds;
import com.dynious.refinedrelocation.lib.Names;
import com.dynious.refinedrelocation.lib.Reference;
import codechicken.nei.api.API;
import codechicken.nei.api.IConfigureNEI;

public class NEIConfig implements IConfigureNEI
{
	@Override
	public void loadConfig()
	{
		API.hideItem(BlockIds.RELOCATION_PORTAL);
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
