package com.example.cultureplatform;

import com.example.database.DatabaseConnector;
import com.example.database.MessageAdapter;
import com.example.database.data.User;
import com.example.fragment.CalendarFragment;
import com.example.fragment.ClassifyFragment;
import com.example.fragment.RecommendFragment;
import com.example.fragment.TestFragment;
import com.example.fragment.UserFragment;
import com.example.widget.InterceptableViewPager;

import android.app.ActionBar;
import android.app.ActionBar.TabListener;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;

import android.util.Log;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.widget.Toast;

/**
 * Entrance of the Application. Create the viewPager, which contains some
 * fragments with various functions.
 */
public class MainActivity extends Activity {
	private InterceptableViewPager viewPager;
	private FragmentPagerAdapter fragmentPagerAdapter;
	@SuppressWarnings("unchecked")
	private Class<Fragment>[] cls = new Class[] { RecommendFragment.class,
			ClassifyFragment.class, UserFragment.class, CalendarFragment.class,
			TestFragment.class };

	/**
	 * Entrance. Initial the actionBar and the viewPager.
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTheme(R.style.ActionBar);
		setContentView(R.layout.activity_main);


		//connector.testConnect(new MessageAdapter() {});
		// 为了测验模拟登录
		//User user = new User();
		//user.setId(1);
		//user.setName("test@test");
		//user.setAuthority(User.AUTHORITY_AUTHORIZED);
		//((ApplicationHelper) this.getApplication()).setCurrentUser(user);
		// 测验结束

		// test
		/*
		 * Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
		 * R.drawable.init); MessageAdapter callback = new MessageAdapter(){
		 * 
		 * @Override public void onDone(String ret) { // TODO Auto-generated
		 * method stub Toast.makeText(getApplicationContext(), "Done",
		 * Toast.LENGTH_SHORT).show(); }
		 * 
		 * }; DatabaseConnector databaseConnector = new DatabaseConnector();
		 * databaseConnector.asyncUpload(bitmap, callback);
		 */

		// Set actionBar
		final ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		// Set fragmentPagerAdapter
		fragmentPagerAdapter = new FragmentPagerAdapter(getFragmentManager()) {
			@Override
			public int getCount() {
				return cls.length;
			}

			@Override
			public Fragment getItem(int arg0) {
				Fragment fragment = null;
				Class<Fragment> class1 = cls[arg0];
				try {
					fragment = class1.newInstance();
				} catch (InstantiationException e) {
					 Log.e("CP Error",e.getMessage());Log.w("CP Exception", Log.getStackTraceString(e));
				} catch (IllegalAccessException e) {
					 Log.e("CP Error",e.getMessage());Log.w("CP Exception", Log.getStackTraceString(e));
				} finally {
					if (fragment == null) {
						fragment = new TestFragment();
						((TestFragment) fragment).setA(arg0);
					}
				}
				return fragment;
			}

			@Override
			public CharSequence getPageTitle(int position) {
				CharSequence s;
				s = getItem(position).toString();
				return s;
			}

		};

		// Set viewPager
		viewPager = (InterceptableViewPager) findViewById(R.id.pager);
		viewPager.setOffscreenPageLimit(4);
		viewPager.setAdapter(fragmentPagerAdapter);
		viewPager.setOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageSelected(int arg0) {
				actionBar.setSelectedNavigationItem(arg0);
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
			}
		});

		// Link viewPager and actionBar
		for (int i = 0; i < fragmentPagerAdapter.getCount(); i++) {
			Tab tab = actionBar.newTab();
			tab.setText(fragmentPagerAdapter.getPageTitle(i)).setTabListener(
					new TabListener() {

						@Override
						public void onTabUnselected(Tab tab,
								FragmentTransaction ft) {
						}

						@Override
						public void onTabSelected(Tab tab,
								FragmentTransaction ft) {
							viewPager.setCurrentItem(tab.getPosition());
						}

						@Override
						public void onTabReselected(Tab tab,
								FragmentTransaction ft) {
						}
					});
			actionBar.addTab(tab);
		}
	}

	final private <T> boolean intent2(Class<T> cls) {
		Intent intent = new Intent(this, cls);
		startActivity(intent);
		return true;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_bar_user:
			return intent2(UserActivity.class);
		case R.id.action_bar_add_activity:
			if (((ApplicationHelper) this.getApplication()).getUserAuthority() == User.AUTHORITY_AUTHORIZED
					|| ((ApplicationHelper) this.getApplication())
							.getUserAuthority() == User.AUTHORITY_ULTIMATED)
				return intent2(AddActActivity.class);
			else {
				Toast.makeText(this, "没有足够的权限", Toast.LENGTH_SHORT).show();
				return super.onOptionsItemSelected(item);
			}
		case R.id.action_bar_search:
			return intent2(SearchActivity.class);
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Fragment classifyFragment = (Fragment) fragmentPagerAdapter
					.instantiateItem(viewPager, getActionBar()
							.getSelectedNavigationIndex());
			if (classifyFragment instanceof ClassifyFragment
					&& ((ClassifyFragment) classifyFragment).isOpen()) {
				((ClassifyFragment) classifyFragment).switchPanel();
				return true;
			}
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

}
