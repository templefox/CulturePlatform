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
     * 设置ViewPager是否拦截点击事件
     * @param value if true, ViewPager拦截点击事件
     *                                 if false, ViewPager将不能滑动，ViewPager的子View可以获得点击事件
     *                                 主要受影响的点击事件为横向滑动
     *
     */
	public void setInterceptable(boolean isIntercept) {
		this.isIntercept = isIntercept;
	}

}
