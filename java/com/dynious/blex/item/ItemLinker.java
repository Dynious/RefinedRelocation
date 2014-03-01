package com.dynious.blex.item;

import com.dynious.blex.BlockExtenders;
import com.dynious.blex.helper.BlockHelper;
import com.dynious.blex.lib.Names;
import com.dynious.blex.lib.Resources;
import com.dynious.blex.tileentity.IDisguisable;
import com.dynious.blex.tileentity.TileWirelessBlockExtender;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatMessageComponent;
import net.minecraft.util.Icon;
import net.minecraft.world.World;

import java.util.List;

public class ItemLinker extends Item
{
    private Icon unlinkedIcon;
    private Icon linkedIcon;

    public ItemLinker(int id)
    {
        super(id);
        this.setUnlocalizedName(Names.linker);
        this.setCreativeTab(BlockExtenders.tabBlEx);
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
            list.add("Linked position: " + x + ":" + y + ":" + z + " (" + BlockHelper.getBlockDisplayName(par2EntityPlayer.getEntityWorld(), x, y, z) + ")");
        }
        else
        {
            list.add("Unlinked");
        }
    }

    @Override
    public boolean onItemUseFirst(ItemStack itemStack, EntityPlayer entityPlayer, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ)
    {
        if (entityPlayer.isSneaking())
            return false;

        TileEntity tile = world.getBlockTileEntity(x, y, z);
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
                int linkedBlockId = world.getBlockId(linkedX, linkedY, linkedZ);
                int linkedBlockMetadata = world.getBlockMetadata(linkedX, linkedY, linkedZ);
                Block linkedBlock = Block.blocksList[linkedBlockId];
                TileEntity linkedTile = world.getBlockTileEntity(linkedX, linkedY, linkedZ);
                if (linkedBlock != null && disguisable.canDisguiseAs(linkedBlock, linkedBlockMetadata))
                {
                    if (linkedTile != null && linkedTile instanceof IDisguisable)
                    {
                        linkedBlock = ((IDisguisable)linkedTile).getDisguise();
                        if (linkedBlock == null)
                        {
                            if (world.isRemote)
                                entityPlayer.sendChatToPlayer(new ChatMessageComponent()
                                        .addText("Can not disguise as " + BlockHelper.getBlockDisplayName(world, linkedX, linkedY, linkedZ)));
                            return false;
                        }
                        linkedBlockMetadata = ((IDisguisable)linkedTile).getDisguiseMeta();
                    }
                    disguisable.setDisguise(linkedBlock, linkedBlockMetadata);
                    if (world.isRemote)
                        entityPlayer.sendChatToPlayer(new ChatMessageComponent()
                                .addText("Disguised " + BlockHelper.getBlockDisplayName(world, x, y, z) + " as " + BlockHelper.getBlockDisplayName(world, linkedX, linkedY, linkedZ)));
                }
                else
                {
                    if (world.isRemote)
                        entityPlayer.sendChatToPlayer(new ChatMessageComponent()
                                .addText("Can not disguise as " + BlockHelper.getBlockDisplayName(world, linkedX, linkedY, linkedZ)));
                }
            }
            else if (disguisable.getDisguise() != null)
            {
                disguisable.clearDisguise();
                if (world.isRemote)
                    entityPlayer.sendChatToPlayer(new ChatMessageComponent()
                            .addText("Undisguised " + BlockHelper.getBlockDisplayName(world, x, y, z)));
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
        TileEntity tile = world.getBlockTileEntity(x, y, z);
        if (tile == null || (!(tile instanceof TileWirelessBlockExtender) && (!(tile instanceof IDisguisable) || entityPlayer.isSneaking())))
        {
            linkTileAtPosition(itemStack, x, y, z);
            if (world.isRemote)
                entityPlayer.sendChatToPlayer(new ChatMessageComponent()
                        .addText("Linker set to position " + x + ":" + y + ":" + z + " (" + BlockHelper.getBlockDisplayName(world, x, y, z) + ")"));
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
                entityPlayer.sendChatToPlayer(new ChatMessageComponent()
                        .addText("Linker is no longer linked"));
            return stack;
        }
        return super.onItemRightClick(stack, world, entityPlayer);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister par1IconRegister)
    {
        itemIcon = linkedIcon = par1IconRegister.registerIcon(Resources.MOD_ID + ":"
                + Names.linker);
        unlinkedIcon = par1IconRegister.registerIcon(Resources.MOD_ID + ":"
                + Names.linker + "Unlinked");
    }

    @Override
    public Icon getIcon(ItemStack stack, int pass)
    {
        return getIconIndex(stack);
    }

    @Override
    public Icon getIconIndex(ItemStack stack)
    {
        return isLinked(stack) ? linkedIcon : unlinkedIcon;
    }
}
