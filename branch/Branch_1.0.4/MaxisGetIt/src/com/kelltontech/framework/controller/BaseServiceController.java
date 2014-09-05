/**
 * BaseServiceController.java
 * © DbyDx Software ltd. @2011 - 2011+1
 * Confidential and proprietary.
 */
package com.kelltontech.framework.controller;

import android.app.Activity;
import android.util.Base64;

import com.kelltontech.framework.model.Response;
import com.kelltontech.framework.network.HttpClientConnection;
import com.kelltontech.framework.network.ServiceResponse;
import com.kelltontech.framework.ui.IActionController;
import com.kelltontech.framework.utils.StringUtil;
import com.kelltontech.maxisgetit.R;
import com.kelltontech.maxisgetit.constants.AppConstants;
import com.kelltontech.maxisgetit.constants.Events;
import com.kelltontech.maxisgetit.constants.FlurryEventsConstants;
import com.kelltontech.maxisgetit.controllers.SessionTokenController;
import com.kelltontech.maxisgetit.service.AppSharedPreference;
import com.kelltontech.maxisgetit.ui.activities.MaxisMainActivity;
import com.kelltontech.maxisgetit.utils.AnalyticsHelper;
import com.kelltontech.maxisgetit.utils.Utility;

/**
 *
 */
public abstract class BaseServiceController implements IServiceController {

	protected static final String API_HEADER_NAME_SESSID 		= "SESSID";
	protected static final String API_HEADER_NAME_TOKKEN 		= "TOKKEN";
	protected static final String API_HEADER_NAME_VERSION 		= "VERSION";

	protected static final String[] API_HEADER_NAMES_ARRAY_1 = { API_HEADER_NAME_SESSID, API_HEADER_NAME_VERSION };
	protected static final String[] API_HEADER_NAMES_ARRAY_2 = { API_HEADER_NAME_SESSID, API_HEADER_NAME_TOKKEN, API_HEADER_NAME_VERSION };
	
	protected static final String API_AUTH_TOKEN_KEY = "andi787";
	
	protected IActionController mScreen;
	protected int mEventType;

	/**
	 * Constructor.
	 * 
	 * @param screen
	 *            instance of a screen to response.
	 * @param eventType
	 *            Every screen has multiple events IllegalArgumentException if
	 *            Screen instance is null.
	 */
	public BaseServiceController(IActionController screen, int eventType) {
		super();
		this.mScreen = screen;
		this.mEventType = eventType;
	}

	/**
	 * Setting default request time-out time as 60 seconds for all requests.
	 */
	static {
		HttpClientConnection.getInstance().setDefaultRequestTimeOut(AppConstants.MILLIS_1_MINUTE);
	}
	
	/**
	 * @return controller
	 */
	public IActionController getController() {
		return mScreen;
	}

	/**
	 * @return requested event
	 */
	public int getEventType() {
		return mEventType;
	}
	
	protected Response getErrorResponse(String message,int code){
		Response res=new Response();
		res.setErrorCode(code);
		res.setErrorText(message);
		responseService(res);
		return res;
	}
	
	@Override
	public final void handleServiceResponse(ServiceResponse serviceResponse) {
		if( serviceResponse.getHttpResponseCode() == 403 ) {
			resendCurrentRequestWithFreshToken(serviceResponse);
		} else if( serviceResponse.getException() != null ) {
			handleException(serviceResponse.getException()); 
		} else {
			try {
				if( mEventType == Events.SESSION_TOKEN_EVENT ) {
					responseService(serviceResponse.getHttpHeaders());
					return;
				}
				if(isAppUpdated(serviceResponse.getHttpHeaders()) && mScreen instanceof MaxisMainActivity){
					((MaxisMainActivity)mScreen).runOnUiThread(new Runnable()
				    {           
				        @Override
				        public void run()
				        {
				        	((MaxisMainActivity)mScreen).showPlayStoreDialog(((MaxisMainActivity)mScreen).getString(R.string.app_update_message));   
				        }
				    });
					return;
				}
				Response response = new Response();
				StringBuffer buffer = new StringBuffer();
				if( serviceResponse.getResponseData() != null ) {
					buffer = new StringBuffer(new String(serviceResponse.getResponseData()));
				}
				response.setResponseText(buffer.toString());
				responseService(response);
			} catch (Exception e) {
				handleException(e);
			}
		}
	}
	
