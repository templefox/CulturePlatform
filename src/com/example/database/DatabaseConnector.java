package com.example.database;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.CompressFormat;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;

public class DatabaseConnector {
	// static public String target_url = "http://templefox.xicp.net:998/";
	static public String target_url = "http://192.168.1.104:998/";
	static public String upload_url = target_url + "upload_picture.php";
	// static public String target_url = "http://192.168.1.115:998/";
	static final private String end = "\r\n";
	static final private String twoHyphens = "--";
	static final private String boundary = "******";
	static final public String METHOD = "METHOD";
	static final public String UPLOAD = "UPLOAD";
	static final public int TIME_OUT = 3000;
	public static Lock lock = new ReentrantLock(true);
	static private ExecutorService threadPool = Executors.newCachedThreadPool();
	private Map<String, String> keyValue = new HashMap<String, String>();
	private Map<String, Bitmap> keyBitmap = new HashMap<String, Bitmap>();

	public DatabaseConnector addParams(String key, String value) {
		keyValue.put(key, value);
		return this;
	}

	@Deprecated
	public void asyncConnect(final MessageAdapter callback) {
		final MessageHandler handler = new MessageHandler();
		threadPool.execute(new Runnable() {
			@Override
			public void run() {
				handler.setMessageAdapter(callback);
				syncConnect(target_url, keyValue, handler);
			}
		});
	}

	public void executeConnector(final MessageAdapter callback) {
		final MessageHandler handler = new MessageHandler();
		threadPool.execute(new Runnable() {
			@Override
			public void run() {
				//lock.lock();
				handler.setMessageAdapter(callback);
				multifunctionalConnect(target_url, keyValue, null, handler);
				//lock.unlock();
			}
		});
	}

	/**
	 * Use HTTP connect with class httpUrlConnection that is recommended by
	 * google and efficient.
	 * 
	 * @param url
	 *            The URL to connect
	 * @param keyValuePair
	 *            Parameters as type of <String,String>
	 * @param keyFilePair
	 *            Parameters as type of <String,Bitmap>, key as file name.
	 * @param handler
	 *            A handler to deal with the response.
	 * @throws IOException
	 * @throws ClientProtocolException
	 */
	private void multifunctionalConnect(final String url,
			final Map<String, String> keyValuePair,
			final Map<String, Bitmap> keyFilePair, final MessageHandler handler) {
		try {
			URL con_url = new URL(target_url);
			HttpURLConnection httpURLConnection = (HttpURLConnection) con_url
					.openConnection();

			// Some initialization.
			httpURLConnection.setChunkedStreamingMode(128 * 1024);// 128K
			httpURLConnection.setDoInput(true);
			httpURLConnection.setDoOutput(true);
			httpURLConnection.setUseCaches(false);
			httpURLConnection.setConnectTimeout(TIME_OUT);
			httpURLConnection.setReadTimeout(TIME_OUT);

			// Set property.
			httpURLConnection.setRequestMethod("POST");
			httpURLConnection.setRequestProperty("Connection", "Keep-Alive");
			httpURLConnection.setRequestProperty("Charset", "UTF-8");
			if(keyFilePair!=null&&!keyFilePair.isEmpty())
				httpURLConnection.setRequestProperty("Content-Type",
					"multipart/form-data;boundary=" + boundary);

			// Execute
			DataOutputStream dos = new DataOutputStream(
					httpURLConnection.getOutputStream());
			httpExecute(keyValuePair, keyFilePair, dos);

			String result = getResponse(httpURLConnection);
			Message message = new Message();
			Bundle bundle = new Bundle();
			bundle.putString(null, result);
			message.setData(bundle);
			handler.sendMessage(message);
		} catch (NullPointerException e) {
			Log.e("CP Error", e.getMessage());
			Log.w("CP Exception", Log.getStackTraceString(e));
		} catch (SocketTimeoutException e) {
			Log.e("CP Error", e.getMessage());
			Log.w("CP Exception", Log.getStackTraceString(e));
			Message msg = new Message();
			Bundle b = new Bundle();
			b.putString(null, MessageHandler.TIME_OUT_STR);
			msg.setData(b);
			handler.sendMessage(msg);
		} catch (UnknownHostException e) {
			Log.e("CP Error", e.getMessage());
			Log.w("CP Exception", Log.getStackTraceString(e));
			Message msg = new Message();
			Bundle b = new Bundle();
			b.putString(null, MessageHandler.CONNECT_ERROR_STR);
			msg.setData(b);
			handler.sendMessage(msg);
		} catch (ConnectException e) {
			Log.e("CP Error", e.getMessage());
			Log.w("CP Exception", Log.getStackTraceString(e));
			Message msg = new Message();
			Bundle b = new Bundle();
			b.putString(null, MessageHandler.TIME_OUT_STR);
			msg.setData(b);
			handler.sendMessage(msg);
		} catch (MalformedURLException e) {
			Log.e("CP Error", e.getMessage());
			Log.w("CP Exception", Log.getStackTraceString(e));
		} catch (IOException e) {
			Log.e("CP Error", e.getMessage());
			Log.w("CP Exception", Log.getStackTraceString(e));
		}
	}

