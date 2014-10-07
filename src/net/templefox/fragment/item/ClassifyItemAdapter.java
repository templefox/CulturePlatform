package net.templefox.fragment.item;

import java.util.ArrayList;
import java.util.List;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.BeforeTextChange;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;
import org.androidannotations.annotations.UiThread;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.Cancellable;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import net.templefox.cultureplatform.ApplicationHelper;
import net.templefox.cultureplatform.DetailActivity;
import net.templefox.cultureplatform.DetailActivity_;
import net.templefox.database.DataLoader;
import net.templefox.database.DatabaseConnector;
import net.templefox.database.IonRemoteLoader;
import net.templefox.database.LoadDataListener;
import net.templefox.database.MessageAdapter;
import net.templefox.database.SQLiteWorker;
import net.templefox.database.data.Activity;
import net.templefox.database.data.Attention;
import net.templefox.database.data.CurrentUser;
import net.templefox.database.data.Entity;
import net.templefox.database.data.User;
import net.templefox.misc.DateFormater;
import net.templefox.misc.Encoder;
import net.templefox.misc.SortedUniqueList;
import net.templefox.widget.AsyncImageView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import net.templefox.cultureplatform.R;

@SuppressLint("SimpleDateFormat")
@EBean
public class ClassifyItemAdapter extends BaseAdapter implements DataLoader {
	private SortedUniqueList<Activity> activities = new SortedUniqueList<Activity>();
	/*
	 * private int currentPosition = 0; private int BLOCK_NUMBER = 2; private
	 * boolean upfresh = true;
	 */
	@RootContext
	android.app.Activity rootContext;

	@Bean
	CurrentUser currentUser;

	@Bean
	IonRemoteLoader remoteLoader;

	public void setActivities(List<Activity> activities) {
		this.activities = new SortedUniqueList<Activity>(activities);
	}

	public SortedUniqueList<Activity> getActivities() {
		return activities;
	}

	@Override
	public int getCount() {
		return (activities == null) ? 0 : activities.size();
	}

	@Override
	public Object getItem(int position) {
		return activities.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder view;

		if (convertView == null) {
			convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_classify, parent, false);
			view = new ViewHolder();
			view.imageView = (ImageView) convertView.findViewById(R.id.item_cla_image);
			view.textViewTitle = (TextView) convertView.findViewById(R.id.item_cla_title);
			view.textViewLocation = (TextView) convertView.findViewById(R.id.item_cla_location);
			view.textViewDate = (TextView) convertView.findViewById(R.id.item_cla_date);
			view.toggleButton = (ToggleButton) convertView.findViewById(R.id.item_cla_attention);
			convertView.setTag(view);
		} else {
			view = (ViewHolder) convertView.getTag();
			if (!view.imageLoaderCancellable.isDone()) {
				view.imageLoaderCancellable.cancel();
			}
		}

		final Activity currentActivity = activities.get(position);

		String image_url = DatabaseConnector.urlPath + currentActivity.getPictureUrl();

