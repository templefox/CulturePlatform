package com.example.database.data;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.database.SQLiteManager;

public abstract class Entity {

	public static <T extends Entity> void insertIntoSQLite(Set<T> entities, Context context) {
		SQLiteManager manager = new SQLiteManager(context);
		SQLiteDatabase db = manager.getWritableDatabase();
		db.beginTransaction();
		
		try 
		{
			for (Entity entity : entities) 
			{
				insertOrUpdata(entity, db);
			}
			db.setTransactionSuccessful();
		}finally{
			db.endTransaction();
			manager.close();
		}
	}
	public static <T extends Entity> void insertIntoSQLite(T entity, Context context) {
		SQLiteManager manager = new SQLiteManager(context);
		SQLiteDatabase db = manager.getWritableDatabase();
		db.beginTransaction();
		
		try 
		{
			insertOrUpdata(entity, db);
			db.setTransactionSuccessful();
		}finally{
			db.endTransaction();
			manager.close();
		}
	}
	private static synchronized void insertOrUpdata(Entity entity, SQLiteDatabase db) {
		ContentValues values = entity.getContentValues();
		db.replace(entity.getTableName(), null, values);
	}	
	protected abstract ContentValues getContentValues();
	protected abstract String getTableName();

	//TODO ����
	public static List<ContentValues> selectFromSQLite(String table,String[] columns,Context context) {
		List<ContentValues> list = new ArrayList<ContentValues>();
		
		
		SQLiteManager manager = new SQLiteManager(context);
		SQLiteDatabase db = manager.getReadableDatabase();
		
	
		Cursor cursor = db.query(table, columns, null, null, null, null, null);
		
		while (cursor.moveToNext()) {
			ContentValues contentValues = new ContentValues();
			for (String columnName : cursor.getColumnNames()) {
				String value = cursor.getString(cursor.getColumnIndex(columnName));
				contentValues.put(columnName, value);
			}
			list.add(contentValues);
		}
		
		manager.close();
		
		return list;
	}
	
	public static List<ContentValues> selectFromSQLite(String table,String[] columns,String where,String[] whereArgs,Context context) {
		List<ContentValues> list = new ArrayList<ContentValues>();
		
		
		SQLiteManager manager = new SQLiteManager(context);
		SQLiteDatabase db = manager.getReadableDatabase();
		
	
		Cursor cursor = db.query(table, columns, where, whereArgs, null, null, null);
		
		while (cursor.moveToNext()) {
			ContentValues contentValues = new ContentValues();
			for (String columnName : cursor.getColumnNames()) {
				String value = cursor.getString(cursor.getColumnIndex(columnName));
				contentValues.put(columnName, value);
			}
			list.add(contentValues);
		}
		
		manager.close();
		
		return list;
	}
	
	
	public abstract Entity transJSON(JSONObject obj) throws JSONException, ParseException;

	
}