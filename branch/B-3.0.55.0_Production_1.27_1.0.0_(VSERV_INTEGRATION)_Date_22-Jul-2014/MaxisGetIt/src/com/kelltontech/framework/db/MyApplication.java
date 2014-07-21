package com.kelltontech.framework.db;

import android.app.Application;
import android.content.Context;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.kelltontech.framework.utils.stringutils.StringUtil;
import com.kelltontech.maxisgetit.constants.AppConstants;
import com.kelltontech.maxisgetit.db.DataBaseHelper;

/**
 * @author arshvardhan.atreya
 * @description The MyApplication class maintains the queue for session tracking requests, initializes the DatabaseHelper and the GoogleAnalytics classes and stores their instances so that they can be used by other
 * activities when ever required
 * @modifiedOn 23-05-2014
 */

public class MyApplication extends Application {

	public static final String TAG = MyApplication.class.getSimpleName();
	private DataBaseHelper dataHelper;
	private static MyApplication mInstance;
	private RequestQueue mRequestQueue;

	public static Context getAppContext() {
		return getInstance();
	}

	public static synchronized MyApplication getInstance() {
		return mInstance;
	}

	protected static void setInstance(MyApplication mInstance) {
		MyApplication.mInstance = mInstance;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		setInstance(this);
		Log.d(TAG, "APPLICATION onCreate");
		this.dataHelper = new DataBaseHelper(this);
	}

	@Override
	public void onTerminate() {
		Log.d(TAG, "APPLICATION onTerminate");
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

	public RequestQueue getRequestQueue() {
		if (mRequestQueue == null) {
			mRequestQueue = Volley.newRequestQueue(getApplicationContext());
		}

		return mRequestQueue;
	}

	public <T> void addToRequestQueue(Request<T> req, String tag) {
		// set the default tag if tag is empty
		req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
		getRequestQueue().add(req);
	}

	public <T> void addToRequestQueue(Request<T> req) {
		req.setTag(TAG);
		getRequestQueue().add(req);
	}

	public void cancelPendingRequests(Object tag) {
		if (mRequestQueue != null) {
			mRequestQueue.cancelAll(tag);
		}
	}

}
