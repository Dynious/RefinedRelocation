package com.dynious.refinedrelocation.grid;

import com.dynious.refinedrelocation.api.APIUtils;
import com.dynious.refinedrelocation.api.filter.IFilterModule;

import java.util.ArrayList;
import java.util.List;

public class FilterModuleRegistry
{
    private static List<RegisteredModule> modules = new ArrayList<RegisteredModule>();

    public static void add(String identifier, String displayName, Class<? extends IFilterModule> clazz) throws IllegalArgumentException
    {
        if (identifier == null || identifier.isEmpty() || clazz == null)
        {
            throw new IllegalArgumentException("Parameter is null or empty");
        }
        if (contains(identifier))
        {
            throw new IllegalArgumentException("Identifier already registered");
        }
        modules.add(new RegisteredModule(identifier, displayName, clazz));
    }

    public static IFilterModule getModule(String identifier)
    {
        if (contains(identifier))
        {
            try
            {
                return get(identifier).getClazz().newInstance();
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
        for (RegisteredModule module : modules)
        {
            if (module.clazz.equals(clazz))
            {
                return module.getIdentifier();
            }
        }

        return "";
    }

    public static String getName(int index)
    {
        return modules.get(index).getName();
    }

    public static void registerModules()
    {
        APIUtils.registerFilterModule("creativeTab", "CREATIVE SHIZZLE", FilterCreativeTabs.class);
    }

    public static boolean contains(String identifier)
    {
        for (RegisteredModule module : modules)
        {
            if (module.getIdentifier().equals(identifier))
                return true;
        }
        return false;
    }

    private static RegisteredModule get(String identifier)
    {
        for (RegisteredModule module : modules)
        {
            if (module.getIdentifier().equals(identifier))
                return module;
        }
        return null;
    }

    public static IFilterModule getNew(int index)
    {
        try
        {
            return modules.get(index).getClazz().newInstance();
        } catch (InstantiationException e)
        {
            e.printStackTrace();
        } catch (IllegalAccessException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public static int getSize()
    {
        return modules.size();
    }

    private static class RegisteredModule
    {
        private String identifier;
        private String name;
        private Class<? extends IFilterModule> clazz;

        public RegisteredModule(String identifier, String name, Class<? extends IFilterModule> clazz)
        {
            this.identifier = identifier;
            this.name = name;
            this.clazz = clazz;
        }

        public String getIdentifier()
        {
            return identifier;
        }

        public String getName()
        {
            return name;
        }

        public Class<? extends IFilterModule> getClazz()
        {
            return clazz;
        }
    }
}
