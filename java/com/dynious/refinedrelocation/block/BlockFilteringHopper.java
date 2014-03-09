package com.dynious.refinedrelocation.block;

import com.dynious.refinedrelocation.RefinedRelocation;
import com.dynious.refinedrelocation.lib.GuiIds;
import com.dynious.refinedrelocation.lib.Names;
import com.dynious.refinedrelocation.lib.Resources;
import com.dynious.refinedrelocation.tileentity.TileFilteringHopper;
import cpw.mods.fml.common.ObfuscationReflectionHelper;
import cpw.mods.fml.common.network.FMLNetworkHandler;
import cpw.mods.fml.relauncher.ReflectionHelper;
import net.minecraft.block.BlockHopper;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import java.lang.reflect.Field;

public class BlockFilteringHopper extends BlockHopper
{
    static final Field iconField = ReflectionHelper.findField(BlockHopper.class, ObfuscationReflectionHelper.remapFieldNames(BlockHopper.class.getName(), "hopperIcon", "field_149921_b", "b"));
    static final Field iconFieldTop = ReflectionHelper.findField(BlockHopper.class, ObfuscationReflectionHelper.remapFieldNames(BlockHopper.class.getName(), "hopperTopIcon", "field_149923_M", "M"));
    static final Field iconFieldInside = ReflectionHelper.findField(BlockHopper.class, ObfuscationReflectionHelper.remapFieldNames(BlockHopper.class.getName(), "hopperInsideIcon", "field_149924_N", "N"));

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
                FMLNetworkHandler.openGui(player, RefinedRelocation.instance, GuiIds.FILTERED, world, x, y, z);
            else
                FMLNetworkHandler.openGui(player, RefinedRelocation.instance, GuiIds.FILTERING_HOPPER, world, x, y, z);
            return true;
        }
        return super.onBlockActivated(world, x, y, z, player, par6, par7, par8, par9);
    }

    @Override
    public void registerBlockIcons(IIconRegister par1IconRegister)
    {
        try
        {
            iconField.set(this, par1IconRegister.registerIcon(Resources.MOD_ID + ":" + "filteringHopper_outside"));
            iconFieldTop.set(this, par1IconRegister.registerIcon(Resources.MOD_ID + ":" + "filteringHopper_top"));
            iconFieldInside.set(this, par1IconRegister.registerIcon(Resources.MOD_ID + ":" + "filteringHopper_inside"));
        } catch (IllegalAccessException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public String getItemIconName()
    {
        return Resources.MOD_ID + ":" + "filteringHopper";
    }

}
