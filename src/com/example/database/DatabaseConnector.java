package com.example.database;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
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
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;


import android.os.Bundle;
import android.os.Message;
import android.util.Log;


public class DatabaseConnector {
	static public String target_url = "http://templefox.xicp.net:998/";
	
	static public void asyncConnect(
			final Map<String, String> params,final MessageAdapter callback)
	{
		final MessageHandler handler = new MessageHandler();
		new Thread(){
			@Override
			
			
			
			public void run(){		
				handler.setMessageAdapter(callback);
				syncConnect(target_url, params, handler);
			}
		}.start();
	}
	
	private static void syncConnect(final String url, final Map<String, String> params, final MessageHandler handler)
	{
		String json = null;
		BufferedReader reader = null;

		try
		{
			HttpClient client = new DefaultHttpClient();
			HttpUriRequest request = getRequest(url, params);
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
	        Bundle b = new Bundle();// ´æ·ÅÊý¾Ý
	        b.putString(null, json);
	        msg.setData(b);
			handler.sendMessage(msg);
		} catch (ClientProtocolException e)
		{
			Log.e("HttpConnectionUtil", e.getMessage(), e);
		} catch (IOException e)
		{
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
	
	private static HttpUriRequest getRequest(String url, Map<String, String> params)
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
	}
}
