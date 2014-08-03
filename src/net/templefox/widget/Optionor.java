package net.templefox.widget;



import java.util.List;

import net.templefox.cultureplatform.R;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.CompoundButton;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class Optionor extends LinearLayout {
	private RadioGroup group;
	@SuppressWarnings("unused")
	private HorizontalScrollView view;
	
	public Optionor(Context context) {
		super(context);
	}

	public Optionor(Context context, AttributeSet attrs) {
		super(context, attrs);
		if (isInEditMode()) { return; }
		
		LayoutInflater.from(context).inflate(R.layout.optionor, this,true);
		
		group = (RadioGroup) findViewById(R.id.optionor_radiogroup);
		view = (HorizontalScrollView) findViewById(R.id.optionor);
		
		
	}
	
	public void removeAllString(){
		group.removeAllViews();
	}
	
	public void addString(String name){
		RadioButton button = new RadioButton(getContext());
		button.setText(name);

		button.setButtonDrawable(android.R.color.transparent);
		button.setPadding(10, 0, 0, 0);
		button.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(isChecked)
					buttonView.setTextColor(Color.RED);
				else 
					buttonView.setTextColor(Color.BLACK);
				
			}
		});

		
		group.addView(button);
		if(name.equals("È«²¿")){
			button.setChecked(true);
		}
	}
	
	public void addAll(List<String> names){
		for (String name : names) {
			addString(name);
		}
	}
	
	public void setOnCheckedChangedListener(OnCheckedChangeListener listener){
		group.setOnCheckedChangeListener(listener);
	}

}
