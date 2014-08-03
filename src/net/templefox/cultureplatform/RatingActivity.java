package net.templefox.cultureplatform;

import net.templefox.database.DatabaseConnector;
import net.templefox.database.MessageAdapter;
import net.templefox.database.data.Activity;
import net.templefox.database.data.User;
import net.templefox.widget.TextRatingBar;

import com.example.cultureplatform.R;

import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RatingActivity extends android.app.Activity {
	private TextRatingBar contentRating;
	private TextRatingBar staffRating;
	private TextRatingBar reporterRating;
	private TextRatingBar environmentRating;
	private EditText editText;
	private Button submit;
	private Activity activity;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTheme(R.style.ActionBar);
		setContentView(R.layout.activity_rating);
		
		activity = (Activity) getIntent().getExtras().get("activity");
		
		contentRating = (TextRatingBar) findViewById(R.id.rating_content);
		staffRating = (TextRatingBar)findViewById(R.id.rating_staff);
		reporterRating = (TextRatingBar)findViewById(R.id.rating_reporter);
		environmentRating = (TextRatingBar)findViewById(R.id.rating_environment);
		editText = (EditText)findViewById(R.id.rating_edit);
		submit = (Button)findViewById(R.id.rating_submit);
		
		
		submit.setOnClickListener(onsubmit);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.rating, menu);
		return true;
	}
	
	private OnClickListener onsubmit = new OnClickListener(){

		@Override
		public void onClick(View v) {
			User user = ((ApplicationHelper)getApplication()).getCurrentUser();
			if(user == null){
				return;
			}
			DatabaseConnector connector = new DatabaseConnector();
			connector.addParams(DatabaseConnector.METHOD, "SETRATING");
			connector.addParams("user_id", user.getId().toString());
			connector.addParams("activity_id", activity.getId().toString());
			connector.addParams("content", contentRating.getDoubleRating().toString());
			connector.addParams("reporter", reporterRating.getDoubleRating().toString());
			connector.addParams("environment", environmentRating.getDoubleRating().toString());
			connector.addParams("staff", staffRating.getDoubleRating().toString());
			connector.addParams("advice", "hello");
			connector.executeConnector(new MessageAdapter() {

				@Override
				public void onDone(String ret) {
					Toast.makeText(getApplicationContext(), "评分提交成功", Toast.LENGTH_SHORT).show();
					finish();
				}

				@Override
				public void onErrorOccur(String ret) {
					
				}
				
				@Override
				public void onTimeout() {
					Toast.makeText(getApplicationContext(), "连接超时", Toast.LENGTH_SHORT).show();
				}
				
			});
		}};
}
