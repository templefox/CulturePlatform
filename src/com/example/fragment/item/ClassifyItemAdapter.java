package com.example.fragment.item;

import java.util.List;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.example.cultureplatform.DetailActivity;
import com.example.cultureplatform.R;
import com.example.database.DatabaseConnector;
import com.example.database.MessageAdapter;
import com.example.database.data.Activity;
import com.example.database.data.Attention;
import com.example.database.data.Entity;
import com.example.database.data.User;

public class ClassifyItemAdapter extends BaseAdapter {
	List<Activity> activities;

	/**
	 * 关注按钮的触发监听
	 * 
	 * @author Administrator
	 * 
	 */
	private class onTakeAttentionListener implements
			CompoundButton.OnCheckedChangeListener {

		Activity activity;
		User user;

		public onTakeAttentionListener(Activity activity, User user) {
			this.activity = activity;
			this.user = user;
		}

		@Override
		public void onCheckedChanged(final CompoundButton buttonView,
				boolean isChecked) {

			if (user == null) {
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
		if (activities == null)
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

		if (convertView == null) {
			convertView = LayoutInflater.from(parent.getContext()).inflate(
					R.layout.item_classify, null);
		}
		Button button = (Button) convertView.findViewById(R.id.item_cla_button);
		TextView textView = (TextView) convertView
				.findViewById(R.id.item_cla_name);
		ToggleButton toggleButton = (ToggleButton) convertView
				.findViewById(R.id.item_cla_attention);
		


		final Activity currentActivity = activities.get(position);
		
		convertView.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(v.getContext(),DetailActivity.class);
				intent.putExtra("activity", currentActivity);
				v.getContext().startActivity(intent);
				return;
			}
		});
		
		if (currentActivity.getisAttention() == 1) {
			toggleButton.setOnCheckedChangeListener(null);
			toggleButton.setChecked(true);
			toggleButton.setEnabled(false);
		}
		toggleButton.setOnCheckedChangeListener(new onTakeAttentionListener(
				currentActivity, null));

		button.setText(currentActivity.getName());
		textView.setText(currentActivity.getName());

		return convertView;
	}

}