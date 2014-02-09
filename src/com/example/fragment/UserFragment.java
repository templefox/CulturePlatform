package com.example.fragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import com.example.cultureplatform.ApplicationHelper;
import com.example.cultureplatform.LoginActivity;
import com.example.cultureplatform.R;
import com.example.database.data.Activity;
import com.example.database.data.Entity;
import com.example.database.data.User;
import com.example.fragment.item.ClassifyItemAdapter;
import com.example.fragment.item.UserItemAdapter;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

@SuppressLint("SimpleDateFormat")
public class UserFragment extends AbsFragment {
	private View noUser;
	private View yesUser;
	private User currentUser;
	private ListView listView;
	private UserItemAdapter itemAdapter = new UserItemAdapter(null);

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.frag_user, container,false);
		Button loginButton = (Button) view.findViewById(R.id.user_frag_button_log_in);
		noUser = view.findViewById(R.id.user_frag_no);
		yesUser = view.findViewById(R.id.user_frag_yes);
		listView = (ListView) view.findViewById(R.id.list_user_frag);
		listView.setAdapter(itemAdapter);
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
		initUserView();
		super.onStart();
	}



	/**
	 * Check if user is login.
	 */
	private void initUserView() {
		currentUser = ((ApplicationHelper)getActivity().getApplication()).getCurrentUser();
		
		if(currentUser == null){
			noUser.setVisibility(View.VISIBLE);
			yesUser.setVisibility(View.GONE);
		}else {
			noUser.setVisibility(View.GONE);
			yesUser.setVisibility(View.VISIBLE);
			load();
		}
	}



	@Override
	public void download() {}

	@Override
	public void load() {
		List<Activity> activities = new ArrayList<Activity>();
			
		List<ContentValues> list = Entity.selectFromSQLite("activity,attention", new String[]{"activity.id","activity.name","activity.date"}
					,"UserID = ? and ActivityID = activity.id",new String[]{currentUser.getId().toString()}, getActivity());
		
		
		for(ContentValues contentValue: list){
			Activity activity = new Activity();
			activity.setId(contentValue.getAsInteger("id"));
			activity.setName(contentValue.getAsString("name"));
			try {
				activity.setDate(new SimpleDateFormat("yyyy-MM-dd").parse(contentValue.getAsString("date")));
			} catch (ParseException e) {
				Log.e("CP Error", e.getMessage());
				Log.w("CP Exception", Log.getStackTraceString(e));
			}
			activities.add(activity);
		}
		freshList(activities);
	}

	private void freshList(List<Activity> activities) {
		itemAdapter.setActivities(activities);
		itemAdapter.notifyDataSetChanged();
	}



	/**
	 * Call when this Fragment change its visibility.
	 * Causing it take time to automatic login, at the first time being visible,
	 *  check if user log in then initialize the view.
	 */
	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		if(isVisibleToUser&&firstIn())
		{
			initUserView();
		}
	}



	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "ÎÒµÄ¹Ø×¢";
	}
}
