package com.hoy.helpers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import com.hoy.constants.MilongaHoyConstants;

/**
 * Created with IntelliJ IDEA.
 * User: ksairi
 * Date: 5/29/13
 * Time: 3:04 PM
 * To change this template use File | Settings | File Templates.
 */
public class SQLiteMilongaHelper extends SQLiteOpenHelper {

    private Context context;

    public static final String TABLE_MILONGA = "milonga";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_DATA = "data";


    public static final String TABLE_PROMO_IMG = "promo_img";
    public static final String COLUMN_IMG_BASE_64_STRING = "base64String";
    public static final String COLUMN_IMG_WIDTH = "column_img_width";
    public static final String COLUMN_IMG_HEIGHT = "column_img_height";
    public static final String PROMO_IMG_URL_DESTINATION = "promoImgUrlDestination";

    public static final String TABLE_MILONGA_IMAGE = "milonga_image";
    public static final String COLUMN_MILONGA_ID = "column_milonga_id";
    public static final String COLUMN_MILONGA_IMG_LAST_UPDATE = "column_milonga_img_last_update";


    private static final String DATABASE_NAME = "hoy_milonga.db";
    private static final int DATABASE_VERSION = 2;

    private static final String TABLE_MILONGA_CREATE = "create table "
            .concat(TABLE_MILONGA).concat("(").concat(COLUMN_ID)
            .concat(" integer primary key autoincrement, ").concat(COLUMN_DATA)
            .concat(" text not null);");

    private static final String TABLE_PROMO_IMG_CREATE = " create table "
            .concat(TABLE_PROMO_IMG).concat("(").concat(COLUMN_ID)
            .concat(" integer primary key autoincrement, ").concat(COLUMN_IMG_BASE_64_STRING)
            .concat(" text not null, ")
            .concat(PROMO_IMG_URL_DESTINATION).concat(" text,")
            .concat(COLUMN_IMG_WIDTH).concat(" integer not null,")
            .concat(COLUMN_IMG_HEIGHT).concat(" integer not null);");

    private static final String TABLE_MILONGA_IMAGE_CREATE = " create table "
            .concat(TABLE_MILONGA_IMAGE).concat("(").concat(COLUMN_ID)
            .concat(" integer primary key autoincrement, ").concat(COLUMN_IMG_BASE_64_STRING)
            .concat(" text not null, ")
            .concat(COLUMN_MILONGA_ID).concat(" text not null, ")
            .concat(COLUMN_MILONGA_IMG_LAST_UPDATE).concat(" text not null)");

    public SQLiteMilongaHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(TABLE_MILONGA_CREATE);
        database.execSQL(TABLE_PROMO_IMG_CREATE);
        database.execSQL(TABLE_MILONGA_IMAGE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(SQLiteMilongaHelper.class.getName(),
                ("Upgrading database from version " + oldVersion).concat(" to "
                        + newVersion).concat(", which will destroy all old data"));
        db.execSQL("DROP TABLE IF EXISTS " .concat(TABLE_MILONGA));
        db.execSQL("DROP TABLE IF EXISTS " .concat(TABLE_PROMO_IMG));
        db.execSQL("DROP TABLE IF EXISTS " .concat(TABLE_MILONGA_IMAGE));
        onCreate(db);
    }

}
