package com.example.iyangpu.fragments;

import java.util.Set;

import com.example.iyangpu.ActView;
import com.example.iyangpu.MainActivity;
import com.example.iyangpu.R;
import com.example.iyangpu.data.Activity;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.CalendarView.OnDateChangeListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;



public class CalendarFrag extends Fragment {
		LinearLayout layout;
		CalendarView calendar;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.frag__calendar, container,false);

		layout = (LinearLayout) view.findViewById(R.id.calendar_linear);
		calendar = (CalendarView) view.findViewById(R.id.the_calendar);
		
		calendar.setOnDateChangeListener(new OnDataChange());
		
		return view;
	}
	
	class OnDataChange implements OnDateChangeListener{

		@Override
		public void onSelectedDayChange(CalendarView view, int year, int month,
				int dayOfMonth) {
			
			MainActivity mainActivity = (MainActivity) getActivity();
			mainActivity.reload_activities_with_date(year, month, dayOfMonth);
		}
	}
	
	public void refresh_frag_calendar(Set<Activity> activities) {
		layout.removeAllViews();
		for (Activity activity : activities) {
			ActView av = new ActView(getActivity(), activity);
			layout.addView(av);
		}
	}
}
