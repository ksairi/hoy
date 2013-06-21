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
public class SQLiteMilongaHelper extends SQLiteOpenHelper {

  	public static final String TABLE_MILONGA = "milonga";
  	public static final String COLUMN_ID = "_id";
  	public static final String COLUMN_DATA = "data";

	public static final String PROMO_IMG_BASE_64_STRING = "base64String";
	public static final String TABLE_PROMO_IMG = "promo_img";
	public static final String COLUMN_IMG_WIDTH = "column_img_width";
	public static final String COLUMN_IMG_HEIGHT = "column_img_height";
	public static final String PROMO_IMG_URL_DESTINATION = "promoImgUrlDestination";


  private static final String DATABASE_NAME = "hoy_milonga.db";
  private static final int DATABASE_VERSION = 1;

  	private static final String TABLE_MILONGA_CREATE = "create table "
      + TABLE_MILONGA + "(" + COLUMN_ID
      + " integer primary key autoincrement, " + COLUMN_DATA
      + " text not null);";

	private static final String TABLE_PROMO_IMG_CREATE = " create table "
      	+ TABLE_PROMO_IMG + "(" + COLUMN_ID
      	+ " integer primary key autoincrement, " + PROMO_IMG_BASE_64_STRING
      	+ " text not null, "
		+ PROMO_IMG_URL_DESTINATION + " text,"
	  	+ COLUMN_IMG_WIDTH + " integer not null,"
	  	+ COLUMN_IMG_HEIGHT + " integer not null);";

  public SQLiteMilongaHelper(Context context) {
    super(context, DATABASE_NAME, null, DATABASE_VERSION);
  }

  @Override
  public void onCreate(SQLiteDatabase database) {
    database.execSQL(TABLE_MILONGA_CREATE);
	database.execSQL(TABLE_PROMO_IMG_CREATE);
  }

  @Override
  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    Log.w(SQLiteMilongaHelper.class.getName(),
			"Upgrading database from version " + oldVersion + " to "
					+ newVersion + ", which will destroy all old data");
    db.execSQL("DROP TABLE IF EXISTS " + TABLE_MILONGA);
	db.execSQL("DROP TABLE IF EXISTS " + TABLE_PROMO_IMG);
    onCreate(db);
  }

}
