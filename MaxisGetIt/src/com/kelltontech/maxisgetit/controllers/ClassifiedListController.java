package com.kelltontech.maxisgetit.controllers;

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
import com.kelltontech.maxisgetit.constants.FlurryEventsConstants;
import com.kelltontech.maxisgetit.parsers.ClassifiedListParser;
import com.kelltontech.maxisgetit.requests.GenralRequest;
import com.kelltontech.maxisgetit.response.ClassifiedListResponse;
import com.kelltontech.maxisgetit.utils.AnalyticsHelper;

public class ClassifiedListController extends BaseServiceController {
	private Context mActivity;

	public ClassifiedListController(IActionController screen, int eventType) {
		super(screen, eventType);
		mActivity = (Context) screen;
	}

	@Override
	public void initService() {
		// TODO Auto-generated method stub
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
			
			serviceRq.setHttpMethod(HttpClientConnection.HTTP_METHOD.GET);
			
			String url = AppConstants.BASE_URL + GenralRequest.CLASSIFIED_LIST_METHOD;
			Log.d("maxis", "url " + url);

			String userId = (String) requestData;
			GenralRequest baseReq = new GenralRequest(mActivity);
			serviceRq.setUrl(HttpHelper.getURLWithPrams(url, baseReq.getHeadersWithUID(userId,AppConstants.MyClassifieds)));
			
			HttpClientConnection.getInstance().addRequest(serviceRq);
		} catch (Exception e) {
			logRequestException(e, "ClassifiedListController");
			
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
			message.obj = response.getErrorText() ;
		} else {
			try {
				response.setPayload(new ClassifiedListParser().parse(response.getResponseText()));
			} catch (Exception e) {
				logResponseException(e, "ClassifiedListController");
			}
			
			if (response.getPayload() instanceof ClassifiedListResponse) {
				ClassifiedListResponse clResp = (ClassifiedListResponse) response.getPayload();
				if (clResp.getClassifiedList().size() < 1) {
					message.obj = mActivity.getResources().getString(R.string.no_result_found);
				} else {
					message.arg1 = 0;
					message.obj = clResp;
				}
			} else {
				message.obj = mActivity.getResources().getString(R.string.internal_error);
			}
		}
		mScreen.setScreenData(message, mEventType, 0);
	}

}
