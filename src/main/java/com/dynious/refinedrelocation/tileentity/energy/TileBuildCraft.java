package com.dynious.refinedrelocation.tileentity.energy;

import buildcraft.api.power.IPowerReceptor;
import cpw.mods.fml.common.Optional.Interface;
import net.minecraft.tileentity.TileEntity;

@Interface(iface = "buildcraft.api.power.IPowerReceptor", modid = "BuildCraft|Energy")
public abstract class TileBuildCraft extends TileEntity implements IPowerReceptor
{
}
