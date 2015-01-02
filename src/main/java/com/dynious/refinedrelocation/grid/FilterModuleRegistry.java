package com.dynious.refinedrelocation.grid;

import com.dynious.refinedrelocation.api.filter.IFilterModule;

import java.util.HashMap;
import java.util.Map;

public class FilterModuleRegistry
{
    private static Map<String, Class<? extends IFilterModule>> modules = new HashMap<String, Class<? extends IFilterModule>>();

    public static void add(String identifier, Class<? extends IFilterModule> clazz) throws IllegalArgumentException
    {
        if (identifier == null || identifier.isEmpty() || clazz == null)
        {
            throw new IllegalArgumentException("Parameter is null or empty");
        }
        if (contains(identifier))
        {
            throw new IllegalArgumentException("Identifier already registered");
        }
        modules.put(identifier, clazz);
    }

    public static boolean contains(String identifier)
    {
        return modules.containsKey(identifier);
    }

    public static IFilterModule getModule(String identifier)
    {
        if (modules.containsKey(identifier))
        {
            try
            {
                return modules.get(identifier).newInstance();
            } catch (InstantiationException e)
            {
                e.printStackTrace();
            } catch (IllegalAccessException e)
            {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static String getIdentifier(Class<? extends IFilterModule> clazz)
    {
        for (Map.Entry<String, Class<? extends IFilterModule>> e : modules.entrySet())
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
        /*
        APIUtils.registerRelocatorModule("filter", RelocatorModuleFilter.class);
        APIUtils.registerRelocatorModule("oneWay", RelocatorModuleOneWay.class);
        APIUtils.registerRelocatorModule("extraction", RelocatorModuleExtraction.class);
        APIUtils.registerRelocatorModule("blockedExtraction", RelocatorModuleBlockedExtraction.class);
        APIUtils.registerRelocatorModule("sneaky", RelocatorModuleSneaky.class);
        APIUtils.registerRelocatorModule("stock", RelocatorModuleStock.class);
        APIUtils.registerRelocatorModule("redstoneBlock", RelocatorModuleRedstoneBlock.class);
        APIUtils.registerRelocatorModule("spread", RelocatorModuleSpread.class);
        APIUtils.registerRelocatorModule("itemDetector", RelocatorModuleItemDetector.class);
        APIUtils.registerRelocatorModule("multiModule", RelocatorMultiModule.class);
        APIUtils.registerRelocatorModule("sneakyExtraction", RelocatorModuleSneakyExtraction.class);
        APIUtils.registerRelocatorModule("crafting", RelocatorModuleCrafting.class);
        */
    }
}
