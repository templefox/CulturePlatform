package com.example.fragment;

import com.example.cultureplatform.R;
import com.example.widget.Panel;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class ClassifyFragment extends Fragment {

	private Panel panel;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		Context theme = new ContextThemeWrapper(getActivity(), R.style.TheLight);
		LayoutInflater localInflater = inflater.cloneInContext(theme);
		View view = inflater.inflate(R.layout.frag_classify, container,false);
		panel = (Panel) view.findViewById(R.id.classify_panel);
		return view;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "·ÖÀà";
	}
	
	public void dropDown() {
		if(panel.isOpen())
			panel.setOpen(false, true);
		else
			panel.setOpen(true, true);
	}

}
