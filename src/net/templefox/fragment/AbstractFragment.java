package net.templefox.fragment;

import android.app.Fragment;

public abstract class AbstractFragment extends Fragment {
	private boolean first = true;
	
	/**
	 * Surround the block that only run once at the first.
	 * @return true if is first time here, or false.
	 */
	@Deprecated
	protected boolean firstIn()
	{
		boolean result = first;
		first = false;
		return result;
	}
	
	/**
	 * Download online data, which take time. After download finished, please call load() manually.
	 */
	@Deprecated
	public abstract void download();
	
	/**
	 * Load local data.
	 */
	@Deprecated
	public abstract void load();

	
}
