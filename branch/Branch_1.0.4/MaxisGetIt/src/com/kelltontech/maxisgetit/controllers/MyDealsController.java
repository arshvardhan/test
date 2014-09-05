package com.kelltontech.maxisgetit.controllers;

import java.util.Hashtable;

import android.content.Context;
import android.os.Message;
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
import com.kelltontech.maxisgetit.constants.Events;
import com.kelltontech.maxisgetit.parsers.DealCategoriesParser;
import com.kelltontech.maxisgetit.parsers.DealsListParser;
import com.kelltontech.maxisgetit.requests.GenralRequest;
import com.kelltontech.maxisgetit.response.DealCategoriesResponse;
import com.kelltontech.maxisgetit.response.DealsListResponse;

public class MyDealsController extends BaseServiceController {
	private Context mActivity;
	

	public MyDealsController(IActionController screen, int eventType) {
		super(screen, eventType);
		mActivity = (Context) screen;
	}

	@Override
	public void initService() {
		// Auto-generated method stub
	}

	public void requestService(Object requestData) {
		String userId = (String) requestData;
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
			serviceRq.setHttpMethod(HttpClientConnection.HTTP_METHOD.GET);

			String url = AppConstants.BASE_URL;
			if (mEventType == Events.MY_DEALS_LIST){
				url += GenralRequest.MY_DEAL_LIST_Method;
			} else{
				url += GenralRequest.MY_DEAL_CATEGORIES_Method;
			}
			
			Log.d("maxis", "url " + url);

			GenralRequest genralRequest = new GenralRequest(mActivity);
			Hashtable<String, String> urlParams = genralRequest.getHeadersWithUID(userId,AppConstants.MyDeals);
			serviceRq.setUrl(HttpHelper.getURLWithPrams(url, urlParams));

			HttpClientConnection.getInstance().addRequest(serviceRq);
		} catch (Exception e) {
			logRequestException(e, "MyDealsController");
			
			Response res = getErrorResponse(mActivity.getResources().getString(R.string.internal_error), 111);
			responseService(res);
		}
	}


	@Override
	public void responseService(Object object) {
		Response response = (Response) object;
		Message message = new Message();
		message.arg2 = mEventType;
		message.arg1 = 1;
		if (response.isError()) {
			message.obj = response.getErrorText();
		} else {
			
			try {
				if (mEventType == Events.MY_DEALS_LIST) {
					response.setPayload(new DealsListParser().parse(response.getResponseText()));
				} else {
					response.setPayload(new DealCategoriesParser().parse(response.getResponseText()));
				}
			} catch (Exception e) {
				logResponseException(e, "MyDealsController");
			}
			
			if (response.getPayload() instanceof DealCategoriesResponse) {
				DealCategoriesResponse myDealsRes = (DealCategoriesResponse) response.getPayload();
				message.arg1 = 0;
				message.obj = myDealsRes;
			} else if (response.getPayload() instanceof DealsListResponse) {
				DealsListResponse myDealsRes = (DealsListResponse) response.getPayload();
				message.arg1 = 0;
				message.obj = myDealsRes;
			} else {
				message.obj = mActivity.getResources().getString(R.string.internal_error);
			}
		}
		mScreen.setScreenData(message, mEventType, 0);
	}

}
