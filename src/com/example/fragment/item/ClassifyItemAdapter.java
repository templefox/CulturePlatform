package com.example.fragment.item;

import java.text.SimpleDateFormat;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import com.example.cultureplatform.DetailActivity;
import com.example.cultureplatform.LoginActivity;
import com.example.cultureplatform.R;
import com.example.database.DatabaseConnector;
import com.example.database.MessageAdapter;
import com.example.database.data.Activity;
import com.example.database.data.Attention;
import com.example.database.data.Entity;
import com.example.database.data.User;
import com.example.widget.AsyncImageView;

@SuppressLint("SimpleDateFormat")
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

		ViewHolder myViews;
		
		
		if (convertView == null) {
			
			myViews = new ViewHolder();
			convertView = LayoutInflater.from(parent.getContext()).inflate(
					R.layout.item_classify, parent,false);
			myViews.asyImageView = (AsyncImageView)convertView.findViewById(R.id.item_cla_image);
			myViews.textViewTitle = (TextView) convertView
					.findViewById(R.id.item_cla_title);
			myViews.textViewLocation =  (TextView) convertView
					.findViewById(R.id.item_cla_location);
			myViews.textViewDate = (TextView) convertView
					.findViewById(R.id.item_cla_date);
			myViews.toggleButton = (ToggleButton) convertView
					.findViewById(R.id.item_cla_attention);
			convertView.setTag(myViews);
			
			
		}else {
			myViews = (ViewHolder ) convertView.getTag();
			myViews.asyImageView.cancelTask();
		}
		
		if(! myViews.asyImageView.getDrawable().equals(R.drawable.rihanna)){
			myViews.asyImageView.setImageResource(R.drawable.rihanna);
		}
		
		
		
		final Activity currentActivity = activities.get(position);

		String image_url = DatabaseConnector.target_url+currentActivity.getPictureUrl();
		//String image_url = "http://i9.hexunimg.cn/2012-07-12/143481552.jpg";

		
		myViews.asyImageView.asyncLoad(image_url);

		

		convertView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(v.getContext(), DetailActivity.class);
				intent.putExtra("activity", currentActivity);
				v.getContext().startActivity(intent);
				return;
			}
		});

		if (currentActivity.getisAttention() == 1) {
			myViews.toggleButton.setOnCheckedChangeListener(null);
			myViews.toggleButton.setChecked(true);
			myViews.toggleButton.setEnabled(false);
		}else {
			myViews.toggleButton.setOnCheckedChangeListener(new onTakeAttentionListener(
				currentActivity, null));
			myViews.toggleButton.setChecked(false);
			myViews.toggleButton.setEnabled(true);
		}


		myViews.textViewTitle.setText(currentActivity.getName());
		myViews.textViewLocation.setText(currentActivity.getAddress());
		try {
			myViews.textViewDate.setText(new SimpleDateFormat("yyyy-MM-dd").format(currentActivity.getDate()));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return convertView;
	}
	
	private class ViewHolder {
		TextView textViewTitle;
		TextView textViewLocation;
		TextView textViewDate;
		ToggleButton toggleButton;
		AsyncImageView asyImageView;
		
	}
	
}