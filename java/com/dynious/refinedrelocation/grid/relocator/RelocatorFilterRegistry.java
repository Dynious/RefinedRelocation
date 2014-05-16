package com.dynious.refinedrelocation.grid.relocator;

import com.dynious.refinedrelocation.api.APIUtils;
import com.dynious.refinedrelocation.api.filter.IRelocatorFilter;

import java.util.HashMap;
import java.util.Map;

public class RelocatorFilterRegistry
{
    private static Map<String, Class<? extends IRelocatorFilter>> filters = new HashMap<String, Class<? extends IRelocatorFilter>>();

    public static void add(String identifier, Class<? extends IRelocatorFilter> clazz) throws IllegalArgumentException
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

    public static IRelocatorFilter getFilter(String identifier)
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

    public static String getIdentifier(Class<? extends IRelocatorFilter> clazz)
    {
        for (Map.Entry<String, Class<? extends IRelocatorFilter>> e : filters.entrySet())
        {
            if (e.getValue().equals(clazz))
            {
                return e.getKey();
            }
        }

        return "";
    }

    public static void registerModFilters()
    {
        APIUtils.registerRelocatorFilter("filterStandard", RelocatorFilterStandard.class);
    }
}
