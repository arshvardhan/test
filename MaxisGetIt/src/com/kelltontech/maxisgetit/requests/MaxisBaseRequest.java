package com.kelltontech.maxisgetit.requests;

import java.util.Hashtable;

import android.content.Context;

import com.kelltontech.framework.db.MyApplication;
import com.kelltontech.framework.utils.NativeHelper;
import com.kelltontech.framework.utils.StringUtil;
import com.kelltontech.maxisgetit.constants.AppConstants;
import com.kelltontech.maxisgetit.dao.GPS_Data;
import com.kelltontech.maxisgetit.dao.MaxisStore;

public abstract class MaxisBaseRequest {
	public static final String KEY_NAME = "name";
	public static final String KEY_MOBILE = "mobile";
	public static final String KEY_MOBILE_NUM = "mobile_no";
	public static final String KEY_CITY_ID = "city_id";
	public static final String KEY_UID = "uid";
	public static final String KEY_USER_ID = "user_id";
	public static final String KEY_IMSI = "imsi_no";
	public static final String KEY_HANDSET_INFO = "handset_info";
	public static final String KEY_PASSWD = "password";
	public static final String KEY_NEW_PASS= "new_password";
	public static final String KEY_VERIFY_CODE = "verify_code";
	public static final String KEY_CATEGORY_ID = "category_id";
	public static final String KEY_COMPANY_ID = "company_id";
	public static final String KEY_APP_KEY = "APP_KEY";
	public static final String KEY_PLATFORM = "plateform";
	public static final String KEY_LANGUAGE = "language_code";
	public static final String KEY_LATITUDE = "latitude";
	public static final String KEY_LONGITUDE = "longitude";
	public static final String KEY_RESOLUTION = "resolution";
	public static final String KEY_PAGE_NUMBER = "page_number";
	public static final String KEY_SCREEN_TYPE="screen_type";
	public static final String VALUE_PLATFORM = "Andi";
	public static final String VALUE_APP_KEY = "GETITK1234";
	public static final String KEY_GROUP_ACTION_TYPE = "action_type";
	public static final String KEY_GROUP_TYPE = "type";
	public static final String KEY_COMP_ID = "cid";
	public static final String KEY_CAT_ID = "cat_id";
	public static final String KEY_ERROR_STATUS = "error_status";
	public static final String KEY_REMARK = "remark";
	public static final String KEY_REVIEW = "review";
	public static final String KEY_RATING = "rating";
	public static final String KEY_TEMPLATE_TYPE = "template_type";
	public static final String KEY_POST_DEAL_CATEGORY_ID = "catid";
	public static final String KEY_POST_DEAL_REMOVE_IMG_NAME = "image";
	
	public static final String KEY_DEAL_ID = "deal_id";
	public static final String KEY_NUMBER = "phone_no";
	public static final String KEY_L3CAT_ID = "category_id";
			
	private Context mContext;
	protected String mLocaleCode;
	protected String mScreenType;
	private String deviceId = "";
	public static final String DEVICE_ID = "device_id";
	
	public MaxisBaseRequest(Context context) {
		this.mContext = context;
		MaxisStore store=MaxisStore.getStore(mContext);
		mLocaleCode=store.getLocaleCode();
		mScreenType=NativeHelper.getDisplayDensity(mContext);
		deviceId = MyApplication.getDeviceId();
	}

	public Hashtable<String, String> getDefaultHeaders(String screenName) {
		Hashtable<String, String> hashtable = new Hashtable<String, String>();
		hashtable.put(KEY_APP_KEY, VALUE_APP_KEY);
		hashtable.put(KEY_PLATFORM, VALUE_PLATFORM);
		hashtable.put(KEY_SCREEN_TYPE, mScreenType);
		hashtable.put(KEY_LANGUAGE, mLocaleCode);
		hashtable.put(DEVICE_ID,deviceId);
		hashtable.put(AppConstants.KEY_PAGE_REVIEW, screenName);
		
		return hashtable;
	}
	
