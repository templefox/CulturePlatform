package net.templefox.widget;

import android.R.bool;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ListView;

public class MListView extends ListView {

	public MListView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public MListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public MListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
		boolean b = super.onTouchEvent(ev);
		return false;
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
		boolean b = super.dispatchTouchEvent(ev);
		return false;
	}
}
