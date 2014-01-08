package com.kelltontech.maxisgetit.controllers;
/**
 * @author Atul Bhardwaj 
 * @email atul.bhardwaj@dbydx.com
 * 
 * code updated by anoop singh
 */
import org.apache.http.entity.ByteArrayEntity;

import android.content.Context;

import com.kelltontech.framework.controller.BaseServiceController;
import com.kelltontech.framework.model.MyError;
import com.kelltontech.framework.model.Response;
import com.kelltontech.framework.network.HttpClientConnection;
import com.kelltontech.framework.network.ServiceRequest;
import com.kelltontech.framework.ui.IActionController;
import com.kelltontech.framework.utils.NativeHelper;
import com.kelltontech.maxisgetit.constants.AppConstants;
import com.kelltontech.maxisgetit.model.uploadImage.RequestUploadPhoto;
import com.kelltontech.maxisgetit.model.uploadImage.ResponseUploadPhoto;


public class ContestUploadImageController extends BaseServiceController {

	private Context       mActivity;
	private long         _hitTime;

	public ContestUploadImageController(IActionController screen, int eventType) {
		super(screen, eventType);
		mActivity = (Context) mScreen;
	}

	@Override
	public void initService() {
		// nothing to do here
	}
	
	public void setHitTime(long hitTime) {
		_hitTime=hitTime;
	}
	
	@Override
	public void requestService( Object requestData ) {
		try {
			if(!NativeHelper.isDataConnectionAvailable(mActivity)) {
				responseService(new MyError(MyError.NETWORK_NOT_AVAILABLE));
				return;
			}

			ServiceRequest serviceRq = new ServiceRequest();
			serviceRq.setRequestData(requestData);
			serviceRq.setServiceController(this);
			serviceRq.setDataType(mEventType);
			serviceRq.setPriority(HttpClientConnection.PRIORITY.LOW);
			serviceRq.setHttpMethod(HttpClientConnection.HTTP_METHOD.POST);
			
			serviceRq.setRequestTimeOut(AppConstants.MILLIS_3_MINUTE);
			
			String url = AppConstants.BASE_URL_CONTEST + "saveImage?" + AppConstants.URL_ENCODED_PARAMS;
			serviceRq.setUrl(url);
			
			RequestUploadPhoto requestUpload = (RequestUploadPhoto) requestData;  
			String json = requestUpload.toJson();
			serviceRq.setPostData(new ByteArrayEntity(json.toString().getBytes()));
			
			serviceRq.setHttpHeaders( API_HEADER_NAMES_ARRAY_2, getApiHeaderValuesArray2());
			
			HttpClientConnection.getInstance().addRequest(serviceRq);
		} catch(Exception e) {
			logRequestException(e, "ContestUploadingImageController");
			responseService(new MyError(MyError.EXCEPTION));
		}
	}

	@Override
	public void responseService(Object object) {
		if( object instanceof Response) {
			try {
				String responseStr = ((Response) object).getResponseText();
				ResponseUploadPhoto filterResonse = new ResponseUploadPhoto().fromJson(responseStr);  
				mScreen.setScreenData(filterResonse, mEventType , _hitTime);
			} catch(Exception e) {
				logResponseException(e, "ContestUploadingImageController");
				mScreen.setScreenData(new MyError(MyError.EXCEPTION), mEventType , _hitTime);
			}
		} else if( object instanceof MyError ) {
			mScreen.setScreenData((MyError)object, mEventType , _hitTime);
		}
	}
}


