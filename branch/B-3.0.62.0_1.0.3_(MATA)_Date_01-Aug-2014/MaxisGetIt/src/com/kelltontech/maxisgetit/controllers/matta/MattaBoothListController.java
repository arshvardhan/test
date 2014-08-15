package com.kelltontech.maxisgetit.controllers.matta;

import java.util.Hashtable;

import android.app.Activity;
import android.util.Log;

import com.kelltontech.framework.controller.BaseServiceController;
import com.kelltontech.framework.model.Response;
import com.kelltontech.framework.network.HttpClientConnection;
import com.kelltontech.framework.network.HttpHelper;
import com.kelltontech.framework.network.ServiceRequest;
import com.kelltontech.framework.ui.IActionController;
import com.kelltontech.framework.utils.NativeHelper;
import com.kelltontech.maxisgetit.BuildConfig;
import com.kelltontech.maxisgetit.R;
import com.kelltontech.maxisgetit.constants.AppConstants;
import com.kelltontech.maxisgetit.model.matta.booths.list.MattaBoothListResponse;
import com.kelltontech.maxisgetit.requests.matta.MattaBoothListRequest;
import com.kelltontech.maxisgetit.requests.matta.MattaRequest;
import com.kelltontech.maxisgetit.utils.GsonUtil;

/**
 * @author arsh.vardhan
 * @modified 02-Aug-2014
 */
public class MattaBoothListController extends BaseServiceController {
	private Activity mActivity;
	public MattaBoothListController(IActionController screen, int eventType) {
		super(screen, eventType);
		mActivity = (Activity) screen;
	}

	@Override
	public void initService() {
	}

	@Override
	public void requestService(Object requestData) {
		MattaBoothListRequest boothListReq = (MattaBoothListRequest) requestData;
		try {
			if (!NativeHelper.isDataConnectionAvailable(mActivity)) {
				Response res = new Response();
				res.setErrorCode(101);
				res.setErrorText(mActivity.getResources().getString(R.string.network_unavailable));
				responseService(res);
				return;
			}
			MattaRequest mattaRequest = new MattaRequest(mActivity);
			Hashtable<String, String> urlParams = mattaRequest.getBoothListingHeaders(boothListReq);

			ServiceRequest serviceRq = new ServiceRequest();
			serviceRq.setRequestData(requestData);
			serviceRq.setServiceController(this);
			serviceRq.setDataType(mEventType);
			serviceRq.setPriority(HttpClientConnection.PRIORITY.LOW);
			serviceRq.setHttpHeaders(API_HEADER_NAMES_ARRAY_2, getApiHeaderValuesArray2());
			String url = AppConstants.BASE_URL + MattaRequest.MATTA_BOOTH_LIST_METHOD;
			if(BuildConfig.DEBUG) 
				Log.d(AppConstants.FINDIT_DEBUG_TAG, "url " + url);
			serviceRq.setUrl(HttpHelper.getURLWithPrams(url, urlParams));
			serviceRq.setHttpMethod(HttpClientConnection.HTTP_METHOD.GET);
			HttpClientConnection.getInstance().addRequest(serviceRq);
		} catch (Exception e) {
			logRequestException(e, "MattaBoothListController");
			Response res = getErrorResponse(mActivity.getResources().getString(R.string.communication_failure), 111);
			responseService(res);
		}
	}

	@Override
	public void responseService(Object object) {
//		JSONParser jsonParser = JSONHandler.getInstanse();
		if (object instanceof Response) {
			try {
				String responseStr = ((Response) object).getResponseText();
//				MattaBoothListResponse boothListResponse = jsonParser.mapFromJSON(responseStr, MattaBoothListResponse.class);
				MattaBoothListResponse boothListResponse =  GsonUtil.mapFromJSONHanldeObject(responseStr, MattaBoothListResponse.class);
				mScreen.setScreenData(boothListResponse, mEventType, 0);
			} catch (Exception e) {
				logResponseException(e, "MattaBoothListController");
			}
		} 
	}
}