package com.example.iyangpu.fragments;

import com.example.iyangpu.MainActivity;
import com.example.iyangpu.R;
import com.example.iyangpu.data.Activity;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RatingBar;
import android.view.View.OnClickListener;

public class RatingFrag extends Fragment {
	RatingBar content;
	RatingBar reporter;
	RatingBar staff;
	RatingBar environment;
	Activity activity;
	
	public RatingFrag setActivity(Activity activity) {
		this.activity = activity;
		return this;
	}
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.frag__rating, container, false);
		
		Button button = (Button) view.findViewById(R.id.rating_submit);
		content = (RatingBar) view.findViewById(R.id.ratingbar_content);
		reporter = (RatingBar) view.findViewById(R.id.ratingbar_reporter);
		staff = (RatingBar) view.findViewById(R.id.ratingbar_staff);
		environment = (RatingBar) view.findViewById(R.id.ratingbar_environment);
		
		button.setOnClickListener(new SubmitListener());
		
		return view;
	}
	
	
	class SubmitListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			int co = (int) (content.getRating()*2);
			int re = (int) (reporter.getRating()*2);
			int st = (int) (staff.getRating()*2);
			int en = (int) (environment.getRating()*2);
			
			MainActivity a = (MainActivity) getActivity();
			a.upload_rating(co, re, st, en, activity);
		}
		
	}
}
