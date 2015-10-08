package com.dynious.refinedrelocation.api;

import net.minecraft.item.ItemStack;

public class ModObjects
{
    /* Blocks */
    public static ItemStack blockExtender;
    public static ItemStack advancedBlockExtender;
    public static ItemStack filteredBlockExtender;
    public static ItemStack advancedFilteredBlockExtender;
    public static ItemStack wirelessBlockExtender;
    public static ItemStack buffer;
    public static ItemStack advancedBuffer;
    public static ItemStack filteredBuffer;
    public static ItemStack sortingChest;
    public static ItemStack sortingConnector;
    public static ItemStack sortingInterface;

    /**
     * @deprecated use sortingInputPane instead
     */
    @Deprecated
    public static ItemStack sortingImporter;
    public static ItemStack sortingInputPane;

    public static ItemStack filteringHopper;
    public static ItemStack relocationPortal;

    /**
     * @deprecated use playerRelocatorBase instead
     */
    @Deprecated
    public static ItemStack relocationController;
    public static ItemStack playerRelocatorBase;

    public static ItemStack powerLimiter;
    public static ItemStack relocator;

    /* Items */
    public static ItemStack linker;
    public static ItemStack sortingUpgrade;
    public static ItemStack playerRelocator;
    public static ItemStack relocatorModule;
    public static ItemStack toolbox;

    /* Mod Dependant Blocks (does not include metadata variants)*/
    public static ItemStack sortingIronChest;
    public static ItemStack sortingBarrel;
    
    /**
     * @deprecated no longer functional, pending remove in 1.8
     */
    @Deprecated
    public static ItemStack sortingAlchemicalChest;
}
