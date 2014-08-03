package net.templefox.fragment.item;

import java.util.List;

import net.templefox.cultureplatform.DetailActivity;
import net.templefox.database.DatabaseConnector;
import net.templefox.database.data.Activity;
import net.templefox.widget.AsyncImageView;

import net.templefox.cultureplatform.R;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

public class RecommendItemAdapter extends BaseAdapter{
	List<Activity> activities;
	
	
	public void setActivities(List<Activity> activities) {
		this.activities = activities;
	}

	public RecommendItemAdapter(List<Activity> activities) {
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
		// TODO Auto-generated method stub
		ViewHolder viewHolder;
		if(convertView == null){
			viewHolder = new ViewHolder();
			convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recommend, parent,false);
			viewHolder.imageView = (AsyncImageView) convertView.findViewById(R.id.item_recommend_image);
			viewHolder.name = (TextView) convertView.findViewById(R.id.item_recommend_name);
			convertView.setTag(viewHolder);
		}else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		
		
		final Activity activity = activities.get(position);
		
		convertView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(v.getContext(), DetailActivity.class);
				intent.putExtra("activity", activity);
				v.getContext().startActivity(intent);
				return;
			}
		});
		
		String image_url = DatabaseConnector.url+activity.getPictureUrl();
		viewHolder.imageView.asyncLoad(image_url);
		//viewHolder.imageView.asyncLoad("http://i9.hexunimg.cn/2012-07-12/143481552.jpg");
		viewHolder.name.setText(activity.getName());
		
		return convertView;
	}
	
	private class ViewHolder{
		AsyncImageView imageView;
		TextView name;
	}
}