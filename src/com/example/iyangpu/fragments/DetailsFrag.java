package com.example.iyangpu.fragments;

import java.util.Set;

import com.example.iyangpu.CommentView;
import com.example.iyangpu.R;
import com.example.iyangpu.data.Activity;
import com.example.iyangpu.data.Comment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

public class DetailsFrag extends Fragment {
	Activity activity;
	LinearLayout llo;
	
	public Fragment setActivity(Activity activity) {
		// TODO Auto-generated method stub
		this.activity = activity;
		return this;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.frag__detail, container,false);
		
		llo = (LinearLayout) view.findViewById(R.id.linear_comments);
		
		TextView name = (TextView) view.findViewById(R.id.detail_name);
		name.setText(activity.getName());
		
		
		
		return view;
	}
	
	public void refresh_details_comments(Set<Comment> comments) {
		llo.removeAllViews();
		for (Comment comment : comments) {
			//TODO
			CommentView commentView = new CommentView(getActivity(), comment);
			llo.addView(commentView);
		}
	}

}
