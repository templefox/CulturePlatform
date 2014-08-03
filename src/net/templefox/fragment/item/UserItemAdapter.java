package net.templefox.fragment.item;

import java.text.SimpleDateFormat;
import java.util.List;

import net.templefox.cultureplatform.ApplicationHelper;
import net.templefox.cultureplatform.DetailActivity;
import net.templefox.database.DatabaseConnector;
import net.templefox.database.data.Activity;
import net.templefox.widget.AsyncImageView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ToggleButton;

import net.templefox.cultureplatform.R;

@SuppressLint("SimpleDateFormat")
public class UserItemAdapter extends BaseAdapter{
	List<Activity> activities;
	
	/**
	 * 关注按钮的触发监听
	 * @author Administrator
	 *
	 */	
	public void setActivities(List<Activity> activities) {
		this.activities = activities;
	}

	public UserItemAdapter(List<Activity> activities) {
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
		
		if(! myViews.asyImageView.getDrawable().equals(R.drawable.default_pic)){
			myViews.asyImageView.setImageResource(R.drawable.default_pic);
		}
		
		
		
		final Activity currentActivity = activities.get(position);

		String image_url = DatabaseConnector.url+currentActivity.getPictureUrl();
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


		myViews.toggleButton.setVisibility(View.GONE);


		myViews.textViewTitle.setText(currentActivity.getName());
		myViews.textViewLocation.setText(currentActivity.getAddress());
		try {
			myViews.textViewDate.setText(new SimpleDateFormat("yyyy-MM-dd").format(currentActivity.getDate()));
		} catch (NullPointerException e) {
			Log.e("CP Error", e.getMessage());
			Log.w("CP Exception", Log.getStackTraceString(e));
		} catch (IllegalArgumentException e) {
			Log.e("CP Error", e.getMessage());
			Log.w("CP Exception", Log.getStackTraceString(e));
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