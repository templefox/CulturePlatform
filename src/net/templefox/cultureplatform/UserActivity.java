package net.templefox.cultureplatform;

import net.templefox.database.data.User;

import com.example.cultureplatform.R;

import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class UserActivity extends Activity {
	private User currentUser;
	private Button loginButton;
	private Button logOutButton;
	private TextView nameView;
	private View userView;
	private View noUserView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTheme(R.style.ActionBar);
		setContentView(R.layout.activity_user);
		
		ActionBar actionBar = getActionBar();
		actionBar.setTitle("��������");
		actionBar.setDisplayHomeAsUpEnabled(true);
		
		loginButton = (Button) findViewById(R.id.button_log_in);
		logOutButton = (Button)findViewById(R.id.button_log_out);
		userView = findViewById(R.id.user_view);
		noUserView = findViewById(R.id.no_user_view);
		nameView = (TextView) findViewById(R.id.user_name);
		
		
		loginButton.setOnClickListener(loginListener);
		logOutButton.setOnClickListener(logOutListener);
	}

	@Override
	protected void onStart() {
		super.onStart();
		
		ApplicationHelper appHelper = (ApplicationHelper) getApplicationContext();
		
		if(!appHelper.isUserLogin())
		{
			//TODO ���û�е�ǰ�û���¼��Ϣ������ʾĳЩ��ť��
			noUserView.setVisibility(View.VISIBLE);
			userView.setVisibility(View.INVISIBLE);
		}
		else
		{
			//TODO ����е�ǰ�û���¼��Ϣ������ĳЩ��ť����ʾ�û���Ϣ��
			currentUser = appHelper.getCurrentUser();
			
			noUserView.setVisibility(View.INVISIBLE);
			userView.setVisibility(View.VISIBLE);
			
			nameView.setText(currentUser.getName());
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

	private OnClickListener logOutListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			((ApplicationHelper)getApplication()).setCurrentUser(null);
			SharedPreferences sp = getSharedPreferences("Setting",0);
			Editor ed = sp.edit();
			ed.putBoolean("autologin",false);
			ed.putString("Email", "");
			ed.putString("password", "");
			ed.commit();

			finish();
		}
	};
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) { 
        case android.R.id.home: 
            finish();
            return true; 
        default: 
            return super.onOptionsItemSelected(item);  
	    }
	}
}