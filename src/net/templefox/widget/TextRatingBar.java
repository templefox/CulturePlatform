package net.templefox.widget;

import net.templefox.cultureplatform.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class TextRatingBar extends RelativeLayout {
	private String title = "title";
	private TextView textView;
	private RatingBar ratingBar;
	
	public TextRatingBar(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public TextRatingBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		if (isInEditMode()) { return; }
		
		LayoutInflater.from(context).inflate(R.layout.text_ratingbar, this,true);
		
		TypedArray a = context.obtainStyledAttributes(attrs,  
                R.styleable.TextRatingBar);  
		title = a.getString(R.styleable.TextRatingBar_title);  
		a.recycle(); 
		
		
		textView = (TextView) findViewById(R.id.rating_text);
		textView.setText(title);
		
		
		ratingBar = (RatingBar)findViewById(R.id.rating_bar);
		
	}
	
	public Integer getDoubleRating(){
		return  (int) (ratingBar.getRating()*2);
	}
}
