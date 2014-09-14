package net.templefox.fragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.templefox.database.DatabaseConnector;
import net.templefox.database.MessageAdapter;
import net.templefox.database.SQLiteWorker;
import net.templefox.database.data.Activity;
import net.templefox.fragment.item.RecommendItemAdapter;

import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import net.templefox.cultureplatform.R;

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
@EFragment(R.layout.frag_recommend)
public class RecommendFragment extends AbstractFragment {
	private RecommendItemAdapter adapter = new RecommendItemAdapter(null);
	
	@ViewById(R.id.reco_left)
	LinearLayout left;
	
	@ViewById(R.id.reco_right)
	LinearLayout right;
	
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
						activity.resolveJSON(obj);
					} catch (JSONException e) {
						 Log.e("CP Error",e.getMessage());Log.w("CP Exception", Log.getStackTraceString(e));
					} catch (ParseException e) {
						 Log.e("CP Error",e.getMessage());Log.w("CP Exception", Log.getStackTraceString(e));
					}
					activities.add(activity);

				}
				SQLiteWorker.insertIntoSQLite(activities, getActivity());
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
		connector.executeConnector(activityAdapter);
	}

	@SuppressLint("SimpleDateFormat")
	@Override
	public void load() {
		List<Activity> activities = new ArrayList<Activity>();
		List<ContentValues> list = SQLiteWorker.selectFromSQLite("activity",
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
