package net.templefox.widget;

import java.lang.reflect.Field;

import com.example.cultureplatform.R;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.text.Html.ImageGetter;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextView;

public class ImageTextView extends TextView {

	public ImageTextView(Context context) {
		super(context);
	}

	public ImageTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public ImageTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public void setValue(String html) {
		ImageGetter getter = new ImageGetter() {
			@Override
			public Drawable getDrawable(String source) {
				Drawable drawable = getResources().getDrawable(
						getResourceId(source));
				drawable.setBounds(0, 0, drawable.getIntrinsicWidth() / 2,
						drawable.getIntrinsicHeight() / 2);

				return drawable;
			}
		};
		CharSequence charSequence = Html.fromHtml(html, getter, null);
		this.setText(charSequence);
	}

	/**
	 * Get a resource id according its variable name as String.
	 * 
	 * @param name
	 *            Drawable variable name.
	 * @return Drawable id.
	 */
	private int getResourceId(String name) {
		Field field;
		try {
			field = R.drawable.class.getField(name);
			return Integer.parseInt(field.get(null).toString());
		} catch (NoSuchFieldException e) {
			Log.e("CP Error", e.getMessage());
			Log.w("CP Exception", Log.getStackTraceString(e));
		} catch (NumberFormatException e) {
			Log.e("CP Error", e.getMessage());
			Log.w("CP Exception", Log.getStackTraceString(e));
		} catch (IllegalArgumentException e) {
			Log.e("CP Error", e.getMessage());
			Log.w("CP Exception", Log.getStackTraceString(e));
		} catch (IllegalAccessException e) {
			Log.e("CP Error", e.getMessage());
			Log.w("CP Exception", Log.getStackTraceString(e));
		}

		return 0;
	}

}
