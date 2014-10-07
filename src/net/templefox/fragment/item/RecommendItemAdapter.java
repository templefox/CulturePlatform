package net.templefox.fragment.item;

import java.util.List;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;
import org.androidannotations.annotations.ViewById;

import com.google.gson.JsonArray;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import net.templefox.cultureplatform.DetailActivity;
import net.templefox.cultureplatform.DetailActivity_;
import net.templefox.database.DatabaseConnector;
import net.templefox.database.data.Activity;
import net.templefox.database.data.Entity;
import net.templefox.misc.Encoder;
import net.templefox.misc.SortedUniqueList;
import net.templefox.widget.AsyncImageView;
import net.templefox.widget.staggeredGrid.StaggeredGridView;
import net.templefox.widget.staggeredGrid.util.DynamicHeightImageView;

import net.templefox.cultureplatform.R;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

@EBean
public class RecommendItemAdapter extends BaseAdapter{
	SortedUniqueList<Activity> activities = new SortedUniqueList<Activity>();
	@RootContext
	android.app.Activity rootContext;

	
	public void setActivities(SortedUniqueList<Activity> activities) {
		this.activities = activities;
	}	
	
	public SortedUniqueList<Activity> getActivities() {
		return activities;
	}

	public RecommendItemAdapter() {
		super();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if(activities==null)
			return 0;
		return activities.size()+1;
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
			viewHolder.imageView = (DynamicHeightImageView) convertView.findViewById(R.id.item_recommend_image);
			viewHolder.name = (TextView) convertView.findViewById(R.id.item_recommend_name);
			convertView.setTag(viewHolder);
		}else {
			viewHolder = (ViewHolder) convertView.getTag();
			viewHolder.imageView.setVisibility(View.VISIBLE);
		}
		
		if (position==0) {
			viewHolder.name.setText("Title");
			viewHolder.imageView.setVisibility(View.GONE);
			
			return convertView;
		}else {
			--position;
		}
		
		final Activity activity = activities.get(position);
		
		convertView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = DetailActivity_.intent(v.getContext()).get();
				intent.putExtra("activity", activity);
				v.getContext().startActivity(intent);
				return;
			}
		});
		
		String image_url = DatabaseConnector.urlPath+activity.getPictureUrl();
		
		Ion.with(viewHolder.imageView).placeholder(R.drawable.default_pic).deepZoom().load(image_url);
		viewHolder.name.setText(activity.getName());
		
		return convertView;
	}
	
	private class ViewHolder{
		DynamicHeightImageView imageView;
		TextView name;
	}
}