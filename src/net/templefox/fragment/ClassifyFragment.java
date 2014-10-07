package net.templefox.fragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.templefox.cultureplatform.ApplicationHelper;
import net.templefox.database.DataLoader;
import net.templefox.database.DatabaseConnector;
import net.templefox.database.FakeLoadDataListener;
import net.templefox.database.IonRemoteLoader;
import net.templefox.database.LoadDataListener;
import net.templefox.database.MessageAdapter;
import net.templefox.database.SQLiteWorker;
import net.templefox.database.data.Activity;
import net.templefox.database.data.Attention;
import net.templefox.database.data.CurrentUser;
import net.templefox.database.data.Entity;
import net.templefox.database.data.Location;
import net.templefox.database.data.Type;
import net.templefox.database.data.User;
import net.templefox.fragment.item.ClassifyItemAdapter;
import net.templefox.misc.DateFormater;
import net.templefox.misc.Encoder;
import net.templefox.misc.OnUserChangedListener;
import net.templefox.widget.Optionor;
import net.templefox.widget.Panel;
import net.templefox.widget.pullrefresh.PullToRefreshBase;
import net.templefox.widget.pullrefresh.PullToRefreshListView;
import net.templefox.widget.pullrefresh.PullToRefreshBase.OnRefreshListener2;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.RootContext;
import org.androidannotations.annotations.ViewById;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import net.templefox.cultureplatform.R;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.widget.RadioGroup.OnCheckedChangeListener;

@EFragment(R.layout.frag_classify)
@SuppressLint("SimpleDateFormat")
public class ClassifyFragment extends AbstractFragment {
	private int currentPosition = 0;
	private int BLOCK_NUMBER = 20;

	@Bean
	CurrentUser currentUser;

	@ViewById(R.id.classify_panel)
	Panel panel;

	@ViewById(R.id.optionor_type)
	Optionor optionorType;

	@ViewById(R.id.optionor_location)
	Optionor optionorLocation;

	@Bean
	ClassifyItemAdapter adapter;

	@Bean
	IonRemoteLoader remoteLoader;

	@ViewById(R.id.list_classify)
	PullToRefreshListView listView;

	private String selectedType = "%";
	private String selectedLocation = "%";

	public void notifyList(List<Activity> activities) {
		adapter.setActivities(activities);
		adapter.notifyDataSetChanged();
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
		currentUser.setOnUserChangedListener(new OnUserChangedListener() {
			@Override
			public void onUserChanged(Integer newId) {
				// TODO
			}
		});

		loadOptions();
		initListView();
		initOptionorOnChangeListener();
	}

	private void loadOptions() {
		remoteLoader.selectRemoteDate("SELECT id_type,name FROM type", new FutureCallback<JsonArray>() {
			@Override
			public void onCompleted(Exception arg0, JsonArray arg1) {
				Context rootContext = getActivity();
				if (arg0 != null) {
					Toast.makeText(rootContext, arg0.getMessage(), Toast.LENGTH_SHORT).show();
				} else if (arg1.size() < 1) {
					Toast.makeText(rootContext, "No type!", Toast.LENGTH_SHORT).show();
				} else {
					JsonElement resultElement = arg1.get(0).getAsJsonObject().get("result");
					if (resultElement != null && resultElement.getAsString().equals("error")) {
						Toast.makeText(rootContext, arg1.get(0).getAsJsonObject().get("error").getAsString(), Toast.LENGTH_LONG).show();
						return;
					}
					for (int i = 0; i < arg1.size(); i++) {
						Type type = new Type();
						JsonObject object = arg1.get(i).getAsJsonObject();
						type.setId(object.get("id_type").getAsInt());
						type.setName(object.get("name").getAsString());
						SQLiteWorker.insertIntoSQLite(type, rootContext);
					}
				}
				List<ContentValues> types = SQLiteWorker.selectFromSQLite("type", new String[] { "name" }, rootContext);
				optionorType.addString("全部");
				for (ContentValues contentValues : types) {
					optionorType.addString(contentValues.getAsString("name"));
				}
			}
		});

		remoteLoader.selectRemoteDate("SELECT id_organization,name FROM organization", new FutureCallback<JsonArray>() {
			@Override
			public void onCompleted(Exception arg0, JsonArray arg1) {
				Context rootContext = getActivity();
				if (arg0 != null) {
					Toast.makeText(rootContext, arg0.getMessage(), Toast.LENGTH_SHORT).show();
				} else if (arg1.size() < 1) {
					Toast.makeText(rootContext, "No organization!", Toast.LENGTH_SHORT).show();
				} else {
					JsonElement resultElement = arg1.get(0).getAsJsonObject().get("result");
					if (resultElement != null && resultElement.getAsString().equals("error")) {
						Toast.makeText(rootContext, arg1.get(0).getAsJsonObject().get("error").getAsString(), Toast.LENGTH_LONG).show();
						return;
					}
					for (int i = 0; i < arg1.size(); i++) {
						Location location = new Location();
						JsonObject object = arg1.get(i).getAsJsonObject();
						location.setId(object.get("id_organization").getAsInt());
						location.setName(object.get("name").getAsString());
						SQLiteWorker.insertIntoSQLite(location, rootContext);
					}
				}
				List<ContentValues> types = SQLiteWorker.selectFromSQLite("location", new String[] { "name" }, rootContext);
				optionorLocation.addString("全部");
				for (ContentValues contentValues : types) {
					optionorLocation.addString(contentValues.getAsString("name"));
				}
			}
		});
	}

