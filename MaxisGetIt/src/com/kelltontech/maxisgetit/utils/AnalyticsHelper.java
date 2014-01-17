package com.kelltontech.maxisgetit.utils;

import java.util.HashMap;

import android.app.Activity;
import android.util.Log;

import com.flurry.android.FlurryAgent;
import com.kelltontech.maxisgetit.constants.AppConstants;

/**
 * 
 * @author arshvardhan.atreya
 * Created on: 11-11-2013
 * Modified on:18-11-2013 
 * Description: AnalyticsHelper class includes the methods to integrate FlurryAgent analytics tool in the FindIt project
 *
 */

public class AnalyticsHelper {
	

	public static void onActivityCreate() {
		FlurryAgent.onPageView();
	}
	
	/**
	 * @param activity
	 */
	public static void onActivityStart(Activity activity) {
		FlurryAgent.onStartSession(activity, AppConstants.FLURRY_KEY);
	}

	/**
	 * 
	 * @param activity
	 */
	public static void onActivityStop(Activity activity) {
		FlurryAgent.onEndSession(activity);
	}
	
	public static void setContinueSession(long seconds) {
		FlurryAgent.setContinueSessionMillis(seconds * 1000);
	}
	
	public static void setLogEnabled(boolean state) {
		FlurryAgent.setLogEnabled(state);
	}
	
	public static void logEvent(String event) {
		FlurryAgent.logEvent(event);
	}
	
	public static void logEvent(String event, boolean trackSession) {
		FlurryAgent.logEvent(event, trackSession);
	}
	
	public static void logEvent(String event, HashMap<String, String> map) {
		FlurryAgent.logEvent(event,map);
	}
	
	public static void logEvent(String event, HashMap<String, String> map, boolean trackSession) {
		FlurryAgent.logEvent(event,map,trackSession);
	}

	public static void endTimedEvent(String event) {
		FlurryAgent.endTimedEvent(event);
	}
	
	public static void endTimedEvent(String event, HashMap<String, String> map) {
		FlurryAgent.endTimedEvent(event,map);
	}
	
	public static void onError(String errorId, String message, Throwable exception) {
		FlurryAgent.onError(errorId, message, exception);
		exception.printStackTrace();
		Log.e(AppConstants.FINDIT_ERROR_TAG, message, exception);
	}

	//Deprecated method
	public static void onError(String errorId, String message, String errorClass) {
		FlurryAgent.onError(errorId, message, errorClass);
	}
	
	public static void setUserID(String userId) {
		FlurryAgent.setUserId(userId);
	}
	
	public static void getReleaseVersion() {
		int v = FlurryAgent.getAgentVersion();
		Log.i("FlurryAgent version", String.valueOf(v));
	}
}
