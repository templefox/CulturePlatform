package net.templefox.widget;

import java.io.IOException;
import java.net.SocketTimeoutException;

import net.templefox.database.DatabaseConnector;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import com.example.cultureplatform.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;

/**
 * ImageView that able to download asynchronously.
 */
public class AsyncImageView extends ImageView {

	public static MemoryCache memoryCache = new MemoryCache();
	private DownloadImageTask task;

	public AsyncImageView(Context context) {
		super(context);
	}

	public AsyncImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public AsyncImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public void asyncLoad(String image_url) {
		task = new DownloadImageTask();
		task.execute(image_url);
	}

	/**
	 * Cancel current downloading image.
	 */
	public void cancelTask() {
		task.cancel(true);
	}

	/**
	 * An asyncTask to download image.
	 */
	private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
		private ImageView imageView;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		synchronized protected Bitmap doInBackground(String... params) {
			do {
				imageView = AsyncImageView.this;

				Bitmap bitmap = memoryCache.get((String) params[0]);
				if (bitmap != null) {
					return bitmap;
				} else {
					HttpParams param = new BasicHttpParams();
					HttpConnectionParams.setConnectionTimeout(param, DatabaseConnector.TIME_OUT/10);
					HttpConnectionParams.setSoTimeout(param, DatabaseConnector.TIME_OUT/10);
					HttpClient httpClient = new DefaultHttpClient(param);
					HttpGet httpGet = new HttpGet((String) params[0]);

					HttpResponse httpResponse;
					try {
						httpResponse = httpClient.execute(httpGet);

						if (isCancelled())
							break;
						if (httpResponse.getStatusLine().getStatusCode() == 200) {
							HttpEntity httpEntity = httpResponse.getEntity();
							if (isCancelled())
								break;
							byte[] data = EntityUtils.toByteArray(httpEntity);
							if (isCancelled())
								break;
							bitmap = BitmapFactory.decodeByteArray(data, 0,
									data.length);
						}
					} catch (ClientProtocolException e) {
						 Log.e("CP Error",e.getMessage());Log.w("CP Exception", Log.getStackTraceString(e));
					}catch (SocketTimeoutException e) {
						// TODO: handle exception
					} catch (ConnectTimeoutException e) {
						// TODO: handle exception
					}
					catch (IOException e) {
						 Log.e("CP Error",e.getMessage());Log.w("CP Exception", Log.getStackTraceString(e));
					}

					memoryCache.put((String) params[0], bitmap);
					return bitmap;
				}
			} while (false);
			return null;
		}

		@Override
		protected void onPostExecute(Bitmap result) {
			super.onPostExecute(result);
			if (result != null)
				imageView.setImageBitmap(result);
			else {
				imageView.setImageResource(R.drawable.default_pic);
			}

		}

	}

}
