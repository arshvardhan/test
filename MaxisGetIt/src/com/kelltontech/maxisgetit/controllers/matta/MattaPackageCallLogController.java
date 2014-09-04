package com.kelltontech.maxisgetit.controllers.matta;

	import java.util.Hashtable;

import org.apache.http.entity.ByteArrayEntity;

import android.app.Activity;
import android.util.Log;

import com.kelltontech.framework.controller.BaseServiceController;
import com.kelltontech.framework.model.Response;
import com.kelltontech.framework.network.HttpClientConnection;
import com.kelltontech.framework.network.HttpHelper;
import com.kelltontech.framework.network.ServiceRequest;
import com.kelltontech.framework.ui.IActionController;
import com.kelltontech.framework.utils.NativeHelper;
import com.kelltontech.framework.utils.StringUtil;
import com.kelltontech.maxisgetit.BuildConfig;
import com.kelltontech.maxisgetit.R;
import com.kelltontech.maxisgetit.constants.AppConstants;
import com.kelltontech.maxisgetit.requests.matta.MattaRequest;

	/**
	 * @author arsh.vardhan
	 * @modified 24-Aug-2014
	 */
	public class MattaPackageCallLogController extends BaseServiceController {
		private Activity mActivity;
		public MattaPackageCallLogController(IActionController screen, int eventType) {
			super(screen, eventType);
			mActivity = (Activity) screen;
		}

		@Override
		public void initService() {
		}

		@Override
		public void requestService(Object requestData) {
			String packageCallLogReq = (String) requestData;
			try {
				if (!NativeHelper.isDataConnectionAvailable(mActivity)) {
					Response res = new Response();
					res.setErrorCode(101);
					res.setErrorText(mActivity.getResources().getString(R.string.network_unavailable));
					responseService(res);
					return;
				}
				MattaRequest mattaRequest = new MattaRequest(mActivity);
				Hashtable<String, String> urlParams = mattaRequest.getCallerTrackerHeaders();

				ServiceRequest serviceRq = new ServiceRequest();
				serviceRq.setRequestData(requestData);
				serviceRq.setServiceController(this);
				serviceRq.setDataType(mEventType);
				serviceRq.setPriority(HttpClientConnection.PRIORITY.LOW);
				serviceRq.setHttpHeaders(API_HEADER_NAMES_ARRAY_2, getApiHeaderValuesArray2());
				String url = AppConstants.BASE_URL + MattaRequest.MATTA_PACKAGE_CALL_LOG_METHOD;
				if(BuildConfig.DEBUG) 
					Log.d(AppConstants.FINDIT_DEBUG_TAG, "url " + url);
				serviceRq.setUrl(HttpHelper.getURLWithPrams(url, urlParams));

				if (!StringUtil.isNullOrEmpty(packageCallLogReq)) {
					serviceRq.setHttpMethod(HttpClientConnection.HTTP_METHOD.POST);
					serviceRq.setPostData(new ByteArrayEntity(packageCallLogReq.getBytes()));
					if(BuildConfig.DEBUG) 
						Log.d(AppConstants.FINDIT_DEBUG_TAG, "post payload " + packageCallLogReq);
				}
				HttpClientConnection.getInstance().addRequest(serviceRq);
				
			} catch (Exception e) {
				logRequestException(e, "MattaPackageCallLogController");
				Response res = getErrorResponse(mActivity.getResources().getString(R.string.communication_failure), 111);
				responseService(res);
			}
		}

		@Override
		public void responseService(Object object) {
			if (object instanceof Response) {
				try {
					String responseStr = ((Response) object).getResponseText();
					if(BuildConfig.DEBUG) 
						Log.d(AppConstants.FINDIT_DEBUG_TAG, "MattaPackageCallLog Response" + responseStr);
//					MattaPackageListResponse packageListResponse = GsonUtil.mapFromJSONHanldeObject(responseStr, MattaPackageListResponse.class);
//					mScreen.setScreenData(packageListResponse, mEventType, 0);
				} catch (Exception e) {
					logResponseException(e, "MattaPackageCallLogController");
				}
			} 
		}
}
