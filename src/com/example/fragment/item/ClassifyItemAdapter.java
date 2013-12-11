package com.example.fragment.item;

import java.io.InputStream;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;
import com.example.cultureplatform.DetailActivity;
import com.example.cultureplatform.HttpUtils;
import com.example.cultureplatform.R;
import com.example.database.DatabaseConnector;
import com.example.database.MessageAdapter;
import com.example.database.data.Activity;
import com.example.database.data.Attention;
import com.example.database.data.Entity;
import com.example.database.data.User;
import com.example.widget.AsyncImageView;

public class ClassifyItemAdapter extends BaseAdapter {
	List<Activity> activities;
	
	
	String pictureUrl = "http://c.hiphotos.baidu.com/image/w%3D2048/sign=cfe9bc1480cb39dbc1c06056e42e0824/b64543a98226cffc0c3a674cb8014a90f603ea38.jpg";

	/**
	 * ��ע��ť�Ĵ�������
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
		// TODO �ڴ˳�ʼ��ÿһ��item�ڵ����ݣ����item�Ľ������ܡ�

		if (convertView == null) {
			convertView = LayoutInflater.from(parent.getContext()).inflate(
					R.layout.item_classify, null);
		}
		
		final Activity currentActivity = activities.get(position);

		String image_url = DatabaseConnector.target_url+currentActivity.getPictureUrl();

		AsyncImageView asyImageView = (AsyncImageView)convertView.findViewById(R.id.item_cla_image);
		
		
		asyImageView.asyncLoad(image_url,asyImageView);


		TextView textViewTitle = (TextView) convertView
				.findViewById(R.id.item_cla_title);

		TextView textViewLocation = (TextView) convertView
				.findViewById(R.id.item_cla_location);

		TextView textViewDate = (TextView) convertView
				.findViewById(R.id.item_cla_date);

		ToggleButton toggleButton = (ToggleButton) convertView
				.findViewById(R.id.item_cla_attention);

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
			toggleButton.setOnCheckedChangeListener(null);
			toggleButton.setChecked(true);
			toggleButton.setEnabled(false);
		}
		toggleButton.setOnCheckedChangeListener(new onTakeAttentionListener(
				currentActivity, null));

		textViewTitle.setText(currentActivity.getName());
		textViewLocation.setText(currentActivity.getAddress());
		try {
			textViewDate.setText(new SimpleDateFormat("yyyy-MM-dd").format(currentActivity.getDate()));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return convertView;
	}

}