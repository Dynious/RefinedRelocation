package com.dynious.refinedrelocation.grid.relocator;

import com.dynious.refinedrelocation.api.APIUtils;
import com.dynious.refinedrelocation.api.relocator.IRelocatorModule;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;

import java.util.HashMap;
import java.util.Map;

public class RelocatorModuleRegistry
{
    private static Map<String, Class<? extends IRelocatorModule>> filters = new HashMap<String, Class<? extends IRelocatorModule>>();

    public static void add(String identifier, Class<? extends IRelocatorModule> clazz) throws IllegalArgumentException
    {
        if (identifier == null || identifier.isEmpty() || clazz == null)
        {
            throw new IllegalArgumentException("Parameter is null or empty");
        }
        if (contains(identifier))
        {
            throw new IllegalArgumentException("Identifier already registered");
        }
        filters.put(identifier, clazz);
    }

    public static boolean contains(String identifier)
    {
        return filters.containsKey(identifier);
    }

    public static IRelocatorModule getFilter(String identifier)
    {
        if (filters.containsKey(identifier))
        {
            try
            {
                return filters.get(identifier).newInstance();
            }
            catch (InstantiationException e)
            {
                e.printStackTrace();
            }
            catch (IllegalAccessException e)
            {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static String getIdentifier(Class<? extends IRelocatorModule> clazz)
    {
        for (Map.Entry<String, Class<? extends IRelocatorModule>> e : filters.entrySet())
        {
            if (e.getValue().equals(clazz))
            {
                return e.getKey();
            }
        }

        return "";
    }

    public static void registerModules()
    {
        APIUtils.registerRelocatorFilter("filter", RelocatorModuleFilter.class);
        APIUtils.registerRelocatorFilter("oneWay", RelocatorModuleOneWay.class);
        APIUtils.registerRelocatorFilter("extraction", RelocatorModuleExtraction.class);
        APIUtils.registerRelocatorFilter("blockedExtraction", RelocatorModuleBlockedExtraction.class);
        APIUtils.registerRelocatorFilter("sneaky", RelocatorModuleSneaky.class);
        APIUtils.registerRelocatorFilter("stock", RelocatorModuleStock.class);
        APIUtils.registerRelocatorFilter("redstoneBlock", RelocatorModuleRedstoneBlock.class);
    }

    @SideOnly(Side.CLIENT)
    public static void registerIcons(IIconRegister register)
    {
        for (Class clazz : filters.values())
        {
            try
            {
                IRelocatorModule module = (IRelocatorModule) clazz.newInstance();
                module.registerIcons(register);
            }
            catch (InstantiationException e)
            {
                e.printStackTrace();
            }
            catch (IllegalAccessException e)
            {
                e.printStackTrace();
            }
        }
    }
}
