package com.kelltontech.maxisgetit.service;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * This class is intended for implementing SharedPreference in the app which can be
 * used for light weight data to be stored in the db.
 */
public class AppSharedPreference {

	private static final String PREF_NAME 		               			    = "findit";
	public  static final String MOBILE_NUMBER		                  		= "mobile_number";
	public  static final String USER_NAME		                  		    = "user_name";
	public  static final String LONGITUDE_N		                  		    = "longitude_n";
	public  static final String LATITUDE_N		                  		    = "latitude_n";
	public  static final String LONGITUDE		                  		    = "longitude";
	public  static final String LATITUDE		                  		    = "latitude";
	public  static final String LOCATION_TIME		                  		= "location_time";
	public  static final String LOCATION_PERMISSION_ENABLED		            = "location_permission_enabled";
	
	public  static final String API_SESSION_ID	= "api_session_id";
	public  static final String API_TOKEN	= "api_auth_token";
	

	public static int getInt(String key, int defValue,Context context)
	{
		return getSharedPreferences(context).getInt(key, defValue);
	}

	public static String getString(String key, String defValue,Context context)
	{
		return getSharedPreferences(context).getString(key, defValue);
	}


	public static void putInt(String key, int defaultValue,Context context)
	{
		Editor editor = getSharedPreferences(context).edit();
		editor.putInt(key, defaultValue);
		editor.commit();
	}

	public static void putString(String key, String defaultValue,Context context)
	{
		Editor editor = getSharedPreferences(context).edit();
		editor.putString(key, defaultValue);
		editor.commit();
	}

	private static SharedPreferences getSharedPreferences(Context context)
	{
		SharedPreferences pref =  context.getSharedPreferences(AppSharedPreference.PREF_NAME, 0);
		return pref;
	}
	public static boolean getBoolean(String key, boolean defValue,Context context)
	{
		return getSharedPreferences(context).getBoolean(key, defValue);
	}

	public static float getFloat(String key, float defValue,Context context)
	{
		return getSharedPreferences(context).getFloat(key, defValue);
	}
	public static long getLong(String key, long defValue,Context context)
	{
		return getSharedPreferences(context).getLong(key, defValue);
	}
	public static void putBoolean(String key, boolean defaultValue,Context context)
	{
		Editor editor = getSharedPreferences(context).edit();
		editor.putBoolean(key, defaultValue);
		editor.commit();
	}

	public static void putFloat(String key, float defaultValue,Context context)
	{
		Editor editor = getSharedPreferences(context).edit();
		editor.putFloat(key, defaultValue);
		editor.commit();
	}
	public static void putLong(String key, long defaultValue,Context context)
	{
		Editor editor = getSharedPreferences(context).edit();
		editor.putLong(key, defaultValue);
		editor.commit();
	}

	
}
