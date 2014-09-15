package com.kelltontech.maxisgetit.requests;

import java.util.Hashtable;

import android.content.Context;

import com.kelltontech.framework.db.MyApplication;
import com.kelltontech.framework.utils.StringUtil;
import com.kelltontech.maxisgetit.constants.AppConstants;
import com.kelltontech.maxisgetit.dao.CatgoryBase;
import com.kelltontech.maxisgetit.dao.GPS_Data;

public class GenralRequest extends MaxisBaseRequest {
	public static final String SESSION_TOKEN_METHOD = "getSessionToken.xml";
	public static final String TYPE_BY_CATEGORY_Method = "getTypeByCategory.xml";
	public static final String TEMPLET_Method = "getTemplate.xml";
	public static final String COMPANY_lEADS_Method = "myLeads.xml";
	public static final String MY_DEAL_LIST_Method = "myDeals.xml";
	public static final String MY_DEAL_CATEGORIES_Method = "myDeals.xml";
	public static final String Root_Category_Method = "getCategory.xml";
	public static final String REFINE_SEARCH_LOCALITY_METHOD = "getSolrLocalityListing.xml";
	// public static final String CATEGORY_METHOD = "getCategory.xml";
	public static final String CATEGORY_METHOD = "getGroupCategoryData.xml";

	public static final String REFINE_ATTRI_METHOD = "attributeRefine.xml";
	public static final String lOCAL_SEARCH_METHOD = "localSearch.xml";
	public static final String CLASSIFIED_LIST_METHOD = "getClassifieds.xml";
	public static final String CITY_LIST_METHOD = "getCities.xml";
	public static final String AREA_LIST_METHOD = "getLocality.xml";
	public static final String VERIFY_REGISTRATION_METHOD = "verifyMobile.xml";
	public static final String LOGIN_METHOD = "login.xml";
	public static final String USER_DETAIL_METHOD = "userDetail.xml";
	public static final String POST_CLASSIFIED_METHOD = "postClassified.xml";
	// public static final String POST_DEAL_METHOD = "postDeal.xml";
	public static final String POST_DEAL_METHOD = "saveDeal.xml";
	public static final String EDIT_PROFILE_METHOD = "editProfile.xml";
	public static final String APP_ACTIVATION_METHOD = "getAppActivationCode.xml";
	public static final String APP_ACTIVATION_VERIFICATION_METHOD = "appActivation.xml";
	public static final String ERROR_MENDATORY_FIELDS = "getModeratorErrorStatus.xml";
	public static final String SAVE_ERROR_REPORT = "saveErrorReport.xml";
	public static final String POST_REVIEW = "addReviewRating.xml";
	public static final String COMPANY_REVIEWS = "companyReviews.xml";
	public static final String FORGET_PASSWORD = "forgotPassword.xml";
	public static final String COMPANY_DETAIL_ADD_FAV_METHOD = "saveUserFavoriteCompany.xml";
	public static final String COMPANY_DETAIL_REMOVE_FAV_METHOD = "removeFavoriteCompany.xml";
	public static final String FAV_COMP_LIST = "userFavoriteCompanyList.xml";
	public static final String POST_IMAGE_METHOD = "saveImage.xml";

	public static final String POST_DEAL_CITY_LIST_METHOD = "getCityBYCatandCompanyID.xml";
	public static final String POST_DEAL_LOCALITY_LIST_METHOD = "getLiocalityNameByCityName.xml";
	public static final String REMOVE_POST_IMAGE_METHOD = "deleteDealImage.xml";

	public static final String OUTLET_DETAIL_METHOD = "getOutletsyByDealandcid.xml";
	public static final String DEAL_DOWNLOAD_METHOD = "sendSmsbyuidanddealid.xml";

	public static final String CITY_LISTING_DEALS_METHOD = "getDealCities.json"; // it
	// will
	// also
	// available
	// in
	// xml.
	public static final String LOCALITY_LISTING_DEALS_METHOD = "getDealLocalityByCity.json";
	public static final String MY_ACCOUNT_DASHBOARD = "myaccount.json";
	public static final String MY_ACCOUNT_DASHBOARD_LIFECYCLE = "mydashboard.json";
	public static final String BANNER_NAVIGATION_METHOD = "logBannerReport.json";
	public static final String PAID_COMPANY_LIST_METHOD = "getAssociatedPaidCompanies.json";

