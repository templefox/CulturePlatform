package com.example.fragment;

import com.example.cultureplatform.R;
import com.example.widget.AsyncImageView;
import com.example.widget.InterceptableViewPager;
import com.example.widget.TouchImageView;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class GalleryFragment extends Fragment {
	private InterceptableViewPager pager;
	public GalleryFragment() {
		// TODO Auto-generated constructor stub
	}
	
	public void setViewPager(InterceptableViewPager pager){
		this.pager = pager;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.frag_gallery, container,false);
		AsyncImageView imageView = (AsyncImageView) view.findViewById(R.id.gallery_image);
		imageView.asyncLoad("http://img.nynet.com.cn/underwear/fashion/Atlas/details/2012-7-3/1286882949071wrareks3lk.jpg");
		return view;
	}

}
