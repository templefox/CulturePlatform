package com.example.cultureplatform;

import com.example.database.data.Activity;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class DetailActivity extends android.app.Activity {
	private Activity currentActivity;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		currentActivity = (Activity) getIntent().getSerializableExtra("activity");
		
		setTheme(R.style.ActionBar);
		setContentView(R.layout.activity_detail);
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setTitle(currentActivity.getName());
		
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.detail, menu);
		
		
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
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

}
