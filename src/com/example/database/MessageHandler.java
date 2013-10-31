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
			//�յ�����Ϣ
			messageAdapter.noReceiveHandler();
		}
		else if(get.equals("CONERROR"))
		{
			//���ݿ����Ӵ���
			Toast.makeText(activity.getApplicationContext(), "���Ӵ��������������Ӻͷ�����״̬", Toast.LENGTH_SHORT).show();
		}
		else if(get.equals("ERROR")) 
		{
			//���ݿ��������
			messageAdapter.setFailedHandler();
		}
		else if(get.startsWith("DONE")) {
			//���ݿ�����ɹ�
			int activity_id = 0;
			try {
				activity_id = Integer.parseInt(get.substring(4));
			} catch (Exception e) {}
			messageAdapter.setSucceedHandler(activity_id);
			
		}
		else {
			//�յ�������Ϣ ��json��ʽ
			try {
				JSONArray array = new JSONArray(get);
				messageAdapter.getSucceedHandler();
			} catch (JSONException e) {
				//��������
				messageAdapter.errorHandler();
			}
		}
		
		messageAdapter.onFinish();
	}

}