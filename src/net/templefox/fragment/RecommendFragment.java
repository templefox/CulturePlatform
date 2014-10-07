package net.templefox.fragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.templefox.database.DatabaseConnector;
import net.templefox.database.IonRemoteLoader;
import net.templefox.database.MessageAdapter;
import net.templefox.database.SQLiteWorker;
import net.templefox.database.data.Activity;
import net.templefox.fragment.item.RecommendItemAdapter;
import net.templefox.misc.DateFormater;
import net.templefox.widget.staggeredGrid.StaggeredGridView;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;

import net.templefox.cultureplatform.R;

import android.R.bool;
import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.LinearLayout;
import android.widget.Toast;

/**
 * First screen of the application. Download first part of activities.
 */
@EFragment(R.layout.frag_recommend)
public class RecommendFragment extends AbstractFragment implements OnScrollListener {
	@Bean
	RecommendItemAdapter adapter;

	@Bean
	IonRemoteLoader remoteLoader;
	
	private boolean mHasRequestedMore = false;

	private int currentPosition = 0;
	private int BLOCK_NUMBER = 20;
	@ViewById(R.id.recommend_staggered_grid)
	StaggeredGridView gridView;

	@ViewById(R.id.recomment_empty)
	View emptyView;

	@Override
	public String toString() {
		return "ÍÆ¼ö»î¶¯";
	}

	@AfterInject
	protected void FirstLoadDate() {
		int offset = 0;
		int limit = BLOCK_NUMBER;
		remoteLoader.selectRemoteDate(
				String.format("select id_activity,name,datetime,picture_urls from activity order by datetime desc limit %s,%s", offset, limit),
				new FutureCallback<JsonArray>() {
					@Override
					public void onCompleted(Exception arg0, JsonArray arg1) {
						Context rootContext = getActivity();
						if (arg0 != null) {
							Toast.makeText(rootContext, arg0.getMessage(), Toast.LENGTH_SHORT).show();
						} else if (arg1.size() < 1) {
							Toast.makeText(rootContext, "No more activity!", Toast.LENGTH_SHORT).show();
						} else {
							JsonElement resultElement = arg1.get(0).getAsJsonObject().get("result");
							if (resultElement != null && resultElement.getAsString().equals("error")) {
								Toast.makeText(rootContext, arg1.get(0).getAsJsonObject().get("error").getAsString(), Toast.LENGTH_LONG)
										.show();
								return;
							}
							currentPosition += arg1.size();
							for (int i = 0; i < arg1.size(); i++) {
								Activity activity = new Activity();
								JsonObject object = arg1.get(i).getAsJsonObject();
								activity.setId(object.get("id_activity").getAsInt());
								activity.setName(object.get("name").getAsString());
								activity.setDate(DateFormater.parse(object.get("datetime").getAsString()));
								activity.setPictureUrl(object.get("picture_urls").getAsString());
								adapter.getActivities().add(activity);
								SQLiteWorker.insertIntoSQLite(activity, rootContext);
							}
							adapter.notifyDataSetChanged();
						}
					}
				});
	}

	@AfterViews
	protected void afterViews() {
		gridView.setEmptyView(emptyView);
		View footer = LayoutInflater.from(getActivity()).inflate(R.layout.recommend_loading, null);
		gridView.addFooterView(footer);
		
		gridView.setAdapter(adapter);

		gridView.setOnScrollListener(this);
	}

	@Deprecated
	@Override
	public void download() {
	}

	@Deprecated
	@SuppressLint("SimpleDateFormat")
	@Override
	public void load() {
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
		if (!mHasRequestedMore) {
	    	
            int lastInScreen = firstVisibleItem + visibleItemCount;
            if (lastInScreen >= totalItemCount) {
                Log.d("", "onScroll lastInScreen - so load more");
                mHasRequestedMore = true;
                loadMoreItems();
            }
        }
	}
    private void loadMoreItems() {
    	remoteLoader.selectRemoteDate(
				String.format("select id_activity,name,datetime,picture_urls from activity order by datetime desc limit %s,%s", currentPosition, BLOCK_NUMBER),
				new FutureCallback<JsonArray>() {
					@Override
					public void onCompleted(Exception arg0, JsonArray arg1) {
						Context rootContext = getActivity();
						if (arg0 != null) {
							Toast.makeText(rootContext, arg0.getMessage(), Toast.LENGTH_SHORT).show();
						} else if (arg1.size() < 1) {
							Toast.makeText(rootContext, "No more activity!", Toast.LENGTH_SHORT).show();
						} else {
							JsonElement resultElement = arg1.get(0).getAsJsonObject().get("result");
							if (resultElement != null && resultElement.getAsString().equals("error")) {
								Toast.makeText(rootContext, arg1.get(0).getAsJsonObject().get("error").getAsString(), Toast.LENGTH_LONG)
										.show();
								return;
							}
							currentPosition += arg1.size();
							for (int i = 0; i < arg1.size(); i++) {
								Activity activity = new Activity();
								JsonObject object = arg1.get(i).getAsJsonObject();
								activity.setId(object.get("id_activity").getAsInt());
								activity.setName(object.get("name").getAsString());
								activity.setDate(DateFormater.parse(object.get("datetime").getAsString()));
								activity.setPictureUrl(object.get("picture_urls").getAsString());
								adapter.getActivities().add(activity);
								SQLiteWorker.insertIntoSQLite(activity, rootContext);
							}
							adapter.notifyDataSetChanged();
			            	mHasRequestedMore = false;
						}
					}
				});
        
    }
	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
	}

}
