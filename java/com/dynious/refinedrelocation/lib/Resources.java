package com.dynious.refinedrelocation.lib;

import net.minecraft.util.ResourceLocation;

public class Resources
{
    public static final String MOD_ID = Reference.MOD_ID.toLowerCase();
    public static final String GUI_SHEET_LOCATION = "textures/gui/";
    public static final String MODEL_SHEET_LOCATION = "textures/model/";
    public static final String MODEL_LOCATION = "models/";

    public static final ResourceLocation GUI_ADVANCED_BLOCK_EXTENDER = new ResourceLocation(MOD_ID, GUI_SHEET_LOCATION + "advancedBlockExtender.png");
    public static final ResourceLocation GUI_FILTERED_BLOCK_EXTENDER = new ResourceLocation(MOD_ID, GUI_SHEET_LOCATION + "filteredBlockExtender.png");
    public static final ResourceLocation GUI_ADVANCED_FILTERED_BLOCK_EXTENDER = new ResourceLocation(MOD_ID, GUI_SHEET_LOCATION + "advancedFilteredBlockExtender.png");
    public static final ResourceLocation GUI_ADVANCED_BUFFER = new ResourceLocation(MOD_ID, GUI_SHEET_LOCATION + "advancedBuffer.png");
    public static final ResourceLocation GUI_SHARED = new ResourceLocation(MOD_ID, GUI_SHEET_LOCATION + "shared.png");
    public static final ResourceLocation GUI_SORTING_IMPORTER = new ResourceLocation(MOD_ID, GUI_SHEET_LOCATION + "sortingImporter.png");
    public static final ResourceLocation GUI_POWER_LIMITER = new ResourceLocation(MOD_ID, GUI_SHEET_LOCATION + "powerLimiter.png");

    public static final ResourceLocation MODEL_ENDERPEARL = new ResourceLocation(MOD_ID, MODEL_LOCATION + "enderPearl.obj");

    public static final ResourceLocation MODEL_TEXTURE_BLOCK_EXTENDER = new ResourceLocation(MOD_ID, MODEL_SHEET_LOCATION + "blockExtender0.png");
    public static final ResourceLocation MODEL_TEXTURE_ADVANCED_BLOCK_EXTENDER = new ResourceLocation(MOD_ID, MODEL_SHEET_LOCATION + "blockExtender1.png");
    public static final ResourceLocation MODEL_TEXTURE_FILTERED_BLOCK_EXTENDER = new ResourceLocation(MOD_ID, MODEL_SHEET_LOCATION + "blockExtender2.png");
    public static final ResourceLocation MODEL_TEXTURE_ADVANCED_FILTERED_BLOCK_EXTENDER = new ResourceLocation(MOD_ID, MODEL_SHEET_LOCATION + "blockExtender3.png");
    public static final ResourceLocation MODEL_TEXTURE_WIRELESS_BLOCK_EXTENDER = new ResourceLocation(MOD_ID, MODEL_SHEET_LOCATION + "blockExtender4.png");
    public static final ResourceLocation MODEL_TEXTURE_ENDERPEARL = new ResourceLocation(MOD_ID, MODEL_SHEET_LOCATION + "enderPearl.png");
    public static final ResourceLocation MODEL_TEXTURE_OVERLAY_CHEST =  new ResourceLocation(MOD_ID, MODEL_SHEET_LOCATION + "chestFilterOverlay.png");

    public static final ResourceLocation TEXTURE_BLUR = new ResourceLocation(MOD_ID, "textures/misc/blur.png");
}
