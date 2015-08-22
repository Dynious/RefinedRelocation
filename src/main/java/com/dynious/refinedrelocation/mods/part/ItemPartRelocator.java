package com.dynious.refinedrelocation.mods.part;

import codechicken.lib.vec.BlockCoord;
import codechicken.lib.vec.Vector3;
import codechicken.multipart.JItemMultiPart;
import codechicken.multipart.TMultiPart;
import com.dynious.refinedrelocation.RefinedRelocation;
import com.dynious.refinedrelocation.lib.Names;
import com.dynious.refinedrelocation.lib.Strings;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import org.lwjgl.input.Keyboard;

import java.util.List;

public class ItemPartRelocator extends JItemMultiPart
{
    public ItemPartRelocator()
    {
        super();
        this.setUnlocalizedName(Names.relocator);
        this.setCreativeTab(RefinedRelocation.tabRefinedRelocation);
    }

    @Override
    public TMultiPart newPart(ItemStack itemStack, EntityPlayer player, World world, BlockCoord blockCoord, int i, Vector3 vector3)
    {
        return PartFactory.INSTANCE.createPart("tile." + Names.relocator, false);
    }

    @Override
    public void addInformation(ItemStack itemStack, EntityPlayer player, List list, boolean flag)
    {
        super.addInformation(itemStack, player, list, flag);

        if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT))
        {
            String[] tooltipLines = StatCollector.translateToLocal(getUnlocalizedName(itemStack) + ".tooltip").split("\\\\n");
            for (String s : tooltipLines)
            {
                list.add("\u00a73" + s);
            }
        } else {
            list.add("\u00a76" + StatCollector.translateToLocal(Strings.TOOLTIP_SHIFT));
        }
    }
}
