package com.example.cultureplatform;

import java.util.HashSet;
import java.util.Set;

import com.example.database.data.Activity;
import com.example.database.data.User;

import android.app.Application;

public class ApplicationHelper extends Application {
	private User currentUser = null;
	private Set<Activity> activities = new HashSet<Activity>();
	
	public Set<Activity> getActivities() {
		return activities;
	}

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
