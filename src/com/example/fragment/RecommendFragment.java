package com.example.fragment;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.cultureplatform.R;
import com.example.database.DatabaseConnector;
import com.example.database.MessageAdapter;
import com.example.database.data.Activity;
import com.example.database.data.Entity;
import com.example.fragment.item.RecommendItemAdapter;

import android.content.ContentValues;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;


/**
 * 默认的第一屏，在此第一次载入当前所有活动
 * @author Administrator
 *
 */
public class RecommendFragment extends FragmentHelper {
	private RecommendItemAdapter adapter = new RecommendItemAdapter(null);
	private LinearLayout left;
	private LinearLayout right;
	private final int MAX_COUNT = 8;
	
 	public void freshList(List<Activity> activities) {
 		try {
 			right.removeAllViews();
 			left.removeViews(1, left.getChildCount()-1);
			adapter.setActivities(activities);
			for(int i=0;i<adapter.getCount()&&i<MAX_COUNT;i++){
				if(i%2==0){
					right.addView(adapter.getView(i, null, right));
					
				}else
				{
					left.addView(adapter.getView(i, null, left));
				}
			}
		} catch (Exception e) {
			String s = e.getMessage();
			s.getBytes();
		}
	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.frag_recommend, container,false);
		left = (LinearLayout) view.findViewById(R.id.reco_left);
		right = (LinearLayout) view.findViewById(R.id.reco_right);
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
		connector.addParams("limit", Integer.toString(8));
		connector.addParams("offset", Integer.toString(0));
		connector.asyncConnect(activityAdapter);
	}


	@Override
	public void reLoad() {
		List<Activity> activities = new ArrayList<Activity>();
		List<ContentValues> list = Entity.selectFromSQLite("activity", new String[]{"id","name","picture_url"}, getActivity());
		for(ContentValues contentValue: list){
			Activity activity = new Activity();
			activity.setId(contentValue.getAsInteger("id"));
			activity.setName(contentValue.getAsString("name"));
			activity.setPictureUrl(contentValue.getAsString("picture_url"));
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
