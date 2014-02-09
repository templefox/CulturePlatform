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

import com.example.cultureplatform.R;
import com.example.database.DatabaseConnector;
import com.example.database.MessageAdapter;
import com.example.database.data.Activity;
import com.example.database.data.Entity;
import com.example.fragment.item.RecommendItemAdapter;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

/**
 * First screen of the application. Download first part of activities.
 */
public class RecommendFragment extends AbsFragment {
	private RecommendItemAdapter adapter = new RecommendItemAdapter(null);
	private LinearLayout left;
	private LinearLayout right;
	private final int MAX_COUNT = 8;

	public void freshList(List<Activity> activities) {
		right.removeAllViews();
		left.removeViews(1, left.getChildCount() - 1);
		adapter.setActivities(activities);
		for (int i = 0; i < adapter.getCount() && i < MAX_COUNT; i++) {
			if (i % 2 == 0) {
				right.addView(adapter.getView(i, null, right));
			} else {
				left.addView(adapter.getView(i, null, left));
			}
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.frag_recommend, container, false);
		left = (LinearLayout) view.findViewById(R.id.reco_left);
		right = (LinearLayout) view.findViewById(R.id.reco_right);
		return view;
	}

	@Override
	public String toString() {
		return "推荐活动";
	}

	@Override
	public void download() {
		final MessageAdapter activityAdapter = new MessageAdapter() {

			@Override
			public void onRcvJSONArray(JSONArray array) {
				Set<Activity> activities = new HashSet<Activity>();
				for (int i = 0; i < array.length(); i++) {
					Activity activity = new Activity();
					JSONObject obj;
					try {
						obj = array.getJSONObject(i);
						activity.transJSON(obj);
					} catch (JSONException e) {
						 Log.e("CP Error",e.getMessage());Log.w("CP Exception", Log.getStackTraceString(e));
					} catch (ParseException e) {
						 Log.e("CP Error",e.getMessage());Log.w("CP Exception", Log.getStackTraceString(e));
					}
					activities.add(activity);

				}
				Entity.insertIntoSQLite(activities, getActivity());
			}

			@Override
			public void onTimeout() {
				Toast.makeText(getActivity(), "连接超时", Toast.LENGTH_SHORT)
						.show();
			}

			@Override
			public void onFinish() {
				load();
			}

		};

		DatabaseConnector connector = new DatabaseConnector();
		connector.addParams(DatabaseConnector.METHOD, "GETACTIVITY");
		connector.addParams("limit", Integer.toString(8));
		connector.addParams("offset", Integer.toString(0));
		connector.asyncConnect(activityAdapter);
	}

	@SuppressLint("SimpleDateFormat")
	@Override
	public void load() {
		List<Activity> activities = new ArrayList<Activity>();
		List<ContentValues> list = Entity.selectFromSQLite("activity",
				new String[] { "id", "name", "address", "picture_url", "date",
						"type", "theme", "temperature", "reporter_info",
						"content", "procedure","time" }, getActivity(), "date desc");
		for (ContentValues contentValue : list) {
			Activity activity = new Activity();
			activity.setId(contentValue.getAsInteger("id"));
			activity.setName(contentValue.getAsString("name"));
			activity.setAddress(contentValue.getAsString("address"));
			try {
				activity.setDate(new SimpleDateFormat("yyyy-MM-dd")
						.parse(contentValue.getAsString("date")));

			} catch (ParseException e) {
				 Log.e("CP Error",e.getMessage());Log.w("CP Exception", Log.getStackTraceString(e));
			} catch (NullPointerException e) {
				 Log.e("CP Error",e.getMessage());Log.w("CP Exception", Log.getStackTraceString(e));
			} catch (IllegalArgumentException e) {
				 Log.e("CP Error",e.getMessage());Log.w("CP Exception", Log.getStackTraceString(e));
			}
			try {
				activity.setTime(new SimpleDateFormat("HH:mm:ss")
						.parse(contentValue.getAsString("time")));

			} catch (ParseException e) {
				 Log.e("CP Error",e.getMessage());Log.w("CP Exception", Log.getStackTraceString(e));
			} catch (NullPointerException e) {
				 Log.e("CP Error",e.getMessage());Log.w("CP Exception", Log.getStackTraceString(e));
			} catch (IllegalArgumentException e) {
				 Log.e("CP Error",e.getMessage());Log.w("CP Exception", Log.getStackTraceString(e));
			}

			activity.setPictureUrl(contentValue.getAsString("picture_url"));
			activity.setReporterInfo(contentValue.getAsString("reporter_info"));
			activity.setTheme(contentValue.getAsString("theme"));
			activity.setType(contentValue.getAsString("type"));
			activity.setTemperature(Integer.parseInt(contentValue
					.getAsString("temperature")));
			activity.setContent(contentValue.getAsString("content"));
			activity.setProcedure(contentValue.getAsString("procedure"));
			activities.add(activity);
		}
		freshList(activities);
	}

	@Override
	public void onResume() {
		super.onResume();
		load();
		if (firstIn())
			download();
	}

}
