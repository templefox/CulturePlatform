package com.example.fragment;

import java.util.List;

import com.example.cultureplatform.R;
import com.example.database.data.Activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.CalendarView.OnDateChangeListener;
import android.widget.ListView;
import android.widget.TextView;

public class CalendarFragment extends FragmentHelper {
	private ListView listView;
	private CalendarItemAdapter adapter = new CalendarItemAdapter(null);
	private CalendarView calendarView;
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.frag_calendar, container,false);
		listView = (ListView) view.findViewById(R.id.list_calendar);
		calendarView = (CalendarView) view.findViewById(R.id.my_calendar);
		
		calendarView.setOnDateChangeListener(new OnDateChangeListener() {
			
			@Override
			public void onSelectedDayChange(CalendarView view, int year, int month,
					int dayOfMonth) {
				// TODO Auto-generated method stub
				
			}
		});
		
		return view;
	}

	@Override
	public void reDownload() {
		// TODO Auto-generated method stub

	}

	@Override
	public void reLoad() {
		// TODO Auto-generated method stub

	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "我的日历";
	}
	
	private class CalendarItemAdapter extends BaseAdapter{
		List<Activity> activities;
		
		/**
		 * 关注按钮的触发监听
		 * @author Administrator
		 *
		 */	
		public void setActivities(List<Activity> activities) {
			this.activities = activities;
		}

		public CalendarItemAdapter(List<Activity> activities) {
			super();
			// TODO Auto-generated constructor stub
			this.activities = activities;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			if(activities==null)
				return 0;
			return activities.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return activities.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO 在此初始化每一个item内的内容，添加item的交互功能。

			if(convertView == null){
				convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_calendar, null);
			}
			
			TextView textView = (TextView) convertView.findViewById(R.id.item_calendar_name);
						
			Activity currentActivity = activities.get(position);
					
			textView.setText(currentActivity.getName());
			
			return convertView;
		}
		
	}
	
	
}
