package com.dynious.refinedrelocation.api.filter;

import net.minecraft.nbt.NBTTagCompound;

import java.util.List;

/**
 * @deprecated This interface will be renamed to IMultiFilter during the update to Minecraft 1.8. We already provide a dummy interface named like that which currently just extends this, so consider using that instead.
 */
@Deprecated
public interface IFilterGUI extends IFilter {
	boolean isBlacklisting();

	void setBlacklists(boolean blacklists);

	List<String> getWAILAInformation(NBTTagCompound compound);

	void filterChanged();

	void writeToNBT(NBTTagCompound compound);

	void readFromNBT(NBTTagCompound compound);

	boolean isDirty();

	void markDirty(boolean dirty);

	int getFilterCount();

	IMultiFilterChild getFilterAtIndex(int index);

	void setFilterType(int filterIndex, String filterType);
}
