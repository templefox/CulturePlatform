package net.templefox.cultureplatform;

import java.sql.PreparedStatement;
import java.text.ParseException;

import net.templefox.database.DatabaseConnector;
import net.templefox.database.MessageAdapter;
import net.templefox.database.data.CurrentUser;
import net.templefox.database.data.User;
import net.templefox.misc.Encoder;
import net.templefox.misc.SharedPreferences_;
import net.templefox.widget.BlurActionBar.GlassActionBarHelper;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.sharedpreferences.Pref;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import net.templefox.cultureplatform.R;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.Menu;
import android.widget.Toast;

@EActivity
public class StartActivity extends Activity {
	private GlassActionBarHelper helper;
	
	@Bean
	CurrentUser currentUser;

	@Pref
	SharedPreferences_ preferences;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTheme(R.style.AppTheme_TranslucentActionBar);
		helper = new GlassActionBarHelper().contentLayout(R.layout.activity_start);
        setContentView(helper.createView(this));
		
	}
	
	@AfterViews
	protected void afterViews() {
		if (preferences.autoLogin().get()) {
			Ion.with(this)
					.load(DatabaseConnector.url)
					.setBodyParameter(DatabaseConnector.METHOD,
							DatabaseConnector.M_SELECT)
					.setBodyParameter(
							DatabaseConnector.QUERY,
							Encoder.encodeString(
									"select * from user where name = '%s' and password = '%s'",
									preferences.user().get(), preferences
											.password().get())).asJsonArray()
					.setCallback(new FutureCallback<JsonArray>() {

						@Override
						public void onCompleted(Exception arg0, JsonArray arg1) {
							if (arg0 != null) {
								Toast.makeText(StartActivity.this,
										arg0.getMessage(), Toast.LENGTH_SHORT)
										.show();
							} else if (arg1.size() < 1) {
								Toast.makeText(StartActivity.this,
										"No such user!", Toast.LENGTH_SHORT)
										.show();
							} else {
								JsonObject object = arg1.get(0)
										.getAsJsonObject();
								if (object.get("result") != null) {
									Log.e("cpe", object.get("error")
											.getAsString());
								} else {
									currentUser = (CurrentUser) currentUser
											.transJSON(object);
									Toast.makeText(StartActivity.this, "Login:"+currentUser.getEMail(), Toast.LENGTH_SHORT).show();
								}
							}
							toMainActivity();

						}
					});
		} else {
			toMainActivity();
		}
	}
	
	@Background(delay=1000)
	void toMainActivity(){
		MainActivity_.intent(StartActivity.this).start();
		finish();
	}

	/*
	 * class init_task extends AsyncTask<Integer, Integer, Integer> {
	 * 
	 * @Override protected Integer doInBackground(Integer... arg0) { // 此处处理事务
	 * // 判断用户是否选择自动登录 int result;
	 * 
	 * for (int i = 0; i < arg0[0]; i++) { try { Thread.sleep(350);
	 * publishProgress(i % 4); } catch (InterruptedException e) {
	 * Log.e("CP Error", e.getMessage()); Log.w("CP Exception",
	 * Log.getStackTraceString(e)); } } SharedPreferences sp =
	 * getSharedPreferences("Setting", 0); if (sp.getBoolean("autologin",
	 * false)) result = 1; else result = 2; return result; }
	 * 
	 * @Override protected void onProgressUpdate(Integer... values) {
	 * 
	 * ImageView i; Drawable dark =
	 * getResources().getDrawable(R.drawable.dark_point); Drawable light =
	 * getResources().getDrawable(R.drawable.light_point); switch(values[0]){
	 * case 0: i = (ImageView)findViewById(R.id.imageView5);
	 * i.setImageDrawable(light); i = (ImageView)findViewById(R.id.imageView2);
	 * i.setImageDrawable(dark); break; case 1: i =
	 * (ImageView)findViewById(R.id.imageView2); i.setImageDrawable(light); i =
	 * (ImageView)findViewById(R.id.imageView3); i.setImageDrawable(dark);
	 * break; case 2: i = (ImageView)findViewById(R.id.imageView3);
	 * i.setImageDrawable(light); i = (ImageView)findViewById(R.id.imageView4);
	 * i.setImageDrawable(dark); break; case 3: i =
	 * (ImageView)findViewById(R.id.imageView4); i.setImageDrawable(light); i =
	 * (ImageView)findViewById(R.id.imageView5); i.setImageDrawable(dark);
	 * break; }
	 * 
	 * 
	 * }
	 * 
	 * @Override protected void onPostExecute(Integer result) { if (result == 1)
	 * { SharedPreferences sp = getSharedPreferences("Setting", 0); String Email
	 * = sp.getString("Email", ""); String password = sp.getString("password",
	 * "");
	 * 
	 * DatabaseConnector connector = new DatabaseConnector();
	 * connector.addParams(DatabaseConnector.METHOD, "GETUSER");
	 * connector.addParams("Email", Email); connector.addParams("password",
	 * password); connector.executeConnector(new MessageAdapter() {
	 * 
	 * @Override public void onRcvJSONArray(JSONArray array) { User user = new
	 * User(); for (int i = 0; i < array.length(); i++) {
	 * 
	 * try { JSONObject obj = array.getJSONObject(i); user.transJSON(obj);
	 * Toast.makeText(getApplicationContext(), "OK", Toast.LENGTH_SHORT).show();
	 * } catch (JSONException e) { Log.e("CP Error", e.getMessage());
	 * Log.w("CP Exception", Log.getStackTraceString(e)); } catch
	 * (ParseException e) { Log.e("CP Error", e.getMessage());
	 * Log.w("CP Exception", Log.getStackTraceString(e)); } } ApplicationHelper
	 * appHelper = (ApplicationHelper) getApplicationContext();
	 * appHelper.setCurrentUser(user);
	 * 
	 * }
	 * 
	 * @Override public void onTimeout() {
	 * Toast.makeText(getApplicationContext(), "连接超时",
	 * Toast.LENGTH_SHORT).show(); }
	 * 
	 * });
	 * 
	 * Intent toMain = new Intent(getApplication(), MainActivity.class);
	 * startActivity(toMain); StartActivity.this.finish(); } else if (result ==
	 * 2) { Intent toMain = new Intent(StartActivity.this, MainActivity.class);
	 * startActivity(toMain); StartActivity.this.finish(); } } }
	 */

}
