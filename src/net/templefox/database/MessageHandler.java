package net.templefox.database;

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
	public final static String CONNECT_ERROR_STR = "CONERROR";
	public final static String TIME_OUT_STR = "TIMEOUT";
	public final static String DONE_QUERY_STR = "DONE";
	public final static String ERROR_QUERY_STR = "ERROR";
	
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
			messageAdapter.onEmptyReceived();
		}
		else if(get.equals(CONNECT_ERROR_STR))
		{
			//���ݿ����Ӵ���
			
		}
		else if(get.startsWith(ERROR_QUERY_STR)) 
		{
			//���ݿ��������
			messageAdapter.onErrorOccur(get.substring(5));
		}
		else if(get.startsWith(DONE_QUERY_STR)) {
			//���ݿ�����ɹ�
			messageAdapter.onDone(get.substring(4));
			
		}
		else if (get.equals(TIME_OUT_STR)) {
			messageAdapter.onTimeout();
		}
		
		else {
			//�յ�������Ϣ ��json��ʽ
			try {
				JSONArray array = new JSONArray(get);
				messageAdapter.onRcvJSONArray(array);
			} catch (JSONException e) {
				//��������
				messageAdapter.onElseFail();
			}
		}
		
		messageAdapter.onFinish();
	}

}
