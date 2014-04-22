package com.kelltontech.maxisgetit.controllers;
/**
 * @author Atul Bhardwaj
 * @email atul.bhardwaj@dbydx.com
 */
import android.content.Context;
import android.util.Log;

import com.kelltontech.framework.controller.BaseServiceController;
import com.kelltontech.framework.db.MyApplication;
import com.kelltontech.framework.model.MyError;
import com.kelltontech.framework.model.Response;
import com.kelltontech.framework.network.HttpClientConnection;
import com.kelltontech.framework.network.ServiceRequest;
import com.kelltontech.framework.ui.IActionController;
import com.kelltontech.framework.utils.NativeHelper;
import com.kelltontech.maxisgetit.constants.AppConstants;
import com.kelltontech.maxisgetit.constants.Events;
import com.kelltontech.maxisgetit.model.listModel.RequestDistance;
import com.kelltontech.maxisgetit.model.listModel.ResponseList;
import com.kelltontech.maxisgetit.requests.MaxisBaseRequest;


public class ContestListController extends BaseServiceController{
	private Context       mActivity;
	private long         _hitTime;



	public ContestListController(IActionController screen, int eventType) {
		super(screen, eventType);
		mActivity = (Context) mScreen;
	}

	@Override
	public void initService() {}
	
	public void setHitTime(long hitTime)
	{
		_hitTime=hitTime;
	}
	@Override
	public void requestService( Object requestData ) {
		try {
			if(!NativeHelper.isDataConnectionAvailable(mActivity)){
				responseService(new MyError(MyError.NETWORK_NOT_AVAILABLE));
				return;
			}

			
			ServiceRequest serviceRq = new ServiceRequest();
			serviceRq.setRequestData(requestData);
			serviceRq.setServiceController(this);
			serviceRq.setDataType(mEventType);
			serviceRq.setPriority(HttpClientConnection.PRIORITY.LOW);
			serviceRq.setHttpMethod(HttpClientConnection.HTTP_METHOD.GET);
			
			serviceRq.setHttpHeaders( API_HEADER_NAMES_ARRAY_2, getApiHeaderValuesArray2());
			
			RequestDistance  requestDistance=(RequestDistance) requestData;
			int recordPerPage=requestDistance.getRecordsPerPage();
			int pageNumber=requestDistance.getPageNumber();
			String url;
			if(mEventType==Events.CATEGORY_LIST_EVENT) {
				url = AppConstants.BASE_URL_CONTEST + "categoryList?" + AppConstants.URL_ENCODED_PARAMS + "&latitude="+requestDistance.getLatitude()+"&longitude="+requestDistance.getLongitude()+"&per_page_record="+recordPerPage+"&page_number="+pageNumber+"&"+MaxisBaseRequest.DEVICE_ID+"="+MyApplication.getDeviceId();
			} else  {
				url = AppConstants.BASE_URL_CONTEST + "companyList?" + AppConstants.URL_ENCODED_PARAMS + "&latitude="+requestDistance.getLatitude()+"&longitude="+requestDistance.getLongitude()+"&per_page_record="+recordPerPage+"&page_number="+pageNumber+"&"+MaxisBaseRequest.DEVICE_ID+"="+MyApplication.getDeviceId();
				if(requestDistance.getCategoryId() != 0 ) {
					url += "&category_id="+requestDistance.getCategoryId();
				}
			}
			Log.d("maxis", "url " + url);
			serviceRq.setUrl(url);
			HttpClientConnection.getInstance().addRequest(serviceRq);
		}catch(Exception e){
			logRequestException(e, "ContestListController");
			responseService(new MyError(MyError.EXCEPTION));
		}
	}

	@Override
	public void responseService(Object object) {
		if( object instanceof Response) {
			try {
				String responseStr = ((Response) object).getResponseText();
				responseStr = responseStr.replaceAll("&amp;", "&");
				ResponseList filterResonse = new ResponseList().fromJson(responseStr,mEventType);  
				mScreen.setScreenData(filterResonse, mEventType , _hitTime);
			} catch(Exception e) {
				logResponseException(e, "ContestListController");
				mScreen.setScreenData(new MyError(MyError.EXCEPTION), mEventType , _hitTime);
			}
		} else if( object instanceof MyError ) {
			mScreen.setScreenData((MyError)object, mEventType , _hitTime);
		}
	}
}


