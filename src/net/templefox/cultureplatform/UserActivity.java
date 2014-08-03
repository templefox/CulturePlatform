package net.templefox.cultureplatform;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.OptionsMenuItem;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.sharedpreferences.Pref;

import net.templefox.database.data.CurrentUser;
import net.templefox.database.data.User;
import net.templefox.misc.SharedPreferences_;

import net.templefox.cultureplatform.R;

import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

@EActivity(R.layout.activity_user)
@OptionsMenu(R.menu.user)
public class UserActivity extends Activity {
	@Pref
	SharedPreferences_ preferences;

	@Bean
	CurrentUser currentUser;

	@ViewById(R.id.user_name)
	TextView nameView;

	@ViewById(R.id.user_view)
	View userView;

	@ViewById(R.id.no_user_view)
	View noUserView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTheme(R.style.ActionBar);

		ActionBar actionBar = getActionBar();
		actionBar.setTitle("个人中心");
		actionBar.setDisplayHomeAsUpEnabled(true);
	}

	@Override
	protected void onStart() {
		super.onStart();

		ApplicationHelper appHelper = (ApplicationHelper) getApplicationContext();

		if (TextUtils.isEmpty(currentUser.getName())) {
			// 如果没有当前用户登录信息，则显示某些按钮。
			noUserView.setVisibility(View.VISIBLE);
			userView.setVisibility(View.INVISIBLE);
		} else {
			// 如果有当前用户登录信息，隐藏某些按钮，显示用户信息。
			// currentUser = appHelper.getCurrentUser();

			noUserView.setVisibility(View.INVISIBLE);
			userView.setVisibility(View.VISIBLE);

			nameView.setText(currentUser.getName());
		}

	}

	@Click(R.id.button_log_in)
	protected void login() {
		LoginActivity_.intent(UserActivity.this).start();
	}

	@Click(R.id.button_log_out)
	protected void logout() {
		currentUser.setName(null);
		currentUser.setId(null);
		currentUser.setPassword(null);

		preferences.edit().autoLogin().put(false).apply();
		LoginActivity_.intent(this).start();
		finish();
	}

	@OptionsItem(android.R.id.home)
	protected void onReturn() {
		finish();
	}
}
