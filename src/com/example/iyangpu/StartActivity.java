package com.example.iyangpu;

import java.util.List;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.view.Menu;
import android.widget.ImageView;
import com.example.iyangpu.data.*;



public class StartActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.init_screen);
		
		//创建新线程做后台，前台等待符号
		new init_task().execute(3);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.start, menu);
		return true;
	}
	
	/**
	 * 第一屏，进行一个载入工作。
	 */
	class init_task extends AsyncTask<Integer,Integer,Integer> {

		@Override
		protected Integer doInBackground(Integer... arg0) {
			//此处处理事务
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
			if(sp.getBoolean("autoLogin", false))
				ret = 1;
			else ret = 2;
			return ret;
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			ImageView i; 
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
			}

			super.onProgressUpdate(values);
		}

		@Override
		protected void onPostExecute(Integer result) {
			if(result == 1)//请求自动登录
			{
				SharedPreferences sp = getSharedPreferences("Setting",0);
				String name = sp.getString("name", "");
				String pass = sp.getString("password","");
				Bundle b = new Bundle();
				b.putString("name", name);
				b.putString("password", pass);
				Intent toMain = new Intent(getApplication(),MainActivity.class);
				toMain.putExtras(b);
				startActivity(toMain);
				StartActivity.this.finish();
			}
			else if(result == 2)//用户需登录或注册
			{
				Intent tolog = new Intent(getApplication(),LoginActivity.class);
				startActivity(tolog);
				StartActivity.this.finish();
			}
		}
	}
}

