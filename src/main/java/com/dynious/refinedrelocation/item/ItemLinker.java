package com.dynious.refinedrelocation.item;

import com.dynious.refinedrelocation.RefinedRelocation;
import com.dynious.refinedrelocation.helper.BlockHelper;
import com.dynious.refinedrelocation.lib.Names;
import com.dynious.refinedrelocation.lib.Resources;
import com.dynious.refinedrelocation.lib.Strings;
import com.dynious.refinedrelocation.tileentity.IDisguisable;
import com.dynious.refinedrelocation.tileentity.TileWirelessBlockExtender;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

import java.util.List;

public class ItemLinker extends Item
{
    private IIcon unlinkedIcon;
    private IIcon linkedIcon;

    public ItemLinker()
    {
        super();
        this.setUnlocalizedName(Names.linker);
        this.setCreativeTab(RefinedRelocation.tabRefinedRelocation);
        this.setMaxStackSize(1);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Override
    public void addInformation(ItemStack stack, EntityPlayer par2EntityPlayer,
                               List list, boolean par4)
    {
        if (isLinked(stack))
        {
            int x = stack.getTagCompound().getInteger("tileX");
            int y = stack.getTagCompound().getInteger("tileY");
            int z = stack.getTagCompound().getInteger("tileZ");
            list.add(StatCollector.translateToLocalFormatted(Strings.LINKED_POS, x, y, z, BlockHelper.getBlockDisplayName(par2EntityPlayer.getEntityWorld(), x, y, z)));
        }
        else
        {
            list.add(StatCollector.translateToLocal(Strings.UNLINKED));
        }
    }

    @Override
    public boolean onItemUseFirst(ItemStack itemStack, EntityPlayer entityPlayer, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ)
    {
        if (entityPlayer.isSneaking())
            return false;

        TileEntity tile = world.getTileEntity(x, y, z);
        if (tile != null && tile instanceof IDisguisable)
        {
            IDisguisable disguisable = (IDisguisable) tile;

            if (!disguisable.canDisguise())
            {
                return false;
            }

            if (isLinked(itemStack))
            {
                int linkedX = itemStack.getTagCompound().getInteger("tileX");
                int linkedY = itemStack.getTagCompound().getInteger("tileY");
                int linkedZ = itemStack.getTagCompound().getInteger("tileZ");
                int linkedBlockMetadata = world.getBlockMetadata(linkedX, linkedY, linkedZ);
                Block linkedBlock = world.getBlock(linkedX, linkedY, linkedZ);
                TileEntity linkedTile = world.getTileEntity(linkedX, linkedY, linkedZ);
                if (linkedTile != null && linkedTile instanceof IDisguisable)
                {
                    linkedBlock = ((IDisguisable)linkedTile).getDisguise();
                    linkedBlockMetadata = ((IDisguisable)linkedTile).getDisguiseMeta();
                }
                if (linkedBlock != null && disguisable.canDisguiseAs(linkedBlock, linkedBlockMetadata))
                {
                    disguisable.setDisguise(linkedBlock, linkedBlockMetadata);
                    if (world.isRemote)
                        entityPlayer.addChatComponentMessage(new ChatComponentText(
                                (StatCollector.translateToLocalFormatted(Strings.DISGUISED_AS, BlockHelper.getBlockDisplayName(world, x, y, z), BlockHelper.getBlockDisplayName(linkedBlock, linkedBlockMetadata)))));
                }
                else
                {
                    if (world.isRemote)
                        entityPlayer.addChatComponentMessage(new ChatComponentText(
                                (StatCollector.translateToLocalFormatted(Strings.CANNOT_DISGUISE_AS, BlockHelper.getBlockDisplayName(world, linkedX, linkedY, linkedZ)))));
                }
            }
            else if (disguisable.getDisguise() != null)
            {
                disguisable.clearDisguise();
                if (world.isRemote)
                    entityPlayer.addChatComponentMessage(new ChatComponentText(
                            (StatCollector.translateToLocal(Strings.UNDISGUISED) + " " + BlockHelper.getBlockDisplayName(world, x, y, z))));
            }
            else
            {
                return false;
            }

            // if the client returns true here, the server doesn't call onItemUseFirst
            if (!world.isRemote)
                return true;
        }
        return false;
    }

    @Override
    public boolean onItemUse(ItemStack itemStack, EntityPlayer entityPlayer, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ)
    {
        TileEntity tile = world.getTileEntity(x, y, z);
        if (tile == null || (!(tile instanceof TileWirelessBlockExtender) && (!(tile instanceof IDisguisable) || entityPlayer.isSneaking())))
        {
            linkTileAtPosition(itemStack, x, y, z);
            if (world.isRemote)
                entityPlayer.addChatComponentMessage(new ChatComponentText(
                        (StatCollector.translateToLocalFormatted(Strings.LINKER_SET, x, y, z, BlockHelper.getBlockDisplayName(world, x, y, z)))));
        }
        return true;
    }

    private void linkTileAtPosition(ItemStack stack, int x, int y, int z)
    {
        if (!stack.hasTagCompound())
        {
            stack.setTagCompound(new NBTTagCompound());
        }
        stack.getTagCompound().setInteger("tileX", x);
        stack.getTagCompound().setInteger("tileY", y);
        stack.getTagCompound().setInteger("tileZ", z);
    }

    private void unlink(ItemStack stack)
    {
        if (stack.hasTagCompound())
        {
            stack.setTagCompound(null);
        }
    }

    public boolean isLinked(ItemStack stack)
    {
        return stack.hasTagCompound();
    }

    @Override
    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer entityPlayer)
    {
        if (isLinked(stack))
        {
            entityPlayer.swingItem();
            unlink(stack);
            if (world.isRemote)
                entityPlayer.addChatComponentMessage(new ChatComponentText((StatCollector.translateToLocal(Strings.NO_LONGER_LINKED))));
            return stack;
        }
        return super.onItemRightClick(stack, world, entityPlayer);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister par1IconRegister)
    {
        itemIcon = linkedIcon = par1IconRegister.registerIcon(Resources.MOD_ID + ":"
                + Names.linker);
        unlinkedIcon = par1IconRegister.registerIcon(Resources.MOD_ID + ":"
                + Names.linker + "Unlinked");
    }

    @Override
    public IIcon getIcon(ItemStack stack, int pass)
    {
        return getIconIndex(stack);
    }

    @Override
    public IIcon getIconIndex(ItemStack stack)
    {
        return isLinked(stack) ? linkedIcon : unlinkedIcon;
    }
}