	private String getResponse(HttpURLConnection httpURLConnection)
			throws IOException, UnsupportedEncodingException {
		InputStream inputStream = httpURLConnection.getInputStream();
		InputStreamReader inputStreamReader = new InputStreamReader(
				inputStream, HTTP.UTF_8);
		BufferedReader reader = new BufferedReader(inputStreamReader);
		StringBuilder result = new StringBuilder();
		for (String s = reader.readLine(); s != null; s = reader.readLine()) {
			result.append(s);
		}

		return result.toString();
	}

	private void httpExecute(final Map<String, String> keyValuePair,
			final Map<String, Bitmap> keyFilePair,
			DataOutputStream dataOutPutStream) throws FileNotFoundException,
			IOException {
		HttpEntity httpEntity = null;
		if (keyFilePair == null || keyFilePair.isEmpty()) {
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			if (keyValuePair != null && !keyValuePair.isEmpty())
				for (String key : keyValuePair.keySet()) {
					list.add(new BasicNameValuePair(key, keyValuePair.get(key)));
				}
			httpEntity = new UrlEncodedFormEntity(list);
		} else {

			MultipartEntityBuilder entityBuilder = MultipartEntityBuilder.create();
			entityBuilder.setCharset(Charset.forName(HTTP.UTF_8));
			entityBuilder.setBoundary("******");
			if (keyValuePair != null && !keyValuePair.isEmpty()) {
				for (String key : keyValuePair.keySet()) {
					entityBuilder.addTextBody(key, keyValuePair.get(key),
							ContentType.TEXT_PLAIN);
				}
			}

			if (keyFilePair != null && !keyFilePair.isEmpty()) {
				for (String key : keyFilePair.keySet()) {
					File file = null;
					try {
						file = File.createTempFile("temp", null);
						keyFilePair.get(key).compress(CompressFormat.JPEG, 100,
								new FileOutputStream(file));
						entityBuilder.addBinaryBody(key, file,
								ContentType.MULTIPART_FORM_DATA, file.getName());
					} finally {
						file.delete();
					}
				}
			}
			httpEntity = entityBuilder.build();
		}
		httpEntity.writeTo(dataOutPutStream);
		dataOutPutStream.flush();
	}

