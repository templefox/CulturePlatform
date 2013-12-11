package com.example.fragment;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.example.cultureplatform.ApplicationHelper;
import com.example.cultureplatform.R;
import com.example.database.data.Activity;
import com.example.database.data.Entity;
import com.example.database.data.User;
import com.example.fragment.item.CalendarItemAdapter;

import android.content.ContentValues;
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
	private View view_yes;
	private View view_no;
	private User currentUser;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.frag_calendar, container,false);
		listView = (ListView) view.findViewById(R.id.list_calendar);
		calendarView = (CalendarView) view.findViewById(R.id.my_calendar);
		view_yes = view.findViewById(R.id.calendar_yes);
		view_no = view.findViewById(R.id.calendar_no);
		
		calendarView.setOnDateChangeListener(new OnDateChangeListener() {
			
			@Override
			public void onSelectedDayChange(CalendarView view, int year, int month,
					int dayOfMonth) {
				// TODO Auto-generated method stub
				String query_date = Integer.toString(year)+"."+Integer.toString(month+1)+"."+Integer.toString(dayOfMonth);
				List<ContentValues> list = Entity.selectFromSQLite("activity", new String[]{"id,name"},"date = ? ",new String[]{query_date}, view.getContext());
				List<Activity> activities = new ArrayList<Activity>();
				
				for(ContentValues value:list){
					Activity activity = new Activity();
					activity.setId(value.getAsInteger("id"));
					activity.setName(value.getAsString("name"));
					activities.add(activity);
				}
				freshList(activities);
			}
		});
		
		return view;
	}

	public void freshList(List<Activity> activities){
		adapter.setActivities(activities);
		listView.setAdapter(adapter);
	}
	
	@Override
	public void onStart() {
		currentUser = ((ApplicationHelper)this.getActivity().getApplication()).getCurrentUser();
		
		if(currentUser == null){
			view_yes.setVisibility(View.GONE);
			view_no.setVisibility(View.VISIBLE);
		}
		else{
			view_yes.setVisibility(View.VISIBLE);
			view_no.setVisibility(View.GONE);
			if(firstIn()){}		
			reLoad();
		}
		
		
		super.onStart();
	}

	@Override
	public void reDownload() {
		// TODO Auto-generated method stub

	}

	@Override
	public void reLoad() {
		List<ContentValues> list = Entity.selectFromSQLite("activity", new String[]{"id,name"},"date = strftime('%Y.%m.%d','now') ",new String[]{}, getActivity());
		List<Activity> activities = new ArrayList<Activity>();
		
		for(ContentValues value:list){
			Activity activity = new Activity();
			activity.setId(value.getAsInteger("id"));
			activity.setName(value.getAsString("name"));
			activities.add(activity);
		}
		freshList(activities);
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "我的日历";
	}
}
