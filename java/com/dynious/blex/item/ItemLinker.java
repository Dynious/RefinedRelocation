package com.dynious.blex.item;

import com.dynious.blex.BlockExtenders;
import com.dynious.blex.helper.BlockHelper;
import com.dynious.blex.lib.Names;
import com.dynious.blex.lib.Resources;
import com.dynious.blex.tileentity.TileBlockExtender;
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
import net.minecraft.world.World;

import java.util.List;

public class ItemLinker extends Item
{
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
        if (stack.hasTagCompound())
        {
            int x = stack.getTagCompound().getInteger("tileX");
            int y = stack.getTagCompound().getInteger("tileY");
            int z = stack.getTagCompound().getInteger("tileZ");
            list.add("Linked position: " + x + ":" + y + ":" + z + " (" + BlockHelper.getBlockDisplayName(par2EntityPlayer.getEntityWorld(), x, y, z) + ")");
        }
    }

    @Override
    public boolean onItemUseFirst(ItemStack itemStack, EntityPlayer entityPlayer, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ)
    {
        if (entityPlayer.isSneaking())
            return false;
        
        TileEntity tile = world.getBlockTileEntity(x, y, z);
        if (tile == null || !(tile instanceof TileWirelessBlockExtender))
        {
            if (tile != null && itemStack.hasTagCompound() && tile instanceof TileBlockExtender)
            {
                int linkedX = itemStack.getTagCompound().getInteger("tileX");
                int linkedY = itemStack.getTagCompound().getInteger("tileY");
                int linkedZ = itemStack.getTagCompound().getInteger("tileZ");
                int linkedBlockId = world.getBlockId(linkedX, linkedY, linkedZ);
                int linkedBlockMetadata = world.getBlockMetadata(linkedX, linkedY, linkedZ);
                Block linkedBlock = Block.blocksList[linkedBlockId];
                if (linkedBlock != null && linkedBlock.isOpaqueCube())
                {
                    ((TileBlockExtender) tile).setDisguise(linkedBlock, linkedBlockMetadata);
                }
                else
                {
                    if (world.isRemote)
                        entityPlayer.sendChatToPlayer(new ChatMessageComponent()
                                .addText("Can not disguise as " + BlockHelper.getBlockDisplayName(world, linkedX, linkedY, linkedZ) + " because it is not a solid cube"));
                }
                // if the client returns true here, the server doesn't call onItemUseFirst
                if (!world.isRemote)
                    return true;
            }
        }
        return false;
    }

    @Override
    public boolean onItemUse(ItemStack itemStack, EntityPlayer entityPlayer, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ)
    {
        TileEntity tile = world.getBlockTileEntity(x, y, z);
        if (tile == null || !(tile instanceof TileWirelessBlockExtender))
        {
            linkTileAtPosition(itemStack, x, y, z);
            if (world.isRemote)
                entityPlayer.sendChatToPlayer(new ChatMessageComponent()
                        .addText("Linker set to position " + x + ":" + y + ":" + z + " (" + BlockHelper.getBlockDisplayName(world, x, y, z) + ")"));

            return true;
        }
        return false;
    }

    private static void linkTileAtPosition(ItemStack stack, int x, int y, int z)
    {
        if (!stack.hasTagCompound())
        {
            stack.setTagCompound(new NBTTagCompound());
        }
        stack.getTagCompound().setInteger("tileX", x);
        stack.getTagCompound().setInteger("tileY", y);
        stack.getTagCompound().setInteger("tileZ", z);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister par1IconRegister)
    {
        itemIcon = par1IconRegister.registerIcon(Resources.MOD_ID + ":"
                + Names.linker);
    }
}
