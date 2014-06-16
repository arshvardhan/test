package com.kelltontech.maxisgetit.controllers;

import java.util.Hashtable;

import android.content.Context;
import android.os.Message;
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
import com.kelltontech.maxisgetit.dao.OutLetDetails;
import com.kelltontech.maxisgetit.parsers.CompanyDetailParser;
import com.kelltontech.maxisgetit.parsers.DealCategoriesParser;
import com.kelltontech.maxisgetit.parsers.OutLetDetailParser;
import com.kelltontech.maxisgetit.requests.GenralRequest;
import com.kelltontech.maxisgetit.requests.OutLetDetailRequest;

public class OutLetDetailtController extends BaseServiceController {
	private Context mActivity;

	public OutLetDetailtController(IActionController screen, int eventType) {
		super(screen, eventType);
		mActivity = (Context) screen;
	}

	@Override
	public void initService() {
		// Auto-generated method stub
	}

	public void requestService(Object requestData) {
		OutLetDetailRequest detailRequest = (OutLetDetailRequest) requestData;

		String cid = detailRequest.getComp_id();
		String deal_id = detailRequest.getDeal_id();
		String l3cat_id = detailRequest.getL3cat_id();
		int page_number = detailRequest.getPage_number();
		String cityName = detailRequest.getCityName();
		String localityName = detailRequest.getLocalityName();
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
			serviceRq.setHttpMethod(HttpClientConnection.HTTP_METHOD.GET);

			String url = AppConstants.BASE_URL;

			url += GenralRequest.OUTLET_DETAIL_METHOD;

			Log.d("maxis", "url " + url);

			GenralRequest genralRequest = new GenralRequest(mActivity);
			Hashtable<String, String> urlParams = genralRequest
					.getHeadersWithCompanyIDndDealID(cid, deal_id, l3cat_id,
							page_number , cityName , localityName);
			serviceRq.setUrl(HttpHelper.getURLWithPrams(url, urlParams));

			HttpClientConnection.getInstance().addRequest(serviceRq);
		} catch (Exception e) {
			logRequestException(e, "OUTLET DEATIL");

			Response res = getErrorResponse(
					mActivity.getResources().getString(R.string.internal_error),
					111);
			responseService(res);
		}
	}

	@Override
	public void responseService(Object object) {

		Response response = (Response) object;
		if (!response.isError()) {
			try {

				response.setPayload(new OutLetDetailParser().parse(response.getResponseText()));

			} catch (Exception e) {
				handleException(e);
				logResponseException(e, "CompanyDetailsController");
				return;
			}
		}
		mScreen.setScreenData(response, mEventType, 0);
	}

}
