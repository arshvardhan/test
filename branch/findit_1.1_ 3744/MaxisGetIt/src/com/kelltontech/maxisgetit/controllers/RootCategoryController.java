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
import com.kelltontech.maxisgetit.parsers.RootCategoryParser;
import com.kelltontech.maxisgetit.requests.GenralRequest;

public class RootCategoryController extends BaseServiceController {
	private Context mActivity;

	public RootCategoryController(IActionController screen, int eventType) {
		super(screen, eventType);
		mActivity = (Context) screen;
	}

	@Override
	public void initService() {
		// Auto-generated method stub
	}

	@Override
	public void requestService(Object requestData) {
		try {
			if (!NativeHelper.isDataConnectionAvailable(mActivity)) {
				Response res = getErrorResponse(mActivity.getResources().getString(R.string.network_unavailable), 111);
				responseService(res);
				return;
			}

			ServiceRequest serviceRq = new ServiceRequest();
			serviceRq.setRequestData(requestData);
			serviceRq.setServiceController(this);
			serviceRq.setDataType(mEventType);
			serviceRq.setPriority(HttpClientConnection.PRIORITY.LOW);
			serviceRq.setHttpHeaders( API_HEADER_NAMES_ARRAY_2, getApiHeaderValuesArray2());
			
			String url = AppConstants.BASE_URL + GenralRequest.Root_Category_Method;
			Log.d("maxis", "url " + url);

			GenralRequest baseRequest = new GenralRequest(mActivity);
			serviceRq.setUrl(HttpHelper.getURLWithPrams(url, baseRequest.getDefaultHeadersWithGPS()));
			serviceRq.setHttpMethod(HttpClientConnection.HTTP_METHOD.GET);
			HttpClientConnection.getInstance().addRequest(serviceRq);
		} catch (Exception e) {
			logRequestException(e, "RootCategoryController");
			
			Response res = getErrorResponse(mActivity.getResources().getString(R.string.internal_error), 111);
			responseService(res);
		}
	}

	@Override
	public void responseService(Object object) {
		Response response = (Response) object;
		if( !response.isError() ){
			try {
				response.setPayload(new RootCategoryParser().parse(response.getResponseText()));
			} catch (Exception e) {
				logResponseException(e, "RootCategoryController");
				handleException(e);
				return;
			}
		}
		mScreen.setScreenData(response, mEventType, 0);
	}
}
