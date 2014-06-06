package com.kelltontech.framework.db;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

public class ExportDataUtil {

	public void exportDatabaseFile(Activity callerActivity, String dbName, String exportFileName) {
		if (isExternalStorageAvail()) {
			new ExportDatabaseFileTask(callerActivity).execute(dbName, exportFileName);
		} else {
			Toast.makeText(callerActivity, "External storage is not available, unable to export data.", Toast.LENGTH_SHORT).show();
		}
	}

	public void exportDataAsXml(Activity callerActivity, String dbName, String exportFileName) {
		if (ExportDataUtil.this.isExternalStorageAvail()) {
			new ExportDataAsXmlTask(callerActivity).execute(dbName, exportFileName);
		} else {
			Toast.makeText(callerActivity, "External storage is not available, unable to export data.", Toast.LENGTH_SHORT).show();
		}
	}

	private boolean isExternalStorageAvail() {
		return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
	}

	private class ExportDatabaseFileTask extends AsyncTask<String, Void, Boolean> {
		Activity _callerActivity;

		public ExportDatabaseFileTask(Activity callerActivity) {
			super();
			_callerActivity = callerActivity;
		}

		private final ProgressDialog dialog = new ProgressDialog(_callerActivity);

		// can use UI thread here
		protected void onPreExecute() {
			this.dialog.setMessage("Exporting database...");
			this.dialog.show();
		}

		// automatically done on worker thread (separate from UI thread)
		protected Boolean doInBackground(final String... args) {

			File dbFile = new File(Environment.getDataDirectory() + "/data/com.GetIt.db/databases/" + args[0] + ".db");// TODO
																														// NEED
																														// TO
																														// CHAGE

			File exportDir = new File(Environment.getExternalStorageDirectory(), args[1]);
			if (!exportDir.exists()) {
				exportDir.mkdirs();
			}
			File file = new File(exportDir, dbFile.getName());

			try {
				file.createNewFile();
				this.copyFile(dbFile, file);
				return true;
			} catch (IOException e) {
				Log.e(MyApplication.TAG, e.getMessage(), e);
				return false;
			}
		}

		// can use UI thread here
		protected void onPostExecute(final Boolean success) {
			if (this.dialog.isShowing()) {
				this.dialog.dismiss();
			}
			if (success) {
				Toast.makeText(_callerActivity, "Export successful!", Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(_callerActivity, "Export failed", Toast.LENGTH_SHORT).show();
			}
		}

		void copyFile(File src, File dst) throws IOException {
			FileChannel inChannel = new FileInputStream(src).getChannel();
			FileChannel outChannel = new FileOutputStream(dst).getChannel();
			try {
				inChannel.transferTo(0, inChannel.size(), outChannel);
			} finally {
				if (inChannel != null)
					inChannel.close();
				if (outChannel != null)
					outChannel.close();
			}
		}

	}

	private class ExportDataAsXmlTask extends AsyncTask<String, Void, String> {
		Activity _callerActivity;

		public ExportDataAsXmlTask(Activity callerActivity) {
			super();
			_callerActivity = callerActivity;
		}

		private final ProgressDialog dialog = new ProgressDialog(_callerActivity);

		// can use UI thread here
		protected void onPreExecute() {
			this.dialog.setMessage("Exporting database as XML...");
			this.dialog.show();
		}

		// automatically done on worker thread (separate from UI thread)
		protected String doInBackground(final String... args) {
			DataXmlExporter dm = new DataXmlExporter(((MyApplication) _callerActivity.getApplication()).getDataHelper().getDb());
			try {
				String dbName = args[0];
				String exportFileName = args[1];
				dm.export(dbName, exportFileName);
			} catch (IOException e) {
				Log.e(MyApplication.TAG, e.getMessage(), e);
				return e.getMessage();
			}
			return null;
		}

		// can use UI thread here
		protected void onPostExecute(final String errMsg) {
			if (this.dialog.isShowing()) {
				this.dialog.dismiss();
			}
			if (errMsg == null) {
				Toast.makeText(_callerActivity, "Export successful!", Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(_callerActivity, "Export failed - " + errMsg, Toast.LENGTH_SHORT).show();
			}
		}
	}
}