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
	 * 	�ɷ��������ݿ��ȡ���ݣ����뱾�����ݿⲢ�ҵ���reload()�����ڶ�ȡ�������첽��ȡ����Ҫ�ȴ�ʱ�䣬�������ڸú�����ֱ�ӵ���reload()�������ֳ�ͻ��
	 */
	public abstract void reDownload();
	
	/**
	 * ֱ�Ӹ��ݱ��ض�ȡ���ݿ����ݲ�ˢ�½��档
	 */
	public abstract void reLoad();

}
