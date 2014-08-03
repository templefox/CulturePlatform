package net.templefox.cultureplatform;

import java.util.HashSet;
import java.util.Set;

import net.templefox.fragment.GalleryFragment;
import net.templefox.widget.InterceptableViewPager;

import net.templefox.cultureplatform.R;

import android.os.Bundle;
import android.app.Activity;
import android.app.Fragment;
import android.support.v13.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;

public class GalleryActivity extends Activity {
	private ViewPager viewPager;
	private Set<String> picture_urls = new HashSet<String>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_gallery);
		
		viewPager = (ViewPager) findViewById(R.id.gallery_pager);
		
		FragmentStatePagerAdapter pagerAdapter = new FragmentStatePagerAdapter(getFragmentManager()) {
			
			@Override
			public int getCount() {
				//return picture_urls.size();
				return 3;
			}
			
			@Override
			public Fragment getItem(int arg0) {
				GalleryFragment fragment = new GalleryFragment();
				//fragment.setViewPager((InterceptableViewPager) viewPager);
				return fragment;
			}
		};
		
		viewPager.setAdapter(pagerAdapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.gallary, menu);
		return true;
	}

}
