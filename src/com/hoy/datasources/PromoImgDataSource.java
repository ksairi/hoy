package com.hoy.datasources;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import com.hoy.constants.MilongaHoyConstants;
import com.hoy.helpers.SQLiteMilongaHelper;
import com.hoy.helpers.SharedPreferencesHelper;

/**
 * Created with IntelliJ IDEA.
 * User: ksairi
 * Date: 6/5/13
 * Time: 11:20 PM
 * To change this template use File | Settings | File Templates.
 */
public class PromoImgDataSource {

    private SQLiteDatabase database;
    private SQLiteMilongaHelper dbMilongaHelper;
    private String[] allColumns = {SQLiteMilongaHelper.COLUMN_IMG_BASE_64_STRING, SQLiteMilongaHelper.PROMO_IMG_URL_DESTINATION};

    public PromoImgDataSource(Context context) {
        dbMilongaHelper = new SQLiteMilongaHelper(context);
    }

    public synchronized void open() throws SQLException {
        database = dbMilongaHelper.getWritableDatabase();
    }

    public synchronized void close() {
        dbMilongaHelper.close();
    }

    public synchronized void createData(String base64, String urlDestination, Integer width, Integer height) {

        ContentValues values = new ContentValues();
        values.put(SQLiteMilongaHelper.COLUMN_IMG_BASE_64_STRING, base64);
        values.put(SQLiteMilongaHelper.PROMO_IMG_URL_DESTINATION, urlDestination);
        values.put(SQLiteMilongaHelper.COLUMN_IMG_WIDTH, width);
        values.put(SQLiteMilongaHelper.COLUMN_IMG_HEIGHT, height);
        database.insert(SQLiteMilongaHelper.TABLE_PROMO_IMG, null,
                values);
    }

    public void deleteImgPromo(String id) {

        System.out.println("Milonga deleted with id: " + id);
        database.delete(SQLiteMilongaHelper.TABLE_PROMO_IMG, SQLiteMilongaHelper.COLUMN_ID
                + " = " + id, null);
    }

    public void truncateImgPromoTable() {

        database.delete(SQLiteMilongaHelper.TABLE_PROMO_IMG, null, null);
    }

    private String[] getImgPromo(Cursor cursor) {
        String[] result = new String[2];
        result[MilongaHoyConstants.PROMO_IMG_BASE_64_INDEX_POSITION] = cursor.getString(MilongaHoyConstants.PROMO_IMG_BASE_64_INDEX_POSITION); //get the base_64 column
        result[MilongaHoyConstants.PROMO_IMG_URL_DESTINATION_INDEX_POSITION] = cursor.getString(MilongaHoyConstants.PROMO_IMG_URL_DESTINATION_INDEX_POSITION); //get the url column
        return result;
    }

    private synchronized void updateImgPromos(String data, Integer width, Integer height) {
        ContentValues values = new ContentValues();
        values.put(SQLiteMilongaHelper.COLUMN_DATA, data);
        values.put(SQLiteMilongaHelper.COLUMN_IMG_WIDTH, width);
        values.put(SQLiteMilongaHelper.COLUMN_IMG_HEIGHT, height);
        database.update(SQLiteMilongaHelper.TABLE_PROMO_IMG, values, getWhereClauseString(), new String[]{data});

    }


    private String getWhereClauseString() {

        return SQLiteMilongaHelper.COLUMN_DATA.concat("=?");
    }

    public String[] getNextPromoImgDataByIndex(Context context, Integer index) {

        String[] result = null;

        Cursor cursor = database.query(SQLiteMilongaHelper.TABLE_PROMO_IMG,
                allColumns, null, null, null, null, null);

        Integer cursorCount = cursor.getCount();
        if (cursorCount > 0) {
            index = (index < cursorCount - 1) ? ++index : 0;
            cursor.moveToPosition(index);
            result = getImgPromo(cursor);
            SharedPreferencesHelper.setValueSharedPreferences(context, MilongaHoyConstants.CURRENT_IMG_PROMO_INDEX, index.toString());
        }
        // Make sure to close the cursor
        cursor.close();

        return result;
    }

    public String[] getPromoImgDataByIndex(Integer index) {

        String[] result = null;

        Cursor cursor = database.query(SQLiteMilongaHelper.TABLE_PROMO_IMG,
                allColumns, null, null, null, null, null);

        Integer cursorCount = cursor.getCount();
        if (cursorCount > 0 && index >= 0 && index <= cursorCount - 1) {
            cursor.moveToPosition(index);
            result = getImgPromo(cursor);
        }
        // Make sure to close the cursor
        cursor.close();

        return result;
    }
}
