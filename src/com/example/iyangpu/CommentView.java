package com.example.iyangpu;

import com.example.iyangpu.data.Comment;

import android.content.Context;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.RelativeLayout.LayoutParams;

public class CommentView extends RelativeLayout {
	TextView content;
	Comment comment;
	public CommentView(Context context, Comment comment) {
		super(context);
		// TODO Auto-generated constructor stub
		
		this.comment = comment;
		
		content = new TextView(context);
		content.setText(comment.getContent());
		
		
		
		RelativeLayout.LayoutParams para = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
		para.topMargin = 50;
		para.bottomMargin = 50;
		para.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM|RelativeLayout.ALIGN_PARENT_LEFT|RelativeLayout.ALIGN_PARENT_TOP);
		
		this.addView(content, para);
	}

}
