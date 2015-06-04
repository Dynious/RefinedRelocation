package com.dynious.refinedrelocation.api.filter;

public interface IChecklistFilter
{

    String getFilterName();
    int getFilterIndex();
    String getName(int optionIndex);
    void setValue(int optionIndex, boolean value);
    boolean getValue(int optionIndex);
    int getOptionCount();

}
