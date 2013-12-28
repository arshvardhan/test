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
import com.kelltontech.maxisgetit.requests.PostErrorRequest;

public class PostErrorController extends BaseServiceController {
	private Context mActivity;

	public PostErrorController(IActionController screen, int eventType) {
		super(screen, eventType);
		mActivity = (Context) screen;
	}

	@Override
	public void initService() {
		// TODO Auto-generated method stub
	}

	@Override
	public void requestService(Object requestData) {
		try{
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
			String url = AppConstants.BASE_URL + GenralRequest.SAVE_ERROR_REPORT + genralRequest.getDefaultParameterString();
			Log.d("maxis", "url " + url);
			serviceRq.setUrl(url);
			
			PostErrorRequest postErrorRequest = (PostErrorRequest)requestData;
			JSONObject jsonObject=new JSONObject();
			jsonObject.put(GenralRequest.KEY_COMP_ID, postErrorRequest.getCompId());
			jsonObject.put(GenralRequest.KEY_CAT_ID, postErrorRequest.getCatId());
			jsonObject.put(GenralRequest.KEY_ERROR_STATUS, postErrorRequest.getErrorStatus());
			jsonObject.put(GenralRequest.KEY_REMARK, postErrorRequest.getRemark());
			jsonObject.put(GenralRequest.KEY_UID, postErrorRequest.getUserId());
			jsonObject.put(GenralRequest.KEY_NAME, postErrorRequest.getName());
			jsonObject.put(GenralRequest.KEY_MOBILE, postErrorRequest.getMobile());
			serviceRq.setPostData(new ByteArrayEntity(jsonObject.toString().getBytes()));
			
			HttpClientConnection.getInstance().addRequest(serviceRq);
		} catch (Exception e) {
			logRequestException(e, "PostErrorController");
			
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
				logResponseException(e, "PostErrorController");
				handleException(e);
				return;
			}
		}
		mScreen.setScreenData(response, mEventType, 0);
		
	}

}
