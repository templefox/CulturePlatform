package net.templefox.fragment;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import net.templefox.widget.AsyncImageView;
import net.templefox.widget.InterceptableViewPager;
import net.templefox.widget.TouchImageView;
import net.templefox.widget.gestureImageView.GestureImageView;

import net.templefox.cultureplatform.R;
import net.templefox.cultureplatform.events.EventBus4Gallary;
import net.templefox.cultureplatform.events.OnViewPagerShowEvent;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

@EFragment(R.layout.frag_gallery)
public class GalleryFragment extends Fragment {
	@ViewById(R.id.gallery_image)
	GestureImageView imageView;
	
	@Bean
	EventBus4Gallary eventBus;
	
	@AfterViews
	protected void afterViews(){
		try{
		eventBus.register(this);
		}catch(Exception exception){
			String str = exception.getMessage();
			str.endsWith("");
		}
	}

	public void onEvent(OnViewPagerShowEvent event){
		imageView.reset();
	}
}
