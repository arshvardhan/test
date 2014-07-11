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
import com.kelltontech.maxisgetit.parsers.TempletParser;
import com.kelltontech.maxisgetit.requests.GenralRequest;
import com.kelltontech.maxisgetit.requests.SaleTemplate;
import com.kelltontech.maxisgetit.response.ControlDetailResponse;

public class TempletController extends BaseServiceController {
	private Context mActivity;

	public TempletController(IActionController screen, int eventType) {
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

			SaleTemplate saleTemplate = (SaleTemplate)requestData;
			GenralRequest genralRequest = new GenralRequest(mActivity);
			Hashtable<String, String> urlParams = genralRequest.getSaleTemplateHeaders(saleTemplate.getCategoryId(), saleTemplate.getTemplateType());

			ServiceRequest serviceRq = new ServiceRequest();
			serviceRq.setRequestData(requestData);
			serviceRq.setServiceController(this);
			serviceRq.setDataType(mEventType);
			serviceRq.setPriority(HttpClientConnection.PRIORITY.LOW);
			serviceRq.setHttpHeaders( API_HEADER_NAMES_ARRAY_2, getApiHeaderValuesArray2());
			
			String url = AppConstants.BASE_URL + GenralRequest.TEMPLET_Method;
			//String url = "http://192.168.13.16/trunk/ClassifiedRest/" + GenralRequest.TEMPLET_Method;
			Log.d("maxis", "url " + url);

			serviceRq.setUrl(HttpHelper.getURLWithPrams(url, urlParams));
			serviceRq.setHttpMethod(HttpClientConnection.HTTP_METHOD.GET);

			HttpClientConnection.getInstance().addRequest(serviceRq);

		} catch (Exception e) {
			logRequestException(e, "TempletController");
			
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
				response.setPayload(new TempletParser().parse(response.getResponseText()));
			} catch (Exception e) {
				logResponseException(e, "TempletController");
			}
			
			if (response.getPayload() instanceof ControlDetailResponse) {
				ControlDetailResponse controlResp = (ControlDetailResponse) response.getPayload();
				if (controlResp.isErrorFromServer()) {
					message.obj = controlResp.getServerMessage();
				} else {
//					if(controlResp.getControlList().size()==0){
//						message.obj=mActivity.getResources().getString(R.string.no_result_found);
//					}else{
						message.arg1 = 0;
						message.obj = controlResp;
//					}
				}
			} else {
				message.obj = mActivity.getResources().getString(R.string.internal_error);
			}
		}
		mScreen.setScreenData(message, mEventType, 0);
	}

}
