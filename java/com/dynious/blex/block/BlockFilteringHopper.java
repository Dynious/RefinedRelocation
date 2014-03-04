package com.dynious.blex.block;

import com.dynious.blex.BlockExtenders;
import com.dynious.blex.lib.GuiIds;
import com.dynious.blex.lib.Names;
import com.dynious.blex.lib.Resources;
import com.dynious.blex.tileentity.TileFilteringHopper;
import cpw.mods.fml.common.ObfuscationReflectionHelper;
import cpw.mods.fml.common.network.FMLNetworkHandler;
import cpw.mods.fml.relauncher.ReflectionHelper;
import net.minecraft.block.BlockHopper;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import java.lang.reflect.Field;

public class BlockFilteringHopper extends BlockHopper
{
    static final Field iconField = ReflectionHelper.findField(BlockHopper.class, ObfuscationReflectionHelper.remapFieldNames(BlockHopper.class.getName(), "hopperIcon"));
    static final Field iconFieldTop = ReflectionHelper.findField(BlockHopper.class, ObfuscationReflectionHelper.remapFieldNames(BlockHopper.class.getName(), "hopperTopIcon"));
    static final Field iconFieldInside = ReflectionHelper.findField(BlockHopper.class, ObfuscationReflectionHelper.remapFieldNames(BlockHopper.class.getName(), "hopperInsideIcon"));

    protected BlockFilteringHopper(int id)
    {
        super(id);
        this.setUnlocalizedName(Names.filteringHopper);
        this.setCreativeTab(BlockExtenders.tabBlEx);
        this.setHardness(3.0F).setResistance(8.0F).setStepSound(soundWoodFootstep);
    }

    @Override
    public TileEntity createNewTileEntity(World world)
    {
        return new TileFilteringHopper();
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int par6, float par7, float par8, float par9)
    {
        if (!world.isRemote)
        {
            if (player.isSneaking())
                FMLNetworkHandler.openGui(player, BlockExtenders.instance, GuiIds.FILTERED, world, x, y, z);
            else
                FMLNetworkHandler.openGui(player, BlockExtenders.instance, GuiIds.FILTERING_HOPPER, world, x, y, z);
            return true;
        }
        return super.onBlockActivated(world, x, y, z, player, par6, par7, par8, par9);
    }

    @Override
    public void registerIcons(IconRegister par1IconRegister)
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
