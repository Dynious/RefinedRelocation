package com.dynious.refinedrelocation.grid.filter;

import com.dynious.refinedrelocation.api.APIUtils;
import com.dynious.refinedrelocation.api.filter.IMultiFilterChild;
import com.dynious.refinedrelocation.api.relocator.IRelocatorModule;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class MultiFilterRegistry
{

    private static Map<String, Class<? extends IMultiFilterChild>> filters = new HashMap<String, Class<? extends IMultiFilterChild>>();

    public static void add(String identifier, Class<? extends IMultiFilterChild> clazz) throws IllegalArgumentException
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

    public static IMultiFilterChild getFilter(String identifier)
    {
        if (filters.containsKey(identifier))
        {
            try
            {
                return filters.get(identifier).newInstance();
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

    public static String getIdentifier(Class<? extends IMultiFilterChild> clazz)
    {
        for (Map.Entry<String, Class<? extends IMultiFilterChild>> e : filters.entrySet())
        {
            if (e.getValue().equals(clazz))
            {
                return e.getKey();
            }
        }

        return "";
    }

    public static void registerFilters()
    {
        APIUtils.registerMultiFilterChild(CreativeTabFilter.TYPE_NAME, CreativeTabFilter.class);
        APIUtils.registerMultiFilterChild(PresetFilter.TYPE_NAME, PresetFilter.class);
        APIUtils.registerMultiFilterChild(CustomUserFilter.TYPE_NAME, CustomUserFilter.class);
    }

    public static Collection<Class<? extends IMultiFilterChild>> getFilters()
    {
        return filters.values();
    }
}
