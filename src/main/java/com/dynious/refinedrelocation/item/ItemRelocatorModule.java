package com.dynious.refinedrelocation.item;

import com.dynious.refinedrelocation.RefinedRelocation;
import com.dynious.refinedrelocation.api.relocator.IRelocatorModule;
import com.dynious.refinedrelocation.api.item.IItemRelocatorModule;
import com.dynious.refinedrelocation.grid.relocator.*;
import com.dynious.refinedrelocation.lib.Names;
import com.dynious.refinedrelocation.lib.Resources;
import com.dynious.refinedrelocation.lib.Strings;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraft.util.StatCollector;

import java.util.List;

public class ItemRelocatorModule extends Item implements IItemRelocatorModule
{
    private Icon[] icons = new Icon[12];

    public ItemRelocatorModule(int id)
    {
        super(id);
        this.setUnlocalizedName(Names.relocatorModule);
        this.setHasSubtypes(true);
        this.setCreativeTab(RefinedRelocation.tabRefinedRelocation);
    }
    @Override
    public String getUnlocalizedName(ItemStack stack)
    {
        return getUnlocalizedName() + stack.getItemDamage();
    }

    @Override
    public int getMetadata(int meta)
    {
        return meta;
    }

    @Override
    public IRelocatorModule getRelocatorModule(ItemStack stack)
    {
        switch(stack.getItemDamage())
        {
            case 0:
                return null;
            case 1:
                return new RelocatorModuleFilter();
            case 2:
                return new RelocatorModuleOneWay();
            case 3:
                return new RelocatorModuleExtraction();
            case 4:
                return new RelocatorModuleBlockedExtraction();
            case 5:
                return new RelocatorModuleSneaky();
            case 6:
                return new RelocatorModuleStock();
            case 7:
                return new RelocatorModuleRedstoneBlock();
            case 8:
                return new RelocatorModuleSpread();
            case 9:
                return new RelocatorModuleItemDetector();
            case 10:
                return new RelocatorMultiModule();
            case 11:
                return new RelocatorModuleSneakyExtraction();
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void getSubItems(int par1, CreativeTabs par2CreativeTabs, List par3List)
    {
        for (int j = 0; j < 12; ++j)
        {
            par3List.add(new ItemStack(par1, 1, j));
        }
    }

    @Override
    public void addInformation(ItemStack itemStack, EntityPlayer player, List list, boolean b)
    {
        list.add(StatCollector.translateToLocal(Strings.RELOCATOR_MODULE));

        String[] info = StatCollector.translateToLocal(Strings.RELOCATOR_MODULE_INFO + itemStack.getItemDamage()).split("\\\\n");
        for (String line : info)
        {
            list.add("\u00A7f" + line);
        }
    }

    @Override
    public void registerIcons(IconRegister par1IconRegister)
    {
        for (int i = 0; i < icons.length; i++)
        {
            icons[i] = par1IconRegister.registerIcon(Resources.MOD_ID + ":" + Names.relocatorModule + i);
        }
    }

    @Override
    public Icon getIconFromDamage(int meta)
    {
        return meta < icons.length ? icons[meta] : null;
    }
}
