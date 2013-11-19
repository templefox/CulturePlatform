package com.example.cultureplatform;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.database.DatabaseConnector;
import com.example.database.MessageAdapter;
import com.example.database.SQLiteManager;
import com.example.database.data.Type;
import com.example.database.data.User;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.Toast;

public class StartActivity extends Activity {
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_start);
		
		new init_task().execute(3);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.start, menu);
		return true;
	}
	
	class init_task extends AsyncTask<Integer,Integer,Integer> {

		@Override
		protected Integer doInBackground(Integer... arg0) {
			//此处处理事务
			//判断用户是否选择自动登录
			int ret;
			


			
			for(int i = 0; i< arg0[0];i++)
			{
				try {
					Thread.sleep(350);
					publishProgress(i%4);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			SharedPreferences sp = getSharedPreferences("Setting",0);
			if(sp.getBoolean("autologin", false))
				ret = 1;
			else ret = 2;
			return ret;
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			/*ImageView i; 
			Drawable dark = getResources().getDrawable(R.drawable.dark_point);
			Drawable light = getResources().getDrawable(R.drawable.light_point);
			switch(values[0]){
			case 0:
				i = (ImageView)findViewById(R.id.imageView5);
				i.setImageDrawable(light);
				i = (ImageView)findViewById(R.id.imageView2);
				i.setImageDrawable(dark);
				break;
			case 1:
				i = (ImageView)findViewById(R.id.imageView2);
				i.setImageDrawable(light);
				i = (ImageView)findViewById(R.id.imageView3);
				i.setImageDrawable(dark);
				break;
			case 2:
				i = (ImageView)findViewById(R.id.imageView3);
				i.setImageDrawable(light);
				i = (ImageView)findViewById(R.id.imageView4);
				i.setImageDrawable(dark);
				break;
			case 3:
				i = (ImageView)findViewById(R.id.imageView4);
				i.setImageDrawable(light);
				i = (ImageView)findViewById(R.id.imageView5);
				i.setImageDrawable(dark);
				break;
			}*/

		}

		@Override
		protected void onPostExecute(Integer result) {
			//if(result == 1)//请求自动登录
			if(result == 1)
			{
				SharedPreferences sp = getSharedPreferences("Setting",0);
				String Email = sp.getString("Email", "");
				String password = sp.getString("password","");
				
				DatabaseConnector connector = new DatabaseConnector();
				connector.addParams(DatabaseConnector.METHOD, "GETUSER");
				connector.addParams("Email",Email);
				connector.addParams("password", password);
				connector.asyncConnect(new MessageAdapter(){
					@Override
					public void onRcvJSONArray(JSONArray array) {
						User user = new User();
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
						ApplicationHelper appHelper = (ApplicationHelper) getApplicationContext();
						appHelper.setCurrentUser(user);
						
					}
					
					
					
					
					
				});
				
				Intent toMain = new Intent(getApplication(),MainActivity.class);
				startActivity(toMain);
				StartActivity.this.finish();
			}
			else if(result == 2)
			{
				Intent toMain = new Intent(StartActivity.this,MainActivity.class);
				startActivity(toMain);
				StartActivity.this.finish();
			}
		}
	}

}
