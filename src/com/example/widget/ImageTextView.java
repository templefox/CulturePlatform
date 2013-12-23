package com.example.widget;

import java.lang.reflect.Field;

import com.example.cultureplatform.R;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.text.Html.ImageGetter;
import android.util.AttributeSet;
import android.widget.TextView;

public class ImageTextView extends TextView {

	public ImageTextView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	
	
	public ImageTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public ImageTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	public void setValue(String html) {

		CharSequence charSequence = Html.fromHtml(html, new ImageGetter() {

			@Override
			public Drawable getDrawable(String source) {
				// TODO Auto-generated method stub

				Drawable drawable = getResources().getDrawable(
						getResourceId(source));
				
				drawable.setBounds(0, 0, drawable.getIntrinsicWidth() / 2,
						drawable.getIntrinsicHeight() / 2);

				return drawable;
			}
		}, null);
		this.setText(charSequence);
		

	}

	private int getResourceId(String name) {

		try {
			// ������ԴID�ı��������Field����ʹ�÷��������ʵ��
			Field field = R.drawable.class.getField(name);
			// ȡ�ò�������ԴID���ֶΣ���̬��������ֵ��ʹ�÷������
			return Integer.parseInt(field.get(null).toString());

		} catch (Exception e) {
			// TODO: handle exception
		}

		return 0;
	}

}
