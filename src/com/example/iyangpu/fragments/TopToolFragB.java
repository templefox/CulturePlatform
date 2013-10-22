package com.example.iyangpu.fragments;

import com.example.iyangpu.MainActivity;
import com.example.iyangpu.R;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.RadioButton;

public class TopToolFragB extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.frag_top_tool_b, container,false);
		
		RadioButton all = (RadioButton) view.findViewById(R.id.my_all);
		RadioButton calendar = (RadioButton) view.findViewById(R.id.my_calendar);
		RadioButton rating = (RadioButton) view.findViewById(R.id.my_rating);
		
		all.setOnCheckedChangeListener(new OnAllClicked());
		calendar.setOnCheckedChangeListener(new OnCalendarClicked());
		rating.setOnCheckedChangeListener(new OnRaingClicked());
		
		return view;
	}
	
	
	class OnAllClicked implements OnCheckedChangeListener{

		@Override
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {
			// TODO Auto-generated method stub
			if(isChecked){
				MainActivity mainActivity = (MainActivity) getActivity();
				FragmentManager fm = mainActivity.getFragmentManager();
				FragmentTransaction fTransaction = fm.beginTransaction();
				fTransaction.replace(R.id.fragment_to_be_changed_middle, new TheActivitiesViewFrag());
				fTransaction.commit();
				fm.executePendingTransactions();
				
				mainActivity.reload_middle_my_activities(false);
			}
		}
		
	}
	
	class OnCalendarClicked implements OnCheckedChangeListener{
		@Override
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {
			if(isChecked){
				FragmentTransaction fTransaction = getActivity().getFragmentManager().beginTransaction();
				fTransaction.replace(R.id.fragment_to_be_changed_middle, new CalendarFrag());
				fTransaction.commit();
			}
		}
		
	}

	class OnRaingClicked implements OnCheckedChangeListener{

		@Override
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {
			if(isChecked){
				MainActivity mainActivity = (MainActivity) getActivity();
				FragmentManager fm = mainActivity.getFragmentManager();
				FragmentTransaction fTransaction = fm.beginTransaction();
				fTransaction.replace(R.id.fragment_to_be_changed_middle, new TheActivitiesViewFrag());
				fTransaction.commit();
				fm.executePendingTransactions();
				
				mainActivity.reload_activities_to_rating();
			}
		}
		
	}
}
