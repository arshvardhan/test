package com.kelltontech.maxisgetit.controllers;

import org.apache.http.entity.ByteArrayEntity;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

import com.kelltontech.maxisgetit.R;
import com.kelltontech.framework.controller.BaseServiceController;
import com.kelltontech.framework.model.Response;
import com.kelltontech.framework.network.HttpClientConnection;
import com.kelltontech.framework.network.ServiceRequest;
import com.kelltontech.framework.ui.IActionController;
import com.kelltontech.framework.utils.NativeHelper;
import com.kelltontech.maxisgetit.constants.AppConstants;
import com.kelltontech.maxisgetit.parsers.GenralResponseParser;
import com.kelltontech.maxisgetit.requests.GenralRequest;
import com.kelltontech.maxisgetit.requests.PostReviewRequest;

public class PostReviewController extends BaseServiceController {
	private Context mActivity;

	public PostReviewController(IActionController screen, int eventType) {
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
			serviceRq.setHttpMethod(HttpClientConnection.HTTP_METHOD.POST);
			
			GenralRequest genralRequest = new GenralRequest(mActivity);
			String url = AppConstants.BASE_URL + GenralRequest.POST_REVIEW;
			url += genralRequest.getDefaultParameterString(AppConstants.Company_detail);
			Log.d("maxis", "url " + url);
			serviceRq.setUrl(url);
			
			PostReviewRequest postRevRequest = (PostReviewRequest) requestData;
			JSONObject jsonObject = new JSONObject();
			jsonObject.put(GenralRequest.KEY_COMP_ID, postRevRequest.getCompId());
			jsonObject.put(GenralRequest.KEY_CAT_ID, postRevRequest.getCatId());
			jsonObject.put(GenralRequest.KEY_RATING, postRevRequest.getRating());
			jsonObject.put(GenralRequest.KEY_REVIEW, postRevRequest.getReview());
			jsonObject.put(GenralRequest.KEY_UID, postRevRequest.getUserId());
			jsonObject.put(GenralRequest.KEY_MOBILE, postRevRequest.getMobileNumber());
			jsonObject.put(GenralRequest.KEY_NAME, postRevRequest.getUserName());
			Log.d("json", jsonObject.toString());
			
			serviceRq.setPostData(new ByteArrayEntity(jsonObject.toString().getBytes()));
			
			HttpClientConnection.getInstance().addRequest(serviceRq);
		} catch (Exception e) {
			logRequestException(e, "PostReviewController");
			
			Response res = getErrorResponse(mActivity.getResources().getString(R.string.internal_error), 111);
			responseService(res);
		}

	}

	@Override
	public void responseService(Object object) {
		Response response = (Response) object;
		if( !response.isError() ){
			try {
				response.setPayload(new GenralResponseParser().parse(response.getResponseText()));
			} catch (Exception e) {
				logResponseException(e, "PostReviewController");
				handleException(e);
				return;
			}
		}
		mScreen.setScreenData(object, mEventType, 0);
	}

}
