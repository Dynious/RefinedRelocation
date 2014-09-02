package com.dynious.refinedrelocation.block;

import cofh.api.block.IDismantleable;
import com.dynious.refinedrelocation.RefinedRelocation;
import com.dynious.refinedrelocation.helper.*;
import com.dynious.refinedrelocation.item.ModItems;
import com.dynious.refinedrelocation.lib.Mods;
import com.dynious.refinedrelocation.lib.Names;
import com.dynious.refinedrelocation.lib.Settings;
import com.dynious.refinedrelocation.lib.Strings;
import com.dynious.refinedrelocation.tileentity.*;
import cpw.mods.fml.common.Optional;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Optional.Interface(iface = "cofh.api.block.IDismantleable", modid = Mods.COFH_BLOCK_API_ID)
public class BlockExtender extends BlockContainer implements IDismantleable
{
    public BlockExtender()
    {
        super(Material.rock);
        this.setBlockName(Names.blockExtender);
        this.setHardness(3.0F);
        this.setCreativeTab(RefinedRelocation.tabRefinedRelocation);
    }


    @Override
    public TileEntity createNewTileEntity(World world, int meta)
    {
        switch (meta)
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
    public void getSubBlocks(Item item, CreativeTabs par2CreativeTabs,
                             List par3List)
    {
        for (int metadata = 0; metadata < (Settings.DISABLE_WIRELESS_BLOCK_EXTENDER ? 4 : 5); ++metadata)
        {
            par3List.add(new ItemStack(item, 1, metadata));
        }
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z,
                                    EntityPlayer player, int par6, float par7, float par8, float par9)
    {
        TileEntity tile = world.getTileEntity(x, y, z);
        if (player.isSneaking())
        {
            if (player.getCurrentEquippedItem() == null)
            {
                if (tile != null && tile instanceof TileBlockExtender && !(tile instanceof TileWirelessBlockExtender))
                {
                    TileBlockExtender blockExtender = (TileBlockExtender) tile;
                    blockExtender.setRedstoneTransmissionEnabled(!blockExtender.isRedstoneTransmissionEnabled());
                    if (world.isRemote)
                    {
                        player.addChatComponentMessage(
                                new ChatComponentText(StatCollector.translateToLocal(Strings.REDSTONE_TRANSMISSION) + ' ' +
                                StatCollector.translateToLocal(blockExtender.isRedstoneTransmissionEnabled() ? Strings.ENABLED : Strings.DISABLED).toLowerCase()));
                    }
                    return true;
                }
            }
            return false;
        }
        else
        {
            if (tile != null)
            {
                if (tile instanceof TileWirelessBlockExtender)
                {
                    TileWirelessBlockExtender wirelessTile = (TileWirelessBlockExtender) tile;
                    if (player.getCurrentEquippedItem() != null && player.getCurrentEquippedItem().getItem() == ModItems.linker)
                    {
                        if (player.getCurrentEquippedItem().hasTagCompound())
                        {
                            NBTTagCompound tag = player.getCurrentEquippedItem().getTagCompound();
                            int tileX = tag.getInteger("tileX");
                            int tileY = tag.getInteger("tileY");
                            int tileZ = tag.getInteger("tileZ");

                            if (DistanceHelper.getDistanceSq(x, y, z, tileX, tileY, tileZ) <= Math.pow(Settings.MAX_RANGE_WIRELESS_BLOCK_EXTENDER, 2))
                            {
                                wirelessTile.setLink(tileX, tileY, tileZ);
                                if (world.isRemote)
                                {
                                    final String blockDisplayName = world.isBlockHelper.getBlockDisplayName(tile.getWorldObj(), tileX, tileY, tileZ);
                                    String tileDisplayName = BlockHelper.getTileEntityDisplayName(wirelessTile);
                                    player.addChatComponentMessage(new ChatComponentText(StatCollector.translateToLocalFormatted(Strings.LINKED_WITH,
                                            tileDisplayName, blockDisplayName, tileX, tileY, tileZ)));
                                }
                            }
                            else
                            {
                                if (world.isRemote)
                                {
                                    String tileDisplayName = BlockHelper.getTileEntityDisplayName(wirelessTile);
                                    player.addChatComponentMessage(new ChatComponentText(StatCollector.translateToLocalFormatted(Strings.TOO_FAR,
                                            tileDisplayName, Settings.MAX_RANGE_WIRELESS_BLOCK_EXTENDER)));
                                }
                            }
                        }
                        else if (wirelessTile.isLinked())
                        {
                            wirelessTile.clearLink();
                            if (world.isRemote)
                            {
                                String tileDisplayName = BlockHelper.getTileEntityDisplayName(wirelessTile);
                                player.addChatComponentMessage(new ChatComponentText(StatCollector.translateToLocalFormatted(Strings.NO_LONGER_LINKED,
                                        tileDisplayName)));
                            }
                        }
                        return true;
                    }
                }
                return GuiHelper.openGui(player, tile);
            }
        }
        return true;
    }

    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, Block block)
    {
        super.onNeighborBlockChange(world, x, y, z, block);
        TileEntity tile = world.getTileEntity(x, y, z);
        if (tile != null && tile instanceof TileBlockExtender)
        {
            ((TileBlockExtender) tile).blocksChanged = true;
        }
    }

