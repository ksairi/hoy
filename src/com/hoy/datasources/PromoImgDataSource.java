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
	  	private String[] allColumns = {SQLiteMilongaHelper.COLUMN_DATA};

	public PromoImgDataSource(Context context) {
		dbMilongaHelper = new SQLiteMilongaHelper(context);
	}

	public synchronized void open() throws SQLException {
		database = dbMilongaHelper.getWritableDatabase();
	}

	public synchronized void close() {
		dbMilongaHelper.close();
	}

	public synchronized void createData(String base64,Integer width, Integer height) {

	ContentValues values = new ContentValues();
		  values.put(SQLiteMilongaHelper.COLUMN_DATA, base64);
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

	private synchronized String getImgPromoBase64ByBase64(String base64) {

		String imgPromo = MilongaHoyConstants.EMPTY_STRING;

		if(base64 != null){

			Cursor cursor = database.query(SQLiteMilongaHelper.TABLE_PROMO_IMG,
			allColumns,getWhereClauseString() , new String[]{base64}, null, null, null);


			cursor.moveToFirst();
			while (!cursor.isAfterLast()) {
				imgPromo = getImgPromo(cursor);
				cursor.moveToNext();
			}
			// Make sure to close the cursor
			cursor.close();
		}
			return imgPromo;

	}

	private String getImgPromo(Cursor cursor) {
	return cursor.getString(0); //get the data column
	}

	private synchronized void updateImgPromos(String data, Integer width, Integer height){
		ContentValues values = new ContentValues();
		values.put(SQLiteMilongaHelper.COLUMN_DATA, data);
		values.put(SQLiteMilongaHelper.COLUMN_IMG_WIDTH, width);
		values.put(SQLiteMilongaHelper.COLUMN_IMG_HEIGHT, height);
		database.update(SQLiteMilongaHelper.TABLE_PROMO_IMG, values,getWhereClauseString(),new String[]{data});

	}


	private String getWhereClauseString(){

		return SQLiteMilongaHelper.COLUMN_DATA.concat("=?");
	}

	public String getImgPromoBase64ByIndex(Context context, Integer index) {

		String imgPromoBase64 = MilongaHoyConstants.EMPTY_STRING;

		Cursor cursor = database.query(SQLiteMilongaHelper.TABLE_PROMO_IMG,
		allColumns,null , null , null, null, null);

		Integer cursorCount = cursor.getCount();
		if(cursorCount > 0){

			index = (index < cursorCount - 1)?++index:0;
			cursor.moveToPosition(index);
			imgPromoBase64 = getImgPromo(cursor);
			SharedPreferencesHelper.setValueSharedPreferences(context, MilongaHoyConstants.CURRENT_IMG_PROMO_INDEX,index.toString());
		}
		// Make sure to close the cursor
		cursor.close();

		return imgPromoBase64;
	}
}
