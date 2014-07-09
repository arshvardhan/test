package com.kelltontech.maxisgetit.controllers;

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
import com.kelltontech.maxisgetit.R;
import com.kelltontech.maxisgetit.constants.AppConstants;
import com.kelltontech.maxisgetit.model.MyAccountDashboardResponse;
import com.kelltontech.maxisgetit.model.bannerModel.LogBannerReportResponse;
import com.kelltontech.maxisgetit.requests.FavCompanyListRequest;
import com.kelltontech.maxisgetit.requests.GenralRequest;
import com.kelltontech.maxisgetit.utils.JSONHandler;
import com.kelltontech.maxisgetit.utils.JSONParser;

public class BannerNavigationController extends BaseServiceController {
	private Activity mActivity;
	public BannerNavigationController(IActionController screen, int eventType) {
		super(screen, eventType);
		mActivity = (Activity) screen;
	}

	@Override
	public void initService() {
	}

	@Override
	public void requestService(Object requestData) {
		String bannerId = (String)requestData;
		try {
			if (!NativeHelper.isDataConnectionAvailable(mActivity)) {
				Response res = new Response();
				res.setErrorCode(101);
				res.setErrorText(mActivity.getResources().getString(R.string.network_unavailable));
				responseService(res);
				return;
			}

			GenralRequest genralRequest = new GenralRequest(mActivity);
			Hashtable<String, String> urlParams = genralRequest.getHeadersWithBannerId(bannerId, null);

			ServiceRequest serviceRq = new ServiceRequest();
			serviceRq.setRequestData(requestData);
			serviceRq.setServiceController(this);
			serviceRq.setDataType(mEventType);
			serviceRq.setPriority(HttpClientConnection.PRIORITY.LOW);
			serviceRq.setHttpHeaders( API_HEADER_NAMES_ARRAY_2, getApiHeaderValuesArray2());

			String url = AppConstants.BASE_URL + GenralRequest.BANNER_NAVIGATION_METHOD;
			Log.d("maxis", "url " + url);
			serviceRq.setUrl(HttpHelper.getURLWithPrams(url, urlParams));

			serviceRq.setHttpMethod(HttpClientConnection.HTTP_METHOD.GET);

			HttpClientConnection.getInstance().addRequest(serviceRq);
		} catch (Exception e) {
			logRequestException(e, "BannerNavigationController");

			Response res = getErrorResponse(mActivity.getResources().getString(R.string.communication_failure), 111);
			responseService(res);
		}
	}

	@Override
	public void responseService(Object object) {
		JSONParser jsonParser = JSONHandler.getInstanse();
		if (object instanceof Response) {
			try {
				String responseStr = ((Response) object).getResponseText();

				LogBannerReportResponse logBannerReportResponse = jsonParser.mapFromJSON(responseStr, LogBannerReportResponse.class);
				mScreen.setScreenData(logBannerReportResponse, mEventType, 0);
			} catch (Exception e) {
				logResponseException(e, "BannerNavigationController");

			}
		} 
	}

}
