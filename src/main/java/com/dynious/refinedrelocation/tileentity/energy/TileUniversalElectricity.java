package com.dynious.refinedrelocation.tileentity.energy;

import com.dynious.refinedrelocation.lib.Mods;
import cpw.mods.fml.common.Optional.Interface;
import universalelectricity.api.energy.IEnergyInterface;

@Interface(iface = "universalelectricity.api.energy.IEnergyInterface", modid = Mods.UE_ID)
public abstract class TileUniversalElectricity extends TileIndustrialCraft implements IEnergyInterface
{
}
