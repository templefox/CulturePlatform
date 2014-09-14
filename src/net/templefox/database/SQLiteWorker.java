package net.templefox.database;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import net.templefox.database.data.Entity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public abstract class SQLiteWorker {

	public static <T extends Entity> void insertIntoSQLite(Set<T> entities, Context context) {
		SQLiteManager manager = new SQLiteManager(context);
		SQLiteDatabase db = manager.getWritableDatabase();
		db.beginTransaction();
		
		try 
		{
			for (Entity entity : entities) 
			{
				SQLiteWorker.insertOrUpdata(entity, db);
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
			SQLiteWorker.insertOrUpdata(entity, db);
			db.setTransactionSuccessful();
		}finally{
			db.endTransaction();
			manager.close();
		}
	}

	private static synchronized void insertOrUpdata(Entity entity, SQLiteDatabase db) {
		try {
			ContentValues values = entity.getContentValues();
			db.replace(entity.getTableName(), null, values);
		} catch (IllegalArgumentException e) {
			Log.e("CP Error", e.getMessage());
			Log.w("CP Exception", Log.getStackTraceString(e));
		} catch (NullPointerException e) {
			Log.e("CP Error", e.getMessage());
			Log.w("CP Exception", Log.getStackTraceString(e));
		}
	
	}

	public static List<ContentValues> selectFromSQLite(String table,String[] columns,String where,String[] whereArgs,Context context,String orderBy) {
		List<ContentValues> list = new ArrayList<ContentValues>();
		
		SQLiteManager manager = new SQLiteManager(context);
		SQLiteDatabase db = manager.getReadableDatabase();
		
	
		Cursor cursor = db.query(table, columns, where, whereArgs, null, null, orderBy);
		
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
		return selectFromSQLite(table, columns, where, whereArgs, context,null);
	}

	public static List<ContentValues> selectFromSQLite(String table,String[] columns,Context context,String orderBy) {
		return selectFromSQLite(table, columns, null, null, context, orderBy);
	}

	public static List<ContentValues> selectFromSQLite(String table,String[] columns,Context context) {
		return selectFromSQLite(table, columns, context, null);
	}

}
