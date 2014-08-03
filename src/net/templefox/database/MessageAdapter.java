package net.templefox.database;

import org.json.JSONArray;

public abstract class MessageAdapter {
	public void onEmptyReceived(){}
	public void onElseFail() {}
	
	
	/**
	 * ��ѯ�ɹ��Ĵ�����
	 */
	public void onRcvJSONArray(JSONArray array){}
	
	
	public void onDone(String ret) {}
	public void onErrorOccur(String ret) {}
	public void onFinish() {}
	public void onTimeout() {}
}
