package com.kelltontech.maxisgetit.controllers;


import android.content.Context;
import android.util.Log;

import com.kelltontech.maxisgetit.R;
import com.kelltontech.framework.controller.BaseServiceController;
import com.kelltontech.framework.model.Response;
import com.kelltontech.framework.network.HttpClientConnection;
import com.kelltontech.framework.network.HttpHelper;
import com.kelltontech.framework.network.ServiceRequest;
import com.kelltontech.framework.ui.IActionController;
import com.kelltontech.framework.utils.NativeHelper;
import com.kelltontech.maxisgetit.constants.AppConstants;
import com.kelltontech.maxisgetit.parsers.ReportErrorMandatoryFieldParser;
import com.kelltontech.maxisgetit.requests.GenralRequest;

public class ReportErrorMandatoryFieldsController extends BaseServiceController {
	
	private Context mActivity;
	
	public ReportErrorMandatoryFieldsController(IActionController screen, int eventType) {
		super(screen, eventType);
		mActivity = (Context) screen;
	}

	@Override
	public void initService() {
		// nothing to do here
	}

	@Override
	public void requestService(Object requestData) {
		try {
			if (!NativeHelper.isDataConnectionAvailable(mActivity)) {
				Response res = new Response();
				res.setErrorCode(101);
				res.setErrorText(mActivity.getResources().getString(R.string.network_unavailable));
				responseService(res);
				return;
			}

			ServiceRequest serviceRq = new ServiceRequest();
			serviceRq.setServiceController(this);
			serviceRq.setRequestData(requestData);
			serviceRq.setDataType(mEventType);
			serviceRq.setPriority(HttpClientConnection.PRIORITY.LOW);
			serviceRq.setHttpHeaders( API_HEADER_NAMES_ARRAY_2, getApiHeaderValuesArray2());
			
			GenralRequest genralRequest = new GenralRequest(mActivity);
			String url = AppConstants.BASE_URL;
			url = url + GenralRequest.ERROR_MENDATORY_FIELDS;
			Log.d("maxis", "url " + url);
			
			serviceRq.setUrl(HttpHelper.getURLWithPrams(url,genralRequest.getDefaultHeaders(AppConstants.ReportanError)));
			serviceRq.setHttpMethod(HttpClientConnection.HTTP_METHOD.GET);
			
			HttpClientConnection.getInstance().addRequest(serviceRq);
		} catch (Exception e) {
			logRequestException(e, "ReportErrorMandatoryFieldsContrtoller");
			
			Response res = getErrorResponse(mActivity.getResources().getString(R.string.internal_error),111);
			responseService(res);
		}
	}

	@Override
	public void responseService(Object object) {
		Response response = (Response) object;
		
		if( !response.isError() ){
			try {
				response.setPayload(new ReportErrorMandatoryFieldParser().parse(response.getResponseText()));
			} catch (Exception e) {
				logResponseException(e, "ReportErrorMandatoryFieldsContrtoller");
				handleException(e);
				return;
			}
		}
		mScreen.setScreenData(response, mEventType, 0);
	}
}
