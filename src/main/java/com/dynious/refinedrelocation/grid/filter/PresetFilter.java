package com.dynious.refinedrelocation.grid.filter;

import com.dynious.refinedrelocation.grid.MultiFilter;
import com.dynious.refinedrelocation.lib.Strings;
import net.minecraft.block.Block;
import net.minecraft.init.Items;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.IPlantable;

public class PresetFilter extends AbstractFilter {

    public static final int PRESET_COUNT = 14;

    private boolean[] presets = new boolean[PRESET_COUNT];

    public PresetFilter() {
        super(TYPE_PRESET);
    }

    @Override
    public boolean isInFilter(ItemStack itemStack) {
        String[] oreNames = MultiFilter.getOreNames(itemStack);
        for (String oreName : oreNames) {
            if (presets[0] && (oreName.contains("ingot") || itemStack.getItem() == Items.iron_ingot || itemStack.getItem() == Items.gold_ingot))
                return true;
            if (presets[1] && oreName.contains("ore"))
                return true;
            if (presets[2] && oreName.contains("log"))
                return true;
            if (presets[3] && oreName.contains("plank"))
                return true;
            if (presets[4] && oreName.contains("dust"))
                return true;
            if (presets[5] && oreName.contains("crushed") && !oreName.contains("purified"))
                return true;
            if (presets[6] && oreName.contains("purified"))
                return true;
            if (presets[7] && oreName.contains("plate"))
                return true;
            if (presets[8] && oreName.contains("gem"))
                return true;
            if (presets[10] && oreName.contains("dye"))
                return true;
            if (presets[11] && oreName.contains("nugget"))
                return true;
        }
        if (presets[9] && itemStack.getItem() instanceof ItemFood)
            return true;
        if (presets[12] && itemStack.getItem() instanceof ItemBlock && Block.getBlockFromItem(itemStack.getItem()) instanceof IPlantable)
            return true;
        if (presets[13] && TileEntityFurnace.getItemBurnTime(itemStack) > 0)
            return true;
        return false;
    }

    @Override
    public void writeToNBT(NBTTagCompound compound) {
        byte[] byteArray = new byte[presets.length];
        for(int i = 0; i < byteArray.length; i++) {
            byteArray[i] = (byte) (presets[i] ? 1 : 0);
        }
        compound.setByteArray("activePresets", byteArray);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        byte[] byteArray = compound.getByteArray("activePresets");
        for(int i = 0; i < byteArray.length; i++) {
            presets[i] = (byteArray[i] == 1);
        }
    }

    public String getName(int index) {
        switch (index) {
            case 0:
                return StatCollector.translateToLocal(Strings.INGOT_FILTER);
            case 1:
                return StatCollector.translateToLocal(Strings.ORE_FILTER);
            case 2:
                return StatCollector.translateToLocal(Strings.LOG_FILTER);
            case 3:
                return StatCollector.translateToLocal(Strings.PLANK_FILTER);
            case 4:
                return StatCollector.translateToLocal(Strings.DUST_FILTER);
            case 5:
                return StatCollector.translateToLocal(Strings.CRUSHED_ORE_FILTER);
            case 6:
                return StatCollector.translateToLocal(Strings.PURIFIED_ORE_FILTER);
            case 7:
                return StatCollector.translateToLocal(Strings.PLATE_FILTER);
            case 8:
                return StatCollector.translateToLocal(Strings.GEM_FILTER);
            case 9:
                return StatCollector.translateToLocal(Strings.FOOD_FILTER);
            case 10:
                return StatCollector.translateToLocal(Strings.DYE_FILTER);
            case 11:
                return StatCollector.translateToLocal(Strings.NUGGET_FILTER);
            case 12:
                return StatCollector.translateToLocal(Strings.PLANT_FILTER);
            case 13:
                return StatCollector.translateToLocal(Strings.FUEL_FILTER);
        }
        return null;
    }

    public void setPresetActive(int index, boolean active) {
        presets[index] = active;
    }

    public boolean isPresetActive(int index) {
        return presets[index];
    }
}
