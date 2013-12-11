package com.example.widget;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.widget.ImageView;

public class AsyncImageView extends ImageView {

	MemoryCache memoryCache = new MemoryCache();

	public AsyncImageView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public AsyncImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public AsyncImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	public void asyncLoad(String image_url, ImageView imageView) {

		new MyTask().execute(image_url, imageView);

	}

	// “Ï≤Ω‘ÿ»ÎÕ¯¬ÁÕº∆¨
	public class MyTask extends AsyncTask<Object, Void, Bitmap> {

		private ImageView imageView;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();

		}

		@Override
		protected Bitmap doInBackground(Object... params) {
			// TODO Auto-generated method stub

			
			imageView = (ImageView) params[1];
			
			Bitmap bitmap = memoryCache.get((String) params[0]);
			if (bitmap != null) {
				return bitmap;
			} else {

				
				HttpClient httpClient = new DefaultHttpClient();
				HttpGet httpGet = new HttpGet((String) params[0]);
				try {
					HttpResponse httpResponse = httpClient.execute(httpGet);
					if (httpResponse.getStatusLine().getStatusCode() == 200) {
						HttpEntity httpEntity = httpResponse.getEntity();
						byte[] data = EntityUtils.toByteArray(httpEntity);
						bitmap = BitmapFactory.decodeByteArray(data, 0,
								data.length);

					}
				} catch (Exception e) {
					// TODO: handle exception
				}
				memoryCache.put((String) params[0], bitmap);
				return bitmap;
			}

		}

		@Override
		protected void onPostExecute(Bitmap result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			imageView.setImageBitmap(result);

		}

	}

}
