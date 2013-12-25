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
			messageAdapter.onEmptyReceived();
		}
		else if(get.equals("CONERROR"))
		{
			//数据库连接错误
			Toast.makeText(activity.getApplicationContext(), "连接错误，请检查网络连接和服务器状态", Toast.LENGTH_SHORT).show();
		}
		else if(get.startsWith("ERROR")) 
		{
			//数据库操作错误
			messageAdapter.onErrorOccur(get.substring(5));
		}
		else if(get.startsWith("DONE")) {
			//数据库操作成功
			messageAdapter.onDone(get.substring(4));
			
		}
		else if (get.equals("timeout")) {
			messageAdapter.onTimeout();
		}
		
		else {
			//收到其他消息 以json格式
			try {
				JSONArray array = new JSONArray(get);
				messageAdapter.onRcvJSONArray(array);
			} catch (JSONException e) {
				//其他错误
				messageAdapter.onElseFail();
			}
		}
		
		messageAdapter.onFinish();
	}

}
