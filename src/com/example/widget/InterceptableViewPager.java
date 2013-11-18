package com.example.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class InterceptableViewPager extends ViewPager {
	private boolean isIntercept = true;
	private Context context;
	
	public InterceptableViewPager(Context context) {
		super(context);
		this.context =context;
	}

	public InterceptableViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent arg0) {
		// TODO Auto-generated method stub
		if(isIntercept)
			return super.onInterceptTouchEvent(arg0);
    	else
            return false;
	}
	
    /**
     * ����ViewPager�Ƿ����ص���¼�
     * @param value if true, ViewPager���ص���¼�
     *                                 if false, ViewPager�����ܻ�����ViewPager����View���Ի�õ���¼�
     *                                 ��Ҫ��Ӱ��ĵ���¼�Ϊ���򻬶�
     *
     */
	public void setInterceptable(boolean isIntercept) {
		this.isIntercept = isIntercept;
	}

}
