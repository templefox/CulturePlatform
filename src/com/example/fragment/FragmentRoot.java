package com.example.fragment;

import android.app.Fragment;

public abstract class FragmentRoot extends Fragment {
	private boolean first = true;
	
	protected boolean fisrtIn()
	{
		boolean result = first;
		first = false;
		return result;
	}
	
	/**
	 * 	由服务器数据库读取内容，存入本地数据库并且调用reload()。由于读取内容是异步读取，需要等待时间，所以若在该函数后直接调用reload()，将出现冲突。
	 */
	public abstract void reDownload();
	
	/**
	 * 直接根据本地读取数据库内容并刷新界面。
	 */
	public abstract void reLoad();

}
