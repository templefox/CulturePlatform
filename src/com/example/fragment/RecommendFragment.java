package com.example.fragment;

import java.util.ArrayList;
import java.util.List;

import com.example.cultureplatform.R;

import android.app.Fragment;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class RecommendFragment extends Fragment {
	private ListView listView;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.frag_recommend, container,false);
		listView = (ListView) view.findViewById(R.id.list_recommend);
		List<String> list = new ArrayList<String>();
		list.add("11111");
		list.add("22222");		
		list.add("33333");		
		list.add("44444");	
		try {
			listView.setAdapter(new RecommendItemAdapter(list));
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		
		return view;
	}
	
	
	private class RecommendItemAdapter extends BaseAdapter{
		List<String> list;
		
		
		public RecommendItemAdapter(List<String> list) {
			super();
			// TODO Auto-generated constructor stub
			this.list = list;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return list.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return list.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			Button button = null;
			TextView textView = null;
			
			if(convertView == null){
				convertView = LayoutInflater.from(getActivity()).inflate(R.layout.item_recommend, null);
			}
			
			button = (Button) convertView.findViewById(R.id.item_recommend_button);
			textView = (TextView) convertView.findViewById(R.id.item_recommend_name);
			
			button.setText(list.get(position));
			textView.setText(list.get(position));
			
			return convertView;
		}
		
	}
	
}
