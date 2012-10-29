package com.android.textview;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class CreateItemHelper extends SQLiteOpenHelper {

	public CreateItemHelper(Context con){
		super(con, "itemdb", null, 1);
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO 自動生成されたメソッド・スタブ
		String sql = "create table item(_id integer primary key autoincrement," +
				"itemType text not null," +
				"itemName text not null)";
		db.execSQL(sql);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO 自動生成されたメソッド・スタブ

	}

}