	/**
	 * @param exception
	 */
	protected void handleException(Exception exception) {
		Response res = new Response();
		res.setErrorCode(Response.EXCEPTION);
		res.setErrorText("Server not responding, Please try again later.");
		res.setException(exception);
		responseService(res);
	}
	
	/**
	 * @param log request exception
	 */
	protected void logRequestException(Throwable exception, String controller) {
	AnalyticsHelper.onError(FlurryEventsConstants.REQUEST_SERVICE_ERR, controller + " : " + AppConstants.REQUEST_SERVICE_ERROR_MSG, exception);
	}
	
	/**
	 * @param log response exception
	 */
	protected void logResponseException(Throwable exception, String controller) {
		AnalyticsHelper.onError(FlurryEventsConstants.RESPONSE_SERVICE_ERR, controller + " : " + AppConstants.RESPONSE_SERVICE_ERROR_MSG, exception);
	}
	
	/**
	 * @return array for API_HEADER_NAMES_ARRAY_2
	 */
	protected String[] getApiHeaderValuesArray2() {
		String sessionId = AppSharedPreference.getString(AppSharedPreference.API_SESSION_ID, "", (Activity)mScreen);
		String authToken = AppSharedPreference.getString(AppSharedPreference.API_TOKEN, "", (Activity)mScreen);
		
		try {
			if( ! StringUtil.isNullOrEmpty(authToken) ) {
				authToken = new String(Base64.decode(authToken, Base64.DEFAULT));
			}
			if( authToken.indexOf(API_AUTH_TOKEN_KEY) == 0 ) {
				authToken = authToken.substring(API_AUTH_TOKEN_KEY.length(), authToken.length());
			}
			if( ! StringUtil.isNullOrEmpty(authToken) ) {
				authToken = Utility.getMD5Hash(authToken);
			}
			
			
		} catch( Exception e) {
			// ignore
		}
		return new String[] { sessionId, authToken, AppConstants.API_VERSION };
	}
	
	/**
	 * @param response
	 */
	private void resendCurrentRequestWithFreshToken(ServiceResponse serviceResponse) {
		final Object pendingRequestData = serviceResponse.getRequestData();

		IActionController responseHandler = new IActionController() {
			@Override
			public void setScreenData(Object screenData, int event, long time) {
				boolean sessionTokenSuccess = false;
				if( screenData instanceof Boolean ) {
					sessionTokenSuccess = (Boolean)screenData;
				}
				if( sessionTokenSuccess ) {
					requestService(pendingRequestData);
				} else {
					responseService(getErrorResponse("Server not responding, Please try again later.", 999));
				}
			}
			@Override
			public Activity getMyActivityReference() {
				return null;
			}
		};

		SessionTokenController tokenController = new SessionTokenController((Activity)mScreen, responseHandler, Events.SESSION_TOKEN_EVENT);
		tokenController.requestService(null);
	}
	
	private boolean isAppUpdated(org.apache.http.Header[] headers) {
		boolean isAppUpdated = false;
		for(int i = 0; i < headers.length; i++){
			if(headers[i].getName().equalsIgnoreCase("ForceAppUpdate")){
				isAppUpdated = (!StringUtil.isNullOrEmpty(headers[i].getValue()) && "1".equals(headers[i].getValue())) ? true : false;
				//	isAppUpdated = Integer.parseInt(headers[i].getValue())== 1 ? true : false;
				break;
			}
		}
		return isAppUpdated;
	}
}
