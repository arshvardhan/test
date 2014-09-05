package com.kelltontech.maxisgetit.controllers;

import android.content.Context;
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
import com.kelltontech.maxisgetit.constants.AppConstants;
import com.kelltontech.maxisgetit.parsers.UserDetailParser;
import com.kelltontech.maxisgetit.requests.GenralRequest;
import com.kelltontech.maxisgetit.response.UserDetailResponse;

public class UserDetailController extends BaseServiceController {
	private Context mActivity;

	public UserDetailController(IActionController screen, int eventType) {
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
			serviceRq.setHttpMethod(HttpClientConnection.HTTP_METHOD.GET);

			String url = AppConstants.BASE_URL + GenralRequest.USER_DETAIL_METHOD;
			Log.d("maxis", "url " + url);
			GenralRequest baseReq = new GenralRequest(mActivity);
			String mMobile = (String) requestData;
			serviceRq.setUrl(HttpHelper.getURLWithPrams(url, baseReq.getUserDetailHeaders(mMobile)));

			HttpClientConnection.getInstance().addRequest(serviceRq);
		} catch (Exception e) {
			logRequestException(e, "UserDetailController");
			
			Response res = getErrorResponse(mActivity.getResources().getString(R.string.internal_error),111 );
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
				response.setPayload(new UserDetailParser().parse(response.getResponseText()));
			} catch (Exception e) {
				logResponseException(e, "UserDetailController");
			}
			if (response.getPayload() instanceof UserDetailResponse) {
				UserDetailResponse userResp = (UserDetailResponse) response.getPayload();
				if (userResp.isErrorFromServer()) {
					message.obj = userResp.getServerMessage();
				} else {
					message.arg1 = 0;
					message.obj = userResp;
				}
			} else {
				message.obj = mActivity.getResources().getString(R.string.internal_error);
			}
		}
		mScreen.setScreenData(message, mEventType, 0);
	}

}
