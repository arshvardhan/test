/*package com.kelltontech.framework.db;

import android.app.Activity;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

public class PendingLocationDataHandler {
	public static final String TABLE_NAME = "PendingLocationDataTable";
	private static final String ROW_ID = "row_id";
	private static final String PENDING_JSON = "pending_json";

	public static final String[] TABLE_COLUMNS = { PENDING_JSON };
	public static final String CREATE_QUERY = "CREATE TABLE " + TABLE_NAME + "(" + ROW_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + TABLE_COLUMNS[0] + " TEXT )";
	DataBaseHelper mdbHelper;

	public PendingLocationDataHandler(Activity callerActivity) {
		// mdbHelper =
		// ((MyApplication)callerActivity.getApplication()).getDataHelper();
	}

	public synchronized long insertROW(String jsonstring) {
		SQLiteDatabase db = mdbHelper.getWritableDatabase();
		long res = 0;
		try {
			ContentValues vals = new ContentValues();
			vals.put("json", jsonstring);
			res = db.insert(TABLE_NAME, null, vals);
		} catch (Exception e) {
		} finally {
			if (db != null)
				db.close();
		}
		return res;
	}
}
*/