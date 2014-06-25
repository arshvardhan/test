package com.kelltontech.maxisgetit.requests;

import java.util.Hashtable;

import com.kelltontech.maxisgetit.constants.AppConstants;

import android.content.Context;

public class SessionTrackingRequest extends MaxisBaseRequest {

	public static final String METHOD_LOG_RECORD = "logRecrod.xml";
	private static final String KEY_PAGE_VIEW = "pageViewScreen";
	private static final String KEY_SESSION_ID = "sessionId";
	private String sessionId;
	private String pageView;

	public SessionTrackingRequest(Context context, String sessionId, String pageView) {
		super(context);
		this.sessionId = sessionId;
		this.pageView = pageView;
	}

	@Override
	public Hashtable<String, String> getRequestHeaders(String screenName) {
		Hashtable<String, String> ht = getDefaultHeadersWithGPS();
		ht.put(KEY_SESSION_ID, sessionId);
		ht.put(KEY_PAGE_VIEW, pageView);
		return ht;
	}

	public String getMethodName() {
		return METHOD_LOG_RECORD;
	}
}