package com.example.fragment;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONObject;

import com.example.cultureplatform.MainActivity;
import com.example.cultureplatform.R;
import com.example.database.DatabaseConnector;
import com.example.database.MessageAdapter;
import com.example.database.data.Type;
import com.example.widget.Optionor;
import com.example.widget.Panel;
import com.example.widget.Panel.OnPanelListener;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class ClassifyFragment extends FragmentRoot {
	private Optionor optionor;
	private Panel panel;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		Context theme = new ContextThemeWrapper(getActivity(), R.style.TheLight);
		LayoutInflater localInflater = inflater.cloneInContext(theme);
		View view = inflater.inflate(R.layout.frag_classify, container,false);
		panel = (Panel) view.findViewById(R.id.classify_panel);
		optionor = (Optionor) view.findViewById(R.id.optionor1);		
		panel.setOnPanelListener(new OnPanelListener() {
			
			@Override
			public void onPanelOpened(Panel panel) {
				// TODO Auto-generated method stub
				((MainActivity)getActivity()).setViewPagerInterceptable(false);
			}
			
			@Override
			public void onPanelClosed(Panel panel) {
				// TODO Auto-generated method stub
				((MainActivity)getActivity()).setViewPagerInterceptable(true);
			}

			@Override
			public void onTouch(Panel panel) {
				// TODO Auto-generated method stub
				((MainActivity)getActivity()).setViewPagerInterceptable(false);
			}
		});
		
		if(fisrtIn()){
			reDownload();
		}

		
		return view;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "分类";
	}
	
	public void switchPanel() {
		if(panel.isOpen())
		{
			panel.setOpen(false, true);
			
		}
		else
		{
			panel.setOpen(true, true);
			
		}
	}
	
	public void open(boolean value,boolean anim){
		panel.setOpen(value, anim);
	}
	
	public boolean isOpen(){
		return panel.isOpen();
	}

	@Override
	public void reDownload() {
		// TODO Auto-generated method stub
		MessageAdapter adapter = new MessageAdapter() {
			@Override
			public void onRcvJSONArray(JSONArray array) {
				Set<Type> types = new HashSet<Type>();
				for (int i = 0; i < array.length(); i++) {
					try {	
						Type type = new Type();					
						JSONObject obj = array.getJSONObject(i);
						type.transJSON(obj);
						types.add(type);	
					} catch (Exception e) {
						// TODO: handle exception
					}
								
				}
				Type.insertIntoSQLite(types, getActivity());
			}

			@Override
			public void onFinish() {
				reLoad();
			}
		};
		
		DatabaseConnector connector = new DatabaseConnector();
		connector.addParams(DatabaseConnector.METHOD, "GETTYPE");
		connector.asyncConnect(adapter);
	}

	@Override
	public void reLoad() {
		// TODO Auto-generated method stub
		optionor.add("全部");
		List<Type> types = Type.selectFromSQLite(getActivity());
		for (Type type : types) {
			optionor.add(type.getName());
		}
	}

	

}