	@Deprecated
	private void syncConnect(final String url,
			final Map<String, String> params, final MessageHandler handler) {
		String json = null;
		BufferedReader reader = null;

		try {
			HttpParams param = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(param, TIME_OUT);
			HttpConnectionParams.setSoTimeout(param, TIME_OUT);

			HttpClient client = new DefaultHttpClient(param);

			HttpUriRequest request = getRequest(url, params);
			HttpResponse response = client.execute(request);

			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				reader = new BufferedReader(new InputStreamReader(response
						.getEntity().getContent()));
				StringBuilder sb = new StringBuilder();
				for (String s = reader.readLine(); s != null && s.length() != 1
						&& !s.isEmpty(); s = reader.readLine()) {
					String ss = s.substring(1);
					sb.append(ss);
				}

				json = sb.toString();
			}
			Message msg = new Message();
			Bundle b = new Bundle();
			b.putString(null, json);
			msg.setData(b);
			handler.sendMessage(msg);

		} catch (ClientProtocolException e) {
			Log.e("CP Error", e.getMessage());
			Log.w("CP Exception", Log.getStackTraceString(e));
		} catch (UnknownHostException e) {
			Log.e("CP Error", e.getMessage());
			Log.w("CP Exception", Log.getStackTraceString(e));
			Message msg = new Message();
			Bundle b = new Bundle();
			b.putString(null, MessageHandler.TIME_OUT_STR);
			msg.setData(b);
			handler.sendMessage(msg);
		} catch (ConnectTimeoutException e) {
			Log.e("CP Error", e.getMessage());
			Log.w("CP Exception", Log.getStackTraceString(e));
			Message msg = new Message();
			Bundle b = new Bundle();
			b.putString(null, MessageHandler.TIME_OUT_STR);
			msg.setData(b);
			handler.sendMessage(msg);
		} catch (IllegalStateException e) {
			Log.e("CP Error", e.getMessage());
			Log.w("CP Exception", Log.getStackTraceString(e));
		} catch (IOException e) {
			Log.e("CP Error", e.getMessage());
			Log.w("CP Exception", Log.getStackTraceString(e));
		} finally {
			try {

				if (reader != null) {
					reader.close();
				}
			} catch (IOException e) {
				Log.e("CP Error", e.getMessage());
				Log.w("CP Exception", Log.getStackTraceString(e));
			}
		}

	}

	@Deprecated
	private HttpUriRequest getRequest(String url, Map<String, String> params) {
		List<NameValuePair> listParams = new ArrayList<NameValuePair>();
		if (params != null) {
			for (String name : params.keySet()) {
				listParams.add(new BasicNameValuePair(name, params.get(name)));
			}
		}
		try {
			UrlEncodedFormEntity entity = new UrlEncodedFormEntity(listParams,
					HTTP.UTF_8);
			HttpPost request = new HttpPost(url);
			request.addHeader(HTTP.CHARSET_PARAM, HTTP.UTF_8);
			request.setEntity(entity);
			return request;
		} catch (UnsupportedEncodingException e) {
			Log.e("CP Error", e.getMessage());
			Log.w("CP Exception", Log.getStackTraceString(e));
			throw new java.lang.RuntimeException(e.getMessage(), e);
		}
	}

	@Deprecated
	public void asyncUpload(final Bitmap bitmap, final MessageAdapter callback) {
		final MessageHandler handler = new MessageHandler();
		threadPool.execute(new Runnable() {

			@Override
			public void run() {
				handler.setMessageAdapter(callback);
				syncUpload(bitmap, handler);
			}
		});
	}

	@Deprecated
	private void syncUpload(Bitmap bitmap, final MessageHandler handler) {
		String end = "\r\n";
		String twoHyphens = "--";
		String boundary = "******";
		try {
			URL url = new URL(DatabaseConnector.target_url);
			HttpURLConnection httpURLConnection = (HttpURLConnection) url
					.openConnection();
			// 设置每次传输的流大小，可以有效防止手机因为内存不足崩溃
			// 此方法用于在预先不知道内容长度时启用没有进行内部缓冲的 HTTP 请求正文的流。
			httpURLConnection.setChunkedStreamingMode(128 * 1024);// 128K
			// 允许输入输出流
			httpURLConnection.setDoInput(true);
			httpURLConnection.setDoOutput(true);
			httpURLConnection.setUseCaches(false);
			httpURLConnection.setConnectTimeout(TIME_OUT);
			httpURLConnection.setReadTimeout(TIME_OUT);
			// 使用POST方法

			httpURLConnection.setRequestMethod("POST");
			httpURLConnection.setRequestProperty("Connection", "Keep-Alive");
			httpURLConnection.setRequestProperty("Charset", "UTF-8");
			httpURLConnection.setRequestProperty("Content-Type",
					"multipart/form-data;boundary=" + boundary);
			DataOutputStream dos = new DataOutputStream(
					httpURLConnection.getOutputStream());

			// 构造参数
			for (String key : keyValue.keySet()) {
				dos.writeBytes(twoHyphens + boundary + end);
				dos.writeBytes("Content-Disposition: form-data; name=\"" + key
						+ "\"" + end + end);
				dos.write(keyValue.get(key).getBytes());
				dos.writeBytes(end);

			}

			/*
			 * // 构造文件
			 * 
			 * dos.writeBytes(twoHyphens + boundary + end); dos.writeBytes(
			 * "Content-Disposition: form-data; name=\"uploadedfile\"; filename=\""
			 * + bitmap.toString().substring( bitmap.toString().indexOf("@") +
			 * 1) + "\"" + end);
			 * 
			 * //dos.writeBytes(end);
			 * 
			 * // FileInputStream fis = new FileInputStream(uploadFile);
			 * 
			 * File file = File.createTempFile("temp", ".png");
			 * BufferedOutputStream bos = new BufferedOutputStream( new
			 * FileOutputStream(file)); bitmap.compress(CompressFormat.PNG, 80,
			 * bos); bos.flush(); bos.close();
			 * 
			 * FileInputStream fis = new FileInputStream(file);
			 * 
			 * byte[] buffer = new byte[8192]; // 8k int count = 0; // 读取文件
			 * while ((count = fis.read(buffer)) != -1) { dos.write(buffer, 0,
			 * count); } fis.close();
			 * 
			 * 
			 * 
			 * file.delete();
			 */
			// 获得反馈
			dos.writeBytes(twoHyphens + boundary + twoHyphens + end);
			dos.flush();

			InputStream is = httpURLConnection.getInputStream();
			InputStreamReader isr = new InputStreamReader(is, HTTP.UTF_8);
			BufferedReader br = new BufferedReader(isr);
			String result = br.readLine();

			Message msg = new Message();
			Bundle b = new Bundle();
			b.putString(null, result);
			msg.setData(b);
			handler.sendMessage(msg);
			dos.close();
			is.close();
		} catch (SocketTimeoutException e) {
			Log.e("CP Error", e.getMessage());
			Log.w("CP Exception", Log.getStackTraceString(e));
			Message msg = new Message();
			Bundle b = new Bundle();
			b.putString(null, MessageHandler.TIME_OUT_STR);
			msg.setData(b);
			handler.sendMessage(msg);
		} catch (UnknownHostException e) {
			Log.e("CP Error", e.getMessage());
			Log.w("CP Exception", Log.getStackTraceString(e));
			Message msg = new Message();
			Bundle b = new Bundle();
			b.putString(null, MessageHandler.CONNECT_ERROR_STR);
			msg.setData(b);
			handler.sendMessage(msg);
		} catch (ConnectException e) {
			Log.e("CP Error", e.getMessage());
			Log.w("CP Exception", Log.getStackTraceString(e));
			Message msg = new Message();
			Bundle b = new Bundle();
			b.putString(null, MessageHandler.TIME_OUT_STR);
			msg.setData(b);
			handler.sendMessage(msg);
		} catch (IOException e) {
			Log.e("CP Error", e.getMessage());
			Log.w("CP Exception", Log.getStackTraceString(e));
		}
	}
}
