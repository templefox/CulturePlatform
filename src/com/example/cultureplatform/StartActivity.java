package com.example.cultureplatform;

import org.xml.sax.Parser;

import com.example.database.DatabaseConnector;
import com.example.database.MessageAdapter;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.Adapter;
import android.widget.TextView;

public class StartActivity extends Activity {
	int i = 0;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_start);
		
		try {
			DatabaseConnector.asyncConnect(null, adapter);
		} catch (Exception e) {
			String a = e.getMessage();
			a.getBytes();
		}
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.start, menu);
		return true;
	}

	private MessageAdapter adapter = new MessageAdapter() {

		@Override
		public void noReceiveHandler() {
			i = 1;
		}

		@Override
		public void onFinish() {
			// TODO Auto-generated method stub
			i++;
			
			TextView textView = (TextView) findViewById(R.id.asdf);
			textView.setText(Integer.toString(i));
		}
		
	};
}
