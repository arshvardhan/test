package com.kelltontech.maxisgetit.controllers;

import java.util.Hashtable;

import android.content.Context;

import com.kelltontech.framework.controller.BaseServiceController;
import com.kelltontech.framework.model.MyError;
import com.kelltontech.framework.model.Response;
import com.kelltontech.framework.network.HttpClientConnection;
import com.kelltontech.framework.network.HttpHelper;
import com.kelltontech.framework.network.ServiceRequest;
import com.kelltontech.framework.ui.IActionController;
import com.kelltontech.framework.utils.NativeHelper;
import com.kelltontech.maxisgetit.R;
import com.kelltontech.maxisgetit.constants.AppConstants;
import com.kelltontech.maxisgetit.constants.Events;
import com.kelltontech.maxisgetit.model.CitiesAreaResponse;
import com.kelltontech.maxisgetit.model.CommonResponse;
import com.kelltontech.maxisgetit.requests.GenralRequest;
import com.kelltontech.maxisgetit.requests.OutletRefineRequest;
import com.kelltontech.maxisgetit.utils.JSONHandler;
import com.kelltontech.maxisgetit.utils.JSONParser;

public class OutletRefineController extends BaseServiceController {

	private Context mActivity;

	public OutletRefineController(IActionController screen, int eventType) {
		super(screen, eventType);
		mActivity = (Context) screen;
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
				res.setErrorText(mActivity.getResources().getString(
						R.string.network_unavailable));
				responseService(res);
				return;
			}

			ServiceRequest serviceRq = new ServiceRequest();
			serviceRq.setRequestData(requestData);
			serviceRq.setServiceController(this);
			serviceRq.setDataType(mEventType);
			serviceRq.setPriority(HttpClientConnection.PRIORITY.LOW);
			serviceRq.setHttpHeaders(API_HEADER_NAMES_ARRAY_2,
					getApiHeaderValuesArray2());
			serviceRq.setRequestTimeOut(30000);

			OutletRefineRequest outletRefineRequest = (OutletRefineRequest) requestData;

			String url = AppConstants.BASE_URL;
			Hashtable<String, String> urlParams = null;
			GenralRequest genralRequest = new GenralRequest(mActivity);

			if (mEventType == Events.CITY_LISTING_OUTLETS) {
				url += GenralRequest.CITY_LISTING_DEALS_METHOD;
				urlParams = genralRequest.getHeadersForRefineOutlets(
						AppConstants.CityScreen,
						outletRefineRequest.getDeal_id(), null, null);
			} else if (mEventType == Events.LOCALITY_LISTING_OUTLETS) {
				url += GenralRequest.LOCALITY_LISTING_DEALS_METHOD;
				urlParams = genralRequest.getHeadersForRefineOutlets(
						AppConstants.CityScreen,
						outletRefineRequest.getDeal_id(),
						outletRefineRequest.getCityName(), null);
			}

			serviceRq.setUrl(HttpHelper.getURLWithPrams(url, urlParams));
			serviceRq.setHttpMethod(HttpClientConnection.HTTP_METHOD.GET);

			HttpClientConnection.getInstance().addRequest(serviceRq);
		} catch (Exception e) {
			logRequestException(e, "OutletRefineController");

			Response res = getErrorResponse(
					mActivity.getResources().getString(R.string.internal_error),
					111);
			responseService(res);
		}
	}

	@Override
	public void responseService(Object object) {
		JSONParser jsonParser = JSONHandler.getInstanse();
		if (object instanceof Response) {
			try {
				String responseStr = ((Response) object).getResponseText();

				CommonResponse cResponse = jsonParser.mapFromJSON(
						responseStr, CommonResponse.class);

				mScreen.setScreenData(cResponse, mEventType, 0);
			} catch (Exception e) {
				logResponseException(e, "OutletRefineController");
				
			}
		} 
	}

}
