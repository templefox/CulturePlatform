package com.example.cultureplatform;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.json.JSONArray;
import org.json.JSONException;

import com.example.database.DatabaseConnector;
import com.example.database.MessageAdapter;
import com.example.database.data.Activity;
import com.example.database.data.Comment;
import com.example.database.data.Entity;
import com.example.database.data.User;
import com.example.widget.AsyncImageView;
import com.example.widget.ImageTextView;

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
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.ShareActionProvider;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("SimpleDateFormat")
public class DetailActivity extends android.app.Activity {
	private Activity currentActivity;
	private LinearLayout area;
	private User currentUser;

	private ImageTextView dateTextView;
	private ImageTextView locatTextView;
	private ImageTextView typeTextView;
	private ImageTextView themeTextView;
	private ImageTextView reporterTextView;
	private ImageTextView temperatureTextView;
	private ImageTextView clockTextView;
	private TextView contentTextView;
	private TextView procedureTextView;
	private Button showMore;
	private ScrollView scrollView;
	private View gallary_small;

	private AsyncImageView imageView;
	String image_url = "";
	private ShareActionProvider shareActionProvider;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		currentActivity = (Activity) getIntent().getSerializableExtra(
				"activity");

		setTheme(R.style.ActionBar);
		setContentView(R.layout.activity_detail);
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setTitle(currentActivity.getName());

		currentUser = ((ApplicationHelper) getApplication()).getCurrentUser();

		area = (LinearLayout) findViewById(R.id.area_comments);

		image_url = DatabaseConnector.target_url
				+ currentActivity.getPictureUrl();
		dateTextView = (ImageTextView) findViewById(R.id.date);
		locatTextView = (ImageTextView) findViewById(R.id.address);
		typeTextView = (ImageTextView) findViewById(R.id.type);
		themeTextView = (ImageTextView) findViewById(R.id.theme);
		reporterTextView = (ImageTextView) findViewById(R.id.reporter);
		clockTextView = (ImageTextView) findViewById(R.id.clock);
		temperatureTextView = (ImageTextView) findViewById(R.id.temperature);
		contentTextView = (TextView) findViewById(R.id.detail_content);
		procedureTextView = (TextView) findViewById(R.id.procedure);
		imageView = (AsyncImageView) findViewById(R.id.image);
		showMore = (Button) findViewById(R.id.detail_show_more);
		scrollView = (ScrollView) findViewById(R.id.detail_scroll_view);
		gallary_small = findViewById(R.id.detail_gallary_small);

		imageView.asyncLoad(image_url);

		try {
			dateTextView.setValue("<img src='calendar'/> "
					+ new SimpleDateFormat("yyyy-MM-dd").format(currentActivity
							.getDate()));
		} catch (NullPointerException e) {
			Log.e("CP Error", e.getMessage());
			Log.w("CP Exception", Log.getStackTraceString(e));
		} catch (IllegalArgumentException e) {
			Log.e("CP Error", e.getMessage());
			Log.w("CP Exception", Log.getStackTraceString(e));
		}

		try {
			clockTextView.setValue("<img src='clock'/> "
					+ new SimpleDateFormat("HH:mm:ss").format(currentActivity
							.getTime()));
		} catch (NullPointerException e) {
			Log.e("CP Error", e.getMessage());
			Log.w("CP Exception", Log.getStackTraceString(e));
		} catch (IllegalArgumentException e) {
			Log.e("CP Error", e.getMessage());
			Log.w("CP Exception", Log.getStackTraceString(e));
		}

		locatTextView.setValue("<img src='home'/> "
				+ currentActivity.getAddress());
		typeTextView.setValue("<img src='tag' /> " + currentActivity.getType());
		themeTextView.setValue("<img src='favorite' /> "
				+ currentActivity.getTheme());
		reporterTextView.setValue("<img src='user' /> "
				+ currentActivity.getReporterInfo());
		temperatureTextView.setValue("<img src='heart' /> "
				+ Integer.toString(currentActivity.getTemperature()) + "℃");
		temperatureTextView.setTextColor(Color.RED);
		contentTextView.setText(currentActivity.getContent());

		procedureTextView.setText(currentActivity.getProcedure());
		scrollView.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					if (getCurrentFocus() != null
							&& getCurrentFocus().getWindowToken() != null) {
						manager.hideSoftInputFromWindow(getCurrentFocus()
								.getWindowToken(),
								InputMethodManager.HIDE_NOT_ALWAYS);
					}
				}
				return false;
			}
		});

		showMore.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(DetailActivity.this,
						CommentsActivity.class);
				intent.putExtra("activity", currentActivity);
				startActivity(intent);
			}
		});

		gallary_small.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(DetailActivity.this,
						GalleryActivity.class);
				startActivity(intent);
			}
		});
	}

	private void hintUserUnLogin() {
		Toast.makeText(DetailActivity.this, "您尚未登录", Toast.LENGTH_SHORT).show();
	}

	private void hintUserUnCheck() {
		Toast.makeText(DetailActivity.this, "您的账户尚未确认", Toast.LENGTH_SHORT)
				.show();
	}

	@SuppressLint("NewApi")
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.detail, menu);
		MenuItem item = menu.findItem(R.id.detail_share);
		shareActionProvider = (ShareActionProvider) item.getActionProvider();
		setShareIntent();
		return true;
	}

	@SuppressLint("NewApi")
	private void setShareIntent() {
		Intent myIntent = new Intent();
		myIntent.setAction(Intent.ACTION_SEND);
		myIntent.putExtra(Intent.EXTRA_TEXT, currentActivity.getName());
		myIntent.setType("text/plain");
		shareActionProvider.setShareIntent(myIntent);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {

		case R.id.detail_comment:
			if (currentUser == null)
				hintUserUnLogin();
			else if (currentUser.getAuthority() == User.AUTHORITY_UNCHECK)
				hintUserUnCheck();
			else {
				Intent intent = new Intent(this, CommentActivity.class);
				intent.putExtra("activity", currentActivity);
				startActivity(intent);
			}
			break;
		case R.id.detail_rating:
			if (currentUser == null)
				hintUserUnLogin();
			else if (currentUser.getAuthority() == User.AUTHORITY_UNCHECK)
				hintUserUnCheck();
			else {
				Intent intent1 = new Intent(this, RatingActivity.class);
				intent1.putExtra("activity", currentActivity);
				startActivity(intent1);
			}
			break;
		case R.id.detail_setting:
			break;
		case R.id.detail_share:
			break;
		case android.R.id.home:
			finish();
			return true;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onResume() {
		super.onResume();
		load();
		download();

	}

	private void load() {
		List<ContentValues> list = Entity.selectFromSQLite("comment",
				new String[] { "content" }, "ActivityID = ?",
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
						comment.transJSON(array.getJSONObject(i));
						comments.add(comment);
					} catch (JSONException e) {
						Log.e("CP Error", e.getMessage());
						Log.w("CP Exception", Log.getStackTraceString(e));
					} catch (ParseException e) {
						Log.e("CP Error", e.getMessage());
						Log.w("CP Exception", Log.getStackTraceString(e));
					}
				}
				Entity.insertIntoSQLite(comments, DetailActivity.this);
			}

			@Override
			public void onTimeout() {
				Toast.makeText(getApplicationContext(), "连接超时",
						Toast.LENGTH_SHORT).show();
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
			View view = LayoutInflater.from(this).inflate(
					R.layout.item_comment, area, false);
			TextView textView = (TextView) view
					.findViewById(R.id.item_comment_text);
			textView.setText(comment.getContent());
			area.addView(view);
		}
	}

}
