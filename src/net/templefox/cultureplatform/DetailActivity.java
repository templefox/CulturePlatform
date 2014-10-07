package net.templefox.cultureplatform;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.templefox.database.DatabaseConnector;
import net.templefox.database.MessageAdapter;
import net.templefox.database.SQLiteWorker;
import net.templefox.database.data.Activity;
import net.templefox.database.data.Comment;
import net.templefox.database.data.CurrentUser;
import net.templefox.database.data.User;
import net.templefox.widget.AsyncImageView;
import net.templefox.widget.ImageTextView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.OptionsMenuItem;
import org.androidannotations.annotations.ViewById;
import org.json.JSONArray;
import org.json.JSONException;

import com.koushikdutta.ion.Ion;

import net.templefox.cultureplatform.R;

import android.R.integer;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.ShareActionProvider;
import android.widget.TextView;
import android.widget.Toast;

@EActivity(R.layout.activity_detail)
@OptionsMenu(R.menu.detail)
@SuppressLint({ "SimpleDateFormat", "NewApi" })
public class DetailActivity extends android.app.Activity {
	@OptionsMenuItem(R.id.detail_share)
	MenuItem shareItem;

	private Activity currentActivity;

	@ViewById(R.id.area_comments)
	LinearLayout area;

	@Bean
	CurrentUser currentUser;

	@ViewById(R.id.date)
	ImageTextView dateTextView;

	@ViewById(R.id.address)
	ImageTextView locatTextView;

	@ViewById(R.id.type)
	ImageTextView typeTextView;

	@ViewById(R.id.theme)
	ImageTextView themeTextView;

	@ViewById(R.id.reporter)
	ImageTextView reporterTextView;

	@ViewById(R.id.temperature)
	ImageTextView temperatureTextView;

	@ViewById(R.id.clock)
	ImageTextView clockTextView;

	@ViewById(R.id.detail_content)
	TextView contentTextView;

	@ViewById(R.id.procedure)
	TextView procedureTextView;

	@ViewById(R.id.detail_scroll_view)
	ScrollView scrollView;

