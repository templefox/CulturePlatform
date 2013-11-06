package com.example.database;

import org.json.JSONArray;

public abstract class MessageAdapter {
	public void noReceiveHandler(){}
	public void errorHandler() {}
	
	
	/**
	 * 查询成功的处理函数
	 */
	public void getSucceedHandler(JSONArray array){}
	
	
	public void setSucceedHandler(int ret) {}
	public void setFailedHandler() {}
	public void onFinish() {}
}
