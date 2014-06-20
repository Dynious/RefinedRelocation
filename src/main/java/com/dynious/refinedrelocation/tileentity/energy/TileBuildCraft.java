package com.dynious.refinedrelocation.tileentity.energy;

import buildcraft.api.power.IPowerReceptor;
import com.dynious.refinedrelocation.lib.Mods;
import cpw.mods.fml.common.Optional.Interface;
import net.minecraft.tileentity.TileEntity;

@Interface(iface = "buildcraft.api.power.IPowerReceptor", modid = Mods.BC_API_POWER_ID)
public abstract class TileBuildCraft extends TileEntity implements IPowerReceptor
{
}
