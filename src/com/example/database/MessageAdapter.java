package com.example.database;

import org.json.JSONArray;

public abstract class MessageAdapter {
	public void onEmptyReceived(){}
	public void onErrorOccur() {}
	
	
	/**
	 * 查询成功的处理函数
	 */
	public void onRcvJSONArray(JSONArray array){}
	
	
	public void onGetSuccessNum(String ret) {}
	public void onGetFail() {}
	public void onFinish() {}
}
