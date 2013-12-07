package com.example.fragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.cultureplatform.ApplicationHelper;
import com.example.cultureplatform.R;
import com.example.database.DatabaseConnector;
import com.example.database.MessageAdapter;
import com.example.database.data.Activity;
import com.example.database.data.Attention;
import com.example.database.data.Entity;
import com.example.database.data.User;
import com.example.fragment.item.RecommendItemAdapter;

import android.app.Fragment;
import android.content.ContentValues;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;


/**
 * 默认的第一屏，在此第一次载入当前所有活动
 * @author Administrator
 *
 */
public class RecommendFragment extends FragmentHelper {
	private ListView listView;
	private RecommendItemAdapter adapter = new RecommendItemAdapter(null);

 	public void freshList(List<Activity> activities) {
 		try {
			adapter.setActivities(activities);
			listView.setAdapter(adapter);
		} catch (Exception e) {
			String s = e.getMessage();
			s.getBytes();
		}
	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.frag_recommend, container,false);
		listView = (ListView) view.findViewById(R.id.list_recommend);

		listView.setAdapter(adapter);
		
		return view;
	}
	
	@Override
	public String toString() {
		return "推荐活动";
	}



	
	@Override
	public void reDownload() {
		final MessageAdapter activityAdapter = new MessageAdapter() {
			
			@Override
			public void onRcvJSONArray(JSONArray array) {	
				Set<Activity> activities = new HashSet<Activity>();
				for(int i=0 ; i<array.length();i++)
				{
					Activity activity = new Activity();
					JSONObject obj;
					try {
						obj = array.getJSONObject(i);
						activity.transJSON(obj);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					activities.add(activity);
					
				}
				Entity.insertIntoSQLite(activities, getActivity());
			}
			@Override
			public void onFinish() {
				reLoad();
			}
			
		};
		

		DatabaseConnector connector = new DatabaseConnector();
		connector.addParams(DatabaseConnector.METHOD, "GETACTIVITY");
		connector.asyncConnect(activityAdapter);
	}


	@Override
	public void reLoad() {
		List<Activity> activities = new ArrayList<Activity>();
		List<ContentValues> list = Entity.selectFromSQLite("activity", new String[]{"id","name"}, getActivity());
		for(ContentValues contentValue: list){
			Activity activity = new Activity();
			activity.setId(contentValue.getAsInteger("id"));
			activity.setName(contentValue.getAsString("name"));
			activities.add(activity);
		}
		freshList(activities);
		
	}


	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		reLoad();
		if(firstIn())
			reDownload();
	}

	
}
