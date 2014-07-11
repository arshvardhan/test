package com.kelltontech.maxisgetit.db;

import java.util.ArrayList;
import com.kelltontech.framework.db.MyApplication;
import com.kelltontech.maxisgetit.dao.CityOrLocality;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

public class CityTable {
	public static final String TABLE_NAME = "Cities";

	public static final String CITY_ID = "city_id";
	public static final String NAME = "city_name";

	public static final String[] TABLE_COLUMNS = { CITY_ID, NAME };
	
	public static final String CREATE_QUERY = "CREATE TABLE " + TABLE_NAME + " (" + TABLE_COLUMNS[0] + " INTEGER, " + TABLE_COLUMNS[1] +" TEXT )";// + "PRIMARY KEY (" + TABLE_COLUMNS[0] + "))";

	private DataBaseHelper mdbHelper;

	public CityTable(MyApplication myApp) {
		mdbHelper = myApp.getDataHelper();
	}

	public void addCityList(ArrayList<CityOrLocality> cityList) {
		SQLiteDatabase db = mdbHelper.getWritableDatabase();
		try {
			SQLiteStatement insStmt = db.compileStatement("INSERT INTO " + TABLE_NAME + " (" + TABLE_COLUMNS[0] + "," + TABLE_COLUMNS[1]  + ") "
					+ " VALUES (?, ?);");
			db.beginTransaction();
			insertRows(insStmt, cityList);
			db.setTransactionSuccessful();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.endTransaction();
			db.close();
		}
	}

	private void insertRows(SQLiteStatement insStmt, ArrayList<CityOrLocality> cityList) {
		for (int i = 0; i < cityList.size(); i++) {
			CityOrLocality city = cityList.get(i);
			insStmt.bindLong(1, city.getId());
			insStmt.bindString(2, city.getName());
			try {
				long rowId = insStmt.executeInsert(); // should really check
														// value here!
			} catch (SQLiteConstraintException e) {
			}
		}
	}


	public ArrayList<CityOrLocality> getAllCitiesList() {
		SQLiteDatabase db = mdbHelper.getReadableDatabase();
		Cursor cursor = null;
		ArrayList<CityOrLocality> results = new ArrayList<CityOrLocality>();
		try {
			cursor = db.query(TABLE_NAME, TABLE_COLUMNS, null, null, null, null, TABLE_COLUMNS[1]);
			if (cursor.getCount() > 0) {
				cursor.moveToFirst();
				do {
					CityOrLocality city = new CityOrLocality();
					city.setId((int)cursor.getLong(0));
					city.setName(cursor.getString(1));
					results.add(city);
				} while (cursor.moveToNext());
			}

			return results;
		} catch (Exception e) {
			e.printStackTrace();
			return results;
		} finally {
			if (cursor != null && !cursor.isClosed()) {
				cursor.close();
			}
		}
	}

	public static boolean getBoolValue(String str) {
		if (str == null || str.trim().equals("") || str.trim().equals("0"))
			return false;
		return true;
	}

	public void deleteAll() {
		SQLiteDatabase db = mdbHelper.getWritableDatabase();
		db.delete(TABLE_NAME, null, null);
	}
}
