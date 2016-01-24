package com.dynious.refinedrelocation.item;

import com.dynious.refinedrelocation.lib.Strings;
import com.dynious.refinedrelocation.tileentity.TileSortingInterface;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import org.lwjgl.input.Keyboard;

import java.util.List;

public class ItemBlockSortingConnector extends ItemBlock {

    public ItemBlockSortingConnector(Block block) {
        super(block);
        this.setHasSubtypes(true);
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        return getUnlocalizedName() + stack.getItemDamage();
    }

    @Override
    public int getMetadata(int meta) {
        return meta;
    }

    @Override
    public boolean placeBlockAt(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ, int metadata) {
        if (!super.placeBlockAt(stack, player, world, x, y, z, side, hitX, hitY, hitZ, metadata)) {
            return false;
        }
        TileEntity tile = world.getTileEntity(x, y, z);
        if (tile != null && tile instanceof TileSortingInterface) {
            ((TileSortingInterface) tile).setConnectedSide(ForgeDirection.getOrientation(side).getOpposite());
        }
        return true;
    }

    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack itemStack, EntityPlayer entityPlayer, List list, boolean flag) {
        if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)) {
            String[] tooltipLines = StatCollector.translateToLocal(getUnlocalizedName(itemStack) + ".tooltip").split("\\\\n");
            for (String s : tooltipLines) {
                list.add("\u00a73" + s);
            }
        } else {
            list.add("\u00a76" + StatCollector.translateToLocal(Strings.TOOLTIP_SHIFT));
        }
    }

}
