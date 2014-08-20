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
 * @modified 20-Aug-2014
 */
public class MattaRequest extends MaxisBaseRequest {

	public static final String MATTA_CATEGORY_METHOD 			= "getGroupCategoryData.xml";
	public static final String MATTA_BOOTH_LIST_METHOD 			= "getBooths.json";
	public static final String MATTA_BOOTH_DETAIL_METHOD 		= "viewPackageCompany.json";
	public static final String MATTA_PACKAGE_LIST_METHOD 		= "getPackages.json";
	public static final String MATTA_PACKAGE_DETAIL_METHOD 		= "viewPackage.json";
	public static final String MATTA_FILTER_SEARCH_METHOD 		= "getSearchAttributes.xml";

	public static final String KEY_HALL_ID 						= "hall_id";
	public static final String KEY_COMPANY_ID 					= "company_id";
	public static final String KEY_PACKAGE_ID 					= "package_id";
	public static final String KEY_PAGE_NUMBER 					= "page_number";
	public static final String KEY_SEARCH_ID 					= "id";
	public static final String KEY_SEARCH_TYPE 					= "type";
	public static final String KEY_SOURCE 						= "source";

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
		ht.put(KEY_HALL_ID, !StringUtil.isNullOrEmpty(boothList.getHallId()) ? boothList.getHallId() : "");
		ht.put(KEY_PAGE_NUMBER, boothList.getPageNumber() + "");
		ht.put(KEY_SOURCE, !StringUtil.isNullOrEmpty(boothList.getSource()) ? boothList.getSource() : "");
		ht.put(AppConstants.KEY_PAGE_REVIEW, MattaConstants.Matta_Booth_Listing);
		return ht;
	}

	public Hashtable<String, String> getBoothDetailHeaders(MattaBoothDetailRequest boothDetail) {

		Hashtable<String, String> ht = getDefaultHeadersWithGPS();
		ht.put(KEY_COMPANY_ID, !StringUtil.isNullOrEmpty(boothDetail.getCompanyId()) ? boothDetail.getCompanyId() : "");
		ht.put(KEY_SOURCE, !StringUtil.isNullOrEmpty(boothDetail.getSource()) ? boothDetail.getSource() : "");
		ht.put(KEY_HALL_ID, !StringUtil.isNullOrEmpty(boothDetail.getHallId()) ? boothDetail.getHallId() : "");
		ht.put(AppConstants.KEY_PAGE_REVIEW, MattaConstants.Matta_Booth_Detail);
		return ht;
	}

	public Hashtable<String, String> getPackageListingHeaders(MattaPackageListRequest packageList) {

		Hashtable<String, String> ht = getDefaultHeadersWithGPS();
		ht.put(KEY_COMPANY_ID, !StringUtil.isNullOrEmpty(packageList.getCompanyId()) ? packageList.getCompanyId() : "");
		ht.put(KEY_PAGE_NUMBER, packageList.getPageNumber() + "");
		ht.put(KEY_SOURCE, !StringUtil.isNullOrEmpty(packageList.getSource()) ? packageList.getSource() : "");
		ht.put(KEY_HALL_ID, !StringUtil.isNullOrEmpty(packageList.getHallId()) ? packageList.getHallId() : "");
		ht.put(AppConstants.KEY_PAGE_REVIEW, MattaConstants.Matta_Package_Listing);
		return ht;
	}

	public Hashtable<String, String> getPackageDetailHeaders(MattaPackageDetailRequest packageDetail) {

		Hashtable<String, String> ht = getDefaultHeadersWithGPS();
		ht.put(KEY_PACKAGE_ID, !StringUtil.isNullOrEmpty(packageDetail.getPackageId()) ? packageDetail.getPackageId() : "");
		ht.put(KEY_SOURCE, !StringUtil.isNullOrEmpty(packageDetail.getSource()) ? packageDetail.getSource() : "");
		ht.put(AppConstants.KEY_PAGE_REVIEW, MattaConstants.Matta_Package_Detail);

		return ht;
	}

	public Hashtable<String, String> getFilterSearchHeaders(MattaFilterSearchRequest filterSearch) {

		Hashtable<String, String> ht = getDefaultHeadersWithGPS();
		ht.put(KEY_SEARCH_ID, !StringUtil.isNullOrEmpty(filterSearch.getId()) ? filterSearch.getId() : "");
		ht.put(KEY_SEARCH_TYPE, !StringUtil.isNullOrEmpty(filterSearch.getsearchType()) ? filterSearch.getsearchType() : "");
		ht.put(KEY_SOURCE, !StringUtil.isNullOrEmpty(filterSearch.getSource()) ? filterSearch.getSource() : "");
		ht.put(KEY_HALL_ID, !StringUtil.isNullOrEmpty(filterSearch.getHallId()) ? filterSearch.getHallId() : "");
		ht.put(AppConstants.KEY_PAGE_REVIEW, MattaConstants.Matta_Filter_Search);

		return ht;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Hashtable getRequestHeaders(String screenName) {
		return null;
	}
}