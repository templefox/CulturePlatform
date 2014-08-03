package net.templefox.fragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.templefox.cultureplatform.ApplicationHelper;
import net.templefox.database.DatabaseConnector;
import net.templefox.database.MessageAdapter;
import net.templefox.database.data.Activity;
import net.templefox.database.data.Attention;
import net.templefox.database.data.Entity;
import net.templefox.database.data.Location;
import net.templefox.database.data.Type;
import net.templefox.database.data.User;
import net.templefox.fragment.item.ClassifyItemAdapter;
import net.templefox.widget.Optionor;
import net.templefox.widget.Panel;

import org.androidannotations.annotations.EFragment;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import net.templefox.cultureplatform.R;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.widget.RadioGroup.OnCheckedChangeListener;

@EFragment(R.layout.frag_classify)
@SuppressLint("SimpleDateFormat")
public class ClassifyFragment extends AbsFragment {
	private Optionor optionorType;
	private Optionor optionorLocation;
	private Panel panel;
	private ClassifyItemAdapter adapter = new ClassifyItemAdapter(null);
	private ListView listView;
	private View footerView;
	private final int MAX_ITEM_DOWNLOAD = 1;
	private boolean onReload = false;
	private boolean moreData = true;
	private String selectedType = null;
	private String selectedLocation = null;

	public void notifyList(List<Activity> activities) {
		adapter.setActivities(activities);
		adapter.notifyDataSetChanged();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.frag_classify, container, false);
		panel = (Panel) view.findViewById(R.id.classify_panel);
		optionorType = (Optionor) view.findViewById(R.id.optionor_type);
		optionorLocation = (Optionor) view.findViewById(R.id.optionor_location);
		listView = (ListView) view.findViewById(R.id.list_classify);

		ApplicationHelper application = ((ApplicationHelper) getActivity()
				.getApplication());
		application
				.setOnUserChangedListener(new ApplicationHelper.OnUserChangedListener() {
					@Override
					public void onUserChanged(User newUser) {
						download();
					}
				});

		setOptionorOnChangeListener();

