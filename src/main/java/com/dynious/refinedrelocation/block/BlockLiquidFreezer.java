package com.dynious.refinedrelocation.block;

import cofh.api.block.IDismantleable;
import com.dynious.refinedrelocation.RefinedRelocation;
import com.dynious.refinedrelocation.helper.GuiHelper;
import com.dynious.refinedrelocation.helper.IOHelper;
import com.dynious.refinedrelocation.lib.Names;
import com.dynious.refinedrelocation.tileentity.TileFreezer;
import cpw.mods.fml.common.Optional.Method;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;

import java.util.ArrayList;
import java.util.List;

import static cpw.mods.fml.common.Optional.Interface;
import static cpw.mods.fml.common.Optional.InterfaceList;

public class BlockLiquidFreezer extends BlockContainer
{
    protected BlockLiquidFreezer(int id)
    {
        super(id, Material.rock);
        this.setUnlocalizedName(Names.liquidFreezer);
        this.setHardness(3.0F);
        this.setCreativeTab(RefinedRelocation.tabRefinedRelocation);
    }

    @Override
    public TileEntity createNewTileEntity(World world)
    {
        return new TileFreezer();
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
