package com.hoy.helpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import com.hoy.constants.MilongaHoyConstants;
import com.hoy.dto.EventDTO;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: ksairi
 * Date: 5/29/13
 * Time: 3:09 PM
 * To change this template use File | Settings | File Templates.
 */
public class MilongaDataSource{ // Database fields

	private SQLiteDatabase database;
  	private SQLiteHelper dbHelper;
  	private String[] allColumns = {SQLiteHelper.COLUMN_DATA };
	private String[] args = {};

  public MilongaDataSource(Context context) {
    dbHelper = new SQLiteHelper(context);
  }

  public void open() throws SQLException {
    database = dbHelper.getWritableDatabase();
  }

  public void close() {
    dbHelper.close();
  }

  public void createData(String data) {
    ContentValues values = new ContentValues();
	  String milongas = getAllMilongas();
	  if (!milongas.equals(MilongaHoyConstants.EMPTY_STRING)){
		  updateMilongas(data);
	  }
	  else{
		  values.put(SQLiteHelper.COLUMN_DATA, data);
		      database.insert(SQLiteHelper.TABLE_MILONGAS, null,
		          values);
	  }

  }

  public void deleteMilonga(String id) {

    System.out.println("Comment deleted with id: " + id);
    database.delete(SQLiteHelper.TABLE_MILONGAS, SQLiteHelper.COLUMN_ID
        + " = " + id, null);
  }

  public String getAllMilongas() {

	  String jsonString = MilongaHoyConstants.EMPTY_STRING;

	  Cursor cursor = database.query(SQLiteHelper.TABLE_MILONGAS,
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

	private void updateMilongas(String data){
		ContentValues values = new ContentValues();
		values.put(SQLiteHelper.COLUMN_DATA, data);
		database.update(SQLiteHelper.TABLE_MILONGAS, values,"",args);

	}
}
