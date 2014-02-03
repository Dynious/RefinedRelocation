package com.dynious.blex.block;

import cofh.api.block.IDismantleable;
import com.dynious.blex.BlockExtenders;
import com.dynious.blex.helper.BlockHelper;
import com.dynious.blex.helper.DistanceHelper;
import com.dynious.blex.helper.GuiHelper;
import com.dynious.blex.item.ModItems;
import com.dynious.blex.lib.Names;
import com.dynious.blex.lib.Settings;
import com.dynious.blex.tileentity.*;
import cpw.mods.fml.common.Optional.Method;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatMessageComponent;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static cpw.mods.fml.common.Optional.Interface;
import static cpw.mods.fml.common.Optional.InterfaceList;

@InterfaceList(value = {@Interface(iface = "cofh.api.block.IDismantleable", modid = "CoFHCore")})
public class BlockExtender extends BlockContainer implements IDismantleable
{
    public BlockExtender(int id)
    {
        super(id, Material.rock);
        this.setUnlocalizedName(Names.blockExtender);
        this.setHardness(3.0F);
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
            if (player.getCurrentEquippedItem() == null)
            {
                TileEntity tile = world.getBlockTileEntity(x, y, z);
                if (tile != null && tile instanceof TileBlockExtender)
                {
                    TileBlockExtender blockExtender = (TileBlockExtender) tile;
                    blockExtender.setRedstoneEnabled(!blockExtender.isRedstoneEnabled);
                    if (world.isRemote)
                    {
                        player.sendChatToPlayer(new ChatMessageComponent()
                                .addText(BlockHelper.getTileEntityDisplayName(tile) + " is now "
                                        + (blockExtender.isRedstoneEnabled ? "" : "not ") + "transmitting redstone power"));
                    }
                    return true;
                }
            }
            return false;
        }
        else
        {
            TileEntity tile = world.getBlockTileEntity(x, y, z);
            if (tile != null)
            {
                if (tile instanceof TileWirelessBlockExtender)
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
                                        .addText(BlockHelper.getTileEntityDisplayName(tile) + " linked with " + BlockHelper.getTileEntityDisplayName(((TileWirelessBlockExtender) tile).getConnectedTile()) + " at " + tileX + ":" + tileY + ":" + tileZ));
                            }
                        }
                        else
                        {
                            if (world.isRemote)
                            {
                                player.sendChatToPlayer(new ChatMessageComponent()
                                        .addText("The " + BlockHelper.getTileEntityDisplayName(tile) + " is too far from the linked position (max range: " + Settings.MAX_RANGE_WIRELESS_BLOCK_EXTENDER + ")"));
                            }
                        }
                        return true;
                    }
                }
                GuiHelper.openGui(player, tile);
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
    public boolean canConnectRedstone(IBlockAccess world, int x, int y, int z, int side)
    {
        TileEntity tile = world.getBlockTileEntity(x, y, z);
        return tile != null && tile instanceof TileBlockExtender && ((TileBlockExtender) tile).canConnectRedstone(side);
    }

    @Override
    public int isProvidingStrongPower(IBlockAccess world, int x, int y, int z, int side)
    {
        return isProvidingWeakPower(world, x, y, z, side);
    }

    @Override
    public int isProvidingWeakPower(IBlockAccess world, int x, int y, int z, int side)
    {
        TileEntity tile = world.getBlockTileEntity(x, y, z);
        if (tile != null && tile instanceof TileBlockExtender)
        {
            return ((TileBlockExtender) tile).isPoweringTo(side);
        }
        return 0;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void randomDisplayTick(World world, int x, int y, int z, Random random)
    {
        TileBlockExtender tile = (TileBlockExtender) world.getBlockTileEntity(x, y, z);

        if (!tile.isRedstonePowered)
            return;

        float f = (float) x + 0.5F;
        float f1 = (float) y + 0.5F + (random.nextFloat() * 6F) / 16F;
        float f2 = (float) z + 0.5F;
        float f3 = 0.6F;
        float f4 = random.nextFloat() * 0.6F - 0.3F;

        world.spawnParticle("reddust", f - f3, f1, f2 + f4, 0.0D, 0.0D, 0.0D);
        world.spawnParticle("reddust", f + f3, f1, f2 + f4, 0.0D, 0.0D, 0.0D);
        world.spawnParticle("reddust", f + f4, f1, f2 - f3, 0.0D, 0.0D, 0.0D);
        world.spawnParticle("reddust", f + f4, f1, f2 + f3, 0.0D, 0.0D, 0.0D);
    }

    @Override
    public boolean canProvidePower()
    {
        return true;
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

    public boolean isBlockSolidOnSide(World world, int x, int y, int z, ForgeDirection side)
    {
        return true;
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

    @Override
    public int damageDropped(int metadata)
    {
        return metadata;
    }

    @Method(modid = "CoFHCore")
    @Override
    public ItemStack dismantleBlock(EntityPlayer player, World world, int x,
                                    int y, int z, boolean returnBlock)
    {
        int meta = world.getBlockMetadata(x, y, z);

        ArrayList<ItemStack> items = this.getBlockDropped(world, x, y, z, meta, 0);

        for (ItemStack item : items)
        {

            EntityItem entityitem = new EntityItem(world, x + 0.5F, y + 0.5F, z + 0.5F, item);

            entityitem.delayBeforeCanPickup = 10;

            world.spawnEntityInWorld(entityitem);

            entityitem.motionX *= 0.25F;
            entityitem.motionZ *= 0.25F;
        }

        world.setBlockToAir(x, y, z);
        return null;
    }

    @Method(modid = "CoFHCore")
    @Override
    public boolean canDismantle(EntityPlayer player, World world, int x, int y, int z)
    {
        return true;
    }

    @Override
    public boolean rotateBlock(World worldObj, int x, int y, int z, ForgeDirection axis)
    {
        TileBlockExtender tile = (TileBlockExtender) worldObj.getBlockTileEntity(x, y, z);
        return tile.rotateBlock();
    }
}
