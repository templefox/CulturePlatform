package com.example.cultureplatform;

import java.text.ParseException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.database.DatabaseConnector;
import com.example.database.MessageAdapter;
import com.example.database.data.User;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Activity which displays a login screen to the user, offering registration as
 * well.
 */
public class LoginActivity extends Activity {
	/**
	 * A dummy authentication store containing known user names and passwords.
	 * TODO: remove after connecting to a real authentication system.
	 */
	private static final String[] DUMMY_CREDENTIALS = new String[] {
			"foo@example.com:hello", "bar@example.com:world" };

	/**
	 * Keep track of the login task to ensure we can cancel it if requested.
	 */

	// Values for email and password at the time of the login attempt.
	private String mEmail;
	private String mPassword;

	// UI references.
	private EditText mEmailView;
	private EditText mPasswordView;
	private View mLoginFormView;
	private View mLoginStatusView;
	private TextView mLoginStatusMessageView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTheme(R.style.ActionBar);
		setContentView(R.layout.activity_login);

		getActionBar().setDisplayHomeAsUpEnabled(true);

		initFindView();
		initListener();
	}

	private void initFindView() {
		mEmailView = (EditText) findViewById(R.id.email);
		mPasswordView = (EditText) findViewById(R.id.password);
		mLoginFormView = findViewById(R.id.login_form);
		mLoginStatusView = findViewById(R.id.login_status);
		mLoginStatusMessageView = (TextView) findViewById(R.id.login_status_message);
	}

	private void initListener() {
		mPasswordView
				.setOnEditorActionListener(new TextView.OnEditorActionListener() {
					@Override
					public boolean onEditorAction(TextView textView, int id,
							KeyEvent keyEvent) {
						if (id == R.id.login || id == EditorInfo.IME_NULL) {
							attemptLogin();
							return true;
						}
						return false;
					}
				});
		findViewById(R.id.sign_in_button).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						attemptLogin();
					}
				});
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}

	/**
	 * Attempts to sign in or register the account specified by the login form.
	 * If there are form errors (invalid email, missing fields, etc.), the
	 * errors are presented and no actual login attempt is made.
	 */
	public void attemptLogin() {
		
		//隐藏软键盘
		InputMethodManager imm =(InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE); 
		imm.hideSoftInputFromWindow(mPasswordView.getWindowToken(), 0); 
		
		
		// Reset errors.
		mEmailView.setError(null);
		mPasswordView.setError(null);

		// Store values at the time of the login attempt.
		mEmail = mEmailView.getText().toString();
		mPassword = mPasswordView.getText().toString();

		boolean cancel = false;
		View focusView = null;

		// Check for a valid password.
		if (TextUtils.isEmpty(mPassword)) {
			mPasswordView.setError(getString(R.string.error_field_required));
			focusView = mPasswordView;
			cancel = true;
		} else if (mPassword.length() < 0) {
			mPasswordView.setError(getString(R.string.error_invalid_password));
			focusView = mPasswordView;
			cancel = true;
		}

		// Check for a valid email address.
		if (TextUtils.isEmpty(mEmail)) {
			mEmailView.setError(getString(R.string.error_field_required));
			focusView = mEmailView;
			cancel = true;
		} else if (!mEmail.contains("@")) {
			mEmailView.setError(getString(R.string.error_invalid_email));
			focusView = mEmailView;
			cancel = true;
		}

		if (cancel) {
			// There was an error; don't attempt login and focus the first
			// form field with an error.
			focusView.requestFocus();
		} else {
			// Show a progress spinner, and kick off a background task to
			// perform the user login attempt.
			mLoginStatusMessageView.setText(R.string.login_progress_signing_in);
			showProgress(true);
/*			mAuthTask = new UserLoginTask();
			mAuthTask.execute((Void) null);*/
			
			login(mEmail,mPassword);
			
			
		}
	}

	public void login(String mEmail,String mPassword) {
		DatabaseConnector connector = new DatabaseConnector();
		connector.addParams(DatabaseConnector.METHOD, "GETUSER");
		connector.addParams("Email",mEmail);
		connector.addParams("password", mPassword);
		connector.asyncConnect(loginAdapter);
	}

	/**
	 * Shows the progress UI and hides the login form.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
	private void showProgress(final boolean show) {
		// On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
		// for very easy animations. If available, use these APIs to fade-in
		// the progress spinner.
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
			int shortAnimTime = getResources().getInteger(
					android.R.integer.config_shortAnimTime);

			mLoginStatusView.setVisibility(View.VISIBLE);
			mLoginStatusView.animate().setDuration(shortAnimTime)
					.alpha(show ? 1 : 0)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							mLoginStatusView.setVisibility(show ? View.VISIBLE
									: View.GONE);
						}
					});

			mLoginFormView.setVisibility(View.VISIBLE);
			mLoginFormView.animate().setDuration(shortAnimTime)
					.alpha(show ? 0 : 1)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							mLoginFormView.setVisibility(show ? View.GONE
									: View.VISIBLE);
						}
					});
		} else {
			// The ViewPropertyAnimator APIs are not available, so simply show
			// and hide the relevant UI components.
			mLoginStatusView.setVisibility(show ? View.VISIBLE : View.GONE);
			mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
		}
	}

	public void setAutoLogin()
	{
		SharedPreferences sp = getSharedPreferences("Setting",0);
		Editor ed = sp.edit();
		ed.putBoolean("autologin", true);
		ed.putString("Email", mEmail);
		ed.putString("password", mPassword);
		ed.commit();
	}

	private MessageAdapter loginAdapter = new MessageAdapter() {
		User user = new User();
		@Override
		public void onRcvJSONArray(JSONArray array) {
			for(int i=0 ; i<array.length();i++)
			{
				
				try {
					JSONObject obj = array.getJSONObject(i);
					user.transJSON(obj);
					Toast.makeText(getApplicationContext(), "OK", Toast.LENGTH_SHORT).show();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
			}
			
			showProgress(false);
			ApplicationHelper appHelper = (ApplicationHelper) getApplicationContext();
			appHelper.setCurrentUser(user);
			setAutoLogin();
			finish();
		}
		
		@Override
		public void onTimeout() {
			Toast.makeText(LoginActivity.this, "连接超时", Toast.LENGTH_SHORT).show();
		}

		@Override
		public void onEmptyReceived() {
			final EditText editText = new EditText(LoginActivity.this);
			editText.setHint("请再次输入密码");
			editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
			// 为搜索到用户信息，自动为用户注册新账户
			android.content.DialogInterface.OnClickListener positive = new android.content.DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					if(editText.getText().toString().equals(mPassword))
					{
						DatabaseConnector connector = new DatabaseConnector();
						connector.addParams(DatabaseConnector.METHOD, "ADDUSER");
						connector.addParams("Email", mEmail);
						connector.addParams("password", mPassword);
						connector.asyncConnect(registerAdapter);
					}
					else {
						Toast.makeText(LoginActivity.this, "密码不匹配", Toast.LENGTH_SHORT).show();
						showProgress(false);
					}
				}
			};
			
			android.content.DialogInterface.OnClickListener negative = new android.content.DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					showProgress(false);
				}
			};
			
			new AlertDialog.Builder(LoginActivity.this).setTitle("注册确认").setIcon(
				     android.R.drawable.ic_dialog_info).setView(
				     editText).setPositiveButton("确定", positive)
				     .setNegativeButton("取消", negative).show();
		}
	};
	
	private MessageAdapter registerAdapter = new MessageAdapter() {

		@Override
		public void onDone(String ret) {
			// TODO Auto-generated method stub
			ApplicationHelper helper = (ApplicationHelper) getApplication();
			helper.setCurrentUser(new User());
			helper.getCurrentUser().setEMail(ret);
			helper.getCurrentUser().setName(ret);
			setAutoLogin();
			finish();
		}
		
		@Override
		public void onTimeout() {
			Toast.makeText(getApplicationContext(), "连接超时", Toast.LENGTH_SHORT).show();
		}

		@Override
		public void onErrorOccur() {
			// TODO Auto-generated method stub
			Toast.makeText(LoginActivity.this, "邮箱已被使用", Toast.LENGTH_SHORT).show();
			showProgress(false);
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
