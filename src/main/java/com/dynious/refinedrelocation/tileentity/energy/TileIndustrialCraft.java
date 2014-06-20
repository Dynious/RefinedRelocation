package com.dynious.refinedrelocation.tileentity.energy;

import com.dynious.refinedrelocation.lib.Mods;
import cpw.mods.fml.common.Optional.Interface;
import ic2.api.energy.tile.IEnergySink;

@Interface(iface = "ic2.api.energy.tile.IEnergySink", modid = Mods.IC2_ID)
public abstract class TileIndustrialCraft extends TileCoFHCore implements IEnergySink
{
}
