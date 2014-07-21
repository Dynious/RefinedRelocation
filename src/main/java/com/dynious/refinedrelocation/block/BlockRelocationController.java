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
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockRelocationController extends BlockMultiBlockBase
{
    private static IIcon[] icons = new IIcon[2];

    protected BlockRelocationController()
    {
        super(Material.rock);
        this.setBlockName(Names.relocationController);
        this.setHardness(3.0F);
        this.setCreativeTab(RefinedRelocation.tabRefinedRelocation);
    }

    @Override
    public TileMultiBlockBase createNewTileEntity(World world, int meta)
    {
        return new TileRelocationController();
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float par7, float par8, float par9)
    {
        TileEntity tileEntity = world.getTileEntity(x, y, z);
        if (tileEntity != null && tileEntity instanceof TileRelocationController)
        {
            return ((TileRelocationController) tileEntity).onActivated(world, player, side);
        }
        else
        {
            return super.onBlockActivated(world, x, y, z, player, side, par7, par8, par9);
        }
    }

    @Override
    public IIcon getIcon(IBlockAccess world, int x, int y, int z, int side)
    {
        TileEntity tileEntity = world.getTileEntity(x, y, z);
        if (tileEntity != null && tileEntity instanceof TileRelocationController)
        {
            return ((TileRelocationController) tileEntity).isLocked ? icons[1] : icons[0];
        }
        return icons[0];
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconRegister)
    {
        icons[0] = iconRegister.registerIcon(Resources.MOD_ID + ":" + Names.relocationController);
        icons[1] = iconRegister.registerIcon(Resources.MOD_ID + ":" + Names.relocationController + "Locked");
        blockIcon = iconRegister.registerIcon(Resources.MOD_ID + ":" + Names.relocationController); // For the Item icon
    }
}
