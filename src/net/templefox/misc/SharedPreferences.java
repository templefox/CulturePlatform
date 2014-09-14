package net.templefox.misc;

import org.androidannotations.annotations.sharedpreferences.DefaultBoolean;
import org.androidannotations.annotations.sharedpreferences.DefaultString;
import org.androidannotations.annotations.sharedpreferences.SharedPref;

@SharedPref(value=SharedPref.Scope.UNIQUE)
public interface SharedPreferences {
	@DefaultString("")
	String user();
	
	@DefaultString("")
	String password();
	
	@DefaultBoolean(false)
	boolean autoLogin();
	
	@DefaultBoolean(false)
	boolean remember();
}
