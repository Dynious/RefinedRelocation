package com.dynious.refinedrelocation.block;

import com.dynious.refinedrelocation.RefinedRelocation;
import com.dynious.refinedrelocation.lib.Names;
import com.dynious.refinedrelocation.lib.Resources;
import com.dynious.refinedrelocation.multiblock.BlockMultiBlockBase;
import com.dynious.refinedrelocation.multiblock.TileMultiBlockBase;
import com.dynious.refinedrelocation.tileentity.TileRelocationController;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.world.World;

public class BlockRelocationController extends BlockMultiBlockBase
{
    protected BlockRelocationController(int id)
    {
        super(id, Material.rock);
        this.setUnlocalizedName(Names.relocationController);
        this.setHardness(3.0F);
        this.setCreativeTab(RefinedRelocation.tabRefinedRelocation);
    }

    @Override
    public TileMultiBlockBase createNewTileEntity(World world)
    {
        return new TileRelocationController();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister iconRegister)
    {
        blockIcon = iconRegister.registerIcon(Resources.MOD_ID + ":" + Names.relocationController);
    }
}
