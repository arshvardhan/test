package com.kelltontech.maxisgetit.controllers;

import org.apache.http.entity.ByteArrayEntity;

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
import com.kelltontech.maxisgetit.constants.Events;
import com.kelltontech.maxisgetit.parsers.CompanyListParser;
import com.kelltontech.maxisgetit.requests.CombinedListRequest;

public class CombindListingController extends BaseServiceController {
	private Activity mActivity;
	private int event;

	public CombindListingController(IActionController screen, int eventType) {
		super(screen, eventType);
		mActivity = (Activity) screen;
		this.event = eventType;
	}

	@Override
	public void initService() {
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
			serviceRq.setRequestData(requestData);
			serviceRq.setServiceController(this);
			serviceRq.setDataType(mEventType);
			serviceRq.setPriority(HttpClientConnection.PRIORITY.LOW);
			serviceRq.setHttpHeaders( API_HEADER_NAMES_ARRAY_2, getApiHeaderValuesArray2());

			CombinedListRequest combinedListRequest = (CombinedListRequest) requestData;
			String url = AppConstants.BASE_URL +  combinedListRequest.getRequestMethod();

			if (combinedListRequest.getPostJsonPayload() == null) {
				serviceRq.setHttpMethod(HttpClientConnection.HTTP_METHOD.GET);
				if (event == Events.COMBIND_LISTING_VIEW_MORE_COMPANY_EVENT) {
					serviceRq.setUrl(HttpHelper.getURLWithPrams(url, combinedListRequest.getViewMoreCompanyReqHeaders()));
				} else {
					serviceRq.setUrl(HttpHelper.getURLWithPrams(url, combinedListRequest.getRequestHeaders()));
				}
			} else {
				serviceRq.setHttpMethod(HttpClientConnection.HTTP_METHOD.POST);
				url += combinedListRequest.getUrlAppendableRequestHeaders();
				serviceRq.setPostData(new ByteArrayEntity(combinedListRequest.getPostJsonPayload().toString().getBytes()));
				Log.d("maxis", "post payload " + combinedListRequest.getPostJsonPayload().toString());
				serviceRq.setUrl(url);
			}
			Log.d("maxis", "url " + url);
			HttpClientConnection.getInstance().addRequest(serviceRq);
		} catch (Exception e) {
			logRequestException(e, "CombindListingController");

			Response res = getErrorResponse(mActivity.getResources().getString(R.string.communication_failure), 111);
			responseService(res);
		}
	}

	@Override
	public void responseService(Object object) {
		Response response = (Response) object;

		if( ! response.isError() ) {
			try {
				response.setPayload(new CompanyListParser().parse(response.getResponseText()));
			} catch (Exception e) {
				handleException(e);
				logResponseException(e, "CombindListingController");
				return;
			}
		}
		mScreen.setScreenData(response, mEventType, 0);
	}
}