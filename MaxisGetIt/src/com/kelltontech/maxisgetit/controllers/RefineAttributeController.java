package com.kelltontech.maxisgetit.controllers;

import java.util.Hashtable;

import org.apache.http.entity.ByteArrayEntity;

import android.content.Context;
import android.net.Uri;
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
import com.kelltontech.framework.utils.StringUtil;
import com.kelltontech.maxisgetit.constants.AppConstants;
import com.kelltontech.maxisgetit.constants.Events;
import com.kelltontech.maxisgetit.parsers.RefineAttributeParser;
import com.kelltontech.maxisgetit.requests.GenralRequest;
import com.kelltontech.maxisgetit.requests.RefineSearchRequest;
import com.kelltontech.maxisgetit.response.RefineSelectorResponse;

public class RefineAttributeController extends BaseServiceController {
	private Context mActivity;
	private String screenName;

	public RefineAttributeController(IActionController screen, int eventType) {
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
				Response res = new Response();
				res.setErrorCode(101);
				res.setErrorText(mActivity.getResources().getString(
						R.string.network_unavailable));
				responseService(res);
				return;
			}

			RefineSearchRequest refineSearchRequest = (RefineSearchRequest) requestData;

			ServiceRequest serviceRq = new ServiceRequest();
			serviceRq.setRequestData(refineSearchRequest.getPostData());
			serviceRq.setServiceController(this);
			serviceRq.setDataType(mEventType);
			serviceRq.setPriority(HttpClientConnection.PRIORITY.LOW);
			serviceRq.setHttpHeaders(API_HEADER_NAMES_ARRAY_2,
					getApiHeaderValuesArray2());
			String url = AppConstants.BASE_URL;

			GenralRequest baseRequest = new GenralRequest(mActivity);

			if (mEventType == Events.REFINE_SEARCH_LOCALITY) {
				url += GenralRequest.REFINE_SEARCH_LOCALITY_METHOD;
				url += baseRequest.getUrlAppendableDefaultHeaders()
						+ "&category_id=" + refineSearchRequest.getCategoryId();
				if (refineSearchRequest.getSearchKeyword() != null) {
					url += "&keyword="
							+ Uri.encode(refineSearchRequest.getSearchKeyword());
				}
				if (refineSearchRequest.isDeal()) {
					url += "&type=" + AppConstants.COMP_TYPE_DEAL;
				}

				url += "&pageView=" + AppConstants.Modify_screen;
				serviceRq.setUrl(url);
				serviceRq.setHttpMethod(HttpClientConnection.HTTP_METHOD.POST);
				serviceRq.setPostData(new ByteArrayEntity(refineSearchRequest
						.getPostData().toString().getBytes()));
			} else if (mEventType == Events.REFINE_ATTRIBUTES) {
				url += GenralRequest.REFINE_ATTRI_METHOD;
				if (refineSearchRequest.isDeal()) {
					screenName = AppConstants.Modify_screen;// Deal_Listing;
				} else {
					screenName = AppConstants.Modify_screen;// Company_listing;

				}

				if (StringUtil.isNullOrEmpty(refineSearchRequest
						.getSearchKeyword())) {
					Hashtable<String, String> urlParams = baseRequest
							.getCategoryheadersForRefine(
									refineSearchRequest.getCategoryId(),
									screenName,
									"");
					url = HttpHelper.getURLWithPrams(url, urlParams);
				} else {
					Hashtable<String, String> urlParams = baseRequest
							.getCategoryheadersForRefine(
									refineSearchRequest.getCategoryId(),
									screenName,
									refineSearchRequest.getSearchKeyword());
					url = HttpHelper.getURLWithPrams(url, urlParams);
				}

				if (refineSearchRequest.isDeal()) {
					url += "&type=" + AppConstants.COMP_TYPE_DEAL;
				}
				serviceRq.setUrl(url);
				serviceRq.setHttpMethod(HttpClientConnection.HTTP_METHOD.GET);
			}

			Log.d("maxis", "url " + url);
			HttpClientConnection.getInstance().addRequest(serviceRq);
		} catch (Exception e) {
			logRequestException(e, "RefineAttributeController");

			Response res = getErrorResponse(
					mActivity.getResources().getString(R.string.internal_error),
					111);
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
				response.setPayload(new RefineAttributeParser().parse(response
						.getResponseText()));
			} catch (Exception e) {
				logResponseException(e, "RefineAttributeController");
			}

			if (response.getPayload() instanceof RefineSelectorResponse) {
				RefineSelectorResponse selectorResp = (RefineSelectorResponse) response
						.getPayload();
				if (selectorResp.getSelectors().size() < 1) {
					message.obj = mActivity.getResources().getString(
							R.string.no_result_found);
				} else {
					message.arg1 = 0;
					message.obj = selectorResp;
				}
			} else {
				message.obj = mActivity.getResources().getString(
						R.string.internal_error);
			}
		}
		mScreen.setScreenData(message, mEventType, 0);
	}
}
