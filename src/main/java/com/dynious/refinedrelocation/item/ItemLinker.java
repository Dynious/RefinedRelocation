package com.dynious.refinedrelocation.item;

import com.dynious.refinedrelocation.RefinedRelocation;
import com.dynious.refinedrelocation.api.tileentity.IFilterTileGUI;
import com.dynious.refinedrelocation.helper.BlockHelper;
import com.dynious.refinedrelocation.helper.DistanceHelper;
import com.dynious.refinedrelocation.lib.Names;
import com.dynious.refinedrelocation.lib.Resources;
import com.dynious.refinedrelocation.lib.Strings;
import com.dynious.refinedrelocation.tileentity.IDisguisable;
import com.dynious.refinedrelocation.tileentity.ILinkable;
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
import org.lwjgl.input.Keyboard;

import java.util.List;

public class ItemLinker extends Item {

    private IIcon iconUnlinked;
    private IIcon iconLink;
    private IIcon iconCopy;

    public ItemLinker() {
        setUnlocalizedName(Names.linker);
        setCreativeTab(RefinedRelocation.tabRefinedRelocation);
        setMaxStackSize(1);
    }

    @Override
    public String getUnlocalizedName(ItemStack itemStack) {
        return "item." + Names.linker + getMode(itemStack);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Override
    public void addInformation(ItemStack itemStack, EntityPlayer entityPlayer, List list, boolean flag) {
        if (isLinked(itemStack)) {
            NBTTagCompound linkCompound = itemStack.getTagCompound().getCompoundTag("Link");
            int linkedX = linkCompound.getInteger("tileX");
            int linkedY = linkCompound.getInteger("tileY");
            int linkedZ = linkCompound.getInteger("tileZ");
            list.add("\u00a7a" + StatCollector.translateToLocal(Strings.LINKED_POS) + " \u00a7f" + linkedX + ", " + linkedY + ", " + linkedZ + " (" + BlockHelper.getBlockDisplayName(entityPlayer.getEntityWorld(), linkedX, linkedY, linkedZ) + ")");
        } else {
            list.add(StatCollector.translateToLocal(Strings.UNLINKED));
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)) {
            String[] tooltipLines = StatCollector.translateToLocal(getUnlocalizedName() + ".tooltip").split("\\\\n");
            for (String s : tooltipLines) {
                list.add("\u00a73" + s);
            }
        } else {
            list.add("\u00a76" + StatCollector.translateToLocal(Strings.TOOLTIP_SHIFT));
        }
    }

