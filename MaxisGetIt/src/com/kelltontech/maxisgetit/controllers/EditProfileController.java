package com.kelltontech.maxisgetit.controllers;

import org.apache.http.entity.ByteArrayEntity;
import org.json.JSONObject;

import android.content.Context;
import android.os.Message;
import android.util.Log;

import com.kelltontech.maxisgetit.R;
import com.kelltontech.framework.controller.BaseServiceController;
import com.kelltontech.framework.model.MaxisResponse;
import com.kelltontech.framework.model.Response;
import com.kelltontech.framework.network.HttpClientConnection;
import com.kelltontech.framework.network.ServiceRequest;
import com.kelltontech.framework.ui.IActionController;
import com.kelltontech.framework.utils.NativeHelper;
import com.kelltontech.maxisgetit.constants.AppConstants;
import com.kelltontech.maxisgetit.parsers.GenralResponseParser;
import com.kelltontech.maxisgetit.requests.EditProfileRequest;
import com.kelltontech.maxisgetit.requests.GenralRequest;

public class EditProfileController extends BaseServiceController {
	private Context mActivity;

	public EditProfileController(IActionController screen, int eventType) {
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
			
			EditProfileRequest editProfileRequest = (EditProfileRequest) requestData;
			JSONObject jsonObject=new JSONObject();
			jsonObject.put(GenralRequest.KEY_MOBILE, editProfileRequest.getMobile());
			jsonObject.put(GenralRequest.KEY_NAME,editProfileRequest.getUserName());
			jsonObject.put(GenralRequest.KEY_PASSWD, editProfileRequest.getCurrentPass());
			jsonObject.put(GenralRequest.KEY_NEW_PASS, editProfileRequest.getNewPass());
			serviceRq.setPostData(new ByteArrayEntity(jsonObject.toString().getBytes()));
			
			GenralRequest genralRequest = new GenralRequest(mActivity);
			String url = AppConstants.BASE_URL + GenralRequest.EDIT_PROFILE_METHOD + genralRequest.getDefaultParameterString(AppConstants.MyAccount);;
			
			Log.d("maxis", "url " + url);
			Log.d("json",jsonObject.toString());
			serviceRq.setUrl(url);
			serviceRq.setHttpMethod(HttpClientConnection.HTTP_METHOD.POST);

			HttpClientConnection.getInstance().addRequest(serviceRq);


		} catch (Exception e) {
			logRequestException(e, "EditProfileController");
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
				response.setPayload(new GenralResponseParser().parse(response.getResponseText()));
			} catch (Exception e) {
				logResponseException(e, "EditProfileController");
			}
			if (response.getPayload() instanceof MaxisResponse) {
				MaxisResponse maxResp = (MaxisResponse) response.getPayload();
				if (maxResp.isErrorFromServer()) {
					message.obj = maxResp.getServerMessage();
				} else {
					message.arg1 = 0;
					message.obj = maxResp;
				}
			} else {
				message.obj = mActivity.getResources().getString(R.string.internal_error);
			}
		}
		mScreen.setScreenData(message, mEventType, 0);
	}

}