	public Hashtable<String, String> getHeadersReviewListRequest(int pageNumber, String compId, String catId) {
		Hashtable<String, String> hashtable = new Hashtable<String, String>();
		hashtable.put(KEY_APP_KEY, VALUE_APP_KEY);
		hashtable.put(KEY_PLATFORM, VALUE_PLATFORM);
		hashtable.put(KEY_SCREEN_TYPE, mScreenType);
		hashtable.put(KEY_LANGUAGE, mLocaleCode);
		hashtable.put(KEY_LANGUAGE, mLocaleCode);
		hashtable.put(KEY_PAGE_NUMBER, String.valueOf(pageNumber));
		hashtable.put(KEY_COMP_ID, compId);
		hashtable.put(KEY_CAT_ID, catId);
		hashtable.put(DEVICE_ID,deviceId);
		hashtable.put(AppConstants.KEY_PAGE_REVIEW,AppConstants.Company_detail);
		return hashtable;
	}
	
	public String getDefaultParameterString(String ScreenName){
		String str="?"+KEY_PLATFORM+"="+VALUE_PLATFORM+"&"+KEY_LANGUAGE+"="+mLocaleCode+"&"+KEY_APP_KEY+"="+VALUE_APP_KEY+"&" +MaxisBaseRequest.DEVICE_ID + "=" + deviceId+""+"&"+AppConstants.KEY_PAGE_REVIEW+"="+ScreenName+"";
		return str;
	}
	public Hashtable<String, String> getDefaultHeadersWithGPS() {
		Hashtable<String, String> hashtable = new Hashtable<String, String>();
		hashtable.put(KEY_APP_KEY, VALUE_APP_KEY);
		hashtable.put(KEY_PLATFORM, VALUE_PLATFORM);
		hashtable.put(KEY_SCREEN_TYPE, mScreenType);
		hashtable.put(KEY_LANGUAGE, mLocaleCode);
		hashtable.put(KEY_LATITUDE, GPS_Data.getLatitude()+"");
		hashtable.put(KEY_LONGITUDE, GPS_Data.getLongitude()+"");
		hashtable.put(DEVICE_ID,deviceId);
		return hashtable;
	}
	
	public Hashtable<String, String> getDefaultHeadersWithGPSandPageView() {
		Hashtable<String, String> hashtable = new Hashtable<String, String>();
		hashtable.put(KEY_APP_KEY, VALUE_APP_KEY);
		hashtable.put(KEY_PLATFORM, VALUE_PLATFORM);
		hashtable.put(KEY_SCREEN_TYPE, mScreenType);
		hashtable.put(KEY_LANGUAGE, mLocaleCode);
		hashtable.put(KEY_LATITUDE, GPS_Data.getLatitude()+"");
		hashtable.put(KEY_LONGITUDE, GPS_Data.getLongitude()+"");
		hashtable.put(DEVICE_ID,deviceId);
		hashtable.put(AppConstants.KEY_PAGE_REVIEW,AppConstants.Home_Screen);
		return hashtable;
	}
	
	
	public Hashtable<String, String> getHeadersForRefineOutlets(String screenName , String deal_id ,String cityName ,String localityName) {
		Hashtable<String, String> hashtable = new Hashtable<String, String>();
		hashtable.put(KEY_APP_KEY, VALUE_APP_KEY);
		hashtable.put(KEY_PLATFORM, VALUE_PLATFORM);
		hashtable.put(KEY_SCREEN_TYPE, mScreenType);
		hashtable.put(KEY_LANGUAGE, mLocaleCode);
		hashtable.put(DEVICE_ID,deviceId);
		hashtable.put(AppConstants.KEY_PAGE_REVIEW, screenName);
		hashtable.put(KEY_DEAL_ID, deal_id);
		
		if(!StringUtil.isNullOrEmpty(cityName))
		{
			hashtable.put("city", cityName);
		}
		
		if(!StringUtil.isNullOrEmpty(localityName))
		{
			hashtable.put("locality", localityName);
		}
		
		return hashtable;
	}
	
	
	
	public abstract Hashtable getRequestHeaders(String screenName);

	public String getLocaleCode() {
		return mLocaleCode;
	}

	public void setLocaleCode(String localeCode) {
		this.mLocaleCode = localeCode;
	}
}
