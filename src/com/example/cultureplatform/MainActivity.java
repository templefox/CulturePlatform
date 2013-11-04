package com.example.cultureplatform;

import java.util.ArrayList;
import java.util.List;

import com.example.fragment.TestFragment;

import android.app.ActionBar;
import android.app.ActionBar.TabListener;
import android.app.FragmentTransaction;
import android.app.ActionBar.OnNavigationListener;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;


public class MainActivity extends FragmentActivity implements OnNavigationListener{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setTheme(R.style.ActionBar);
		setContentView(R.layout.activity_main);
		
		//actionBarTest();
		tabsTest();
	}

	private void tabsTest() {
		ActionBar actionBar = getActionBar();
		ViewPager viewPager = (ViewPager)findViewById(R.id.pager);
		
		actionBar.setDisplayHomeAsUpEnabled(false);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		
		FragmentPagerAdapter fragmentPagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
			
			@Override
			public int getCount() {
				// TODO Auto-generated method stub
				
				return 10;
			}
			
			@Override
			public android.support.v4.app.Fragment getItem(int arg0) {
				// TODO Auto-generated method stub
				Fragment fragment = new TestFragment();
				
				
				return fragment;
			}

			@Override
			public CharSequence getPageTitle(int position) {
				// TODO Auto-generated method stub
				
				return "TAB"+(position+1);
			}
			
			
		};
		
		viewPager.setAdapter(fragmentPagerAdapter);
		viewPager.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int arg0) {
				// TODO Auto-generated method stub
				getActionBar().setSelectedNavigationItem(arg0);
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		
		for(int i = 0;i<fragmentPagerAdapter.getCount();i++)
		{
			Tab tab = actionBar.newTab();
			tab.setText(fragmentPagerAdapter.getPageTitle(i)).setTabListener(new TabListener() {
				
				@Override
				public void onTabUnselected(Tab tab, FragmentTransaction ft) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void onTabSelected(Tab tab, FragmentTransaction ft) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void onTabReselected(Tab tab, FragmentTransaction ft) {
					// TODO Auto-generated method stub
					
				}
			});
			actionBar.addTab(tab);
		}
	}

	private void actionBarTest() {
		ActionBar actionBar = getActionBar();
		actionBar.setTitle("hehe");
		actionBar.setDisplayHomeAsUpEnabled(true);
		//actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		//������һ
		actionBar.setDisplayShowTitleEnabled(false);
		
		List<String> list = new ArrayList<String>();
		list.add("111");
		list.add("222");
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line, list);
		actionBar.setListNavigationCallbacks(adapter, this);
		
		Tab tab = actionBar.newTab()
				.setText("HAHA")
				.setTabListener(new TabListener(){
					
					@Override
					public void onTabUnselected(Tab tab, FragmentTransaction ft) {
						// TODO Auto-generated method stub
						
					}
					
					@Override
					public void onTabSelected(Tab tab, FragmentTransaction ft) {
						// TODO Auto-generated method stub
						
					}
					
					@Override
					public void onTabReselected(Tab tab, FragmentTransaction ft) {
						// TODO Auto-generated method stub
						
					}
				});
		
		actionBar.addTab(tab);
		
		tab = actionBar.newTab()
				.setText("heihei")
				.setTabListener(new TabListener(){
					
					@Override
					public void onTabUnselected(Tab tab, FragmentTransaction ft) {
						// TODO Auto-generated method stub
						
					}
					
					@Override
					public void onTabSelected(Tab tab, FragmentTransaction ft) {
						// TODO Auto-generated method stub
						
					}
					
					@Override
					public void onTabReselected(Tab tab, FragmentTransaction ft) {
						// TODO Auto-generated method stub
						
					}
				});
		
		actionBar.addTab(tab);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);
		return true;
		
	}

	@Override
	public boolean onNavigationItemSelected(int itemPosition, long itemId) {
		// TODO Auto-generated method stub
		return false;
	}


}
