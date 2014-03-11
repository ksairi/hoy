package com.hoy.datasources;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.util.Base64;
import com.hoy.constants.MilongaHoyConstants;
import com.hoy.helpers.ImageHelper;
import com.hoy.helpers.SQLiteMilongaHelper;
import com.hoy.helpers.SharedPreferencesHelper;
import com.hoy.utilities.DateUtils;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: ksairi
 * Date: 6/5/13
 * Time: 11:20 PM
 * To change this template use File | Settings | File Templates.
 */
public class MilongaImgDataSource {

    private SQLiteDatabase database;
    private SQLiteMilongaHelper dbMilongaHelper;
    private String[] allColumns = {SQLiteMilongaHelper.COLUMN_MILONGA_IMG_LAST_UPDATE, SQLiteMilongaHelper.COLUMN_IMG_BASE_64_STRING};

    public MilongaImgDataSource(Context context) {
        dbMilongaHelper = new SQLiteMilongaHelper(context);
    }

    public synchronized void open() throws SQLException {
        database = dbMilongaHelper.getWritableDatabase();
    }

    public synchronized void close() {
        dbMilongaHelper.close();
    }

    public synchronized void createData(String milongaId,String base64String) {

        if(getMilongaImgById(milongaId,false) == null){
            ContentValues values = new ContentValues();
            values.put(SQLiteMilongaHelper.COLUMN_IMG_BASE_64_STRING, base64String);
            values.put(SQLiteMilongaHelper.COLUMN_MILONGA_ID, milongaId);
            values.put(SQLiteMilongaHelper.COLUMN_MILONGA_IMG_LAST_UPDATE, DateUtils.getTodayString());
            open();
            database.insert(SQLiteMilongaHelper.TABLE_MILONGA_IMAGE, null,
                    values);
            close();
        }
        else{
            updateMilongaImg(milongaId,base64String);
        }
    }

    private synchronized void updateMilongaImg(String milongaId,String base64String) {
        open();
        ContentValues values = new ContentValues();
        values.put(SQLiteMilongaHelper.COLUMN_IMG_BASE_64_STRING, base64String);
        values.put(SQLiteMilongaHelper.COLUMN_MILONGA_ID, milongaId);
        values.put(SQLiteMilongaHelper.COLUMN_MILONGA_IMG_LAST_UPDATE, DateUtils.getTodayString());
        database.update(SQLiteMilongaHelper.TABLE_MILONGA_IMAGE, values, getWhereClauseString(), new String[]{milongaId});
        close();

    }


    private String getWhereClauseString() {

        return SQLiteMilongaHelper.COLUMN_MILONGA_ID.concat("=?");
    }

    public Bitmap getMilongaImgById(String milongaId,Boolean onlyActive) {
        open();
            Cursor cursor = database.query(SQLiteMilongaHelper.TABLE_MILONGA_IMAGE,
                    allColumns, getWhereClauseString(), new String[]{milongaId}, null, null, null);

            Integer cursorCount = cursor.getCount();
            if (cursorCount > 0) {
                cursor.moveToFirst();
                try{
                    Date milongaImgLastUpdate =  DateUtils.getDateFromString_YYYY_MM_DD(cursor.getString(0));
                    Long dateDifference = Calendar.getInstance().getTimeInMillis() - milongaImgLastUpdate.getTime();
                    if(onlyActive){
                        if((dateDifference / 1000 / 60 / 60 / 24) <= MilongaHoyConstants.MILONGA_IMG_DAYS_LIMIT){
                           String base64String =  cursor.getString(1);
                            cursor.close();
                            return ImageHelper.getBitMap(base64String);
                        }
                        else{
                            return null;
                        }
                    }
                    else{

                        return ImageHelper.getBitMap(cursor.getString(1));
                    }

                }catch (ParseException e){
                    cursor.close();
                    close();
                    return null;
                }

            }
            else{
                    cursor.close();
                    close();
                    return null;
                }
    }
}
