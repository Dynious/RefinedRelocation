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
    private static Map<String, Class<? extends IRelocatorModule>> modules = new HashMap<String, Class<? extends IRelocatorModule>>();

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
        modules.put(identifier, clazz);
    }

    public static boolean contains(String identifier)
    {
        return modules.containsKey(identifier);
    }

    public static IRelocatorModule getModule(String identifier)
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

    public static String getIdentifier(Class<? extends IRelocatorModule> clazz)
    {
        for (Map.Entry<String, Class<? extends IRelocatorModule>> e : modules.entrySet())
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
    }

    @SideOnly(Side.CLIENT)
    public static void registerIcons(IIconRegister register)
    {
        for (Class clazz : modules.values())
        {
            try
            {
                IRelocatorModule module = (IRelocatorModule) clazz.newInstance();
                module.registerIcons(register);
            } catch (InstantiationException e)
            {
                e.printStackTrace();
            } catch (IllegalAccessException e)
            {
                e.printStackTrace();
            }
        }
    }
}
