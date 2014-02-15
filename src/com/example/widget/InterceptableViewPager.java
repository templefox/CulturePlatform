package com.example.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class InterceptableViewPager extends ViewPager {
	private boolean isMovable = true;
	@SuppressWarnings("unused")
	private Context context;
	
	public InterceptableViewPager(Context context) {
		super(context);
		this.context =context;
	}

	public InterceptableViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
	}

	
	public void setMovable(boolean isMovable) {
		this.isMovable = isMovable;
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent arg0) {
		if(isMovable)
			return super.onTouchEvent(arg0);
		else {
			return false;
		}
	}	
}