	private String deviceId = MyApplication.getDeviceId();

	private static final String KEY_APP_CODE = "app_code";

	public GenralRequest(Context context) {
		super(context);
	}

	public Hashtable<String, String> getCategoryheaders(String categoryId) {
		Hashtable<String, String> ht = getDefaultHeadersWithGPS();
		ht.put(KEY_CATEGORY_ID, categoryId);
		ht.put(MaxisBaseRequest.DEVICE_ID, deviceId + "");
		ht.put(AppConstants.KEY_PAGE_REVIEW, AppConstants.Template_Sell_Buy);
		return ht;
	}

	public Hashtable<String, String> getCategoryheadersForRefine(String categoryId, String ScreenName, String keywordForSearch, String searchIn, String stampId) {
		Hashtable<String, String> ht = getDefaultHeadersWithGPS();
		ht.put(KEY_CATEGORY_ID, categoryId);
		ht.put(MaxisBaseRequest.DEVICE_ID, deviceId + "");
		ht.put(AppConstants.KEY_PAGE_REVIEW, ScreenName);
		if (!StringUtil.isNullOrEmpty(keywordForSearch))
			ht.put("keyword", keywordForSearch+"");
		if (!StringUtil.isNullOrEmpty(searchIn))
			ht.put(KEY_SEARCH_RESULTS_IN, searchIn);
		if (!StringUtil.isNullOrEmpty(stampId))
			ht.put(KEY_STAMP_COMPANY_ID, stampId);
		return ht;
	}

	public Hashtable<String, String> getSubCategoryheaders(CatgoryBase cat, boolean isForDeal) {
		Hashtable<String, String> ht = getDefaultHeadersWithGPS();
		ht.put(KEY_CATEGORY_ID, cat.getCategoryId());
		ht.put(KEY_GROUP_ACTION_TYPE, cat.getmGroupActionType());
		ht.put(KEY_GROUP_TYPE, cat.getMgroupType());
		ht.put(MaxisBaseRequest.DEVICE_ID, deviceId + "");

		if (isForDeal) {
			ht.put(AppConstants.KEY_PAGE_REVIEW, AppConstants.Deal_Listing);
		} else {
			ht.put(AppConstants.KEY_PAGE_REVIEW, AppConstants.Category_Screen);
		}

		return ht;
	}

	public Hashtable<String, String> getVerifyRegistrationHeaders(
			String verificationCode, String mobile) {
		Hashtable<String, String> ht = getDefaultHeaders(AppConstants.Home_Screen);
		ht.put(KEY_VERIFY_CODE, verificationCode);
		ht.put(KEY_MOBILE, mobile);
		ht.put(MaxisBaseRequest.DEVICE_ID, deviceId + "");
		return ht;
	}

	public Hashtable<String, String> getSaleTemplateHeaders(String categoryId,
			String templateType) {
		Hashtable<String, String> ht = getDefaultHeadersWithGPS();
		ht.put(KEY_CATEGORY_ID, categoryId);
		ht.put(KEY_TEMPLATE_TYPE, templateType);
		ht.put(MaxisBaseRequest.DEVICE_ID, deviceId + "");
		ht.put(AppConstants.KEY_PAGE_REVIEW, AppConstants.Template_Sell_Buy);
		return ht;
	}

	public Hashtable<String, String> getCompanyDetailRemoveFavHeader(
			String userId, String companyId, String categoryId) {
		Hashtable<String, String> ht = getDefaultHeadersWithGPS();
		ht.put(KEY_USER_ID, userId);
		ht.put(KEY_COMPANY_ID, companyId);
		ht.put(KEY_CATEGORY_ID, categoryId);
		ht.put(MaxisBaseRequest.DEVICE_ID, deviceId + "");
		return ht;
	}

	public Hashtable<String, String> getPostDealCityListHeader(String userId,
			String companyId, String categoryId) {
		Hashtable<String, String> ht = getDefaultHeadersWithGPS();
		ht.put(KEY_UID, userId);
		ht.put(KEY_COMP_ID, companyId);
		ht.put(KEY_POST_DEAL_CATEGORY_ID, categoryId);
		ht.put(MaxisBaseRequest.DEVICE_ID, deviceId + "");
		ht.put(AppConstants.KEY_PAGE_REVIEW, AppConstants.Deal_Post);

		return ht;
	}

