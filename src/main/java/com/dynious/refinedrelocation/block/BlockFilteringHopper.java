package com.dynious.refinedrelocation.block;

import com.dynious.refinedrelocation.RefinedRelocation;
import com.dynious.refinedrelocation.api.APIUtils;
import com.dynious.refinedrelocation.lib.GuiIds;
import com.dynious.refinedrelocation.lib.Names;
import com.dynious.refinedrelocation.lib.Resources;
import com.dynious.refinedrelocation.tileentity.TileFilteringHopper;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.ObfuscationReflectionHelper;
import cpw.mods.fml.relauncher.ReflectionHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.BlockHopper;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import java.lang.reflect.Field;

public class BlockFilteringHopper extends BlockHopper
{

    protected BlockFilteringHopper()
    {
        super();
        this.setBlockName(Names.filteringHopper);
        this.setCreativeTab(RefinedRelocation.tabRefinedRelocation);
        this.setHardness(3.0F).setResistance(8.0F).setStepSound(soundTypeWood);
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta)
    {
        return new TileFilteringHopper();
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int par6, float par7, float par8, float par9)
    {
        if (!world.isRemote)
        {
            if (player.isSneaking())
                APIUtils.openFilteringGUI(player, world, x, y, z);
            else
                player.openGui(RefinedRelocation.instance, GuiIds.FILTERING_HOPPER, world, x, y, z);
            return true;
        }
        return super.onBlockActivated(world, x, y, z, player, par6, par7, par8, par9);
    }

    @Override
    public void registerBlockIcons(IIconRegister par1IconRegister)
    {
        this.field_149921_b = par1IconRegister.registerIcon(Resources.MOD_ID + ":" + "filteringHopper_outside");
        this.field_149923_M = par1IconRegister.registerIcon(Resources.MOD_ID + ":" + "filteringHopper_top");
        this.field_149924_N = par1IconRegister.registerIcon(Resources.MOD_ID + ":" + "filteringHopper_inside");
    }

    @Override
    public String getItemIconName()
    {
        return Resources.MOD_ID + ":" + "filteringHopper";
    }

}
