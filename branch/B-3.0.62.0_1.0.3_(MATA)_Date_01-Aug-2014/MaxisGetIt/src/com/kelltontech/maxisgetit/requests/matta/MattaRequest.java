package com.kelltontech.maxisgetit.requests.matta;


import java.util.Hashtable;

import android.content.Context;

import com.kelltontech.framework.utils.StringUtil;
import com.kelltontech.maxisgetit.constants.AppConstants;
import com.kelltontech.maxisgetit.constants.matta.MattaConstants;
import com.kelltontech.maxisgetit.dao.CatgoryBase;
import com.kelltontech.maxisgetit.requests.MaxisBaseRequest;

/**
 * @author arsh.vardhan
 * @modified 02-Aug-2014
 */
public class MattaRequest extends MaxisBaseRequest {

	public static final String MATTA_CATEGORY_METHOD 			= "getGroupCategoryData.xml";
	public static final String MATTA_BOOTH_LIST_METHOD 			= "getBooths.json";
	public static final String MATTA_BOOTH_DETAIL_METHOD 		= "viewPackageCompany.json";
	public static final String MATTA_PACKAGE_LIST_METHOD 		= "getPackages.json";
	public static final String MATTA_PACKAGE_DETAIL_METHOD 		= "viewPackage.json";
	public static final String MATTA_FILTER_SEARCH_METHOD 		= "getSearchAttributes.json";

	public static final String KEY_HALL_ID 						= "hall_id";
	public static final String KEY_COMPANY_ID 					= "company_id";
	public static final String KEY_PACKAGE_ID 					= "package_id";
	public static final String KEY_PAGE_NUMBER 					= "page_number";
	public static final String KEY_SEARCH_ID 					= "id";
	public static final String KEY_SEARCH_TYPE 					= "type";

	public MattaRequest(Context context) {
		super(context);
	}

	public Hashtable<String, String> getHallListingHeaders(CatgoryBase cat) {

		Hashtable<String, String> ht = getDefaultHeadersWithGPS();
		ht.put(KEY_CATEGORY_ID, cat.getCategoryId());
		ht.put(KEY_GROUP_ACTION_TYPE, cat.getmGroupActionType());
		ht.put(KEY_GROUP_TYPE, cat.getMgroupType());
		ht.put(AppConstants.KEY_PAGE_REVIEW, MattaConstants.Matta_Hall_Listing);

		return ht;
	}

	public Hashtable<String, String> getBoothListingHeaders(MattaBoothListRequest boothList) {

		Hashtable<String, String> ht = getDefaultHeadersWithGPS();
		ht.put(KEY_HALL_ID, boothList.getHallId());
		ht.put(KEY_PAGE_NUMBER, boothList.getPageNumber() + "");
		ht.put(AppConstants.KEY_PAGE_REVIEW, MattaConstants.Matta_Booth_Listing);

		return ht;
	}

	public Hashtable<String, String> getBoothDetailHeaders(MattaBoothDetailRequest boothDetail) {

		Hashtable<String, String> ht = getDefaultHeadersWithGPS();
		ht.put(KEY_COMPANY_ID, boothDetail.getCompanyId());
		ht.put(AppConstants.KEY_PAGE_REVIEW, MattaConstants.Matta_Booth_Detail);

		return ht;
	}

	public Hashtable<String, String> getPackageListingHeaders(MattaPackageListRequest packageList) {

		Hashtable<String, String> ht = getDefaultHeadersWithGPS();
		if(!StringUtil.isNullOrEmpty(packageList.getCompanyId()))
			ht.put(KEY_COMPANY_ID, packageList.getCompanyId());
		ht.put(KEY_PAGE_NUMBER, packageList.getPageNumber() + "");
		ht.put(AppConstants.KEY_PAGE_REVIEW, MattaConstants.Matta_Package_Listing);

		return ht;
	}

	public Hashtable<String, String> getPackageDetailHeaders(MattaPackageDetailRequest packageDetail) {

		Hashtable<String, String> ht = getDefaultHeadersWithGPS();
		ht.put(KEY_PACKAGE_ID, packageDetail.getPackageId());
		ht.put(AppConstants.KEY_PAGE_REVIEW, MattaConstants.Matta_Package_Detail);

		return ht;
	}

	public Hashtable<String, String> getFilterSearchHeaders(MattaFilterSearchRequest filterSearch) {

		Hashtable<String, String> ht = getDefaultHeadersWithGPS();
		ht.put(KEY_SEARCH_ID, filterSearch.getId());
		ht.put(KEY_SEARCH_TYPE, filterSearch.getsearchType() + "");
		ht.put(AppConstants.KEY_PAGE_REVIEW, MattaConstants.Matta_Filter_Search);

		return ht;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Hashtable getRequestHeaders(String screenName) {
		return null;
	}
}
