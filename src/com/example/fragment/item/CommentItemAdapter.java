package com.example.fragment.item;

import java.util.List;
import java.util.zip.Inflater;

import com.example.cultureplatform.R;
import com.example.database.data.Comment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class CommentItemAdapter extends BaseAdapter {
	private List<Comment> comments;
	
	public CommentItemAdapter() {
		
	}
	
	public void setComments(List<Comment> comments){
		this.comments = comments;
	}

	@Override
	public int getCount() {
		if(comments == null)return 0;
		return comments.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return comments.get(position);
	}

	@Override
	public long getItemId(int position) {
		return comments.get(position).getId();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView == null){
			convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comment, parent,true);
		}
		
		Comment currentComment = comments.get(position);
		
		TextView textView = (TextView) convertView.findViewById(R.id.text);
		textView.setText(currentComment.getContent());
		
		return convertView;
	}

}
