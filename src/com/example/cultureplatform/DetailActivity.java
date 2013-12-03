package com.example.cultureplatform;

import com.example.database.data.Activity;

import android.os.Bundle;
import android.view.Menu;
import android.widget.TextView;

public class DetailActivity extends android.app.Activity {
	private Activity currentActivity;
	private TextView textView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTheme(R.style.ActionBar);
		setContentView(R.layout.activity_detail);
		
		currentActivity = (Activity) getIntent().getSerializableExtra("activity");
		textView = (TextView) findViewById(R.id.detail_name);
		
		textView.setText(currentActivity.getName());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.detail, menu);
		return true;
	}

}
