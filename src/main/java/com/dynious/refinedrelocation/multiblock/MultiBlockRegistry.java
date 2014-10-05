package com.dynious.refinedrelocation.multiblock;

import java.util.HashMap;
import java.util.Map;

public class MultiBlockRegistry
{
    private static Map<String, IMultiBlock> multiBlockList = new HashMap<String, IMultiBlock>();

    public static void registerMultiBlock(IMultiBlock multiBlock, String identifier)
    {
        assert multiBlock != null : "registerMultiBlock: MultiBlock cannot be null";
        assert (identifier != null && !identifier.isEmpty()) : "registerMultiBlock: identifier cannot be null";
        assert (!multiBlockList.containsKey(identifier)) : "registerMultiBlock: Identifier already registered";

        multiBlockList.put(identifier, multiBlock);
    }

    public static IMultiBlock getMultiBlock(String identifier)
    {
        return multiBlockList.get(identifier);
    }
}