	@ViewById(R.id.image)
	ImageView imageView;
	String image_url = "";
	private ShareActionProvider shareActionProvider;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTheme(R.style.ActionBar);
		currentActivity = (Activity) getIntent().getSerializableExtra("activity");
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setTitle(currentActivity.getName());
	}

	@AfterViews
	protected void afterViews() {

		image_url = DatabaseConnector.urlPath + currentActivity.getPictureUrl();
		Ion.with(imageView).placeholder(R.drawable.default_pic).load(image_url);
		
		dateTextView.setValue("<img src='calendar'/> " + new SimpleDateFormat("yyyy-MM-dd").format(currentActivity.getDate()));
		clockTextView.setValue("<img src='clock'/> " + new SimpleDateFormat("HH:mm:ss").format(currentActivity.getDate()));
		locatTextView.setValue("<img src='home'/> " + currentActivity.getAddress());
		typeTextView.setValue("<img src='tag' /> " + currentActivity.getType());
		themeTextView.setValue("<img src='favorite' /> " + currentActivity.getTheme());
		reporterTextView.setValue("<img src='user' /> " + currentActivity.getReporterInfo());
		temperatureTextView.setValue("<img src='heart' /> " + Integer.toString(currentActivity.getTemperature()) + "℃");
		temperatureTextView.setTextColor(Color.RED);
		contentTextView.setText(currentActivity.getContent());
		procedureTextView.setText(currentActivity.getProcedure());

		scrollView.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					if (getCurrentFocus() != null && getCurrentFocus().getWindowToken() != null) {
						manager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
					}
				}
				return false;
			}
		});
	}
	
	@Click(R.id.detail_show_more)
	protected void clickShowMore(){
		Intent intent = new Intent(DetailActivity.this, CommentsActivity.class);
		intent.putExtra("activity", currentActivity);
		startActivity(intent);
	}
	
	@Click(R.id.detail_gallary_small)
	protected void clickGallarySmall(){
		GalleryActivity_.intent(this).start();
	}

	private void hintUserUnLogin() {
		Toast.makeText(DetailActivity.this, "您尚未登录", Toast.LENGTH_SHORT).show();
	}

	private void hintUserUnCheck() {
		Toast.makeText(DetailActivity.this, "您的账户尚未确认", Toast.LENGTH_SHORT).show();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Initial share item.
		// TODO
		MenuItem item = menu.findItem(R.id.detail_share);
		shareActionProvider = (ShareActionProvider) item.getActionProvider();
		Intent myIntent = new Intent();
		myIntent.setAction(Intent.ACTION_SEND);
		myIntent.putExtra(Intent.EXTRA_TEXT, currentActivity.getName());
		myIntent.setType("text/plain");
		shareActionProvider.setShareIntent(myIntent);
		return true;
	}

	@OptionsItem(R.id.detail_comment)
	protected void clickComment() {
		if (currentUser == null)
			hintUserUnLogin();
		else if (currentUser.getAuthority() == User.AUTHORITY_UNCHECK)
			hintUserUnCheck();
		else {
			Intent intent = new Intent(this, CommentActivity.class);
			intent.putExtra("activity", currentActivity);
			startActivity(intent);
		}
	}

	@OptionsItem(R.id.detail_rating)
	protected void clickRating() {
		if (currentUser == null)
			hintUserUnLogin();
		else if (currentUser.getAuthority() == User.AUTHORITY_UNCHECK)
			hintUserUnCheck();
		else {
			Intent intent1 = new Intent(this, RatingActivity.class);
			intent1.putExtra("activity", currentActivity);
			startActivity(intent1);
		}
	}

	@OptionsItem(android.R.id.home)
	protected void clickHome() {
		finish();
	}

	private void load() {
		List<ContentValues> list = SQLiteWorker.selectFromSQLite("comment", new String[] { "content" }, "ActivityID = ?",
				new String[] { currentActivity.getId().toString() }, this);
		List<Comment> comments = new ArrayList<Comment>();
		for (ContentValues value : list) {
			Comment comment = new Comment();
			comment.setContent(value.getAsString("content"));
			comments.add(comment);
		}
		addViews(comments);
	}

	private void download() {
		MessageAdapter adapter = new MessageAdapter() {
			@Override
			public void onRcvJSONArray(JSONArray array) {
				Set<Comment> comments = new HashSet<Comment>();
				for (int i = 0; i < array.length(); i++) {
					try {
						Comment comment = new Comment();
						comment.resolveJSON(array.getJSONObject(i));
						comments.add(comment);
					} catch (JSONException e) {
						Log.e("CP Error", e.getMessage());
						Log.w("CP Exception", Log.getStackTraceString(e));
					} catch (ParseException e) {
						Log.e("CP Error", e.getMessage());
						Log.w("CP Exception", Log.getStackTraceString(e));
					}
				}
				SQLiteWorker.insertIntoSQLite(comments, DetailActivity.this);
			}

			@Override
			public void onTimeout() {
				Toast.makeText(getApplicationContext(), "连接超时", Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onFinish() {
				load();
			}
		};

		DatabaseConnector connector = new DatabaseConnector();
		connector.addParams(DatabaseConnector.METHOD, "GETCOMMENT");
		connector.addParams("activity_id", currentActivity.getId().toString());
		connector.executeConnector(adapter);
	}

	private void addViews(List<Comment> comments) {
		area.removeAllViews();
		for (int i = 0; i < comments.size() && i < 2; i++) {
			Comment comment = comments.get(i);
			View view = LayoutInflater.from(this).inflate(R.layout.item_comment, area, false);
			TextView textView = (TextView) view.findViewById(R.id.item_comment_text);
			textView.setText(comment.getContent());
			area.addView(view);
		}
	}

}
