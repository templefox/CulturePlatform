package com.example.fragment;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.json.JSONArray;
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
import com.example.widget.Optionor;
import com.example.widget.Panel;
import com.example.widget.Panel.OnPanelListener;

import android.app.Fragment;
import android.content.ContentValues;
import android.content.Context;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.ToggleButton;

public class ClassifyFragment extends FragmentHelper {
	private Optionor optionor;
	private Optionor optionor2;
	private Panel panel;
	private ClassifyItemAdapter adapter = new ClassifyItemAdapter(null);
	private ListView listView;
	
 	public void freshList(List<Activity> activities) {
 		try {
			adapter.setActivities(activities);
			listView.setAdapter(adapter);
		} catch (Exception e) {
			String s = e.getMessage();
		}
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		Context theme = new ContextThemeWrapper(getActivity(), R.style.TheLight);
		LayoutInflater localInflater = inflater.cloneInContext(theme);
		View view = inflater.inflate(R.layout.frag_classify, container,false);
		panel = (Panel) view.findViewById(R.id.classify_panel);
		optionor = (Optionor) view.findViewById(R.id.optionor1);	
		optionor2 = (Optionor)view.findViewById(R.id.optionor2);
		listView = (ListView)view.findViewById(R.id.list_classify);
		panel.setOnPanelListener(new OnPanelListener() {
			
			@Override
			public void onPanelOpened(Panel panel) {
				// TODO Auto-generated method stub
				((MainActivity)getActivity()).setViewPagerInterceptable(false);
			}
			
			@Override
			public void onPanelClosed(Panel panel) {
				// TODO Auto-generated method stub
				((MainActivity)getActivity()).setViewPagerInterceptable(true);
			}

			@Override
			public void onTouch(Panel panel) {
				// TODO Auto-generated method stub
				((MainActivity)getActivity()).setViewPagerInterceptable(false);
			}
		});
		optionor.setOnCheckedChangedListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				RadioButton button = (RadioButton)group.findViewById(checkedId);
				if(button!=null){
					String type = button.getText().toString();
					reloadActivities(type,null);
				}
			}
		});


		view.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(panel.isOpen())
					panel.setOpen(false, true);
				
			}
		});
		if(firstIn()){
			reLoad();
			reDownload();
		}

		
		return view;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "分类";
	}
	
	public void switchPanel() {
		if(panel.isOpen())
		{
			panel.setOpen(false, true);
			
		}
		else
		{
			panel.setOpen(true, true);
			
		}
	}
	
	public void open(boolean value,boolean anim){
		panel.setOpen(value, anim);
	}
	
	public boolean isOpen(){
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
						// TODO: handle exception
					}
								
				}
				Entity.insertIntoSQLite(types, getActivity());
			}

			@Override
			public void onFinish() {
				reLoad();
			}
		};

		DatabaseConnector connector = new DatabaseConnector();
		connector.addParams(DatabaseConnector.METHOD, "GETTYPE");
		connector.asyncConnect(typeAdapter);

	}

	@Override
	public void reLoad() {
		// TODO Auto-generated method stub
		optionor.removeAllString();
		optionor2.removeAllString();
		optionor.addString("全部");
		List<ContentValues> list = Entity.selectFromSQLite("type",new String[]{"name"},getActivity());
		for (ContentValues contentValue : list) {
			optionor.addString(contentValue.getAsString("name"));
		}
		optionor2.addString("aaa");
		optionor2.addString("bbb");
	}
	
	public void reloadActivities(String type,String location){
		List<Activity> activities = new ArrayList<Activity>();
		
		if(type.equals("全部")){
			type = "%";
		}
		
		List<ContentValues> list = Entity.selectFromSQLite("activity", new String[]{"id","name"}
					,"type like ?",new String[]{type}, getActivity());
		
		
		for(ContentValues contentValue: list){
			Activity activity = new Activity();
			activity.setId(contentValue.getAsInteger("id"));
			activity.setName(contentValue.getAsString("name"));
			activities.add(activity);
		}
		freshList(activities);
	}
	
	public static class ClassifyItemAdapter extends BaseAdapter{
		List<Activity> activities;
		
		/**
		 * 关注按钮的触发监听
		 * @author Administrator
		 *
		 */
		private class onTakeAttentionListener implements CompoundButton.OnCheckedChangeListener{
			
			Activity activity;
			User user;
			public onTakeAttentionListener(Activity activity,User user) {
				this.activity = activity;
				this.user = user;
			}
			@Override
			public void onCheckedChanged(final CompoundButton buttonView,
					boolean isChecked) {

				if(user == null){
					return;
				}
				DatabaseConnector connector = new DatabaseConnector();
				connector.addParams(DatabaseConnector.METHOD, "SETATTENTION");
				connector.addParams("user_id", user.getId().toString());
				connector.addParams("activity_id", activity.getId().toString());
				connector.asyncConnect(new MessageAdapter() {

					@Override
					public void onDone(String ret) {
						Attention attention = new Attention();
						attention.setId(Integer.getInteger(ret));
						attention.setActivityID(activity.getId());
						attention.setUser(user.getId());
						Entity.insertIntoSQLite(attention, buttonView.getContext());
						buttonView.setEnabled(false);
					}

					@Override
					public void onErrorOccur() {
						// TODO Auto-generated method stub
						super.onErrorOccur();
					}
				});
			}
		}
		
		public void setActivities(List<Activity> activities) {
			this.activities = activities;
		}

		public ClassifyItemAdapter(List<Activity> activities) {
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
			// TODO 在此初始化每一个item内的内容，添加item的交互功能。

			if(convertView == null){
				convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_classify, null);
			}
						Button button = (Button) convertView.findViewById(R.id.item_cla_button);
			TextView textView = (TextView) convertView.findViewById(R.id.item_cla_name);
			ToggleButton toggleButton = (ToggleButton)convertView.findViewById(R.id.item_cla_attention);
			
			Activity currentActivity = activities.get(position);
			User user = ((ApplicationHelper)parent.getContext().getApplicationContext()).getCurrentUser();
			if(user!=null)
			{
				List<ContentValues> values = Entity.selectFromSQLite("attention", new String[]{"ActivityID"},"UserID = ?",new String[]{user.getId().toString()}, parent.getContext());
				for (ContentValues value : values) {
					if(value.getAsInteger("ActivityID") == currentActivity.getId()){
						toggleButton.setOnCheckedChangeListener(null);
						toggleButton.setChecked(true);
						toggleButton.setEnabled(false);
					}
				}
			}
			toggleButton.setOnCheckedChangeListener(new onTakeAttentionListener(currentActivity,user));
			
			
			button.setText(currentActivity.getName());
			textView.setText(currentActivity.getName());
			
			return convertView;
		}
		
	}
}
