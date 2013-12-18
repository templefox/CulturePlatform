package com.example.database;

import org.json.JSONArray;

public abstract class MessageAdapter {
	public void onEmptyReceived(){}
	public void onElseFail() {}
	
	
	/**
	 * 查询成功的处理函数
	 */
	public void onRcvJSONArray(JSONArray array){}
	
	
	public void onDone(String ret) {}
	public void onErrorOccur() {}
	public void onFinish() {}
	public void onTimeout() {}
}
