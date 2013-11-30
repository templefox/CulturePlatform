package com.example.database;

import com.example.cultureplatform.R;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteManager extends SQLiteOpenHelper {
	
	public SQLiteManager(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		// TODO Auto-generated constructor stub
	}

	private static final String DATABASE_NAME = "CulturePlatform.db";
	private static final int DATABASE_VERSION = 1;

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		//db.execSQL("SET FOREIGN_KEY_CHECKS=0;");
		db.execSQL(
				"CREATE TABLE activity (" +
				"id  INTEGER(11) NOT NULL," +
				"name  TEXT(45)," +
				"address  TEXT(255) ," +
				"content  TEXT," +
				"type  TEXT(45) ," +
				"theme  TEXT(45)," +
				"date  TEXT," +
				"time  TEXT," +
				"procedure  TEXT," +
				"picture_url  TEXT(45)," +
				"reporter_info  TEXT," +
				"organiserID  INTEGER(11)," +
				"locationID  INTEGER(11)," +
				"temperature  INTEGER(11) NOT NULL DEFAULT 0," +
				"reporterRating  REAL NOT NULL DEFAULT 0," +
				"staffRating  REAL NOT NULL DEFAULT 0," +
				"contentRating  REAL NOT NULL DEFAULT 0," +
				"environmentRating  REAL NOT NULL DEFAULT 0," +
				"PRIMARY KEY (id));");
		
		db.execSQL(
				"CREATE TABLE attention (" +
				"id  INTEGER(11) NOT NULL," +
				"isPresent  INTEGER(1)," +
				"UserID  INTEGER(11)," +
				"ActivityID  INTEGER(11)," +
				"isRating  INTEGER(1)," +
				"contentRating  INTEGER(11)," +
				"reporterRating  INTEGER(11)," +
				"staffRating  INTEGER(11)," +
				"environmentRating  INTEGER(11)," +
				"advice  TEXT," +
				"PRIMARY KEY (id)" +
				")" +
				"" +
				";");
		
		db.execSQL(
				"CREATE TABLE comment (" +
				"id  INTEGER(11) NOT NULL," +
				"Content  TEXT," +
				"datetime  TEXT NOT NULL," +
				"UserID  INTEGER(11)," +
				"ActivityID  INTEGER(11)," +
				"picture_url  TEXT," +
				"PRIMARY KEY (id)" +
				")" +
				"" +
				";");
		
		db.execSQL(
				"CREATE TABLE location (" +
				"id  INTEGER(11) NOT NULL," +
				"name  TEXT(45) NOT NULL," +
				"detail  TEXT," +
				"picture_url  TEXT(45)," +
				"address  TEXT(255) NOT NULL," +
				"PRIMARY KEY (id)" +
				")" +
				"" +
				";");
		
		db.execSQL(
				"CREATE TABLE type (" +
				"id  INTEGER(11) NOT NULL," +
				"name  TEXT(45) NOT NULL," +
				"PRIMARY KEY (id)" +
				")" +
				"" +
				";");
		
		db.execSQL(
				"CREATE TABLE user (" +
				"id  INTEGER(10) NOT NULL," +
				"E_mail  TEXT(45) NOT NULL," +
				"name  TEXT(20) NOT NULL," +
				"reg_time  TEXT NOT NULL," +
				"phone_num  TEXT(11)," +
				"school_id  INTEGER(11)," +
				"authority  INTEGER(1) NOT NULL," +
				"password  TEXT(20) NOT NULL," +
				"PRIMARY KEY (id)" +
				")" +
				"" +
				";");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS activity;");
		db.execSQL("DROP TABLE IF EXISTS attention;");
		db.execSQL("DROP TABLE IF EXISTS comment;");
		db.execSQL("DROP TABLE IF EXISTS location;");
		db.execSQL("DROP TABLE IF EXISTS type;");
		db.execSQL("DROP TABLE IF EXISTS user;");
		
		db.execSQL(
				"CREATE TABLE activity (" +
				"id  INTEGER(11) NOT NULL," +
				"name  TEXT(45)," +
				"address  TEXT(255) ," +
				"content  TEXT," +
				"type  TEXT(45) ," +
				"theme  TEXT(45)," +
				"date  TEXT," +
				"time  TEXT," +
				"procedure  TEXT," +
				"picture_url  TEXT(45)," +
				"reporter_info  TEXT," +
				"organiserID  INTEGER(11)," +
				"locationID  INTEGER(11)," +
				"temperature  INTEGER(11) NOT NULL DEFAULT 0," +
				"reporterRating  REAL NOT NULL DEFAULT 0," +
				"staffRating  REAL NOT NULL DEFAULT 0," +
				"contentRating  REAL NOT NULL DEFAULT 0," +
				"environmentRating  REAL NOT NULL DEFAULT 0," +
				"PRIMARY KEY (id));");
		
		db.execSQL(
				"CREATE TABLE attention (" +
				"id  INTEGER(11) NOT NULL," +
				"isPresent  INTEGER(1)," +
				"UserID  INTEGER(11)," +
				"ActivityID  INTEGER(11)," +
				"isRating  INTEGER(1)," +
				"contentRating  INTEGER(11)," +
				"reporterRating  INTEGER(11)," +
				"staffRating  INTEGER(11)," +
				"environmentRating  INTEGER(11)," +
				"advice  TEXT," +
				"PRIMARY KEY (id)" +
				")" +
				"" +
				";");
		
		db.execSQL(
				"CREATE TABLE comment (" +
				"id  INTEGER(11) NOT NULL," +
				"Content  TEXT," +
				"datetime  TEXT NOT NULL," +
				"UserID  INTEGER(11)," +
				"ActivityID  INTEGER(11)," +
				"picture_url  TEXT," +
				"PRIMARY KEY (id)" +
				")" +
				"" +
				";");
		
		db.execSQL(
				"CREATE TABLE location (" +
				"id  INTEGER(11) NOT NULL," +
				"name  TEXT(45) NOT NULL," +
				"detail  TEXT," +
				"picture_url  TEXT(45)," +
				"address  TEXT(255) NOT NULL," +
				"PRIMARY KEY (id)" +
				")" +
				"" +
				";");
		
		db.execSQL(
				"CREATE TABLE type (" +
				"id  INTEGER(11) NOT NULL," +
				"name  TEXT(45) NOT NULL," +
				"PRIMARY KEY (id)" +
				")" +
				"" +
				";");
		
		db.execSQL(
				"CREATE TABLE user (" +
				"id  INTEGER(10) NOT NULL," +
				"E_mail  TEXT(45) NOT NULL," +
				"name  TEXT(20) NOT NULL," +
				"reg_time  TEXT NOT NULL," +
				"phone_num  TEXT(11)," +
				"school_id  INTEGER(11)," +
				"authority  INTEGER(1) NOT NULL," +
				"password  TEXT(20) NOT NULL," +
				"PRIMARY KEY (id)" +
				")" +
				"" +
				";");
	}

}
