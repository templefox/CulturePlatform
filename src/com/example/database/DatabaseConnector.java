package com.example.database;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.ConnectTimeoutException;
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
	static public String target_url = "http://templefox.xicp.net:998/";
	static public String upload_url = "http://templefox.xicp.net:998/upload_picture.php";
	// static public String target_url = "http://192.168.1.115:998/";
	static final public String METHOD = "METHOD";
	static final public String UPLOAD = "UPLOAD";
	static final private int TIME_OUT = 3000;
	private Map<String, String> params = new HashMap<String, String>();

	public DatabaseConnector addParams(String key, String value) {
		params.put(key, value);
		return this;
	}

	public void asyncConnect(final MessageAdapter callback) {
		final MessageHandler handler = new MessageHandler();
		new Thread() {
			@Override
			public void run() {
				handler.setMessageAdapter(callback);
				syncConnect(target_url, params, handler);
			}
		}.start();
	}

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
			Log.e("HttpConnectionUtil", e.getMessage(), e);
		} catch (UnknownHostException e) {
			Message msg = new Message();
			Bundle b = new Bundle();
			b.putString(null, "timeout");
			msg.setData(b);
			handler.sendMessage(msg);
		} catch (ConnectTimeoutException e) {
			Message msg = new Message();
			Bundle b = new Bundle();
			b.putString(null, "timeout");
			msg.setData(b);
			handler.sendMessage(msg);
		} catch (Exception e) {
			Log.e("HttpConnectionUtil", e.getMessage(), e);
		} finally {
			try {

				if (reader != null) {
					reader.close();
				}
			} catch (IOException e) {
				// ignore me
			}
		}

	}

	private static HttpUriRequest getRequest(String url,
			Map<String, String> params) {
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
			// Should not come here, ignore me.
			throw new java.lang.RuntimeException(e.getMessage(), e);
		}
	}

	public void asyncUpload(final Bitmap bitmap , final MessageAdapter callback) {
		final MessageHandler handler = new MessageHandler();
		new Thread() {
			@Override
			public void run() {
				handler.setMessageAdapter(callback);
				syncUpload(bitmap, handler);
			}
		}.start();
	}

	private void syncUpload(Bitmap bitmap, final MessageHandler handler) {
		String end = "\r\n";
		String twoHyphens = "--";
		String boundary = "******";
		try {
			URL url = new URL(upload_url);
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
			for (String key : params.keySet()) {
				dos.writeBytes(twoHyphens + boundary + end);
				dos.writeBytes("Content-Disposition: form-data; name=\"" + key
						+ "\";" + end);
				dos.writeBytes(end);
				dos.write(params.get(key).getBytes());
				dos.writeBytes(end);
			}

			// 构造文件

			dos.writeBytes(twoHyphens + boundary + end);
			dos.writeBytes("Content-Disposition: form-data; name=\"uploadedfile\"; filename=\""
					+ bitmap.toString().substring(bitmap.toString().indexOf("@")+1) + "\"" + end);
			dos.writeBytes(end);

			// FileInputStream fis = new FileInputStream(uploadFile);

			File file = File.createTempFile("temp", ".png");
			BufferedOutputStream bos = new BufferedOutputStream(
					new FileOutputStream(file));
			bitmap.compress(CompressFormat.PNG, 80, bos);
			bos.flush();
			bos.close();

			FileInputStream fis = new FileInputStream(file);

			byte[] buffer = new byte[8192]; // 8k
			int count = 0;
			// 读取文件
			while ((count = fis.read(buffer)) != -1) {
				dos.write(buffer, 0, count);
			}
			fis.close();

			dos.writeBytes(end);
			dos.writeBytes(twoHyphens + boundary + twoHyphens + end);
			dos.flush();

			// 获得反馈
			InputStream is = httpURLConnection.getInputStream();
			InputStreamReader isr = new InputStreamReader(is, "utf-8");
			BufferedReader br = new BufferedReader(isr);
			String result = br.readLine();

			Message msg = new Message();
			Bundle b = new Bundle();
			b.putString(null, result);
			msg.setData(b);
			handler.sendMessage(msg);
			dos.close();
			is.close();
		} catch (SocketTimeoutException e){
			Message msg = new Message();
			Bundle b = new Bundle();
			b.putString(null, "timeout");
			msg.setData(b);
			handler.sendMessage(msg);
		}catch (UnknownHostException e){
			Message msg = new Message();
			Bundle b = new Bundle();
			b.putString(null, "CONERROR");
			msg.setData(b);
			handler.sendMessage(msg);
		}catch (ConnectException e) {
			Message msg = new Message();
			Bundle b = new Bundle();
			b.putString(null, "timeout");
			msg.setData(b);
			handler.sendMessage(msg);
		}catch (Exception e) {
			e.getMessage();
		}
	}
}