	private void initListView() {
		listView.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (panel.isOpen())
					panel.setOpen(false, true);
				return false;
			}
		});
		listView.setAdapter(adapter);
		registeDataProcess();
	}

	private void registeDataProcess() {
		listView.setOnRefreshListener(new OnRefreshListener2<ListView>() {

			@Override
			public void onPullDownToRefresh(final PullToRefreshBase refreshView) {
				int offset = 0;
				int limit = currentPosition;
				remoteLoader.selectRemoteDate(
						String.format(
								"select id_activity,name,datetime,picture_urls from (select id_activity,name,datetime,type,location,picture_urls from activity_type_location order by datetime desc limit %s,%s) as `a`  where type like '%s' and location like '%s'",
								offset, limit, selectedType, selectedLocation), new FutureCallback<JsonArray>() {
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
										Toast.makeText(rootContext, arg1.get(0).getAsJsonObject().get("error").getAsString(),
												Toast.LENGTH_LONG).show();
										refreshView.onRefreshComplete();
										return;
									}
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
								refreshView.onRefreshComplete();
							}
						});
			}

			@Override
			public void onPullUpToRefresh(final PullToRefreshBase refreshView) {
				int offset = currentPosition;
				int limit = BLOCK_NUMBER;
				remoteLoader.selectRemoteDate(
						String.format(
								"select id_activity,name,datetime,picture_urls from (select id_activity,name,datetime,type,location,picture_urls from activity_type_location order by datetime desc limit %s,%s) as `a`  where type like '%s' and location like '%s'",
								offset, limit, selectedType, selectedLocation), new FutureCallback<JsonArray>() {
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
										Toast.makeText(rootContext, arg1.get(0).getAsJsonObject().get("error").getAsString(),
												Toast.LENGTH_LONG).show();
										refreshView.onRefreshComplete();
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
								refreshView.onRefreshComplete();
							}
						});
			}
		});
	}

	/**
	 * Set optionor to make sure that list view will load after optionor
	 * loading.
	 */
	private void initOptionorOnChangeListener() {
		optionorType.setOnCheckedChangedListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				RadioButton button = (RadioButton) group.findViewById(checkedId);
				if (button != null) {
					selectedType = button.getText().toString();
					if (selectedType.equals("全部")) {
						selectedType = "%%";
					}
					loadActivities(selectedType, selectedLocation);
				}
			}
		});

		optionorLocation.setOnCheckedChangedListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				RadioButton button = (RadioButton) group.findViewById(checkedId);
				if (button != null) {
					selectedLocation = button.getText().toString();
					if (selectedLocation.equals("全部")) {
						selectedLocation = "%%";
					}
					loadActivities(selectedType, selectedLocation);
				}
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
						type.resolveJSON(obj);
						types.add(type);
					} catch (JSONException e) {
						Log.e("CP Error", e.getMessage());
						Log.w("CP Exception", Log.getStackTraceString(e));
					} catch (ParseException e) {
						Log.e("CP Error", e.getMessage());
						Log.w("CP Exception", Log.getStackTraceString(e));
					}
				}
				SQLiteWorker.insertIntoSQLite(types, getActivity());
			}

			@Override
			public void onTimeout() {
				Toast.makeText(getActivity(), "连接超时", Toast.LENGTH_SHORT).show();
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
						location.resolveJSON(obj);
						locations.add(location);
					} catch (JSONException e) {
						Log.e("CP Error", e.getMessage());
						Log.w("CP Exception", Log.getStackTraceString(e));
					} catch (ParseException e) {
						Log.e("CP Error", e.getMessage());
						Log.w("CP Exception", Log.getStackTraceString(e));
					}
				}
				SQLiteWorker.insertIntoSQLite(locations, getActivity());
			}

			@Override
			public void onTimeout() {
				Toast.makeText(getActivity(), "连接超时", Toast.LENGTH_SHORT).show();
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
						attention.resolveJSON(obj);
						attentions.add(attention);
					} catch (JSONException e) {
						Log.e("CP Error", e.getMessage());
						Log.w("CP Exception", Log.getStackTraceString(e));
					} catch (ParseException e) {
						Log.e("CP Error", e.getMessage());
						Log.w("CP Exception", Log.getStackTraceString(e));
					}
				}
				SQLiteWorker.insertIntoSQLite(attentions, getActivity());
			}

			@Override
			public void onFinish() {
			}
		};

		User user = ((ApplicationHelper) getActivity().getApplication()).getCurrentUser();
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
		List<ContentValues> list = SQLiteWorker.selectFromSQLite("type", new String[] { "name" }, getActivity());
		for (ContentValues contentValue : list) {
			optionorType.addString(contentValue.getAsString("name"));
		}

		optionorLocation.addString("全部");
		list = SQLiteWorker.selectFromSQLite("location", new String[] { "name" }, getActivity());
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
		if (type != null && type.equals("全部")) {
			type = "%";
		}

		if (location != null && location.equals("全部")) {
			location = "%";
		}

		if (1 == 1) {
			return;
		}
		List<Activity> activities = new ArrayList<Activity>();

		// User currentUser = ((ApplicationHelper)
		// getActivity().getApplication()).getCurrentUser();

		if (type != null && type.equals("全部")) {
			type = "%";
		}

		if (location != null && location.equals("全部")) {
			location = "%";
		}

		List<ContentValues> list = SQLiteWorker.selectFromSQLite("activity", new String[] { "id", "name", "address", "picture_url", "date",
				"type", "theme", "temperature", "reporter_info", "content", "procedure", "time" }, "type like ?", new String[] { type },
				getActivity(), "date desc");

		List<ContentValues> attentionList = null;
		if (currentUser != null) {
			attentionList = SQLiteWorker.selectFromSQLite("attention", new String[] { "ActivityID" }, "UserID = ?",
					new String[] { currentUser.getId().toString() }, getActivity());
		}

		for (ContentValues contentValue : list) {
			Activity activity = new Activity();
			activity.setId(contentValue.getAsInteger("id"));
			activity.setName(contentValue.getAsString("name"));
			activity.setAddress(contentValue.getAsString("address"));

			try {
				activity.setDate(new SimpleDateFormat("yyyy-MM-dd").parse(contentValue.getAsString("date")));
			} catch (ParseException e) {
				Log.e("CP Error", e.getMessage());
				Log.w("CP Exception", Log.getStackTraceString(e));
			}

			try {
				activity.setTime(new SimpleDateFormat("HH:mm:ss").parse(contentValue.getAsString("time")));
			} catch (ParseException e) {
				Log.e("CP Error", e.getMessage());
				Log.w("CP Exception", Log.getStackTraceString(e));
			}

			activity.setPictureUrl(contentValue.getAsString("picture_url"));
			activity.setReporterInfo(contentValue.getAsString("reporter_info"));
			activity.setTheme(contentValue.getAsString("theme"));
			activity.setType(contentValue.getAsString("type"));
			activity.setTemperature(Integer.parseInt(contentValue.getAsString("temperature")));
			activity.setContent(contentValue.getAsString("content"));
			activity.setProcedure(contentValue.getAsString("procedure"));

			activity.setisAttention(0);
			if (currentUser != null) {
				for (ContentValues attentionCV : attentionList) {
					if (activity.getId() == attentionCV.getAsInteger("ActivityID")) {
						activity.setisAttention(1);
						break;
					}
				}
			}

			activities.add(activity);

		}
		notifyList(activities);
	}
}
