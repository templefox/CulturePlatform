package com.example.iyangpu.fragments;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.iyangpu.DBUtil;
import com.example.iyangpu.MainActivity;
import com.example.iyangpu.R;

import android.R.integer;
import android.R.string;
import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("NewApi")
public class TopToolFragA extends Fragment {
	private String order = "";
	private String location = "全部地点";
	private String type = "全部类型";
	private AutoCompleteTextView word;
	Set<String> words = new HashSet<String>();
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO 
		View view = inflater.inflate(R.layout.frag_top_tool_a, container,false);
		Button button = (Button) view.findViewById(R.id.button_search);
		word = (AutoCompleteTextView) view.findViewById(R.id.search_text);
		
		
		button.setOnClickListener(new OnSearchClicked());
		
		MainActivity a = (MainActivity) getActivity();
		
		
		
		SharedPreferences sp = getActivity().getSharedPreferences("search",0);
		
		words = sp.getStringSet("words", null);
		
		ArrayAdapter<String> adapter = a.getAdapter();
		adapter.addAll(words);
		word.setAdapter(adapter);
		
		return view;
		}
	
	
	
	
	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		
		MainActivity mainActivity = (MainActivity) getActivity();
		mainActivity.reload_top_spinners(false);
	}




	public void setSpinnerContent(List<String> loc, List<String> type) {
		loc.add(0, "全部地点");
		type.add(0, "全部类型");
		
		{
		Spinner location = (Spinner) TopToolFragA.this.getView().findViewById(R.id.spinner_location);
		ArrayAdapter<String>  adapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item,loc);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		location.setAdapter(adapter);
		location.setOnItemSelectedListener(new OnLocationSelected());
		}
		
		{
		Spinner sType = (Spinner) TopToolFragA.this.getView().findViewById(R.id.spinner_type);
		ArrayAdapter<String>  adapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item,type);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		sType.setAdapter(adapter);
		sType.setOnItemSelectedListener(new OnTypeSelected());
		}
	}
	
	class OnLocationSelected implements OnItemSelectedListener{

		@Override
		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// TODO Auto-generated method stub
			TextView text = (TextView) arg1;
			location = text.getText().toString();

			MainActivity a = (MainActivity) getActivity();
			a.reload_middle_activities_with_conditions(order, location, type);
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {		
		}
	}
	
	class OnTypeSelected implements OnItemSelectedListener{

		@Override
		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// TODO Auto-generated method stub
			TextView text = (TextView) arg1;
			type = text.getText().toString();
			
			MainActivity a = (MainActivity) getActivity();
			a.reload_middle_activities_with_conditions(order, location, type);
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
			// TODO Auto-generated method stub
			
		}
		
	}

	class OnSearchClicked implements OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			
			String in =word.getText().toString();
			
			MainActivity mainActivity = (MainActivity) getActivity();
			
			SharedPreferences sp = mainActivity.getSharedPreferences("search",0);
			Editor ed = sp.edit();
			
			if(words.size() == 10)
				words.clear();
			words.add(in);
			
			ed.putStringSet("words", words);
			ed.commit();
			ArrayAdapter<String> adapter = mainActivity.getAdapter();
			adapter.add(in);
			
			
			mainActivity.reload_activities_with_search(in);
			
		}
		
	}
}