    @Override
    public boolean canConnectRedstone(IBlockAccess world, int x, int y, int z, int side)
    {
        TileEntity tile = world.getTileEntity(x, y, z);
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
        TileEntity tile = world.getTileEntity(x, y, z);
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
        TileBlockExtender tile = (TileBlockExtender) world.getTileEntity(x, y, z);

        if (!tile.isRedstoneTransmissionActive())
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
    public boolean renderAsNormalBlock()
    {
        return false;
    }

    @Override
    public boolean isBlockSolid(IBlockAccess world, int x, int y, int z, int side)
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

    @Optional.Method(modid = Mods.COFH_BLOCK_API_ID)
    @Override
    public ArrayList<ItemStack> dismantleBlock(EntityPlayer player, World world, int x,
                                    int y, int z, boolean returnBlock)
    {
        int meta = world.getBlockMetadata(x, y, z);

        ArrayList<ItemStack> items = this.getDrops(world, x, y, z, meta, 0);

        for (ItemStack item : items)
        {
            IOHelper.spawnItemInWorld(world, item, x, y, z);
        }

        world.setBlockToAir(x, y, z);
        return null;
    }

    @Optional.Method(modid = Mods.COFH_BLOCK_API_ID)
    @Override
    public boolean canDismantle(EntityPlayer player, World world, int x, int y, int z)
    {
        return true;
    }

    @Override
    public boolean rotateBlock(World worldObj, int x, int y, int z, ForgeDirection axis)
    {
        TileBlockExtender tile = (TileBlockExtender) worldObj.getTileEntity(x, y, z);
        return tile.rotateBlock();
    }

    // Start block disguise delegation functions

    @Override
    public int getRenderType()
    {
        // this ideally would get the disguise's render type
        // but this func doesn't have the world, x, y, z params needed to get the TileBlockExtender
        // so just return the 'standard' type
        return 0;
    }

    @Override
    public int colorMultiplier(IBlockAccess world, int x, int y, int z)
    {
        TileEntity tileEntity = world.getTileEntity(x, y, z);
        if (tileEntity != null && tileEntity instanceof TileBlockExtender)
        {
            TileBlockExtender tile = (TileBlockExtender) tileEntity;
            Block blockDisguisedAs = tile.getDisguise();
            if (blockDisguisedAs != null)
                return blockDisguisedAs.colorMultiplier(world, x, y, z);
        }
        return super.colorMultiplier(world, x, y, z);
    }

    @Override
    public IIcon getIcon(IBlockAccess world, int x, int y, int z, int side)
    {
        TileEntity tileEntity = world.getTileEntity(x, y, z);
        if (tileEntity != null && tileEntity instanceof TileBlockExtender)
        {
            TileBlockExtender tile = (TileBlockExtender) tileEntity;
            Block blockDisguisedAs = tile.getDisguise();
            int disguisedMeta = tile.blockDisguisedMetadata;
            if (blockDisguisedAs != null)
                return blockDisguisedAs.getIcon(side, disguisedMeta);
        }
        return super.getIcon(world, x, y, z, side);
    }

    @Override
    public boolean shouldSideBeRendered(IBlockAccess world, int x, int y, int z, int side)
    {
        // translate the coordinates back to the BlockExtender, since they get sent offset for some reason
        ForgeDirection dir = ForgeDirection.getOrientation(side);

        TileEntity tileEntity = DirectionHelper.getTileAtSide(world, x, y, z, dir.getOpposite());
        if (tileEntity != null && tileEntity instanceof TileBlockExtender)
        {
            TileBlockExtender tile = (TileBlockExtender) tileEntity;

            if (tile.getDisguise() != null)
                return dir == tile.getConnectedDirection();
        }
        return false;
    }

    // End block disguise delegation functions
}
