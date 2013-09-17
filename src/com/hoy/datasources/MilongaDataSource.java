package com.hoy.datasources;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import com.hoy.constants.MilongaHoyConstants;
import com.hoy.helpers.SQLiteMilongaHelper;

/**
 * Created with IntelliJ IDEA.
 * User: ksairi
 * Date: 5/29/13
 * Time: 3:09 PM
 * To change this template use File | Settings | File Templates.
 */
public class MilongaDataSource { // Database fields

    private SQLiteDatabase database;
    private SQLiteMilongaHelper dbMilongaHelper;
    private String[] allColumns = {SQLiteMilongaHelper.COLUMN_DATA};
    private String[] args = {};

    public MilongaDataSource(Context context) {
        dbMilongaHelper = new SQLiteMilongaHelper(context);
    }

    public synchronized void open() throws SQLException {
        database = dbMilongaHelper.getWritableDatabase();
    }

    public synchronized void close() {
        dbMilongaHelper.close();
    }

    public synchronized void createData(String data) {
        ContentValues values = new ContentValues();
        String milongas = getAllMilongas();
        if (!milongas.equals(MilongaHoyConstants.EMPTY_STRING)) {
            updateMilongas(data);
        } else {
            values.put(SQLiteMilongaHelper.COLUMN_DATA, data);
            database.insert(SQLiteMilongaHelper.TABLE_MILONGA, null,
                    values);
        }

    }

    public void deleteMilonga(String id) {

        System.out.println("Comment deleted with id: " + id);
        database.delete(SQLiteMilongaHelper.TABLE_MILONGA, SQLiteMilongaHelper.COLUMN_ID
                + " = " + id, null);
    }

    public synchronized String getAllMilongas() {

        String jsonString = MilongaHoyConstants.EMPTY_STRING;

        Cursor cursor = database.query(SQLiteMilongaHelper.TABLE_MILONGA,
                allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            jsonString = getJsonString(cursor);
            cursor.moveToNext();
        }
        // Make sure to close the cursor
        cursor.close();
        return jsonString;
    }

    private String getJsonString(Cursor cursor) {
        return cursor.getString(0); //get the data column
    }

    private void updateMilongas(String data) {
        ContentValues values = new ContentValues();
        values.put(SQLiteMilongaHelper.COLUMN_DATA, data);
        database.update(SQLiteMilongaHelper.TABLE_MILONGA, values, "", args);

    }
}
