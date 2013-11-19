package com.example.database.data;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.json.JSONException;
import org.json.JSONObject;

import com.example.database.SQLiteManager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class Type implements Serializable {
	private Integer id;
	private String name;
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
	
	public static void insertIntoSQLite(Set<Type> types, Context context){
		SQLiteManager manager = new SQLiteManager(context);
		SQLiteDatabase db = manager.getWritableDatabase();
		db.beginTransaction();
		
		try 
		{
			for (Type type : types) 
			{
				insertOrUpdata(type, db);
			}
			db.setTransactionSuccessful();
		}finally{
			db.endTransaction();
			manager.close();
		}
	}
	
	public static void insertIntoSQLite(Type type, Context context){
		SQLiteManager manager = new SQLiteManager(context);
		SQLiteDatabase db = manager.getWritableDatabase();
		db.beginTransaction();
		
		try 
		{
			insertOrUpdata(type, db);
			db.setTransactionSuccessful();
		}finally{
			db.endTransaction();
			manager.close();
		}
	}
	
	private static void insertOrUpdata(Type type, SQLiteDatabase db) {
		ContentValues values = new ContentValues();
		values.put("id", type.getId());
		values.put("name", type.getName());
		if(db.insert("type", null, values)==-1)
		{
			String whereClause = "id = "+values.getAsInteger("id");
			values.remove("id");
			db.update("type", values, whereClause, null);
		}
	}
	
	public Type transJSON(JSONObject obj)
			throws JSONException, ParseException {
		Type type = this;
		type.setId(obj.getInt("id"));
		type.setName(obj.getString("name"));
		return type;
	}
	
	public static List<Type> selectFromSQLite(Context context){
		List<Type> types = new ArrayList<Type>();
		
		SQLiteManager manager = new SQLiteManager(context);
		SQLiteDatabase db = manager.getWritableDatabase();
		

		Cursor cursor = db.query("type", new String[]{"name"}, null, null, null, null, null);
		while (cursor.moveToNext()) {
			Type type = new Type();
			type.name = cursor.getString(cursor.getColumnIndex("name"));
			types.add(type);
		}

		manager.close();
		
		return types;
	}
}
