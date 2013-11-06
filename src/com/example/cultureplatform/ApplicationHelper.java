package com.example.cultureplatform;

import com.example.database.data.User;

import android.app.Application;

public class ApplicationHelper extends Application {
	private User currentUser = null;

	public User getCurrentUser() {
		return currentUser;
	}

	public void setCurrentUser(User currentUser) {
		this.currentUser = currentUser;
	}
	
	public boolean isUserLogin()
	{
		return !(currentUser == null);
	}
}
