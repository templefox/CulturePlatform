package net.templefox.fragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import net.templefox.cultureplatform.ApplicationHelper;
import net.templefox.database.data.Activity;
import net.templefox.database.data.Entity;
import net.templefox.database.data.User;
import net.templefox.fragment.item.CalendarItemAdapter;
import net.templefox.fragment.item.ClassifyItemAdapter;

import com.example.cultureplatform.R;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.CalendarView.OnDateChangeListener;
import android.widget.ListView;

@SuppressLint("SimpleDateFormat")
public class CalendarFragment extends AbsFragment {
	private ListView listView;
	//private CalendarItemAdapter adapter = new CalendarItemAdapter(null);
	private ClassifyItemAdapter adapter = new ClassifyItemAdapter(null);
	private CalendarView calendarView;
	private View view_yes;
	private View view_no;
	private User currentUser;
	private boolean lock = false;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.frag_calendar, container,false);
		listView = (ListView) view.findViewById(R.id.list_calendar);
		calendarView = (CalendarView) view.findViewById(R.id.my_calendar);
		view_yes = view.findViewById(R.id.calendar_yes);
		view_no = view.findViewById(R.id.calendar_no);
		
		
		listView.setAdapter(adapter);
		calendarSetOnChangeListener();
		
		return view;
	}

	private void calendarSetOnChangeListener() {
		calendarView.setOnDateChangeListener(new OnDateChangeListener() {
			@Override
			public void onSelectedDayChange(CalendarView view, int year, int month,
					int dayOfMonth) {
				String date = Integer.toString(year)+"."+Integer.toString(month+1)+"."+Integer.toString(dayOfMonth);
				String query_date = "%";
				try {
					query_date = new SimpleDateFormat("yyyy-MM-dd").format(new SimpleDateFormat("yyyy.MM.dd").parse(date));
				} catch (ParseException e) {
					 Log.e("CP Error",e.getMessage());Log.w("CP Exception", Log.getStackTraceString(e));
				}
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
	}

	public void freshList(List<Activity> activities){
		adapter.setActivities(activities);
		adapter.notifyDataSetChanged();
	}
	
	@Override
	public void onStart() {
		initUserView();
		
		
		super.onStart();
	}

	@Override
	public void download() {}

	@Override
	public void load() {
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
	public void setUserVisibleHint(boolean isVisibleToUser) {
		if(isVisibleToUser&&!lock)
		{
			initUserView();
			lock = true;
		}
	}

	private void initUserView() {
		currentUser = ((ApplicationHelper)this.getActivity().getApplication()).getCurrentUser();
		
		if(currentUser == null){
			view_yes.setVisibility(View.GONE);
			view_no.setVisibility(View.VISIBLE);
		}
		else{
			view_yes.setVisibility(View.VISIBLE);
			view_no.setVisibility(View.GONE);
			if(firstIn()){}		
			load();
		}
	}

	@Override
	public String toString() {
		return "我的日历";
	}
}