	public Hashtable<String, String> getPostDealRemoveImgHeader(String imageName) {
		Hashtable<String, String> ht = getDefaultHeadersWithGPS();
		ht.put(KEY_POST_DEAL_REMOVE_IMG_NAME, imageName);
		ht.put(MaxisBaseRequest.DEVICE_ID, deviceId + "");
		ht.put(AppConstants.KEY_PAGE_REVIEW, AppConstants.Deal_Post);
		return ht;
	}

	// public Hashtable<String, String> getEditProfileHeaders(String userName,
	// String mobile, String currPass, String newPass) {
	// Hashtable<String, String> ht = getDefaultHeaders();
	// ht.put(KEY_NAME, userName);
	// ht.put(KEY_MOBILE, mobile);
	// ht.put(KEY_PASSWD, currPass);
	// ht.put(KEY_NEW_PASS, newPass);
	// return ht;
	// }

	public Hashtable<String, String> getLoginRequestHeaders(String mobile,
			String passwd, String screenName) {
		Hashtable<String, String> ht = getDefaultHeaders(screenName);
		ht.put(KEY_MOBILE, mobile);
		ht.put(KEY_PASSWD, passwd);
		ht.put(MaxisBaseRequest.DEVICE_ID, deviceId + "");

		return ht;
	}

	public Hashtable<String, String> getAppActivationHeaders(String mobile,
			String uuid, String appVersion) {
		Hashtable<String, String> ht = getDefaultHeaders(AppConstants.AppAuthenticationCode);
		ht.put(KEY_MOBILE_NUM, mobile);
		ht.put(KEY_IMSI, uuid);
		ht.put(MaxisBaseRequest.DEVICE_ID, deviceId + "");
		if (appVersion != null)
			ht.put(KEY_HANDSET_INFO, appVersion);
		return ht;
	}

	public Hashtable<String, String> getAppActiVerificationHeaders(
			String mobile, String verificationCode) {
		Hashtable<String, String> ht = getDefaultHeaders(AppConstants.TnC);
		ht.put(KEY_MOBILE_NUM, mobile);
		ht.put(KEY_APP_CODE, verificationCode);
		ht.put(MaxisBaseRequest.DEVICE_ID, deviceId + "");
		return ht;
	}

	public Hashtable<String, String> getHeadersWithMobile(String mobile,
			String screenName) {
		Hashtable<String, String> ht = getDefaultHeaders(screenName);
		ht.put(KEY_MOBILE, mobile);
		ht.put(MaxisBaseRequest.DEVICE_ID, deviceId + "");

		return ht;
	}

	public Hashtable<String, String> getHeadersWithUID(String uId,
			String screenName) {
		Hashtable<String, String> ht = getDefaultHeaders(screenName);
		ht.put(MaxisBaseRequest.DEVICE_ID, deviceId + "");
		ht.put(KEY_UID, uId);
		return ht;
	}

	public Hashtable<String, String> getHeadersWithBannerId(String bannerId, String screenName) {
		Hashtable<String, String> ht = getDefaultHeaders(screenName);
		ht.put(MaxisBaseRequest.DEVICE_ID, deviceId + "");
		ht.put(KEY_BANNER_ID, bannerId + "");
		return ht;
	}

	public Hashtable<String, String> getHeadersWithUIDandPageNo(String uId,
			String pageNo) {
		Hashtable<String, String> ht = getDefaultHeaders(AppConstants.MyFavourite);
		ht.put(KEY_UID, uId);
		ht.put("page_number", pageNo);
		ht.put(MaxisBaseRequest.KEY_LATITUDE, GPS_Data.getLatitude() + "");
		ht.put(MaxisBaseRequest.KEY_LONGITUDE, GPS_Data.getLongitude() + "");
		ht.put(MaxisBaseRequest.DEVICE_ID, deviceId + "");
		return ht;
	}

	public Hashtable<String, String> getHeadersWithCompanyID(String cId) {
		Hashtable<String, String> ht = getDefaultHeaders(AppConstants.MyCompany);
		ht.put(KEY_COMPANY_ID, cId);
		ht.put(MaxisBaseRequest.DEVICE_ID, deviceId + "");
		return ht;
	}

