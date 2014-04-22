package com.kelltontech.maxisgetit.controllers;

import org.apache.http.entity.ByteArrayEntity;
import org.json.JSONObject;

import android.content.Context;
import android.os.Message;
import android.util.Log;

import com.kelltontech.framework.controller.BaseServiceController;
import com.kelltontech.framework.model.MaxisResponse;
import com.kelltontech.framework.model.Response;
import com.kelltontech.framework.network.HttpClientConnection;
import com.kelltontech.framework.network.ServiceRequest;
import com.kelltontech.framework.ui.IActionController;
import com.kelltontech.framework.utils.NativeHelper;
import com.kelltontech.maxisgetit.R;
import com.kelltontech.maxisgetit.constants.AppConstants;
import com.kelltontech.maxisgetit.parsers.GenralResponseParser;
import com.kelltontech.maxisgetit.requests.GenralRequest;

public class CompanyDetailRemoveFavController extends BaseServiceController {
	private Context mActivity;
	public boolean fromComapnyList;
	private String screenName ="";

	public CompanyDetailRemoveFavController(IActionController screen, int eventType) {
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
				res.setErrorText(mActivity.getResources().getString(R.string.network_unavailable));
				responseService(res);
				return;
			}

		/*	CompanyDetailRemoveFav removeFav = (CompanyDetailRemoveFav) requestData;
			GenralRequest genralRequest = new GenralRequest(mActivity);
			Hashtable<String, String> urlParams = genralRequest.getCompanyDetailRemoveFavHeader(removeFav.getUserId(), removeFav.getCompanyId(),
					removeFav.getCategoryId());

	*/		ServiceRequest serviceRq = new ServiceRequest();
			serviceRq.setRequestData(requestData);
			serviceRq.setServiceController(this);
			serviceRq.setDataType(mEventType);
			serviceRq.setPriority(HttpClientConnection.PRIORITY.LOW);
			serviceRq.setHttpHeaders(API_HEADER_NAMES_ARRAY_2, getApiHeaderValuesArray2());
			serviceRq.setHttpMethod(HttpClientConnection.HTTP_METHOD.POST);
			serviceRq.setRequestTimeOut(AppConstants.MILLIS_3_MINUTE);
			
			if(fromComapnyList)
			{
				screenName = AppConstants.Company_detail;
			}else
			{
				screenName  = AppConstants.MyFavourite;
			}
			
			
			String url = AppConstants.BASE_URL + GenralRequest.COMPANY_DETAIL_REMOVE_FAV_METHOD
					+ new GenralRequest(mActivity).getDefaultParameterString(screenName);
			Log.d("maxis", "url " + url);
			serviceRq.setUrl(url);
			//serviceRq.setUrl(HttpHelper.getURLWithPrams(url, urlParams));
			JSONObject postJson = (JSONObject) requestData;
			Log.d("maxis", "delete data" + postJson.toString());
			serviceRq.setPostData(new ByteArrayEntity(postJson.toString().getBytes()));

			HttpClientConnection.getInstance().addRequest(serviceRq);
		} catch (Throwable e) {
			logRequestException(e, "CompanyDetailRemoveFavController");
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
				response.setPayload(new GenralResponseParser().parse(response.getResponseText()));
			} catch (Exception e) {
				logResponseException(e, "CompanyDetailRemoveFavController");
			}

			if (response.getPayload() instanceof MaxisResponse) {
				MaxisResponse genResp = (MaxisResponse) response.getPayload();
				if (genResp.isErrorFromServer()) {
					message.obj = genResp.getServerMessage();
				} else {
					message.arg1 = 0;
					message.obj = genResp;
				}
			} else {
				message.obj = mActivity.getResources().getString(R.string.internal_error);
			}
		}
		mScreen.setScreenData(message, mEventType, 0);
	}
}