package com.kelltontech.maxisgetit.utils;

import java.util.HashMap;

import android.app.Activity;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.flurry.android.FlurryAgent;
import com.kelltontech.framework.db.MyApplication;
import com.kelltontech.framework.model.Response;
import com.kelltontech.framework.network.HttpHelper;
import com.kelltontech.framework.utils.NativeHelper;
import com.kelltontech.maxisgetit.R;
import com.kelltontech.maxisgetit.constants.AppConstants;
import com.kelltontech.maxisgetit.constants.FlurryEventsConstants;
import com.kelltontech.maxisgetit.requests.SessionTrackingRequest;
import com.kelltontech.maxisgetit.requests.SimpleXmlRequest;

/**
 * 
 * @author arshvardhan.atreya
 * Created on: 11-11-2013
 * Modified on:18-11-2013 
 * Description: AnalyticsHelper class includes the methods to integrate FlurryAgent analytics tool in the FindIt project
 *
 */

public class AnalyticsHelper {
	
	public static String SESSION_IDENTIFIER = "";
	

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
	
	public static void trackSession(Activity activity, String screenName) {
		try {
			if (!NativeHelper.isDataConnectionAvailable(activity)) {
				Response res = new Response();
				res.setErrorCode(101);
				res.setErrorText(activity.getResources().getString(R.string.network_unavailable));
				return;
			}
			SessionTrackingRequest request = new SessionTrackingRequest(activity, SESSION_IDENTIFIER, screenName);
			String url = HttpHelper.getURLWithPrams(AppConstants.BASE_URL + request.getMethodName(), request.getRequestHeaders(screenName));
			Log.d("maxis", "url " + url);
			SimpleXmlRequest<Response> serviceRequest = new SimpleXmlRequest<Response>(Request.Method.GET, url, Response.class, new Listener<Response>() {
				@Override
				public void onResponse(Response response) {
						Log.d("FINDIT MALAYSIA", "SESSION TRACKING MSG: Success");
				}
					}, 
					new ErrorListener() {
						@Override
						public void onErrorResponse(VolleyError error) {                                
						}
					}
					);  
	
			MyApplication.getInstance().addToRequestQueue(serviceRequest);
		} catch (Exception e) {
			AnalyticsHelper.onError(FlurryEventsConstants.REQUEST_SERVICE_ERR, MyApplication.class.getSimpleName() + " : " + AppConstants.REQUEST_SERVICE_ERROR_MSG, e);
		}
	}
}