	public Hashtable<String, String> getHeadersWithCompanyIDndDealID(
			String cId, String dealId, String l3catId, int pageNumber,
			String cityName, String localityName) {
		Hashtable<String, String> ht = getDefaultHeaders(AppConstants.Deal_Detail);
		ht.put(KEY_COMP_ID, cId);
		ht.put(KEY_DEAL_ID, dealId);
		ht.put(KEY_L3CAT_ID, l3catId);
		ht.put(MaxisBaseRequest.KEY_LATITUDE, GPS_Data.getLatitude() + "");
		ht.put(MaxisBaseRequest.KEY_LONGITUDE, GPS_Data.getLongitude() + "");
		ht.put(MaxisBaseRequest.DEVICE_ID, deviceId + "");
		ht.put("page_number", pageNumber + "");
		if (!StringUtil.isNullOrEmpty(cityName)) {
			ht.put("city", cityName);
		}
		if (!StringUtil.isNullOrEmpty(localityName)) {
			ht.put("locality", localityName);
		}
		return ht;
	}

	public Hashtable<String, String> getUserDetailHeaders(String mobile) {
		Hashtable<String, String> ht = getDefaultHeaders(AppConstants.MyAccount);
		ht.put(MaxisBaseRequest.DEVICE_ID, deviceId + "");
		ht.put(KEY_MOBILE, mobile);
		return ht;
	}

	public String getUrlAppendableDefaultHeaders() {
		StringBuffer ht = new StringBuffer();
		ht.append("?" + MaxisBaseRequest.KEY_APP_KEY + "="
				+ MaxisBaseRequest.VALUE_APP_KEY);
		ht.append("&" + MaxisBaseRequest.KEY_PLATFORM + "="
				+ MaxisBaseRequest.VALUE_PLATFORM);
		ht.append("&" + MaxisBaseRequest.KEY_SCREEN_TYPE + "=" + mScreenType);
		ht.append("&" + MaxisBaseRequest.KEY_LANGUAGE + "=" + getLocaleCode());
		ht.append("&" + MaxisBaseRequest.KEY_LATITUDE + "="
				+ GPS_Data.getLatitude() + "");
		ht.append("&" + MaxisBaseRequest.KEY_LONGITUDE + "="
				+ GPS_Data.getLongitude() + "");
		ht.append("&" + MaxisBaseRequest.DEVICE_ID + "=" + deviceId + "");
		return ht.toString();
	}

	@Override
	public Hashtable getRequestHeaders(String screenName) {
		return null;
	}

	public Hashtable getAreaHeaders(String cityId) {
		Hashtable<String, String> ht = getDefaultHeaders(AppConstants.Locality_Screen);
		ht.put(KEY_CITY_ID, cityId);
		ht.put(MaxisBaseRequest.DEVICE_ID, deviceId + "");
		return ht;
	}

	public Hashtable<String, String> getDownloadDealHeaders(String name,
			String number, String dealId, String source, String screenName) {
		Hashtable<String, String> ht = getDefaultHeaders(screenName);
		ht.put(KEY_NAME, name);
		ht.put(KEY_NUMBER, number);
		ht.put(KEY_DEAL_ID, dealId);
		ht.put(KEY_DEAL_SOURCE, source);
		ht.put(MaxisBaseRequest.DEVICE_ID, deviceId + "");
		return ht;
	}

	public Hashtable<String, String> getHeadersWithCompIdcatId(String compId,
			String catId) {
		Hashtable<String, String> ht = getDefaultHeaders(AppConstants.DashBoardLifeCycle);
		ht.put(KEY_COMP_ID, compId);
		ht.put(KEY_POST_DEAL_CATEGORY_ID, catId);
		ht.put(MaxisBaseRequest.DEVICE_ID, deviceId + "");
		return ht;
	}

	public Hashtable<String, String> getPaidCompListHeaders(PaidCompanyListRequest request) {
		Hashtable<String, String> ht = getDefaultHeadersWithGPS();
		ht.put(AppConstants.KEY_PAGE_REVIEW, AppConstants.PaidCompanyListScreen);
		ht.put(KEY_L3CATEGORY_ID, request.getCategoryId() + "");
		ht.put(KEY_PAID_CATEGORY_ID, request.getCategoryId() + "");
		ht.put(KEY_PER_PAGE_RECORD, request.getPerPageRecord() + "");
		ht.put(KEY_PAGE_NUMBER, request.getPageNumber() + "");
		return ht;
	}
}
