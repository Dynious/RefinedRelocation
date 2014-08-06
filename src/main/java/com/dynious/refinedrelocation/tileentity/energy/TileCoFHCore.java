package com.dynious.refinedrelocation.tileentity.energy;

import cofh.api.energy.IEnergyHandler;
import com.dynious.refinedrelocation.lib.Mods;
import cpw.mods.fml.common.Optional;

@Optional.Interface(iface = "cofh.api.energy.IEnergyHandler", modid = Mods.COFH_ENERGY_API_ID)
public abstract class TileCoFHCore extends TileBuildCraft implements IEnergyHandler
{
}
