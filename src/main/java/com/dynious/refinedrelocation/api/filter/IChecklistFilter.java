package com.dynious.refinedrelocation.api.filter;

public interface IChecklistFilter extends IMultiFilterChild
{
    String getName(int optionIndex);
    void setValue(int optionIndex, boolean value);
    boolean getValue(int optionIndex);
    int getOptionCount();
}