    @Override
    public boolean onItemUseFirst(ItemStack itemStack, EntityPlayer entityPlayer, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
        if(world.isRemote) {
            return false;
        }
        if (entityPlayer.isSneaking()) {
            return false;
        }

        TileEntity tileEntity = world.getTileEntity(x, y, z);
        int mode = getMode(itemStack);
        if(mode == 0) {
            if(tileEntity instanceof IDisguisable && ((IDisguisable) tileEntity).canDisguise()) {
                IDisguisable disguisable = (IDisguisable) tileEntity;
                if (isLinked(itemStack)) {
                    NBTTagCompound linkCompound = itemStack.getTagCompound().getCompoundTag("Link");
                    int linkedX = linkCompound.getInteger("tileX");
                    int linkedY = linkCompound.getInteger("tileY");
                    int linkedZ = linkCompound.getInteger("tileZ");
                    int linkedBlockMetadata = world.getBlockMetadata(linkedX, linkedY, linkedZ);
                    Block linkedBlock = world.getBlock(linkedX, linkedY, linkedZ);
                    TileEntity linkedTile = world.getTileEntity(linkedX, linkedY, linkedZ);
                    if (linkedTile != null && linkedTile instanceof IDisguisable) {
                        linkedBlock = ((IDisguisable) linkedTile).getDisguise();
                        linkedBlockMetadata = ((IDisguisable) linkedTile).getDisguiseMeta();
                    }
                    if (linkedBlock != null && disguisable.canDisguiseAs(linkedBlock, linkedBlockMetadata)) {
                        disguisable.setDisguise(linkedBlock, linkedBlockMetadata);
                        entityPlayer.addChatComponentMessage(new ChatComponentText((StatCollector.translateToLocalFormatted(Strings.DISGUISED_AS, BlockHelper.getBlockDisplayName(world, x, y, z), BlockHelper.getBlockDisplayName(linkedBlock, linkedBlockMetadata)))));
                    } else {
                        entityPlayer.addChatComponentMessage(new ChatComponentText((StatCollector.translateToLocalFormatted(Strings.CANNOT_DISGUISE_AS, BlockHelper.getBlockDisplayName(world, linkedX, linkedY, linkedZ)))));
                    }
                    return true;
                } else if (disguisable.getDisguise() != null) {
                    disguisable.clearDisguise();
                    entityPlayer.addChatComponentMessage(new ChatComponentText((StatCollector.translateToLocal(Strings.UNDISGUISED) + " " + BlockHelper.getBlockDisplayName(world, x, y, z))));
                    return true;
                }
            } else if(tileEntity instanceof ILinkable) {
                ILinkable linkable = (ILinkable) tileEntity;
                if (isLinked(itemStack)) {
                    NBTTagCompound linkCompound = itemStack.getTagCompound().getCompoundTag("Link");
                    int linkedX = linkCompound.getInteger("tileX");
                    int linkedY = linkCompound.getInteger("tileY");
                    int linkedZ = linkCompound.getInteger("tileZ");
                    if(x == linkedX && y == linkedY && z == linkedZ) {
                        entityPlayer.addChatComponentMessage(new ChatComponentText(StatCollector.translateToLocal(Strings.NO_LINK_TO_SELF)));
                        return true;
                    }
                    if (linkable.getMaxLinkRange() > 0 && DistanceHelper.getDistanceSq(x, y, z, linkedX, linkedY, linkedZ) > Math.pow(linkable.getMaxLinkRange(), 2)) {
                        String tileDisplayName = BlockHelper.getTileEntityDisplayName(tileEntity);
                        entityPlayer.addChatComponentMessage(new ChatComponentText(StatCollector.translateToLocalFormatted(Strings.TOO_FAR, tileDisplayName, linkable.getMaxLinkRange())));
                        return true;
                    }
                    linkable.linkTo(world, x, y, z, entityPlayer);
                    String tileDisplayName = BlockHelper.getTileEntityDisplayName(tileEntity);
                    String blockDisplayName = BlockHelper.getBlockDisplayName(world, linkedX, linkedY, linkedZ);
                    entityPlayer.addChatComponentMessage(new ChatComponentText(StatCollector.translateToLocalFormatted(Strings.LINKED_WITH, tileDisplayName, blockDisplayName, linkedX, linkedY, linkedZ)));
                    return true;
                } else if(linkable.isLinked()) {
                    linkable.clearLink(entityPlayer);
                    String tileDisplayName = BlockHelper.getTileEntityDisplayName(tileEntity);
                    entityPlayer.addChatComponentMessage(new ChatComponentText(StatCollector.translateToLocalFormatted(Strings.NO_LONGER_LINKED, tileDisplayName)));
                    return true;
                } else {
                    entityPlayer.addChatComponentMessage(new ChatComponentText((StatCollector.translateToLocal(Strings.NO_LINK))));
                    return true;
                }
            }
            return true;
        } else if(mode == 1) {
            if(!(tileEntity instanceof IFilterTileGUI)) {
                return false;
            }
            IFilterTileGUI multiFilter = (IFilterTileGUI) tileEntity;
            if(isLinked(itemStack)) {
                if(multiFilter.getFilter().getFilterCount() > 0) {
                    entityPlayer.addChatComponentMessage(new ChatComponentText((StatCollector.translateToLocal(Strings.CANNOT_COPY_NOT_EMPTY))));
                    return true;
                }
                NBTTagCompound linkCompound = itemStack.getTagCompound().getCompoundTag("Link");
                int linkedX = linkCompound.getInteger("tileX");
                int linkedY = linkCompound.getInteger("tileY");
                int linkedZ = linkCompound.getInteger("tileZ");
                TileEntity linkedTile = world.getTileEntity(linkedX, linkedY, linkedZ);
                if (!(linkedTile instanceof IFilterTileGUI)) {
                    entityPlayer.addChatComponentMessage(new ChatComponentText((StatCollector.translateToLocal(Strings.CANNOT_COPY_NO_LINK))));
                    unlink(itemStack);
                    return true;
                }
                IFilterTileGUI linkedMultiFilter = (IFilterTileGUI) linkedTile;
                NBTTagCompound copyCompound = new NBTTagCompound();
                linkedMultiFilter.getFilter().writeToNBT(copyCompound);
                multiFilter.getFilter().readFromNBT(copyCompound);
                entityPlayer.addChatComponentMessage(new ChatComponentText((StatCollector.translateToLocalFormatted(Strings.COPIED_FROM, BlockHelper.getBlockDisplayName(world, linkedX, linkedY, linkedZ), BlockHelper.getBlockDisplayName(world, x, y, z)))));
                return true;
            } else {
                entityPlayer.addChatComponentMessage(new ChatComponentText((StatCollector.translateToLocalFormatted(Strings.CANNOT_COPY_NO_LINK))));
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean onItemUse(ItemStack itemStack, EntityPlayer entityPlayer, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
        if(world.isRemote) {
           return true;
        }
        if(!entityPlayer.isSneaking()) {
            return true;
        }
        TileEntity tile = world.getTileEntity(x, y, z);
        int mode = getMode(itemStack);
        if(mode == 0) {
            if (tile instanceof TileWirelessBlockExtender) {
                return true;
            }
        } else if(mode == 1) {
            if(!(tile instanceof IFilterTileGUI)) {
                entityPlayer.addChatComponentMessage(new ChatComponentText((StatCollector.translateToLocalFormatted(Strings.CANNOT_COPY_INVALID, x, y, z, BlockHelper.getBlockDisplayName(world, x, y, z)))));
                return true;
            }
            if(((IFilterTileGUI) tile).getFilter().getFilterCount() == 0) {
                entityPlayer.addChatComponentMessage(new ChatComponentText((StatCollector.translateToLocalFormatted(Strings.CANNOT_COPY_INVALID, x, y, z, BlockHelper.getBlockDisplayName(world, x, y, z)))));
                return true;
            }
        }
        linkTileAtPosition(itemStack, x, y, z);
        entityPlayer.addChatComponentMessage(new ChatComponentText((StatCollector.translateToLocalFormatted(Strings.LINKER_SET, x, y, z, BlockHelper.getBlockDisplayName(world, x, y, z)))));
        return true;
    }

    private void linkTileAtPosition(ItemStack itemStack, int x, int y, int z) {
        if (!itemStack.hasTagCompound()) {
            itemStack.setTagCompound(new NBTTagCompound());
        }
        NBTTagCompound tagCompound = new NBTTagCompound();
        tagCompound.setInteger("tileX", x);
        tagCompound.setInteger("tileY", y);
        tagCompound.setInteger("tileZ", z);
        itemStack.getTagCompound().setTag("Link", tagCompound);
    }

    private void setMode(ItemStack itemStack, int mode) {
        if(!itemStack.hasTagCompound()) {
            itemStack.setTagCompound(new NBTTagCompound());
        }
        itemStack.getTagCompound().setByte("Mode", (byte) mode);
    }

    private int getMode(ItemStack itemStack) {
        if(itemStack.hasTagCompound()) {
            return itemStack.getTagCompound().getByte("Mode");
        }
        return 0;
    }

    private void unlink(ItemStack itemStack) {
        if (itemStack.hasTagCompound()) {
            itemStack.getTagCompound().removeTag("Link");
        }
    }

    public boolean isLinked(ItemStack itemStack) {
        return itemStack.hasTagCompound() && itemStack.getTagCompound().hasKey("Link");
    }

    @Override
    public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer entityPlayer) {
        if(entityPlayer.isSneaking()) {
            ItemStack newItemStack = itemStack.copy();
            unlink(newItemStack);
            switch(getMode(newItemStack)) {
                case 0: setMode(newItemStack, 1); break;
                case 1: setMode(newItemStack, 0); break;
            }
            return newItemStack;
        }
        if (isLinked(itemStack)) {
            entityPlayer.swingItem();
            unlink(itemStack);
            if (world.isRemote) {
                entityPlayer.addChatComponentMessage(new ChatComponentText(StatCollector.translateToLocalFormatted(Strings.NO_LONGER_LINKED, this.getItemStackDisplayName(itemStack))));
            }
            return itemStack;
        }
        return itemStack;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister iconRegister) {
        itemIcon = iconLink = iconRegister.registerIcon(Resources.MOD_ID + ":" + Names.linker + "_link");
        iconUnlinked = iconRegister.registerIcon(Resources.MOD_ID + ":" + Names.linker + "_unlinked");
        iconCopy = iconRegister.registerIcon(Resources.MOD_ID + ":" + Names.linker + "_copy");
    }

    @Override
    public IIcon getIcon(ItemStack itemStack, int pass) {
        return getIconIndex(itemStack);
    }

    @Override
    public IIcon getIconIndex(ItemStack itemStack) {
        switch(getMode(itemStack)) {
            case 0: return iconLink;
            case 1: return iconCopy;
        }
        return iconUnlinked;
    }
}