		convertView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = DetailActivity_.intent(v.getContext()).get();
				intent.putExtra("activity", currentActivity);
				v.getContext().startActivity(intent);
				return;
			}
		});

		checkIsAttention(currentUser, currentActivity, view.toggleButton);

		view.textViewTitle.setText(currentActivity.getName());
		view.textViewLocation.setText(currentActivity.getAddress());
		view.textViewDate.setText(DateFormater.format(currentActivity.getDate()).subSequence(0, 10));
		view.imageLoaderCancellable = Ion.with(view.imageView).placeholder(R.drawable.default_pic).deepZoom().load(image_url);
		/*
		 * try { view.textViewDate.setText(new
		 * SimpleDateFormat("yyyy-MM-dd").format(currentActivity.getDate())); }
		 * catch (NullPointerException e) { Log.e("CP Error", e.getMessage());
		 * Log.w("CP Exception", Log.getStackTraceString(e)); } catch
		 * (IllegalArgumentException e) { Log.e("CP Error", e.getMessage());
		 * Log.w("CP Exception", Log.getStackTraceString(e)); }
		 */
		return convertView;
	}

	private void checkIsAttention(final CurrentUser user, final Activity currentActivity, final ToggleButton toggleButton) {
		remoteLoader.selectRemoteDate(
				String.format("SELECT * FROM cultureplatform.attention_activity_user where user_id = %s and activity_id = %s;",
						user.getId(), currentActivity.getId()), new FutureCallback<JsonArray>() {

					@Override
					public void onCompleted(Exception arg0, JsonArray arg1) {
						if (arg0 != null) {
							Toast.makeText(rootContext, arg0.getMessage(), Toast.LENGTH_SHORT).show();
						} else if (arg1.size() == 0) {
							setToggleButton(false, toggleButton, currentActivity, user);
						} else if (arg1.size() == 1) {
							setToggleButton(true, toggleButton, currentActivity, user);
						} else {
							throw new IllegalStateException("Why you have more than 1 attention relationship??");
						}
					}
				});
	}

	@UiThread
	protected void setToggleButton(boolean isAttention, ToggleButton toggleButton, Activity currentActivity, User user) {
		if (isAttention) {
			toggleButton.setOnCheckedChangeListener(null);
			toggleButton.setChecked(true);
			toggleButton.setEnabled(false);
		} else {
			toggleButton.setOnCheckedChangeListener(new onTakeAttentionListener(currentActivity, user));
			toggleButton.setChecked(false);
			toggleButton.setEnabled(true);
		}

	}

	private class ViewHolder {
		TextView textViewTitle;
		TextView textViewLocation;
		TextView textViewDate;
		ToggleButton toggleButton;
		ImageView imageView;
		Cancellable imageLoaderCancellable;
	}

	/**
	 * 关注按钮的触发监听类
	 */
	private class onTakeAttentionListener implements CompoundButton.OnCheckedChangeListener {

		Activity activity;
		User user;

		public onTakeAttentionListener(Activity activity, User user) {
			this.activity = activity;
			this.user = user;
		}

		@Override
		public void onCheckedChanged(final CompoundButton buttonView, boolean isChecked) {
			if (isChecked == false)
				return;

			if (user == null) {
				buttonView.setChecked(false);
				Toast.makeText(buttonView.getContext(), "登录后可进行关注", Toast.LENGTH_SHORT).show();
				return;
			}
			if (user.getAuthority() == User.AUTHORITY_UNCHECK) {
				buttonView.setChecked(false);
				Toast.makeText(buttonView.getContext(), "您的账户尚未确认", Toast.LENGTH_SHORT).show();
				return;
			}
			
			remoteLoader.insertRemoteDate(String.format("INSERT INTO `cultureplatform`.`attention` (`user_id`, `activity_id`) VALUES ('%s', '%s');",user.getId(),activity.getId()), new FutureCallback<JsonObject>() {

				@Override
				public void onCompleted(Exception arg0, JsonObject arg1) {
					if (arg0 != null) {
						Toast.makeText(rootContext, arg0.getMessage(), Toast.LENGTH_SHORT).show();
					} else if (arg1.get("result").getAsString().equals("error")) {
						Toast.makeText(rootContext, arg1.get("error").getAsString(), Toast.LENGTH_LONG)
						.show();
					} else if (arg1.get("result").getAsString().equals("success")) {
						setToggleButton(true, (ToggleButton)buttonView, activity, user);
					} else {
						throw new IllegalStateException("Neither error nor success?");
					}
				}
			});
		}
	}

	@Deprecated
	@Override
	public List<? extends Entity> loadLocalData(LoadDataListener listener) {
		List<Activity> list = new ArrayList<Activity>();
		return list;
	}

	@Deprecated
	@Override
	public List<? extends Entity> loadRemoteDate(FutureCallback<JsonArray> callback, String query) {
		if (callback == null || query == null || query.equals("")) {
			return null;
		}
		/*
		 * int offset = upfresh ? currentPosition : 0; int limit = upfresh ?
		 * BLOCK_NUMBER : currentPosition;
		 */
		Ion.with(rootContext).load(DatabaseConnector.url).setBodyParameter(DatabaseConnector.METHOD, DatabaseConnector.M_SELECT)
				.setBodyParameter(DatabaseConnector.QUERY, Encoder.encodeString(query)).asJsonArray().setCallback(callback);
		return null;
	}

	/*
	 * "select id_activity,name,datetime from activity limit %s,%s", offset,
	 * limit
	 * 
	 * new FutureCallback<JsonArray>() {
	 * 
	 * @Override public void onCompleted(Exception arg0, JsonArray arg1) { if
	 * (arg0 != null) { Toast.makeText(rootContext, arg0.getMessage(),
	 * Toast.LENGTH_SHORT).show(); } else if (arg1.size() < 1) {
	 * Toast.makeText(rootContext, "No any activity!",
	 * Toast.LENGTH_SHORT).show(); } else { currentPosition += upfresh ?
	 * arg1.size() : 0; for (int i = 0; i < arg1.size(); i++) { Activity
	 * activity = new Activity(); JsonObject object =
	 * arg1.get(i).getAsJsonObject();
	 * activity.setId(object.get("id_activity").getAsInt());
	 * activity.setName(object.get("name").getAsString());
	 * activity.setDate(DateFormater
	 * .parse(object.get("datetime").getAsString())); activities.add(activity);
	 * } } } }
	 */

	/*
	 * public List<? extends Entity> loadRemoteDate(boolean isContinueLoad,
	 * final LoadDataListener listener) { this.upfresh = isContinueLoad; return
	 * loadRemoteDate(listener); }
	 */
}