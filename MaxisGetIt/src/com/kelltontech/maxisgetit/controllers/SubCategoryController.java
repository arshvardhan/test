package com.kelltontech.maxisgetit.controllers;

import java.util.Hashtable;

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
import com.kelltontech.maxisgetit.dao.CategoryGroup;
import com.kelltontech.maxisgetit.parsers.SubCategoryParser;
import com.kelltontech.maxisgetit.requests.GenralRequest;
import com.kelltontech.maxisgetit.response.SubCategoryResponse;

public class SubCategoryController extends BaseServiceController {
	private Context mActivity;
	private CategoryGroup mParentCat;
	public static boolean isForDeal;

	public SubCategoryController(IActionController screen, int eventType) {
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

			mParentCat = (CategoryGroup) requestData;
			GenralRequest genralRequest = new GenralRequest(mActivity);
			Hashtable<String, String> urlParams = genralRequest
					.getSubCategoryheaders(mParentCat, isForDeal);

			ServiceRequest serviceRq = new ServiceRequest();
			serviceRq.setRequestData(requestData);
			serviceRq.setServiceController(this);
			serviceRq.setDataType(mEventType);
			serviceRq.setPriority(HttpClientConnection.PRIORITY.LOW);
			serviceRq.setHttpHeaders(API_HEADER_NAMES_ARRAY_2,
					getApiHeaderValuesArray2());

			String url = AppConstants.BASE_URL + GenralRequest.CATEGORY_METHOD;
			Log.d("maxis", "url " + url);
			serviceRq.setUrl(HttpHelper.getURLWithPrams(url, urlParams));

			serviceRq.setHttpMethod(HttpClientConnection.HTTP_METHOD.GET);

			HttpClientConnection.getInstance().addRequest(serviceRq);
		} catch (Exception e) {
			logRequestException(e, "SubCategoryController");

			Response res = getErrorResponse(
					mActivity.getResources().getString(
							R.string.communication_failure), 111);
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
				response.setPayload(new SubCategoryParser().parse(response
						.getResponseText()));
			} catch (Exception e) {
				logResponseException(e, "SubCategoryController");
			}
			if (response.getPayload() instanceof SubCategoryResponse) {
				SubCategoryResponse categoriesResp = (SubCategoryResponse) response
						.getPayload();
				if (categoriesResp.getCategories().size() < 1) {
					message.obj = mActivity.getResources().getString(
							R.string.no_result_found);
				} else {
					categoriesResp.setParentCategory(mParentCat);
					message.arg1 = 0;
					message.obj = categoriesResp;
				}
			} else {
				message.obj = mActivity.getResources().getString(
						R.string.communication_failure);
			}
		}
		mScreen.setScreenData(message, mEventType, 0);
	}
}