		listView.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (panel.isOpen())
					panel.setOpen(false, true);
				return false;
			}
		});

		initListView();
		return view;
	}

	/**
	 * Set optionor to make sure that list view will load after optionor
	 * loading.
	 */
	private void setOptionorOnChangeListener() {
		optionorType.setOnCheckedChangedListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				RadioButton button = (RadioButton) group
						.findViewById(checkedId);
				if (button != null) {
					selectedType = button.getText().toString();
					loadActivities(selectedType, selectedLocation);
				}
			}
		});

		optionorLocation
				.setOnCheckedChangedListener(new OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(RadioGroup group, int checkedId) {
						RadioButton button = (RadioButton) group
								.findViewById(checkedId);
						if (button != null) {
							selectedLocation = button.getText().toString();
							loadActivities(selectedType, selectedLocation);
						}
					}
				});
	}

	/**
	 * 1.Set footerView for listview. 2.Set onScrollListener. When scroll to the
	 * button, loading more data.
	 */
	private void initListView() {
		footerView = LayoutInflater.from(getActivity()).inflate(
				R.layout.item_classify_footer, null);
		footerView.setVisibility(View.INVISIBLE);

		listView.addFooterView(footerView);
		listView.setAdapter(adapter);

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
						Log.e("CP Error", e.getMessage());
						Log.w("CP Exception", Log.getStackTraceString(e));
					} catch (ParseException e) {
						Log.e("CP Error", e.getMessage());
						Log.w("CP Exception", Log.getStackTraceString(e));
					}
					activities.add(activity);
				}
				Entity.insertIntoSQLite(activities, getActivity());
			}

			@Override
			public void onEmptyReceived() {
				moreData = false;
				listView.removeFooterView(footerView);
				Toast.makeText(getActivity(), "没有更多的数据了", Toast.LENGTH_SHORT)
						.show();
			}

			@Override
			public void onTimeout() {
				moreData = false;
				listView.removeFooterView(footerView);
				Toast.makeText(getActivity(), "连接超时", Toast.LENGTH_SHORT)
						.show();
			}

			@Override
			public void onFinish() {
				listView.postDelayed(new Runnable() {
					@Override
					public void run() {
						if (moreData) {
							footerView.setVisibility(View.INVISIBLE);
							loadActivities(selectedType, selectedLocation);
						}
						onReload = false;
					}
				}, 1000);
			}
		};

		listView.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				if (totalItemCount != 0 + listView.getFooterViewsCount()
						&& view.getLastVisiblePosition() + 1 != totalItemCount
						&& !moreData) {
					moreData = true;
					listView.addFooterView(footerView);
				}

				if (totalItemCount != 0
						&& view.getLastVisiblePosition() + 1 == totalItemCount
						&& !onReload && moreData) {
					onReload = true;
					footerView.setVisibility(View.VISIBLE);
					DatabaseConnector connector = new DatabaseConnector();
					connector
							.addParams(DatabaseConnector.METHOD, "GETACTIVITY");
					connector.addParams("limit",
							Integer.toString(MAX_ITEM_DOWNLOAD));
					connector.addParams("offset",
							Integer.toString(totalItemCount));
					connector.executeConnector(activityAdapter);
				}
			}

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
			}
		});
	}

	@Override
	public String toString() {
		return "分类检索";
	}

	public void switchPanel() {
		if (panel.isOpen()) {
			panel.setOpen(false, true);

		} else {
			panel.setOpen(true, true);

		}
	}

	public void open(boolean value, boolean anim) {
		panel.setOpen(value, anim);
	}

	public boolean isOpen() {
		return panel.isOpen();
	}

	@Override
	public void download() {
		optionorType.removeAllString();
		final MessageAdapter typeAdapter = new MessageAdapter() {
			@Override
			public void onRcvJSONArray(JSONArray array) {
				Set<Type> types = new HashSet<Type>();
				for (int i = 0; i < array.length(); i++) {
					Type type = new Type();
					JSONObject obj;
					try {
						obj = array.getJSONObject(i);
						type.transJSON(obj);
						types.add(type);
					} catch (JSONException e) {
						Log.e("CP Error", e.getMessage());
						Log.w("CP Exception", Log.getStackTraceString(e));
					} catch (ParseException e) {
						Log.e("CP Error", e.getMessage());
						Log.w("CP Exception", Log.getStackTraceString(e));
					}
				}
				Entity.insertIntoSQLite(types, getActivity());
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

		final MessageAdapter locationAdapter = new MessageAdapter() {
			@Override
			public void onRcvJSONArray(JSONArray array) {
				Set<Location> locations = new HashSet<Location>();
				for (int i = 0; i < array.length(); i++) {
					Location location = new Location();
					JSONObject obj;
					try {
						obj = array.getJSONObject(i);
						location.transJSON(obj);
						locations.add(location);
					} catch (JSONException e) {
						Log.e("CP Error", e.getMessage());
						Log.w("CP Exception", Log.getStackTraceString(e));
					} catch (ParseException e) {
						Log.e("CP Error", e.getMessage());
						Log.w("CP Exception", Log.getStackTraceString(e));
					}
				}
				Entity.insertIntoSQLite(locations, getActivity());
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
		
		final MessageAdapter attentionAdapter = new MessageAdapter() {

			@Override
			public void onRcvJSONArray(JSONArray array) {
				Set<Attention> attentions = new HashSet<Attention>();
				for (int i = 0; i < array.length(); i++) {
					try {
						Attention attention = new Attention();
						JSONObject obj;
						obj = array.getJSONObject(i);
						attention.transJSON(obj);
						attentions.add(attention);
					} catch (JSONException e) {
						Log.e("CP Error", e.getMessage());
						Log.w("CP Exception", Log.getStackTraceString(e));
					} catch (ParseException e) {
						Log.e("CP Error", e.getMessage());
						Log.w("CP Exception", Log.getStackTraceString(e));
					}
				}
				Entity.insertIntoSQLite(attentions, getActivity());
			}

			@Override
			public void onFinish() {
			}
		};

		
		
		User user = ((ApplicationHelper) getActivity().getApplication())
				.getCurrentUser();
		DatabaseConnector connector = new DatabaseConnector();
		connector.addParams(DatabaseConnector.METHOD, "GETTYPE");
		connector.executeConnector(typeAdapter);

		DatabaseConnector connector3 = new DatabaseConnector();
		connector3.addParams(DatabaseConnector.METHOD, "GETLOCATION");
		connector3.executeConnector(locationAdapter);

		DatabaseConnector connector2 = new DatabaseConnector();
		connector2.addParams(DatabaseConnector.METHOD, "GETATTENTION");
		if (user != null)
			connector2.addParams("userID", user.getId().toString());
		connector2.executeConnector(attentionAdapter);
		
	}

	/**
	 * Load the optionor but not the listview. Listview will automatically load
	 * when optionor load.
	 */
	@Override
	public void load() {
		optionorType.removeAllString();
		optionorLocation.removeAllString();
		optionorType.addString("全部");
		List<ContentValues> list = Entity.selectFromSQLite("type",
				new String[] { "name" }, getActivity());
		for (ContentValues contentValue : list) {
			optionorType.addString(contentValue.getAsString("name"));
		}

		optionorLocation.addString("全部");
		list = Entity.selectFromSQLite("location", new String[] { "name" },
				getActivity());
		for (ContentValues contentValues : list) {
			optionorLocation.addString(contentValues.getAsString("name"));
		}
	}

	/**
	 * Load activities according the classify condition.
	 * 
	 * @param type
	 *            type of activities.
	 * @param location
	 *            location of activities.
	 */
	private void loadActivities(String type, String location) {
		List<Activity> activities = new ArrayList<Activity>();

		User currentUser = ((ApplicationHelper) getActivity().getApplication())
				.getCurrentUser();

		if (type != null && type.equals("全部")) {
			type = "%";
		}

		if (location != null && location.equals("全部")) {
			location = "%";
		}

		List<ContentValues> list = Entity.selectFromSQLite("activity",
				new String[] { "id", "name", "address", "picture_url", "date",
						"type", "theme", "temperature", "reporter_info",
						"content", "procedure", "time" }, "type like ?",
				new String[] { type }, getActivity(), "date desc");

		List<ContentValues> attentionList = null;
		if (currentUser != null) {
			attentionList = Entity.selectFromSQLite("attention",
					new String[] { "ActivityID" }, "UserID = ?",
					new String[] { currentUser.getId().toString() },
					getActivity());
		}

		for (ContentValues contentValue : list) {
			Activity activity = new Activity();
			activity.setId(contentValue.getAsInteger("id"));
			activity.setName(contentValue.getAsString("name"));
			activity.setAddress(contentValue.getAsString("address"));

			try {
				activity.setDate(new SimpleDateFormat("yyyy-MM-dd")
						.parse(contentValue.getAsString("date")));
			} catch (ParseException e) {
				Log.e("CP Error", e.getMessage());
				Log.w("CP Exception", Log.getStackTraceString(e));
			}

			try {
				activity.setTime(new SimpleDateFormat("HH:mm:ss")
						.parse(contentValue.getAsString("time")));
			} catch (ParseException e) {
				Log.e("CP Error", e.getMessage());
				Log.w("CP Exception", Log.getStackTraceString(e));
			}

			activity.setPictureUrl(contentValue.getAsString("picture_url"));
			activity.setReporterInfo(contentValue.getAsString("reporter_info"));
			activity.setTheme(contentValue.getAsString("theme"));
			activity.setType(contentValue.getAsString("type"));
			activity.setTemperature(Integer.parseInt(contentValue
					.getAsString("temperature")));
			activity.setContent(contentValue.getAsString("content"));
			activity.setProcedure(contentValue.getAsString("procedure"));

			activity.setisAttention(0);
			if (currentUser != null) {
				for (ContentValues attentionCV : attentionList) {
					if (activity.getId() == attentionCV
							.getAsInteger("ActivityID")) {
						activity.setisAttention(1);
						break;
					}
				}
			}

			activities.add(activity);

		}
		notifyList(activities);
	}

	@Override
	public void onResume() {
		super.onResume();
		panel.setOpen(false, false);
		load();
		if (firstIn())
			download();
	}

}
