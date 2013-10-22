package com.example.iyangpu.fragments;

import java.util.Set;

import com.example.iyangpu.ActView;
import com.example.iyangpu.MainActivity;
import com.example.iyangpu.R;
import com.example.iyangpu.data.Activity;

import android.R.integer;
import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

@SuppressLint("NewApi")
public class TheActivitiesViewFrag extends Fragment {
	private LinearLayout llo;
	private TextView title;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.frag__activities, container,false);
		llo = (LinearLayout) view.findViewById(R.id.my_activities_linear);
		title = (TextView) view.findViewById(R.id.title);
		Button fresh = (Button) view.findViewById(R.id.button_fresh);
		fresh.setOnClickListener(new FreshListener());
		
		return view;
	}
	


	public void refresh_frag_my_activities(Set<Activity> activities,String title){
		llo.removeAllViews();
		for(Activity activity:activities){
			ActView av = new ActView(getActivity(), activity);
			llo.addView(av);
			Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.testt);
			av.startAnimation(animation);
			this.title.setText(title);
		}
	}
	
	class FreshListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			
			MainActivity a = (MainActivity) getActivity();
			BottomToolFrag frag = (BottomToolFrag) a.getFragmentManager().findFragmentById(R.id.fragment_bottom);
			if(frag.inAllActivity())
				a.reload_middle_all_activities(true);
			else 
				a.reload_middle_my_activities(true);
				
			
/*			FragmentTransaction fTransaction = getActivity().getFragmentManager().beginTransaction();
			
			fTransaction.replace(R.id.fragment_to_be_changed_middle, new CalendarFrag());
			fTransaction.commit();*/
		}
		
	}

}
