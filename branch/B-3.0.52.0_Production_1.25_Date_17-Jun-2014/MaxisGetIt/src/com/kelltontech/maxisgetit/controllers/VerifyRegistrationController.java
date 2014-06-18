package com.kelltontech.maxisgetit.controllers;

import android.content.Context;
import android.os.Message;
import android.util.Log;

import com.kelltontech.maxisgetit.R;
import com.kelltontech.framework.controller.BaseServiceController;
import com.kelltontech.framework.model.MaxisResponse;
import com.kelltontech.framework.model.Response;
import com.kelltontech.framework.network.HttpClientConnection;
import com.kelltontech.framework.network.HttpHelper;
import com.kelltontech.framework.network.ServiceRequest;
import com.kelltontech.framework.ui.IActionController;
import com.kelltontech.framework.utils.NativeHelper;
import com.kelltontech.maxisgetit.constants.AppConstants;
import com.kelltontech.maxisgetit.parsers.GenralResponseParser;
import com.kelltontech.maxisgetit.requests.ActivationRequest;
import com.kelltontech.maxisgetit.requests.GenralRequest;

public class VerifyRegistrationController extends BaseServiceController {
	private Context mActivity;

	public VerifyRegistrationController(IActionController screen, int eventType) {
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
			
			String url = AppConstants.BASE_URL + GenralRequest.VERIFY_REGISTRATION_METHOD;
			Log.d("maxis", "url " + url);

			GenralRequest genralRequest = new GenralRequest(mActivity);
			ActivationRequest activationRequest = (ActivationRequest) requestData;
			serviceRq.setUrl(HttpHelper.getURLWithPrams(url, genralRequest.getVerifyRegistrationHeaders(activationRequest.getCode(), activationRequest.getMobile())));
			serviceRq.setHttpMethod(HttpClientConnection.HTTP_METHOD.GET);

			HttpClientConnection.getInstance().addRequest(serviceRq);

		} catch (Exception e) {
			logRequestException(e, "VerifyRegistrtationController");
			
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
				logResponseException(e, "VerifyRegistrtationController");
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
