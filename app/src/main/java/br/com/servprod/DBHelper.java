package br.com.servprod;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper {
	private static final String DB_NAME = "db_servprod";
	private static final int DB_VER = 1;

	public DBHelper(Context context){
		super(context, DB_NAME, null, DB_VER);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		try {
			String sql="CREATE TABLE IF NOT EXISTS portfolio(id INTEGER PRIMARY KEY, post INTEGER)";
			db.execSQL(sql);

			sql="CREATE TABLE IF NOT EXISTS posts(id INTEGER PRIMARY KEY, image VARCHAR(80))";
			db.execSQL(sql);

			sql="CREATE TABLE IF NOT EXISTS favorites(id INTEGER PRIMARY KEY, user_id INTEGER, name VARCHAR(80))";
			db.execSQL(sql);

		} catch (Exception e) {
			e.printStackTrace();
			Log.i("DataBase", e.toString());
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
		db.execSQL("drop table portfolio;");
		db.execSQL("drop table posts;");
		db.execSQL("drop table favorites;");
		onCreate(db);
	}

}