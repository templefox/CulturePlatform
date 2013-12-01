package com.example.fragment.item;

import java.util.List;

import com.example.cultureplatform.R;
import com.example.database.data.Activity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class RecommendItemAdapter extends BaseAdapter{
	List<Activity> activities;
	
	
	public void setActivities(List<Activity> activities) {
		this.activities = activities;
	}

	public RecommendItemAdapter(List<Activity> activities) {
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
		// TODO Auto-generated method stub
		Button button = null;
		TextView textView = null;
		
		if(convertView == null){
			convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recommend, null);
		}
		
		button = (Button) convertView.findViewById(R.id.item_recommend_button);
		textView = (TextView) convertView.findViewById(R.id.item_recommend_name);
		
		button.setText(activities.get(position).getName());
		textView.setText(activities.get(position).getName());
		
		return convertView;
	}
	
}