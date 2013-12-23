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
import com.example.cultureplatform.MainActivity;
import com.example.cultureplatform.R;
import com.example.database.DatabaseConnector;
import com.example.database.MessageAdapter;
import com.example.database.data.Activity;
import com.example.database.data.Attention;
import com.example.database.data.Entity;
import com.example.database.data.Type;
import com.example.database.data.User;
import com.example.fragment.item.ClassifyItemAdapter;
import com.example.widget.Optionor;
import com.example.widget.Panel;
import com.example.widget.Panel.OnPanelListener;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.widget.RadioGroup.OnCheckedChangeListener;

@SuppressLint("SimpleDateFormat")
public class ClassifyFragment extends FragmentHelper {
	private Optionor optionor;
	private Optionor optionor2;
	private Panel panel;
	private ClassifyItemAdapter adapter = new ClassifyItemAdapter(null);
	private ListView listView;
	private View footerView;
	private final int MAX_ITEM_DOWNLOAD = 1;
	private boolean onReload = false;
	private boolean moreData = true;
	private String selectedType = null;
	private String selectedLocation = null;
	
	public void freshList(List<Activity> activities) {
		try {
			adapter.setActivities(activities);
			adapter.notifyDataSetChanged();
		} catch (Exception e) {
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		Context theme = new ContextThemeWrapper(getActivity(), R.style.TheLight);
		LayoutInflater localInflater = inflater.cloneInContext(theme);
		View view = inflater.inflate(R.layout.frag_classify, container, false);
		panel = (Panel) view.findViewById(R.id.classify_panel);
		optionor = (Optionor) view.findViewById(R.id.optionor1);
		optionor2 = (Optionor) view.findViewById(R.id.optionor2);
		listView = (ListView) view.findViewById(R.id.list_classify);
		ApplicationHelper application = ((ApplicationHelper) getActivity()
				.getApplication());
		application.getCurrentUser();
		application.setOnUserChanged(new ApplicationHelper.OnUserChanged() {
			@Override
			public void onUserChanged(User newUser) {
				reDownload();
			}
		});
		panel.setOnPanelListener(new OnPanelListener() {

			@Override
			public void onPanelOpened(Panel panel) {
				// TODO Auto-generated method stub
				((MainActivity) getActivity()).setViewPagerInterceptable(false);
			}

			@Override
			public void onPanelClosed(Panel panel) {
				// TODO Auto-generated method stub
				((MainActivity) getActivity()).setViewPagerInterceptable(true);
			}

			@Override
			public void onTouch(Panel panel) {
				// TODO Auto-generated method stub
				((MainActivity) getActivity()).setViewPagerInterceptable(false);
			}
		});
		optionor.setOnCheckedChangedListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				RadioButton button = (RadioButton) group
						.findViewById(checkedId);
				if (button != null) {
					selectedType = button.getText().toString();
					try {
						reloadActivities(selectedType, null);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		});

		initListView();
		view.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (panel.isOpen())
					panel.setOpen(false, true);

			}
		});
		return view;
	}

	private void initListView() {
		
		footerView = LayoutInflater.from(getActivity()).inflate(R.layout.item_classify_footer, null);
		
		listView.addFooterView(footerView);
		listView.setAdapter(adapter);
		footerView.setVisibility(View.INVISIBLE);

		listView.setOnScrollListener(new OnScrollListener() {
			
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// TODO Auto-generated method stub
				Log.v("MonScroll", "state "+scrollState);
			}
			
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				// TODO Auto-generated method stub
				Log.v("MonScroll", "first "+firstVisibleItem);
				Log.v("MonScroll", "visible "+visibleItemCount);
				Log.v("MonScroll", "totalItemCount "+totalItemCount);
				
				if(totalItemCount != 0+listView.getFooterViewsCount() && view.getLastVisiblePosition()+1 != totalItemCount && !moreData)
				{
					moreData = true;
					listView.addFooterView(footerView);
				}
				
				if(totalItemCount != 0 && view.getLastVisiblePosition()+1 == totalItemCount && !onReload && moreData)
				{
					onReload = true;
					footerView.setVisibility(View.VISIBLE);
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
						public void onEmptyReceived() {
							// TODO Auto-generated method stub
							moreData = false;
							listView.removeFooterView(footerView);
							Toast.makeText(getActivity(), "没有更多的数据了", Toast.LENGTH_SHORT).show();
						}
						@Override
						public void onFinish() {
							listView.postDelayed(new Runnable() {
								@Override
								public void run() {
									try {
										if(moreData)
										{
											footerView.setVisibility(View.INVISIBLE);
											reloadActivities(selectedType, selectedLocation);
										}
										onReload = false;
									} catch (Exception e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
								}
							}, 1000);
							
						}
						
					};
					

					DatabaseConnector connector = new DatabaseConnector();
					connector.addParams(DatabaseConnector.METHOD, "GETACTIVITY");
					connector.addParams("limit", Integer.toString(MAX_ITEM_DOWNLOAD));
					connector.addParams("offset", Integer.toString(totalItemCount));
					connector.asyncConnect(activityAdapter);
				}
			}
		});
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "分类";
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
	public void reDownload() {
		// TODO Auto-generated method stub
		optionor.removeAllString();

		final MessageAdapter typeAdapter = new MessageAdapter() {
			@Override
			public void onRcvJSONArray(JSONArray array) {
				Set<Type> types = new HashSet<Type>();
				for (int i = 0; i < array.length(); i++) {
					try {
						Type type = new Type();
						JSONObject obj = array.getJSONObject(i);
						type.transJSON(obj);
						types.add(type);
					} catch (Exception e) {
						// 如果程序退出时仍有网络通讯，此处可能会有异常，不用处理。
					}

				}
				Entity.insertIntoSQLite(types, getActivity());
			}

			@Override
			public void onFinish() {
				reLoad();
			}
		};

		MessageAdapter attentionAdapter = new MessageAdapter() {

			@Override
			public void onRcvJSONArray(JSONArray array) {
				Set<Attention> attentions = new HashSet<Attention>();
				for (int i = 0; i < array.length(); i++) {
					try {
						Attention attention = new Attention();
						JSONObject obj = array.getJSONObject(i);
						attention.transJSON(obj);
						attentions.add(attention);
					} catch (Exception e) {
						// TODO: handle exception
					}

				}
				Entity.insertIntoSQLite(attentions, getActivity());
			}

			@Override
			public void onFinish() {
				// TODO Auto-generated method stub
				DatabaseConnector connector = new DatabaseConnector();
				connector.addParams(DatabaseConnector.METHOD, "GETTYPE");
				connector.asyncConnect(typeAdapter);
			}

		};

		User user = ((ApplicationHelper) getActivity().getApplication())
				.getCurrentUser();

		DatabaseConnector connector = new DatabaseConnector();
		connector.addParams(DatabaseConnector.METHOD, "GETATTENTION");
		if (user != null)
			connector.addParams("userID", user.getId().toString());
		connector.asyncConnect(attentionAdapter);

	}

	@Override
	public void reLoad() {
		// TODO Auto-generated method stub
		optionor.removeAllString();
		optionor2.removeAllString();
		optionor.addString("全部");
		List<ContentValues> list = Entity.selectFromSQLite("type",
				new String[] { "name" }, getActivity());
		for (ContentValues contentValue : list) {
			optionor.addString(contentValue.getAsString("name"));
		}
		optionor2.addString("aaa");
		optionor2.addString("bbb");
	}

	public void reloadActivities(String type, String location) throws Exception {
		List<Activity> activities = new ArrayList<Activity>();

		((ApplicationHelper) getActivity().getApplication())
				.getCurrentUser();

		if (type.equals("全部")) {
			type = "%";
		}

		List<ContentValues> list = Entity.selectFromSQLite("activity",
				new String[] { "id", "name", "address", "picture_url", "date",
						"type", "theme", "temperature", "reporter_info","content","procedure" },
				"type like ?", new String[] { type }, getActivity());

		List<ContentValues> attentionList = Entity.selectFromSQLite(
				"attention", new String[] { "ActivityID" }, getActivity());

		for (ContentValues contentValue : list) {
			Activity activity = new Activity();
			activity.setId(contentValue.getAsInteger("id"));
			activity.setName(contentValue.getAsString("name"));
			activity.setAddress(contentValue.getAsString("address"));
			try {
				activity.setDate(new SimpleDateFormat("yyyy-MM-dd").parse(contentValue.getAsString("date")));
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				activity.setTime(new SimpleDateFormat("HH:mm:ss").parse(contentValue.getAsString("time")));
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			activity.setPictureUrl(contentValue.getAsString("picture_url"));
			activity.setReporterInfo(contentValue.getAsString("reporter_info"));
			activity.setTheme(contentValue.getAsString("theme"));
			activity.setType(contentValue.getAsString("type"));
			activity.setTemperature(Integer.parseInt(contentValue.getAsString("temperature")));
			activity.setContent(contentValue.getAsString("content"));
			activity.setProcedure(contentValue.getAsString("procedure"));
			
			activity.setisAttention(0);
			for (ContentValues attentionCV : attentionList) {
				if (activity.getId() == attentionCV.getAsInteger("ActivityID")) {
					activity.setisAttention(1);
					break;
				}
			}
			activities.add(activity);

		}
		freshList(activities);
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		reLoad();
		if (firstIn())
			reDownload();
	}

}
