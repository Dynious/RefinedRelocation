package com.dynious.refinedrelocation.helper;

public enum EnergyType
{
    MJ(5.0F),
    EU(2.0F),
    RF(0.5F),
    KJ(0.04F);

    private float conversionRate;

    private EnergyType(float conversionRate)
    {
        this.conversionRate = conversionRate;
    }

    public double convertToInternal(double amount)
    {
        return amount * conversionRate;
    }

    public double convertTo(double amount, EnergyType toType)
    {
        return amount * conversionRate / toType.conversionRate;
    }

    public double fromInternal(double amount)
    {
        return amount / conversionRate;
    }
}
