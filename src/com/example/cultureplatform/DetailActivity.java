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

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ShareActionProvider;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("SimpleDateFormat")
public class DetailActivity extends android.app.Activity {
	private Activity currentActivity;
	private LinearLayout area;
	private EditText editText;
	private Button clear;
	private Button submit;
	private User currentUser;
	
	private TextView titleTextView;
	private TextView dateTextView;
	private TextView locatTextView;
	private TextView typeTextView;
	private TextView themeTextView;
	private TextView reporterTextView;
	private TextView temperatureTextView;
	
	private AsyncImageView imageView;
	String image_url = "http://i9.hexunimg.cn/2012-07-12/143481552.jpg";
	private ShareActionProvider shareActionProvider;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		currentActivity = (Activity) getIntent().getSerializableExtra("activity");
		
		
		setTheme(R.style.ActionBar);
		setContentView(R.layout.activity_detail);
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setTitle(currentActivity.getName());
		
		currentUser = ((ApplicationHelper)getApplication()).getCurrentUser();
	
		area = (LinearLayout) findViewById(R.id.area_comments);
		clear = (Button) findViewById(R.id.detail_button_clear);
		submit = (Button) findViewById(R.id.detail_button_submit);
		editText = (EditText) findViewById(R.id.detail_comment_text);
		
		clear.setVisibility(View.INVISIBLE);
		
		editText.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if(count!=0)
					clear.setVisibility(View.VISIBLE);
				else {
					clear.setVisibility(View.INVISIBLE);
				}
			}
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}
			@Override
			public void afterTextChanged(Editable s) {
			}
		});
		clear.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				v.setVisibility(View.INVISIBLE);
				editText.setText("");
			}
		});
		submit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(currentUser != null)
					submit();
				else {
					Toast.makeText(DetailActivity.this, "您尚未登录", Toast.LENGTH_SHORT).show();
				}
				editText.clearFocus();
				editText.setText("");
			}
		});
		
		titleTextView = (TextView) findViewById(R.id.title);
		dateTextView = (TextView) findViewById(R.id.date);
		locatTextView = (TextView) findViewById(R.id.address);
		typeTextView = (TextView) findViewById(R.id.type);
		themeTextView = (TextView) findViewById(R.id.theme);
		reporterTextView = (TextView) findViewById(R.id.reporter);
		temperatureTextView = (TextView) findViewById(R.id.temperature);
		imageView = (AsyncImageView) findViewById(R.id.image);
		
		imageView.asyncLoad(image_url);
		
		
		titleTextView.setText(currentActivity.getName());
		
		try {
			dateTextView.setText(new SimpleDateFormat("yyyy-MM-dd")
					.format(currentActivity.getDate()));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		locatTextView.setText(currentActivity.getAddress());
		//typeTextView.setText(currentActivity.getType());
	    //themeTextView.setText(currentActivity.getTheme());
	    //reporterTextView.setText(currentActivity.getReporterInfo());
	   // temperatureTextView.setText(currentActivity.getTemperature());		
		
		
		
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
	private void setShareIntent(){
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
			Intent intent = new Intent(this, CommentActivity.class);
			intent.putExtra("activity", currentActivity);
			startActivity(intent);
			break;
		case R.id.detail_rating:
			Intent intent1 = new Intent(this, RatingActivity.class);
			intent1.putExtra("activity", currentActivity);
			startActivity(intent1);
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
		reload();
		reDownload();
		
	}

	private void reload(){
		List<ContentValues> list = Entity.selectFromSQLite("comment", new String[]{"content"},
					"ActivityID = ?",new String[]{currentActivity.getId().toString()}, this);
		List<Comment> comments = new ArrayList<Comment>();
		for (ContentValues value : list) {
			Comment comment = new Comment();
			comment.setContent(value.getAsString("content"));
			comments.add(comment);
		}
		addViews(comments);
	}
	
	private void reDownload(){
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
						e.printStackTrace();
					} catch (ParseException e) {
						e.printStackTrace();
					}
				}
				Entity.insertIntoSQLite(comments, DetailActivity.this);
			}
			
			@Override
			public void onTimeout() {
				Toast.makeText(getApplicationContext(), "连接超时", Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onFinish() {
				reload();
			}
		};
		
		DatabaseConnector connector = new DatabaseConnector();
		connector.addParams(DatabaseConnector.METHOD, "GETCOMMENT");
		connector.addParams("activity_id", currentActivity.getId().toString());
		connector.asyncConnect(adapter);
	}

	private void addViews(List<Comment> comments){
		area.removeAllViews();
		for (Comment comment : comments) {
			View view = LayoutInflater.from(this).inflate(R.layout.item_comment, area,false);	
			TextView textView = (TextView) view.findViewById(R.id.item_comment_text);
			textView.setText(comment.getContent());
			area.addView(view);
		}
	}

	private void submit(){
		MessageAdapter adapter = new MessageAdapter() {

			@Override
			public void onDone(String ret) {
				Toast.makeText(DetailActivity.this, "评论成功", Toast.LENGTH_SHORT).show();
				reDownload();
			}
			
			@Override
			public void onTimeout() {
				Toast.makeText(getApplicationContext(), "连接超时", Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onErrorOccur() {
				Toast.makeText(DetailActivity.this, "评论失败，请联系管理员", Toast.LENGTH_SHORT).show();
			}
		};
		
		DatabaseConnector connector = new DatabaseConnector();
		connector.addParams(DatabaseConnector.METHOD, "ADDCOMMENT");
		connector.addParams("user_id", currentUser.getId().toString());
		connector.addParams("activity_id", currentActivity.getId().toString());
		connector.addParams("content", editText.getText().toString());
		connector.asyncConnect(adapter);
	}
}

