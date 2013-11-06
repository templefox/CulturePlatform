package com.example.database;

import org.json.JSONArray;

public abstract class MessageAdapter {
	public void noReceiveHandler(){}
	public void errorHandler() {}
	
	
	/**
	 * ��ѯ�ɹ��Ĵ�����
	 */
	public void getSucceedHandler(JSONArray array){}
	
	
	public void setSucceedHandler(int ret) {}
	public void setFailedHandler() {}
	public void onFinish() {}
}
