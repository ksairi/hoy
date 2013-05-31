package com.hoy.helpers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created with IntelliJ IDEA.
 * User: ksairi
 * Date: 5/29/13
 * Time: 3:04 PM
 * To change this template use File | Settings | File Templates.
 */
public class SQLiteHelper extends SQLiteOpenHelper {

  public static final String TABLE_MILONGAS = "milonga";
  public static final String COLUMN_ID = "_id";
  public static final String COLUMN_DATA = "data";

  private static final String DATABASE_NAME = "milonga.db";
  private static final int DATABASE_VERSION = 1;

  // Database creation sql statement
  private static final String DATABASE_CREATE = "create table "
      + TABLE_MILONGAS + "(" + COLUMN_ID
      + " integer primary key autoincrement, " + COLUMN_DATA
      + " text not null);";

  public SQLiteHelper(Context context) {
    super(context, DATABASE_NAME, null, DATABASE_VERSION);
  }

  @Override
  public void onCreate(SQLiteDatabase database) {
    database.execSQL(DATABASE_CREATE);
  }

  @Override
  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    Log.w(SQLiteHelper.class.getName(),
			"Upgrading database from version " + oldVersion + " to "
					+ newVersion + ", which will destroy all old data");
    db.execSQL("DROP TABLE IF EXISTS " + COLUMN_DATA);
    onCreate(db);
  }

}
