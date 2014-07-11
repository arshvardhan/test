package com.kelltontech.maxisgetit.db;

import java.util.ArrayList;

import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import com.kelltontech.framework.db.MyApplication;
import com.kelltontech.maxisgetit.dao.FavouriteCompanies;

public class FavCompanysTable {
	public static final String TABLE_NAME = "Fav_companies";

	public static final String FAV_COMPANY_ID = " _id";
	public static final String FAV_COMPANY_NAME = "fav_company_name";

	public static final String[] TABLE_COLUMNS = { FAV_COMPANY_ID, FAV_COMPANY_NAME };
	
	public static final String CREATE_QUERY = "CREATE TABLE " + TABLE_NAME + " (" + TABLE_COLUMNS[0] + " INTEGER PRIMARY KEY AUTOINCREMENT, " + TABLE_COLUMNS[1] +" TEXT )";

	private DataBaseHelper mdbHelper;

	public FavCompanysTable(MyApplication myApp) {
		mdbHelper = myApp.getDataHelper();
	}

	public void addFavCompaniesList(ArrayList<FavouriteCompanies> favCompaniesList) {
		SQLiteDatabase db = mdbHelper.getWritableDatabase();
		try {
			SQLiteStatement insStmt = db.compileStatement("INSERT INTO " + TABLE_NAME + " (" + TABLE_COLUMNS[1]  + ") " + " VALUES (?);");
			db.beginTransaction();
			insertRows(insStmt, favCompaniesList);
			db.setTransactionSuccessful();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.endTransaction();
			db.close();
		}
	}

	private void insertRows(SQLiteStatement insStmt, ArrayList<FavouriteCompanies> favCompaniesList) {
		for (int i = 0; i < favCompaniesList.size(); i++) {
			FavouriteCompanies favCompany = favCompaniesList.get(i);
			insStmt.bindString(1, favCompany.getFavComIdCategoryId());
			try {
				insStmt.executeInsert(); 
			} catch (SQLiteConstraintException e) {
			}
		}
	}

	public void delFavCompaniesList(ArrayList<FavouriteCompanies> favCompaniesList) {
		SQLiteDatabase db = mdbHelper.getWritableDatabase();
		try {
			db.beginTransaction();
			for (int i = 0; i < favCompaniesList.size(); i++) {
			FavouriteCompanies favCompany = favCompaniesList.get(i);
			String value = favCompany.getFavComIdCategoryId();
			Log.e("FindIT", "DB: " + value);
			db.delete(TABLE_NAME, FAV_COMPANY_NAME + " = ?", new String[] { value });
			}
			db.setTransactionSuccessful();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.endTransaction();
			db.close();
		}
	}
	
	public ArrayList<FavouriteCompanies> getAllFavCompaniesList() {
		SQLiteDatabase db = mdbHelper.getReadableDatabase();
		Cursor cursor = null;
		ArrayList<FavouriteCompanies> results = new ArrayList<FavouriteCompanies>();
		try {
			cursor = db.query(TABLE_NAME, TABLE_COLUMNS, null, null, null, null, TABLE_COLUMNS[1]);
			if (cursor.getCount() > 0) {
				cursor.moveToFirst();
				do {
					FavouriteCompanies favCompany = new FavouriteCompanies();
					favCompany.setId((int)cursor.getLong(0));
					favCompany.setFavComIdCategoryId(cursor.getString(1));
					results.add(favCompany);
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