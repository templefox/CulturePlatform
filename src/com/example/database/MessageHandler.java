package com.example.database;

import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

public class MessageHandler extends Handler {
	private Map<String, String> para;
	private MessageAdapter messageAdapter;
	private Activity activity;
	
	public void setPara(Map<String, String> para) {
		this.para = para;
	}
	
	public Map<String, String> getPara() {
		return para;
	}
	
	public void setMessageAdapter(MessageAdapter messageAdapter) {
		this.messageAdapter = messageAdapter;
	}

	@Override
	public void handleMessage(Message msg) {
		Bundle b = msg.getData();
		String get = b.getString(null);
		if(get == null || get.isEmpty() || get.equals("-"))
		{
			//收到空信息
			messageAdapter.noReceiveHandler();
		}
		else if(get.equals("CONERROR"))
		{
			//数据库连接错误
			Toast.makeText(activity.getApplicationContext(), "连接错误，请检查网络连接和服务器状态", Toast.LENGTH_SHORT).show();
		}
		else if(get.equals("ERROR")) 
		{
			//数据库操作错误
			messageAdapter.setFailedHandler();
		}
		else if(get.startsWith("DONE")) {
			//数据库操作成功
			int activity_id = 0;
			try {
				activity_id = Integer.parseInt(get.substring(4));
			} catch (Exception e) {}
			messageAdapter.setSucceedHandler(activity_id);
			
		}
		else {
			//收到其他消息 以json格式
			try {
				JSONArray array = new JSONArray(get);
				messageAdapter.getSucceedHandler(array);
			} catch (JSONException e) {
				//其他错误
				messageAdapter.errorHandler();
			}
		}
		
		messageAdapter.onFinish();
	}

}
