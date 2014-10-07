package net.templefox.cultureplatform;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.ViewById;

import net.templefox.fragment.GalleryFragment;
import net.templefox.fragment.GalleryFragment_;
import net.templefox.widget.InterceptableViewPager;

import net.templefox.cultureplatform.R;
import net.templefox.cultureplatform.events.EventBus4Gallary;
import net.templefox.cultureplatform.events.OnViewPagerShowEvent;

import android.os.Bundle;
import android.app.Activity;
import android.app.Fragment;
import android.support.v13.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

@EActivity(R.layout.activity_gallery)
@OptionsMenu(R.menu.gallary)
public class GalleryActivity extends Activity implements OnPageChangeListener{
	@ViewById(R.id.gallery_pager)
	ViewPager viewPager;
	List<String> urls = new ArrayList<String>();
	
	@Bean
	EventBus4Gallary eventBus;
	
	private Set<String> picture_urls = new HashSet<String>();
	
	@AfterViews
	protected void afterViews(){
		viewPager.setOffscreenPageLimit(0);
		
		FragmentStatePagerAdapter pagerAdapter = new FragmentStatePagerAdapter(getFragmentManager()) {
			
			@Override
			public int getCount() {
				return 3;
			}
			
			@Override
			public Fragment getItem(int arg0) {
				GalleryFragment_.builder().build();
				GalleryFragment fragment = GalleryFragment_.builder().build();
				return fragment;
			}
			
		};
		
		viewPager.setAdapter(pagerAdapter);
		viewPager.setOnPageChangeListener(this);
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
		eventBus.post(new OnViewPagerShowEvent());
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
	}

	@Override
	public void onPageSelected(int arg0) {
	}

}
