package com.example.cultureplatform;

import java.util.ArrayList;

import com.example.database.data.User;

import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class UserActivity extends Activity {
	private User currentUser;
	private Button loginButton;
	private TextView nameView;
	private View userView;
	private View noUserView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTheme(R.style.ActionBar);
		setContentView(R.layout.activity_user);
		
		ActionBar actionBar = getActionBar();
		actionBar.setTitle("个人中心");
		actionBar.setDisplayHomeAsUpEnabled(true);
		
		loginButton = (Button) findViewById(R.id.button_log_in);
		userView = findViewById(R.id.user_view);
		noUserView = findViewById(R.id.no_user_view);
		nameView = (TextView) findViewById(R.id.user_name);
		
		loginButton.setOnClickListener(loginListener);
	}

	@Override
	protected void onStart() {
		super.onStart();
		
		ApplicationHelper appHelper = (ApplicationHelper) getApplicationContext();
		
		if(!appHelper.isUserLogin())
		{
			//TODO 如果没有当前用户登录信息，则显示某些按钮。
			noUserView.setVisibility(View.VISIBLE);
			userView.setVisibility(View.INVISIBLE);
		}
		else
		{
			//TODO 如果有当前用户登录信息，隐藏某些按钮，显示用户信息。
			currentUser = appHelper.getCurrentUser();
			
			noUserView.setVisibility(View.INVISIBLE);
			userView.setVisibility(View.VISIBLE);
			
			nameView.setText(currentUser.getNickname());
		}
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.user, menu);
		return true;
	}
	
	
	
	
	private OnClickListener loginListener = new OnClickListener() {		
		@Override
		public void onClick(View v) {
			Intent intent = new Intent(UserActivity.this,LoginActivity.class);  
			startActivity(intent);
		}
	};

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) { 
        case android.R.id.home: 
            Intent intent = new Intent(this, MainActivity.class); 
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); 
            startActivity(intent);
            return true; 
        default: 
            return super.onOptionsItemSelected(item);  
	    }
	}
}
