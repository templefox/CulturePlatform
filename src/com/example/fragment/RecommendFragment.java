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

import android.app.Fragment;
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

public class RecommendFragment extends Fragment {
	private ListView listView;
	private RecommendItemAdapter adapter = new RecommendItemAdapter(null);

	private MessageAdapter recommendAdapter = new MessageAdapter() {
		
		@Override
		public void getSucceedHandler(JSONArray array) {	
			Set<Activity> activities = ((ApplicationHelper)getActivity().getApplication()).getActivities();
			for(int i=0 ; i<array.length();i++)
			{
				Activity activity = new Activity();
				JSONObject obj;
				try {
					obj = array.getJSONObject(i);
					activity.setId(obj.getInt("id"));
					activity.setAddress(obj.getString("address"));
					activity.setContent(obj.getString("content"));
					activity.setDate(new SimpleDateFormat("HH:mm:ss").parse(obj.getString("time")));
					activity.setName(obj.getString("name"));
					activity.setLocation(obj.getInt("locationID"));
					activity.setUser(obj.getInt("organiserID"));
					activity.setType(obj.getString("type"));
					activity.setIsRating(obj.getInt("isRating"));
					activity.setisAttention(1);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				activities.add(activity);
				
			}
		}

		@Override
		public void onFinish() {
			Set<Activity> setActivities = ((ApplicationHelper)getActivity().getApplication()).getActivities();
			List<Activity> activities = new ArrayList<Activity>(setActivities);

			setActivities(activities);
		}
		
	};
	
 	public void setActivities(List<Activity> activities) {
 		try {
			adapter.setActivities(activities);
		} catch (Exception e) {
			String s = e.getMessage();
		}

	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.frag_recommend, container,false);
		listView = (ListView) view.findViewById(R.id.list_recommend);

		listView.setAdapter(adapter);
		
		connectForRecommend();
		
		return view;
	}
	
	public void connectForRecommend()
	{
		DatabaseConnector connector = new DatabaseConnector();
		connector.addParams(DatabaseConnector.METHOD, "GETACTIVITY");
		connector.asyncConnect(recommendAdapter);
	}
	
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
				convertView = LayoutInflater.from(getActivity()).inflate(R.layout.item_recommend, null);
			}
			
			button = (Button) convertView.findViewById(R.id.item_recommend_button);
			textView = (TextView) convertView.findViewById(R.id.item_recommend_name);
			
			button.setText(activities.get(position).getName());
			textView.setText(activities.get(position).getName());
			
			return convertView;
		}
		
	}
	
}
