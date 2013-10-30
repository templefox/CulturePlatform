package com.example.cultureplatform;


import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.view.Menu;
import android.widget.ImageView;

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
			if(sp.getBoolean("autoLogin", false))
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
			if(true)
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
				//Intent tolog = new Intent(getApplication(),LoginActivity.class);
				//startActivity(tolog);
				StartActivity.this.finish();
			}
		}
	}

}
