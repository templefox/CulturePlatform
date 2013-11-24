package com.example.database.data;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.json.JSONException;
import org.json.JSONObject;



import android.content.ContentValues;
import android.provider.SyncStateContract.Helpers;

public class Type extends Entity implements Serializable {
	private Integer id;
	String name;
	public Type() {
	}
	public Integer getId() {
		return id;
	}
	public String getName() {
		return name;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public void setName(String name) {
		this.name = name;
	}
	@Override
	protected ContentValues getContentValues() {
		// TODO Auto-generated method stub
		ContentValues value = new ContentValues();
		value.put("name", name);
		value.put("id", id);
		return value;
	}
	@Override
	protected String getTableName() {
		// TODO Auto-generated method stub
		return "type";
	}
	@Override
	public Entity transJSON(JSONObject obj) throws JSONException,
			ParseException {
		Type type = this;
		type.setId(obj.getInt("id"));
		type.setName(obj.getString("name"));
		return type;
	}
}
