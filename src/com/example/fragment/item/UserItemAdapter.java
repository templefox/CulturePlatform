package com.example.fragment.item;

import java.util.List;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.cultureplatform.R;
import com.example.database.data.Activity;

public class UserItemAdapter extends BaseAdapter{
	List<Activity> activities;
	
	/**
	 * 关注按钮的触发监听
	 * @author Administrator
	 *
	 */	
	public void setActivities(List<Activity> activities) {
		this.activities = activities;
	}

	public UserItemAdapter(List<Activity> activities) {
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
			convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user_frag, null);
		}
		
		Button button = (Button) convertView.findViewById(R.id.item_user_button);
		TextView textView = (TextView) convertView.findViewById(R.id.item_user_name);
					
		Activity currentActivity = activities.get(position);
				
		button.setText(currentActivity.getName());
		textView.setText(currentActivity.getName());
		
		return convertView;
	}
}