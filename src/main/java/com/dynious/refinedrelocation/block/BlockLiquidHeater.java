package com.dynious.refinedrelocation.block;

import cofh.api.block.IDismantleable;
import com.dynious.refinedrelocation.RefinedRelocation;
import com.dynious.refinedrelocation.lib.Names;
import com.dynious.refinedrelocation.tileentity.TileHeater;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;

import static cpw.mods.fml.common.Optional.Interface;
import static cpw.mods.fml.common.Optional.InterfaceList;

public class BlockLiquidHeater extends BlockContainer
{
    protected BlockLiquidHeater(int id)
    {
        super(id, Material.rock);
        this.setUnlocalizedName(Names.liquidHeater);
        this.setHardness(3.0F);
        this.setCreativeTab(RefinedRelocation.tabRefinedRelocation);
    }

    @Override
    public TileEntity createNewTileEntity(World world)
    {
        return new TileHeater();
    }

    public boolean isBlockSolidOnSide(World world, int x, int y, int z, ForgeDirection side)
    {
        return true;
    }

    @Override
    public int getRenderBlockPass()
    {
        return 1;
    }
}
