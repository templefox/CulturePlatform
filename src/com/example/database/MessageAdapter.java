package com.example.database;

public abstract class MessageAdapter {
	public void noReceiveHandler(){}
	public void errorHandler() {}
	public void getSucceedHandler(){}
	public void setSucceedHandler(int ret) {}
	public void setFailedHandler() {}
	public void onFinish() {}
}
