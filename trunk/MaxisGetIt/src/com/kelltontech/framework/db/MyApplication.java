package com.kelltontech.framework.db;

import android.app.Application;
import android.util.Log;
import com.kelltontech.maxisgetit.db.DataBaseHelper;

/**
 * 
 * This initilizes database helper and stores so that this can be used by other
 * activities when ever required
 * 
 */
public class MyApplication extends Application {

	public static final String LOG_TAG = "findit";

	private DataBaseHelper dataHelper;

	@Override
	public void onCreate() {
		super.onCreate();
		Log.d(LOG_TAG, "APPLICATION onCreate");
		// PushLink.setCurrentStrategy(StrategyEnum.ANNOYING_POPUP);
		// PushLink.start(this, R.drawable.app_icon, "a3leua8sjkq0k2m2",
		// NativeHelper.getDeviceId(getApplicationContext()));
		this.dataHelper = new DataBaseHelper(this);
	}

	@Override
	public void onTerminate() {
		Log.d(LOG_TAG, "APPLICATION onTerminate");
		super.onTerminate();
	}

	public DataBaseHelper getDataHelper() {
		return this.dataHelper;
	}

	public void setDataHelper(DataBaseHelper dataHelper) {
		this.dataHelper = dataHelper;
	}
}
