package net.templefox.cultureplatform;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;

import net.templefox.database.DatabaseConnector;
import net.templefox.database.MessageAdapter;
import net.templefox.database.data.User;
import net.templefox.fragment.CalendarFragment;
import net.templefox.fragment.CalendarFragment_;
import net.templefox.fragment.ClassifyFragment;
import net.templefox.fragment.ClassifyFragment_;
import net.templefox.fragment.RecommendFragment;
import net.templefox.fragment.RecommendFragment_;
import net.templefox.fragment.TestFragment;
import net.templefox.fragment.TestFragment_;
import net.templefox.fragment.UserFragment;
import net.templefox.fragment.UserFragment_;
import net.templefox.widget.InterceptableViewPager;

import net.templefox.cultureplatform.R;

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

@EActivity(R.layout.activity_main)
@OptionsMenu(R.menu.main)
public class MainActivity extends Activity {
	private InterceptableViewPager viewPager;
	private FragmentPagerAdapter fragmentPagerAdapter;
	private ActionBar actionBar;
	@SuppressWarnings("unchecked")
	private Class<Fragment>[] cls = new Class[] { RecommendFragment_.class,
			ClassifyFragment_.class, UserFragment_.class, CalendarFragment_.class,
			TestFragment_.class };

	/**
	 * Entrance. Initial the actionBar and the viewPager.
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTheme(R.style.ActionBar);
		
		// connector.testConnect(new MessageAdapter() {});
		// 为了测验模拟登录
		// User user = new User();
		// user.setId(1);
		// user.setName("test@test");
		// user.setAuthority(User.AUTHORITY_AUTHORIZED);
		// ((ApplicationHelper) this.getApplication()).setCurrentUser(user);
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

		// Set fragmentPagerAdapter

		
	}

	@AfterViews
	protected void afterViews(){
		actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
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
					Log.e("CP Error", e.getMessage());
					Log.w("CP Exception", Log.getStackTraceString(e));
				} catch (IllegalAccessException e) {
					Log.e("CP Error", e.getMessage());
					Log.w("CP Exception", Log.getStackTraceString(e));
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

	@OptionsItem(R.id.action_bar_user)
	protected void toUserActivity() {
		UserActivity_.intent(this).start();
	}

	@OptionsItem(R.id.action_bar_add_activity)
	protected void toAddActivity() {
		if (((ApplicationHelper) this.getApplication()).getUserAuthority() == User.AUTHORITY_AUTHORIZED
				|| ((ApplicationHelper) this.getApplication())
						.getUserAuthority() == User.AUTHORITY_ULTIMATED)
			intent2(AddActActivity.class);
		else {
			Toast.makeText(this, "没有足够的权限", Toast.LENGTH_SHORT).show();
		}
	}

	@OptionsItem(R.id.action_bar_search)
	protected void toSearchActivity() {
		intent2(SearchActivity.class);
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
