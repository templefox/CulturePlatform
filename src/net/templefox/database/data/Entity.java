package net.templefox.database.data;

import java.text.ParseException;


import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentValues;


public abstract class Entity {

	public abstract ContentValues getContentValues() throws IllegalArgumentException,NullPointerException;
	public abstract String getTableName();
	
	@Deprecated
	public abstract Entity resolveJSON(JSONObject obj) throws JSONException, ParseException;

}