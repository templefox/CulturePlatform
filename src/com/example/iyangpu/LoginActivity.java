package com.example.iyangpu;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;  
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputType;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_screen);
		
		Button login = (Button)findViewById(R.id.button1);
		CheckBox showPass = (CheckBox)findViewById(R.id.checkBox1);
		CheckBox autoFill = (CheckBox)findViewById(R.id.checkBox2);
		CheckBox autoLogin = (CheckBox)findViewById(R.id.checkBox3);
		SharedPreferences sp = getSharedPreferences("Setting",0);
		EditText name = (EditText)findViewById(R.id.editText1);
		EditText password = (EditText)findViewById(R.id.editText2);
		
		if(sp.getBoolean("autoFill", false)){
			name.setText(sp.getString("name", ""));
			password.setText(sp.getString("password", ""));
			autoFill.setChecked(true);
		}
		
		if(sp.getBoolean("autoLogin", false)){
			autoLogin.setChecked(true);
		}
		

		
		login.setOnClickListener(new loginOnClick());
		showPass.setOnCheckedChangeListener(new showPassOnChange());
		autoFill.setOnCheckedChangeListener(new autoFillUserPass());
		autoLogin.setOnCheckedChangeListener(new autoLogin());
	}

	
	/**
	 * 登录按钮的事件监听。
	 * @author Administrator
	 *
	 */
	class loginOnClick implements OnClickListener{
		@Override
		public void onClick(View arg0) {
			String log_name = ((EditText)findViewById(R.id.editText1)).getText().toString();
			String pass = ((EditText)findViewById(R.id.editText2)).getText().toString();
			
			if(log_name.isEmpty())
			{
				Toast.makeText(getApplicationContext(), "请输入用户名", Toast.LENGTH_SHORT).show();
			}
			else if(pass.isEmpty())
			{
				Toast.makeText(getApplicationContext(), "请输入密码", Toast.LENGTH_SHORT).show();
			}
			else
			{
				Bundle b = new Bundle();
				b.putString("name", log_name);
				b.putString("password", pass);
				
				SharedPreferences sp = getSharedPreferences("Setting",0);
				if(sp.getBoolean("autoFill", false)){
					Editor ed = sp.edit();
					ed.putString("name", log_name);
					ed.putString("password",pass);
					ed.commit();
				}
				
				Intent toMain = new Intent(getApplication(),MainActivity.class);
				toMain.putExtras(b);
				startActivity(toMain);
				LoginActivity.this.finish();
			}
			
		}
	}

	
	/**
	 * 显示密码。
	 * @author Administrator
	 *
	 */
	class showPassOnChange implements OnCheckedChangeListener{
		EditText editPass = (EditText)findViewById(R.id.editText2);
		@Override
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {
			// TODO Auto-generated method stub
			if(isChecked){
				editPass.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
			}
			else{
				editPass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
			}
				
		}
	}

	/**
	 * 记住用户名和密码。
	 * @author Administrator
	 *
	 */
	class autoFillUserPass implements OnCheckedChangeListener{
		@Override
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {
			SharedPreferences sp = getSharedPreferences("Setting",0);
			Editor ed = sp.edit();
			if(isChecked){
				ed.putBoolean("autoFill", true);
				ed.commit();
			}
			else{
				ed.putBoolean("autoFill", false);
				ed.commit();
			}
		}
		
	}

	/**
	 * 设置自动登录。
	 * @author Administrator
	 *
	 */
	class autoLogin implements OnCheckedChangeListener{

		@Override
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {
			// TODO Auto-generated method stub
			SharedPreferences sp = getSharedPreferences("Setting",0);
			Editor ed = sp.edit();
			if(isChecked){
				ed.putBoolean("autoLogin", true);
					ed.commit();
				}
			else{
					ed.putBoolean("autoLogin", false);
					ed.commit();
			}
		}
		
	}
}
