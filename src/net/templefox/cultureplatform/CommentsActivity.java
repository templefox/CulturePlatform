package net.templefox.cultureplatform;

import net.templefox.database.DatabaseConnector;
import net.templefox.database.MessageAdapter;
import net.templefox.database.data.Activity;
import net.templefox.database.data.User;

import net.templefox.cultureplatform.R;

import android.content.Intent;
import android.os.Bundle;
import android.text.AndroidCharacter;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class CommentsActivity extends android.app.Activity {
	private Activity currentActivity;
	private EditText editText;
	private User currentUser;
	private Button clear;
	private Button submit;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTheme(R.style.ActionBar);
		setContentView(R.layout.activity_comments);
		
		
		currentActivity = (Activity) getIntent().getSerializableExtra(
				"activity");
		
		editText = (EditText) findViewById(R.id.detail_comment_text);
		currentUser = ((ApplicationHelper) getApplication()).getCurrentUser();
		clear = (Button) findViewById(R.id.detail_button_clear);
		submit = (Button) findViewById(R.id.detail_button_submit);
		
		
		clear.setVisibility(View.INVISIBLE);
		
		editText.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				if (count != 0)
					clear.setVisibility(View.VISIBLE);
				else {
					clear.setVisibility(View.INVISIBLE);
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
			}
		});
		clear.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				v.setVisibility(View.INVISIBLE);
				editText.setText("");
			}
		});
		submit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (currentUser == null)
					hintUserUnLogin();
				else if (currentUser.getAuthority() == User.AUTHORITY_UNCHECK)
					hintUserUnCheck();
				else
					submit();

				editText.clearFocus();
				editText.setText("");
			}

		});
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.comments, menu);
		return true;
	}
	private void submit() {
		MessageAdapter adapter = new MessageAdapter() {

			@Override
			public void onDone(String ret) {
				Toast.makeText(CommentsActivity.this, "评论成功", Toast.LENGTH_SHORT)
						.show();
				download();
			}

			@Override
			public void onTimeout() {
				Toast.makeText(getApplicationContext(), "连接超时",
						Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onErrorOccur(String ret) {
				Toast.makeText(CommentsActivity.this, "评论失败，请联系管理员",
						Toast.LENGTH_SHORT).show();
			}
		};

		DatabaseConnector connector = new DatabaseConnector();
		connector.addParams(DatabaseConnector.METHOD, "ADDCOMMENT");
		connector.addParams("user_id", currentUser.getId().toString());
		connector.addParams("activity_id", currentActivity.getId().toString());
		connector.addParams("content", editText.getText().toString());
		connector.executeConnector(adapter);
	}
	
	private void hintUserUnLogin() {
		Toast.makeText(CommentsActivity.this, "您尚未登录", Toast.LENGTH_SHORT).show();
	}

	private void hintUserUnCheck() {
		Toast.makeText(CommentsActivity.this, "您的账户尚未确认", Toast.LENGTH_SHORT)
				.show();
	}

	protected void download() {
		// TODO Auto-generated method stub
		
	}
}
