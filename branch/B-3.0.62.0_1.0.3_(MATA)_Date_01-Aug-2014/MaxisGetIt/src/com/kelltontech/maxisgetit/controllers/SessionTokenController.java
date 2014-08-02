package com.kelltontech.maxisgetit.controllers;

import org.apache.http.Header;

import android.app.Activity;
import android.util.Log;

import com.kelltontech.maxisgetit.R;
import com.kelltontech.framework.controller.BaseServiceController;
import com.kelltontech.framework.db.MyApplication;
import com.kelltontech.framework.model.Response;
import com.kelltontech.framework.network.HttpClientConnection;
import com.kelltontech.framework.network.ServiceRequest;
import com.kelltontech.framework.ui.IActionController;
import com.kelltontech.framework.utils.NativeHelper;
import com.kelltontech.framework.utils.StringUtil;
import com.kelltontech.maxisgetit.constants.AppConstants;
import com.kelltontech.maxisgetit.requests.GenralRequest;
import com.kelltontech.maxisgetit.requests.MaxisBaseRequest;
import com.kelltontech.maxisgetit.service.AppSharedPreference;

public class SessionTokenController extends BaseServiceController {
	
	private Activity mActivity;
	
	/**
	 * Header Names to receive values from SESSION_TOKEN_METHOD
	 */
	protected static final String API_HEADER_NAME_SESSION_ID 	= "session_id";
	protected static final String API_HEADER_NAME_TOKEN 		= "token";
	protected static final String API_HEADER_NAME_FORCE_UPDATE 	= "ForceAppUpdate";
	
	public SessionTokenController(Activity activity, IActionController screen, int eventType) {
		super(screen, eventType);
		mActivity = activity;
	}

	@Override
	public void initService() {
		// nothing to do here
	}

	@Override
	public void requestService(Object requestData) {
		try{
			if (!NativeHelper.isDataConnectionAvailable(mActivity)) {
				Response res = new Response();
				res.setErrorCode(101);
				res.setErrorText(mActivity.getResources().getString(R.string.network_unavailable));
				responseService(res);
				return;
			}
			
			ServiceRequest serviceRq = new ServiceRequest();
			serviceRq.setRequestData(requestData);
			serviceRq.setServiceController(this);
			serviceRq.setDataType(mEventType);
			serviceRq.setPriority(HttpClientConnection.PRIORITY.LOW);

			//String url = "http://192.168.12.224/trunk/restapi/testToken.xml?plateform=IOS&language_code=en";
			String url = AppConstants.BASE_URL + GenralRequest.SESSION_TOKEN_METHOD + "?" + AppConstants.URL_ENCODED_PARAMS+"&"+MaxisBaseRequest.DEVICE_ID+"="+MyApplication.getDeviceId();
			Log.d("maxis", "url " + url);
			serviceRq.setUrl(url);
			
			String sessionId = AppSharedPreference.getString(AppSharedPreference.API_SESSION_ID, null, mActivity);
			if( ! StringUtil.isNullOrEmpty(sessionId) ) {
				serviceRq.setHttpHeaders( API_HEADER_NAMES_ARRAY_1, new String[]{sessionId, AppConstants.API_VERSION});
			}
			serviceRq.setHttpMethod(HttpClientConnection.HTTP_METHOD.GET);
			
			HttpClientConnection.getInstance().addRequest(serviceRq);
		} catch (Exception e) {
			logRequestException(e, "SessionTokenController");
			
			Response res = getErrorResponse(mActivity.getResources().getString(R.string.internal_error), 111);
			responseService(res);
		}
	}

	@Override
	public void responseService(Object responseObject) {
		String sessionId = null;
		String authToken = null;
		String forceUpdate = null;
		
		if( responseObject instanceof Header[]) {
			Header[] httpHeadersArr = (Header[]) responseObject;
			for( Header header : httpHeadersArr ) {
				if( API_HEADER_NAME_SESSION_ID.equals(header.getName()) ) {
					sessionId = header.getValue();
				}
				if( API_HEADER_NAME_TOKEN.equals(header.getName()) ) {
					authToken = header.getValue();
				}
				if (API_HEADER_NAME_FORCE_UPDATE.equals(header.getName())) {
					forceUpdate = header.getValue();
				}
			}
		}
		if( StringUtil.isNullOrEmpty(sessionId) || StringUtil.isNullOrEmpty(authToken) ) {
			mScreen.setScreenData(false, mEventType, 0);
		} else {
			AppSharedPreference.putString(AppSharedPreference.API_SESSION_ID, sessionId, mActivity);
			AppSharedPreference.putString(AppSharedPreference.API_TOKEN, authToken, mActivity);
			mScreen.setScreenData(true, mEventType, 0);
			Log.d("Session Token", "sessionId : " + sessionId + "authToken : " + authToken + "forceUpdate : " + forceUpdate);
		}
	}
}
