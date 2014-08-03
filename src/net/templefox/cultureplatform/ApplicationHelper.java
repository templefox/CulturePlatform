package net.templefox.cultureplatform;

import java.util.HashSet;
import java.util.Set;

import net.templefox.database.data.Activity;
import net.templefox.database.data.User;


import android.app.Application;

public class ApplicationHelper extends Application {
	private User currentUser = null;
	private Set<Activity> activities = new HashSet<Activity>();
	OnUserChangedListener onUserChanged;
	
	public void setOnUserChangedListener(OnUserChangedListener onUserChanged) {
		this.onUserChanged = onUserChanged;
	}

	public static interface OnUserChangedListener{
		public void onUserChanged(User newUser);
	}
	
	public Set<Activity> getActivities() {
		return activities;
	}

	public User getCurrentUser() {
		return currentUser;
	}

	public void setCurrentUser(User currentUser) {
		if(this.currentUser != currentUser){
			this.currentUser = currentUser;	
			if(onUserChanged != null)
			onUserChanged.onUserChanged(currentUser);
		}
	}
	
	public boolean isUserLogin()
	{
		return !(currentUser == null);
	}
	
	public int getUserAuthority()
	{
		if(currentUser == null)
			return -1;
		else return currentUser.getAuthority();
	}
}
