package com.example.cultureplatform;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.zip.Inflater;

import org.json.JSONArray;
import org.json.JSONException;

import com.example.database.DatabaseConnector;
import com.example.database.MessageAdapter;
import com.example.database.data.Activity;
import com.example.database.data.Comment;
import com.example.database.data.Entity;

import android.app.ActionBar;
import android.app.ExpandableListActivity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class DetailActivity extends android.app.Activity {
	private Activity currentActivity;
	private LinearLayout area;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		currentActivity = (Activity) getIntent().getSerializableExtra("activity");
		
		setTheme(R.style.ActionBar);
		setContentView(R.layout.activity_detail);
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setTitle(currentActivity.getName());
		
	
		area = (LinearLayout) findViewById(R.id.area_comments);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.detail, menu);
		
		
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.detail_attention:
			break;
		case R.id.detail_comment:
			Intent intent = new Intent(this, RatingActivity.class);
			intent.putExtra("activity", currentActivity);
			startActivity(intent);
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
		List<ContentValues> list = Entity.selectFromSQLite("comment", new String[]{"content"}, this);
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
		for (Comment comment : comments) {
			View view = LayoutInflater.from(this).inflate(R.layout.item_comment, area,false);	
			TextView textView = (TextView) view.findViewById(R.id.item_comment_text);
			textView.setText(comment.getContent());
			area.addView(view);
		}
	}
}

