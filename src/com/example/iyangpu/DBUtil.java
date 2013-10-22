package com.example.iyangpu;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;


import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;


public class DBUtil {
	/**
	 * 异步带参数方法
	 * 
	 * @param url
	 *            网址
	 * @param params
	 *            POST或GET要传递的参数
	 * @param method
	 *            方法,POST或GET
	 * @param callback
	 *            回调方法
	 */
	static public String target_url = "http://192.168.1.115";
	static public void asyncConnect(final String url,
			final Map<String, String> params, final String method,
			final Handler callback)
	{
		new Thread(){
			@Override
			public void run(){
				syncConnect(url, params, method, callback);
			}
		}.start();
	}
	
	/**
	 * 同步带参数方法
	 * 
	 * @param url
	 *            网址
	 * @param params
	 *            POST或GET要传递的参数
	 * @param method
	 *            方法,POST或GET
	 * @param callback
	 *            回调方法
	 */
	private static void syncConnect(final String url, final Map<String, String> params,
			final String method, final Handler callback)
	{
		String json = null;
		BufferedReader reader = null;

		try
		{
			HttpClient client = new DefaultHttpClient();
			HttpUriRequest request = getRequest(url, params, method);
			HttpResponse response = client.execute(request);

			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
				reader = new BufferedReader(new InputStreamReader(response
						.getEntity().getContent()));
				StringBuilder sb = new StringBuilder();
				for (String s = reader.readLine(); s != null&&s.length()!=1&&!s.isEmpty(); s = reader
						.readLine())
				{
					String ss = s.substring(1);
					sb.append(ss);
				}

				json = sb.toString();
			}
			Message msg = new Message();
	        Bundle b = new Bundle();// 存放数据
	        b.putString(null, json);
	        msg.setData(b);
			callback.sendMessage(msg);
		} catch (ClientProtocolException e)
		{
			Log.e("HttpConnectionUtil", e.getMessage(), e);
		} catch (IOException e)
		{
			Message msg = new Message();
			Bundle b = new Bundle();
			b.putString(null, "CONERROR");
			msg.setData(b);
			callback.sendMessage(msg);
			Log.e("HttpConnectionUtil", e.getMessage(), e);
		} catch(Exception e)
		{
			Log.e("HttpConnectionUtil", e.getMessage(), e);
		} 
		finally{
			try
			{
				if(reader!= null)
				{
					reader.close();
				}
			} catch (IOException e)
			{
				// ignore me
			}
		}

	}
	
	/**
	 * POST跟GET传递参数不同,POST是隐式传递,GET是显式传递
	 * 
	 * @param url
	 *            网址
	 * @param params
	 *            参数
	 * @param method
	 *            方法
	 * @return
	 */
	private static HttpUriRequest getRequest(String url, Map<String, String> params,
			String method)
	{
		if (method.equals("POST"))
		{
			List<NameValuePair> listParams = new ArrayList<NameValuePair>();
			if (params != null)
			{
				for (String name : params.keySet())
				{
					listParams.add(new BasicNameValuePair(name, params
							.get(name)));
				}
			}
			try
			{
				UrlEncodedFormEntity entity = new UrlEncodedFormEntity(
						listParams,HTTP.UTF_8);
				HttpPost request = new HttpPost(url);
				request.addHeader(HTTP.CHARSET_PARAM, HTTP.UTF_8);
				request.setEntity(entity);
				return request;
			} catch (UnsupportedEncodingException e)
			{
				// Should not come here, ignore me.
				throw new java.lang.RuntimeException(e.getMessage(), e);
			}
		} else
		{
			if (url.indexOf("?") < 0)
			{
				url += "?";
			}
			if (params != null)
			{
				for (String name : params.keySet())
				{
					try
					{
						url += "&" + name + "="
								+ URLEncoder.encode(params.get(name), "UTF-8");

					} catch (UnsupportedEncodingException e)
					{
						e.printStackTrace();
					}
				}
			}
			HttpGet request = new HttpGet(url);
			return request;
		}
	}
}
