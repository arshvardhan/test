package com.kelltontech.maxisgetit.controllers;

import android.app.Activity;
import android.util.Log;

import com.kelltontech.framework.controller.BaseServiceController;
import com.kelltontech.framework.model.Response;
import com.kelltontech.framework.network.HttpClientConnection;
import com.kelltontech.framework.network.HttpHelper;
import com.kelltontech.framework.network.ServiceRequest;
import com.kelltontech.framework.ui.IActionController;
import com.kelltontech.framework.utils.NativeHelper;
import com.kelltontech.maxisgetit.R;
import com.kelltontech.maxisgetit.constants.AppConstants;
import com.kelltontech.maxisgetit.parsers.CompanyDetailParser;
import com.kelltontech.maxisgetit.requests.DetailRequest;

public class CompanyDetailController extends BaseServiceController {
	private Activity mActivity;
	private boolean isDeal;
	private String screenName;

	public CompanyDetailController(IActionController screen, int eventType) {
		super(screen, eventType);
		mActivity = (Activity) screen;
	}

	@Override
	public void initService() { }

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
			serviceRq.setRequestData(requestData);
			serviceRq.setServiceController(this);
			serviceRq.setDataType(mEventType);
			serviceRq.setPriority(HttpClientConnection.PRIORITY.LOW);
			serviceRq.setHttpHeaders(API_HEADER_NAMES_ARRAY_2, getApiHeaderValuesArray2());
			serviceRq.setHttpMethod(HttpClientConnection.HTTP_METHOD.GET);
			DetailRequest detailRequest = (DetailRequest) requestData;
			isDeal = detailRequest.isDeal;
			String url = AppConstants.BASE_URL + detailRequest.getMethodName();
			Log.d("maxis", "url " + url);
			if(isDeal) {
				screenName = AppConstants.Deal_Detail;
			}
			else {
				screenName = AppConstants.Company_detail;
			}
			serviceRq.setUrl(HttpHelper.getURLWithPrams(url,detailRequest.getRequestHeaders(screenName)));
			HttpClientConnection.getInstance().addRequest(serviceRq);
		} catch (Exception e) {
			if(isDeal) 
				logRequestException(e, "DealDetailController");
			else 
				logRequestException(e, "CompanyDetailController");
			Response res = getErrorResponse(mActivity.getResources().getString(R.string.internal_error),111);
			responseService(res);
		}

	}

	@Override
	public void responseService(Object object) {

		Response response = (Response) object;
		if (!response.isError()) {
			try {
				if (isDeal) {
					response.setPayload(new CompanyDetailParser().parse(response.getResponseText()));
				} else {
					response.setPayload(new CompanyDetailParser().parse(response.getResponseText()));
				}
			} catch (Exception e) {
				handleException(e);
				if(isDeal) 
					logResponseException(e, "DealDetailController");
				else 
					logResponseException(e, "CompanyDetailController");
				return;
			}
		}
		mScreen.setScreenData(response, mEventType, 0);
	}

}
