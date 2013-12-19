package com.kelltontech.maxisgetit.requests;

import java.util.Hashtable;

import android.content.Context;

import com.kelltontech.maxisgetit.dao.CatgoryBase;
import com.kelltontech.maxisgetit.dao.GPS_Data;

public class GenralRequest extends MaxisBaseRequest {
	public static final String SESSION_TOKEN_METHOD = "getSessionToken.xml";
	public static final String TEMPLET_Method = "getTemplate.xml";
	public static final String COMPANY_lEADS_Method = "myLeads.xml";
	public static final String MY_DEAL_LIST_Method = "myDeals.xml";
	public static final String MY_DEAL_CATEGORIES_Method = "myDeals.xml";
	public static final String Root_Category_Method = "getCategory.xml";
	public static final String REFINE_SEARCH_LOCALITY_METHOD = "getSolrLocalityListing.xml";
	//public static final String CATEGORY_METHOD = "getCategory.xml";
	public static final String CATEGORY_METHOD = "getGroupCategoryData.xml";
	
	public static final String REFINE_ATTRI_METHOD= "attributeRefine.xml";
	public static final String lOCAL_SEARCH_METHOD="localSearch.xml";
	public static final String CLASSIFIED_LIST_METHOD="getClassifieds.xml";
	public static final String CITY_LIST_METHOD="getCities.xml";
	public static final String AREA_LIST_METHOD="getLocality.xml";
	public static final String VERIFY_REGISTRATION_METHOD = "verifyMobile.xml";
	public static final String LOGIN_METHOD = "login.xml";
	public static final String USER_DETAIL_METHOD = "userDetail.xml";
	public static final String POST_CLASSIFIED_METHOD = "postClassified.xml";
	public static final String POST_DEAL_METHOD = "postDeal.xml";
	public static final String EDIT_PROFILE_METHOD="editProfile.xml";
	private static final String KEY_COMPANY_ID = "company_id";
	public static final String APP_ACTIVATION_METHOD="getAppActivationCode.xml";
	public static final String APP_ACTIVATION_VERIFICATION_METHOD = "appActivation.xml";
	public static final String ERROR_MENDATORY_FIELDS = "getModeratorErrorStatus.xml";
	public static final String SAVE_ERROR_REPORT="saveErrorReport.xml";
	public static final String POST_REVIEW="addReviewRating.xml";
	public static final String COMPANY_REVIEWS="companyReviews.xml";
	public static final String FORGET_PASSWORD="forgotPassword.xml";
	
	public static final String FAV_COMP_LIST = "userFavoriteCompanyList.xml";
	
	private static final String KEY_APP_CODE = "app_code";
	public GenralRequest(Context context) {
		super(context);
	}

	public Hashtable<String, String> getCategoryheaders(String categoryId) {
		Hashtable<String, String> ht = getDefaultHeadersWithGPS();
		ht.put(KEY_CATEGORY_ID, categoryId);
		return ht;
	}
	
	public Hashtable<String, String> getSubCategoryheaders(CatgoryBase cat) {
		Hashtable<String, String> ht = getDefaultHeadersWithGPS();
		ht.put(KEY_CATEGORY_ID, cat.getCategoryId());
		ht.put(KEY_GROUP_ACTION_TYPE, cat.getmGroupActionType());
		ht.put(KEY_GROUP_TYPE, cat.getMgroupType());
		return ht;
	}

	public Hashtable<String, String> getVerifyRegistrationHeaders(String verificationCode, String mobile) {
		Hashtable<String, String> ht = getDefaultHeaders();
		ht.put(KEY_VERIFY_CODE, verificationCode);
		ht.put(KEY_MOBILE, mobile);
		return ht;
	}

//	public Hashtable<String, String> getEditProfileHeaders(String userName, String mobile, String currPass, String newPass) {
//		Hashtable<String, String> ht = getDefaultHeaders();
//		ht.put(KEY_NAME, userName);
//		ht.put(KEY_MOBILE, mobile);
//		ht.put(KEY_PASSWD, currPass);
//		ht.put(KEY_NEW_PASS, newPass);
//		return ht;
//	}

	public Hashtable<String, String> getLoginRequestHeaders(String mobile, String passwd) {
		Hashtable<String, String> ht = getDefaultHeaders();
		ht.put(KEY_MOBILE, mobile);
		ht.put(KEY_PASSWD, passwd);
		return ht;
	}
	public Hashtable<String, String> getAppActivationHeaders(String mobile,String uuid,String appVersion){
		Hashtable<String, String> ht = getDefaultHeaders();
		ht.put(KEY_MOBILE_NUM, mobile);
		ht.put(KEY_IMSI, uuid);
		if(appVersion!=null)
			ht.put(KEY_HANDSET_INFO, appVersion);
		return ht;
	}
	public Hashtable<String, String> getAppActiVerificationHeaders(String mobile,String verificationCode){
		Hashtable<String, String> ht = getDefaultHeaders();
		ht.put(KEY_MOBILE_NUM, mobile);
		ht.put(KEY_APP_CODE, verificationCode);
		return ht;
	}
	public Hashtable<String, String> getHeadersWithMobile(String mobile) {
		Hashtable<String, String> ht = getDefaultHeaders();
		ht.put(KEY_MOBILE, mobile);
		return ht;
	}
	public Hashtable<String, String> getHeadersWithUID(String uId) {
		Hashtable<String, String> ht = getDefaultHeaders();
		ht.put(KEY_UID, uId);
		return ht;
	}
	public Hashtable<String, String> getHeadersWithUIDandPageNo(String uId , String pageNo) {
		Hashtable<String, String> ht = getDefaultHeaders();
		ht.put(KEY_UID, uId);
		ht.put("page_number", pageNo);
		return ht;
	}
	
	public Hashtable<String, String> getHeadersWithCompanyID(String cId) {
		Hashtable<String, String> ht = getDefaultHeaders();
		ht.put(KEY_COMPANY_ID, cId);
		return ht;
	}
	public Hashtable<String, String> getUserDetailHeaders(String mobile) {
		Hashtable<String, String> ht = getDefaultHeaders();
		ht.put(KEY_MOBILE, mobile);
		return ht;
	}
	public String getUrlAppendableDefaultHeaders() {
		StringBuffer ht =new StringBuffer();
		ht.append("?"+MaxisBaseRequest.KEY_APP_KEY+"="+MaxisBaseRequest.VALUE_APP_KEY);
		ht.append("&"+MaxisBaseRequest.KEY_PLATFORM+"="+ MaxisBaseRequest.VALUE_PLATFORM);
		ht.append("&"+MaxisBaseRequest.KEY_SCREEN_TYPE+"="+ mScreenType);
		ht.append("&"+MaxisBaseRequest.KEY_LANGUAGE+"="+ getLocaleCode());
		ht.append("&"+MaxisBaseRequest.KEY_LATITUDE+"="+ GPS_Data.getLatitude() + "");
		ht.append("&"+MaxisBaseRequest.KEY_LONGITUDE+"="+ GPS_Data.getLongitude() + "");
		return ht.toString();
	}
	@Override
	public Hashtable getRequestHeaders() {
		return null;
	}

	public Hashtable getAreaHeaders(String cityId) {
		Hashtable<String, String> ht = getDefaultHeaders();
		ht.put(KEY_CITY_ID, cityId);
		return ht;		
	}

}
