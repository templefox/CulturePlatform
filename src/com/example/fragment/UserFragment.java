package com.example.fragment;

import java.util.ArrayList;
import java.util.List;

import com.example.cultureplatform.ApplicationHelper;
import com.example.cultureplatform.LoginActivity;
import com.example.cultureplatform.R;
import com.example.cultureplatform.UserActivity;
import com.example.database.DatabaseConnector;
import com.example.database.MessageAdapter;
import com.example.database.data.Activity;
import com.example.database.data.Attention;
import com.example.database.data.Entity;
import com.example.database.data.User;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ToggleButton;

public class UserFragment extends FragmentHelper {
	private View noUser;
	private View yesUser;
	private User currentUser;
	private ListView listView;
	private UserItemAdapter itemAdapter = new UserItemAdapter(null);
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.frag_user, container,false);
		Button loginButton = (Button) view.findViewById(R.id.user_frag_button_log_in);
		noUser = view.findViewById(R.id.user_frag_no);
		yesUser = view.findViewById(R.id.user_frag_yes);
		listView = (ListView) view.findViewById(R.id.list_user_frag);
		
		loginButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(),LoginActivity.class);  
				startActivity(intent);			
			}
		});
		
		return view;
	}
	
	

	@Override
	public void onStart() {
		currentUser = ((ApplicationHelper)getActivity().getApplication()).getCurrentUser();
		
		if(currentUser == null){
			noUser.setVisibility(View.VISIBLE);
			yesUser.setVisibility(View.GONE);
		}else {
			noUser.setVisibility(View.GONE);
			yesUser.setVisibility(View.VISIBLE);
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
		// TODO Auto-generated method stub
		List<Activity> activities = new ArrayList<Activity>();
			
		List<ContentValues> list = Entity.selectFromSQLite("activity,attention", new String[]{"activity.id","activity.name"}
					,"UserID = ? and ActivityID = activity.id",new String[]{currentUser.getId().toString()}, getActivity());
		
		
		for(ContentValues contentValue: list){
			Activity activity = new Activity();
			activity.setId(contentValue.getAsInteger("id"));
			activity.setName(contentValue.getAsString("name"));
			activities.add(activity);
		}
		freshList(activities);
	}

	private void freshList(List<Activity> activities) {
		itemAdapter.setActivities(activities);
		listView.setAdapter(itemAdapter);
	}



	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "我的关注";
	}

	private class UserItemAdapter extends BaseAdapter{
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
}
