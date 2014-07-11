package com.kelltontech.maxisgetit.controllers;
/**
 * @author Atul Bhardwaj  
 * @email atul.bhardwaj@dbydx.com
 */
import android.content.Context;
import android.net.Uri;

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
import com.kelltontech.maxisgetit.model.listModel.ResponseList;
import com.kelltontech.maxisgetit.model.poiModel.RequestPoiSearch;
import com.kelltontech.maxisgetit.model.poiModel.ResponsePoi;
import com.kelltontech.maxisgetit.requests.MaxisBaseRequest;


public class ContestPoiController extends BaseServiceController{
	private Context       mActivity;
	private long         _hitTime;


	/**
	 * 
	 * @param screen
	 * @param eventType
	 */
	public ContestPoiController(IActionController screen, int eventType) {
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
		try{	
			if(!NativeHelper.isDataConnectionAvailable(mActivity)) {
				responseService(new MyError(MyError.NETWORK_NOT_AVAILABLE));
				return;
			}
			
			RequestPoiSearch requestPoiSearch=(RequestPoiSearch) requestData;
			int recordPerPage=requestPoiSearch.getRecordsPerPage();
			int pageNumber=requestPoiSearch.getPageNumber();
			String url =null;
			if (requestPoiSearch.getSearchText()==null||requestPoiSearch.getSearchText().length()==0) {
				url = AppConstants.BASE_URL_CONTEST + "getPOICount?" + AppConstants.URL_ENCODED_PARAMS + "&latitude="+requestPoiSearch.getLatitude()+"&longitude="+requestPoiSearch.getLongitude()+"&"+MaxisBaseRequest.DEVICE_ID+"="+MyApplication.getDeviceId();	
			} else {
				url = AppConstants.BASE_URL_CONTEST + "search?" + AppConstants.URL_ENCODED_PARAMS + "&latitude="+requestPoiSearch.getLatitude()+"&longitude="+requestPoiSearch.getLongitude()+"&keyword="+Uri.encode(requestPoiSearch.getSearchText())+"&per_page_record="+recordPerPage+"&page_number="+pageNumber+"&"+MaxisBaseRequest.DEVICE_ID+"="+MyApplication.getDeviceId();	
			}

			ServiceRequest serviceRq = new ServiceRequest();
			serviceRq.setRequestData(requestData);
			serviceRq.setServiceController(this);
			serviceRq.setDataType(mEventType);
			serviceRq.setPriority(HttpClientConnection.PRIORITY.LOW);
			serviceRq.setUrl(url);
			serviceRq.setHttpMethod(HttpClientConnection.HTTP_METHOD.GET);
			
			serviceRq.setHttpHeaders( API_HEADER_NAMES_ARRAY_2, getApiHeaderValuesArray2());
			
			HttpClientConnection.getInstance().addRequest(serviceRq);
		}catch(Exception e){
			logRequestException(e, "ContestPoiController");
			responseService(new MyError(MyError.EXCEPTION));
		}
	}

	@Override
	public void responseService(Object object) {
		if( object instanceof Response) {
			try{
				String responseStr = ((Response) object).getResponseText();
				responseStr = responseStr.replaceAll("&amp;", "&");
				if(mEventType==Events.POI_SEARCH_EVENT)
				{
					ResponseList responseList = new ResponseList().fromJson(responseStr,mEventType);
					mScreen.setScreenData(responseList, mEventType , _hitTime);
				}
				else
				{
					ResponsePoi filterResonse = new ResponsePoi().fromJson(responseStr);  
					mScreen.setScreenData(filterResonse, mEventType , _hitTime);
				}
			} catch(Exception e) {
				logResponseException(e, "ContestPoiController");
				mScreen.setScreenData(new MyError(MyError.EXCEPTION), mEventType , _hitTime);
			}
		} else if( object instanceof MyError ) {
			mScreen.setScreenData((MyError)object, mEventType , _hitTime);
		}
	}
}


