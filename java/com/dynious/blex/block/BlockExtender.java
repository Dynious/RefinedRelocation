package com.dynious.blex.block;

import com.dynious.blex.BlockExtenders;
import com.dynious.blex.gui.GuiAdvancedBlockExtender;
import com.dynious.blex.gui.GuiAdvancedFilteredBlockExtender;
import com.dynious.blex.gui.GuiFiltered;
import com.dynious.blex.gui.GuiWirelessBlockExtender;
import com.dynious.blex.helper.DistanceHelper;
import com.dynious.blex.item.ModItems;
import com.dynious.blex.lib.Names;
import com.dynious.blex.lib.Settings;
import com.dynious.blex.tileentity.*;
import cpw.mods.fml.common.FMLCommonHandler;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatMessageComponent;
import net.minecraft.world.World;

import java.util.List;

public class BlockExtender extends BlockContainer
{
    public BlockExtender(int id)
    {
        super(id, Material.rock);
        this.setUnlocalizedName(Names.blockExtender);
        this.setCreativeTab(BlockExtenders.tabBlEx);
    }


    @Override
    public TileEntity createNewTileEntity(World world)
    {
        return null;
    }

    @Override
    public TileEntity createTileEntity(World world, int metadata)
    {
        switch (metadata)
        {
            case 0:
                return new TileBlockExtender();
            case 1:
                return new TileAdvancedBlockExtender();
            case 2:
                return new TileFilteredBlockExtender();
            case 3:
                return new TileAdvancedFilteredBlockExtender();
            case 4:
                return new TileWirelessBlockExtender();
            default:
                return null;
        }
    }

    @Override
    @SuppressWarnings({"rawtypes", "unchecked"})
    public void getSubBlocks(int par1, CreativeTabs par2CreativeTabs,
                             List par3List)
    {
        for (int j = 0; j < (Settings.DISABLE_WIRELESS_BLOCK_EXTENDER ? 4 : 5); ++j)
        {
            par3List.add(new ItemStack(par1, 1, j));
        }
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z,
                                    EntityPlayer player, int par6, float par7, float par8, float par9)
    {
        if (player.isSneaking())
        {
            return false;
        }
        else
        {
            TileEntity tile = world.getBlockTileEntity(x, y, z);
            if (tile != null)
            {
                if (tile instanceof TileAdvancedBlockExtender)
                {
                    FMLCommonHandler.instance().showGuiScreen(new GuiAdvancedBlockExtender((TileAdvancedBlockExtender) tile));
                }
                else if (tile instanceof TileFilteredBlockExtender)
                {
                    FMLCommonHandler.instance().showGuiScreen(new GuiFiltered((TileFilteredBlockExtender) tile));
                }
                else if (tile instanceof TileWirelessBlockExtender)
                {
                    if (player.getCurrentEquippedItem() != null && player.getCurrentEquippedItem().getItem() == ModItems.linker && player.getCurrentEquippedItem().hasTagCompound())
                    {
                        NBTTagCompound tag = player.getCurrentEquippedItem().getTagCompound();
                        int tileX = tag.getInteger("tileX");
                        int tileY = tag.getInteger("tileY");
                        int tileZ = tag.getInteger("tileZ");
                        if (DistanceHelper.getDistanceSq(x, y, z, tileX, tileY, tileZ) <= Settings.MAX_RANGE_WIRELESS_BLOCK_EXTENDER * Settings.MAX_RANGE_WIRELESS_BLOCK_EXTENDER)
                        {
                            ((TileWirelessBlockExtender) tile).setConnection(tileX, tileY, tileZ);
                            if (world.isRemote)
                            {
                                player.sendChatToPlayer(new ChatMessageComponent()
                                        .addText("This Wireless Block Extender is now link with the TileEntity at: " + tileX + ":" + tileY + ":" + tileZ));
                            }
                        }
                        else
                        {
                            if (world.isRemote)
                            {
                                player.sendChatToPlayer(new ChatMessageComponent()
                                        .addText("This Wireless Block Extender too far from the TileEntity at: " + tileX + ":" + tileY + ":" + tileZ));
                                player.sendChatToPlayer(new ChatMessageComponent()
                                        .addText("This Wireless Block Extender max range is: " + Settings.MAX_RANGE_WIRELESS_BLOCK_EXTENDER));
                            }
                        }
                        return true;
                    }
                    else
                    {
                        FMLCommonHandler.instance().showGuiScreen(new GuiWirelessBlockExtender((TileWirelessBlockExtender) tile));
                    }
                }
                else if (tile instanceof TileAdvancedFilteredBlockExtender)
                {
                    FMLCommonHandler.instance().showGuiScreen(new GuiAdvancedFilteredBlockExtender((TileAdvancedFilteredBlockExtender) tile));
                }
            }
        }
        return true;
    }

    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, int par5)
    {
        super.onNeighborBlockChange(world, x, y, z, par5);
        TileEntity tile = world.getBlockTileEntity(x, y, z);
        if (tile != null && tile instanceof TileBlockExtender)
        {
            ((TileBlockExtender) tile).blocksChanged = true;
        }
    }

    @Override
    public boolean isOpaqueCube()
    {
        return false;
    }

    @Override
    public int getRenderType()
    {
        return -1;
    }

    @Override
    public boolean renderAsNormalBlock()
    {

        return false;
    }

    @Override
    public int getRenderBlockPass()
    {
        return 1;
    }

    @Override
    protected String getTextureName()
    {
        return "obsidian";
    }
}
