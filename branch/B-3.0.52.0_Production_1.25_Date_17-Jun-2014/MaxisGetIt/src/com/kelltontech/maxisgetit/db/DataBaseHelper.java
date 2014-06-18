package com.kelltontech.maxisgetit.db;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

public class DataBaseHelper extends SQLiteOpenHelper {

	private SQLiteDatabase myDataBase;

	private final Context myContext;

	// private SQLiteStatement insertStmt;

	/**
	 * Constructor Takes and keeps a reference of the passed context in order to
	 * access to the application assets and resources.
	 * 
	 * @param context
	 */
	public DataBaseHelper(Context context) {
		super(context, DBManifest.DB_NAME, null, DBManifest.DATABASE_VERSION);
		this.myContext = context;
		this.myDataBase = getWritableDatabase();
		// this.insertStmt = this.myDataBase.compileStatement(INSERT);
	}

	/**
	 * Creates a empty database on the system and rewrites it with your own
	 * database.
	 * */
	public void createDataBase() throws IOException {

		boolean dbExist = checkDataBase();

		if (dbExist) {
			// do nothing - database already exist
		} else {
			// By calling this method and empty database will be created into
			// the default system path
			// of your application so we are gonna be able to overwrite that
			// database with our database.
			this.getReadableDatabase();
			try {
				copyDataBase();
			} catch (IOException e) {
				throw new Error("Error copying database");
			}
		}

	}

	/**
	 * Check if the database already exist to avoid re-copying the file each
	 * time you open the application.
	 * 
	 * @return true if it exists, false if it doesn't
	 */
	private boolean checkDataBase() {
		SQLiteDatabase checkDB = null;
		try {
			String myPath = DBManifest.DB_PATH + DBManifest.DB_NAME;
			checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);

		} catch (SQLiteException e) {
			// database does't exist yet.
		}
		if (checkDB != null) {
			checkDB.close();
		}
		return checkDB != null ? true : false;
	}

	/**
	 * Copies your database from your local assets-folder to the just created
	 * empty database in the system folder, from where it can be accessed and
	 * handled. This is done by transfering bytestream.
	 * */
	private void copyDataBase() throws IOException {

		// Open your local db as the input stream
		InputStream myInput = myContext.getAssets().open(DBManifest.DB_NAME);

		// Path to the just created empty db
		String outFileName = DBManifest.DB_PATH + DBManifest.DB_NAME;

		// Open the empty db as the output stream
		OutputStream myOutput = new FileOutputStream(outFileName);

		// transfer bytes from the inputfile to the outputfile
		byte[] buffer = new byte[1024];
		int length;
		while ((length = myInput.read(buffer)) > 0) {
			myOutput.write(buffer, 0, length);
		}

		// Close the streams
		myOutput.flush();
		myOutput.close();
		myInput.close();

	}

	/**
	 * @throws SQLException
	 */
	public void openDataBase() throws SQLException {
		// Open the database
		String myPath = DBManifest.DB_PATH + DBManifest.DB_NAME;
		myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);

	}

	@Override
	public synchronized void close() {
		if (myDataBase != null)
			myDataBase.close();

		super.close();

	}

	public SQLiteDatabase getDb() {
		return myDataBase;
	}

	// /**
	// * Compiled Insert Statement
	// *
	// * @param name
	// * @return
	// */
	// public long insert(String name) {
	// this.insertStmt.bindString(1, name);
	// return this.insertStmt.executeInsert();
	// }
	//

	@Override
	public void onCreate(SQLiteDatabase db) {
		for (int i = 0; i < DBManifest.TABLE_NAMES.length; i++) {
			String sql = DBManifest.CREATE_QUERIES[i];
			db.execSQL(sql);
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		if (oldVersion >= newVersion)
			return;

		// String sql = null;
		// if (oldVersion == 1)
		// sql = "alter table " + TABLE_NAME + " add COLUMN_2 text;";
		// if (oldVersion == 2)
		// sql = "";
		//
		// Log.d("EventsData", "onUpgrade	: " + sql);
		// if (sql != null)
		// db.execSQL(sql);
	}

	// Add your public helper methods to access and get content from the
	// database.
	// You could return cursors by doing "return myDataBase.query(....)" so it'd
	// be easy
	// to you to create adapters for your views.

}