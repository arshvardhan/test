package com.kelltontech.framework.db;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.kelltontech.framework.utils.stringutils.StringUtil;
import com.kelltontech.maxisgetit.constants.AppConstants;
import com.kelltontech.maxisgetit.db.DataBaseHelper;

/**
 * @author arshvardhan.atreya
 * @description The MyApplication class initializes the DatabaseHelper and the GoogleAnalytics classes and stores their instances so that they can be used by other
 * activities when ever required
 * @modifiedOn 19-02-2014
 */

public class MyApplication extends Application {

	public static final String LOG_TAG = "findit";
	private DataBaseHelper dataHelper;
	private static MyApplication mInstance;
	/*
	 * Google Analytics configuration values.
	 */
//	private static GoogleAnalytics mGa;
//	private static Tracker mTracker;

	// Placeholder property ID.
	// final String GA_TRACKING_ID = "UA-48160571-1";
	private static final String GA_PROPERTY_ID = "UA-48160571-1";

	// Dispatch period in seconds.
	private static final int GA_DISPATCH_PERIOD = 30;

	// Prevent hits from being sent to reports, i.e. during testing.
	//private static final boolean GA_IS_DRY_RUN = false;

	// GA Logger.
//	private static final LogLevel GA_LOG_VERBOSITY = LogLevel.ERROR;

	// Key used to store a user's tracking preferences in SharedPreferences.
	private static final String TRACKING_PREF_KEY = "trackingPreference";

	/*
	 * Method to handle basic Google Analytics initialization. This call will
	 * not block as all Google Analytics work occurs off the main thread.
	 */
	@SuppressWarnings("deprecation")
	private void initializeGa() {
//		mGa = GoogleAnalytics.getInstance(this);
//		mTracker = mGa.getTracker(GA_PROPERTY_ID);

		// Set dispatch period.
//		GAServiceManager.getInstance().setLocalDispatchPeriod(
//				GA_DISPATCH_PERIOD);

		// Set dryRun flag.
		// mGa.setDryRun(GA_IS_DRY_RUN);

		// Set Logger verbosity.
//		mGa.getLogger().setLogLevel(GA_LOG_VERBOSITY);

		// Set the opt out flag when user updates a tracking preference.
//		SharedPreferences userPrefs = PreferenceManager
//				.getDefaultSharedPreferences(this);
//		userPrefs
//				.registerOnSharedPreferenceChangeListener(new SharedPreferences.OnSharedPreferenceChangeListener() {
//					@Override
//					public void onSharedPreferenceChanged(
//							SharedPreferences sharedPreferences, String key) {
//						if (key.equals(TRACKING_PREF_KEY)) {
//							GoogleAnalytics
//									.getInstance(getApplicationContext())
//									.setAppOptOut(
//											sharedPreferences.getBoolean(key,
//													false));
//						}
//					}
//				});
	}

	/*
	 * Returns the Google Analytics tracker.
	 */
//	public static Tracker getGaTracker() {
//		return mTracker;
//	}

	/*
	 * Returns the Google Analytics instance.
	 */
//	public static GoogleAnalytics getGaInstance() {
//		return mGa;
//	}

	public static Context getAppContext()
	{
		return getInstance();
	}
	
	public static MyApplication getInstance() {
		return mInstance;
	}

	protected static void setInstance(MyApplication mInstance) {
		MyApplication.mInstance = mInstance;
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		setInstance(this);
		initializeGa(); // For Google Analytics initialization.
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
	
	public static String getDeviceId()
	{
		TelephonyManager telephonyManager = (TelephonyManager)getAppContext().getSystemService(Context.TELEPHONY_SERVICE);
		String deviceId = telephonyManager.getDeviceId();
		if(StringUtil.isEmpty(deviceId))
		{

			return getAndroidId();		
		}
		else
		{
			return deviceId;
		}

	}
	public static String getAndroidId()
	{
		String androidId = Settings.Secure.getString(getAppContext().getContentResolver(),Settings.Secure.ANDROID_ID);
		if(!AppConstants.PRODUCTION)
			Log.d("android id", androidId);
		return androidId;
	}
			
}
