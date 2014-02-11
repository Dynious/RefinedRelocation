package com.dynious.blex.tileentity;

public interface IRedstoneTransmitter
{
    public boolean isRedstoneTransmissionEnabled();

    public void setRedstoneTransmissionEnabled(boolean state);

    public boolean isRedstoneTransmissionActive();
    
    public void setRedstoneTransmissionActive(boolean state);
}
