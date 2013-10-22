package com.example.iyangpu.fragments;

import java.util.HashMap;
import java.util.Map;

import com.example.iyangpu.DBUtil;
import com.example.iyangpu.MainActivity;
import com.example.iyangpu.R;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.RadioButton;
import android.widget.RadioGroup;

@SuppressLint("NewApi")
public class BottomToolFrag extends Fragment {
	private boolean all = true;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.frag_bottom_tool,container);

		RadioButton search = (RadioButton) view.findViewById(R.id.button_search_view);
		RadioButton my = (RadioButton) view.findViewById(R.id.button_my_view);

		search.setOnCheckedChangeListener(new searchOnClick());
		my.setOnCheckedChangeListener(new myOnClick());
		
		
		
		return view;
		}

	/**
	 * 
	 * @return true in all, false in my.
	 */
	public boolean inAllActivity() {
		return all;
	}
	
	class searchOnClick implements OnCheckedChangeListener{
		@Override
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {
			if(isChecked){
				MainActivity mainActivity = (MainActivity) getActivity();
				FragmentManager fragmentManager = mainActivity.getFragmentManager();
				Fragment current =fragmentManager.findFragmentById(R.id.fragment_to_be_changed_middle);
				if(current.getClass().equals(DetailsFrag.class)||current.getClass().equals(RatingFrag.class))
					fragmentManager.popBackStackImmediate();
				else if (current.getClass().equals(CalendarFrag.class)) {
					FragmentTransaction transaction = fragmentManager.beginTransaction();
					transaction.replace(R.id.fragment_to_be_changed_middle, new TheActivitiesViewFrag());
					transaction.commit();
					fragmentManager.executePendingTransactions();
				}
				mainActivity.reload_middle_all_activities(false);

				FragmentTransaction transaction = fragmentManager.beginTransaction();
				
				transaction.replace(R.id.fragment_to_be_changed_top, new TopToolFragA());
				transaction.commit();
				
				all = true;
				
			}
		}
	}
	
	
	class myOnClick implements OnCheckedChangeListener{
		@Override
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {
			if(isChecked){
				MainActivity mainActivity = (MainActivity) getActivity();
				FragmentManager fragmentManager = mainActivity.getFragmentManager();
				Fragment current =fragmentManager.findFragmentById(R.id.fragment_to_be_changed_middle);
				if(current.getClass().equals(DetailsFrag.class)||current.getClass().equals(RatingFrag.class))
					fragmentManager.popBackStackImmediate();
				mainActivity.reload_middle_my_activities(false);
				
				FragmentTransaction transaction = fragmentManager.beginTransaction();
				transaction.replace(R.id.fragment_to_be_changed_top, new TopToolFragB());
				transaction.commit();
				
				all = false;
			}
		}
	}
}
