package net.templefox.cultureplatform;

import java.text.ParseException;

import net.templefox.database.DatabaseConnector;
import net.templefox.database.MessageAdapter;
import net.templefox.database.data.CurrentUser;
import net.templefox.database.data.User;
import net.templefox.misc.Encoder;
import net.templefox.misc.SharedPreferences_;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.sharedpreferences.Pref;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import net.templefox.cultureplatform.R;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

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
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

@EActivity(R.layout.activity_login)
@OptionsMenu(R.menu.login)
public class LoginActivity extends Activity {

	@Bean
	CurrentUser currentUser;

	private String mEmail;
	private String mPassword;

	@Pref
	SharedPreferences_ preferences;

	@ViewById(R.id.email)
	EditText mEmailView;

	@ViewById(R.id.password)
	EditText mPasswordView;

	@ViewById(R.id.login_form)
	View mLoginFormView;

	@ViewById(R.id.login_status)
	View mLoginStatusView;

	@ViewById(R.id.login_status_message)
	TextView mLoginStatusMessageView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTheme(R.style.ActionBar);

		getActionBar().setDisplayHomeAsUpEnabled(true);
	}

	@AfterViews
	protected void afterViews() {
		mPasswordView
				.setOnEditorActionListener(new TextView.OnEditorActionListener() {
					@Override
					public boolean onEditorAction(TextView textView, int id,
							KeyEvent keyEvent) {
						if (id == R.id.login || id == EditorInfo.IME_NULL) {
							login();
							return true;
						}
						return false;
					}
				});
	}

	@Click(R.id.sign_in_button)
	protected void login() {
		// 隐藏软键盘
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
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
		} else if (!mEmail.contains("")) {
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
			/*
			 * mAuthTask = new UserLoginTask(); mAuthTask.execute((Void) null);
			 */

			login(mEmail, mPassword);

		}
	}

	private void login(String mEmail, final String mPassword) {
		Ion.with(this)
				.load(DatabaseConnector.url)
				.setBodyParameter(DatabaseConnector.METHOD,
						DatabaseConnector.M_SELECT)
				.setBodyParameter(
						DatabaseConnector.QUERY,
						Encoder.encodeString(
								"select * from user where name = '%s'",
								mEmail)).asJsonArray()
				.setCallback(new FutureCallback<JsonArray>() {

					@Override
					public void onCompleted(Exception arg0, JsonArray arg1) {
						if (arg0 != null) {
							Toast.makeText(LoginActivity.this,
									arg0.getMessage(), Toast.LENGTH_SHORT)
									.show();
						} else if (arg1.size() < 1) {
							Toast.makeText(LoginActivity.this, "No such user!",
									Toast.LENGTH_SHORT).show();
							autoRegist();
						} else {
							JsonObject object = arg1.get(0).getAsJsonObject();
							if (object.get("result") != null) {
								Log.e("cpe", object.get("error").getAsString());
								showProgress(false);

							}else if(!mPassword.equals(object.get("password").getAsString())){
								Toast.makeText(LoginActivity.this, "Wrong password!",
										Toast.LENGTH_SHORT).show();
								showProgress(false);
							}
								else {
								
								currentUser = (CurrentUser) currentUser
										.transJSON(object);
								showProgress(false);
								preferences.edit().user()
										.put(currentUser.getEMail()).password()
										.put(mPassword)
										.autoLogin().put(true).remember()
										.put(true).apply();

								finish();
							}
						}
					}

				});
	}

	private void autoRegist() {
		final EditText editText = new EditText(LoginActivity.this);
		editText.setHint("请再次输入密码");
		editText.setInputType(InputType.TYPE_CLASS_TEXT
				| InputType.TYPE_TEXT_VARIATION_PASSWORD);
		// 为搜索到用户信息，自动为用户注册新账户
		android.content.DialogInterface.OnClickListener positive = new android.content.DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				if (editText.getText().toString().equals(mPassword)) {
					Ion.with(LoginActivity.this)
							.load(DatabaseConnector.url)
							.setBodyParameter(DatabaseConnector.METHOD,
									DatabaseConnector.M_INSERT)
							.setBodyParameter(
									DatabaseConnector.QUERY,
									Encoder.encodeString(
											"INSERT INTO `user` (`email`, `password`) VALUES ('%s', '%s');",
											mEmail, mPassword)).asJsonObject()
							.setCallback(new FutureCallback<JsonObject>() {

								@Override
								public void onCompleted(Exception arg0,
										JsonObject arg1) {
									if (arg0 != null) {
										Toast.makeText(LoginActivity.this,
												arg0.getMessage(),
												Toast.LENGTH_SHORT).show();
									} else {
										if (arg1.get("result").equals("error")) {
											Log.e("cpe", arg1.get("error")
													.getAsString());
											showProgress(false);
										} else {
											currentUser.setEMail(mEmail);
											currentUser.setPassword(mPassword);
											showProgress(false);
											preferences
													.edit()
													.user()
													.put(currentUser.getEMail())
													.password()
													.put(currentUser
															.getPassword())
													.autoLogin().put(true)
													.remember().put(true)
													.apply();
											finish();
										}
									}
								}
							});
				} else {
					Toast.makeText(LoginActivity.this, "密码不匹配",
							Toast.LENGTH_SHORT).show();
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

		new AlertDialog.Builder(LoginActivity.this).setTitle("注册确认")
				.setIcon(android.R.drawable.ic_dialog_info).setView(editText)
				.setPositiveButton("确定", positive)
				.setNegativeButton("取消", negative).show();
	}

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

	@OptionsItem(android.R.id.home)
	protected boolean onReturn() {
		finish();
		return true;
	}

}
