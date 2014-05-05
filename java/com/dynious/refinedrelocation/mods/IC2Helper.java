package com.dynious.refinedrelocation.mods;

import com.dynious.refinedrelocation.lib.Mods;
import cpw.mods.fml.common.Loader;
import ic2.api.energy.event.EnergyTileLoadEvent;
import ic2.api.energy.event.EnergyTileUnloadEvent;
import ic2.api.energy.tile.IEnergyTile;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.MinecraftForge;

public class IC2Helper
{
    public static void addToEnergyNet(TileEntity tile)
    {
        if (Mods.IS_IC2_LOADED && tile instanceof IEnergyTile)
            MinecraftForge.EVENT_BUS.post(new EnergyTileLoadEvent((IEnergyTile) tile));
    }

    public static void removeFromEnergyNet(TileEntity tile)
    {
        if (Mods.IS_IC2_LOADED && tile instanceof IEnergyTile)
            MinecraftForge.EVENT_BUS.post(new EnergyTileUnloadEvent((IEnergyTile) tile));
    }
}
