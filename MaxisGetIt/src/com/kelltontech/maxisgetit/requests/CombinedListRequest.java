package com.kelltontech.maxisgetit.requests;

import java.util.Hashtable;

import com.kelltontech.framework.db.MyApplication;
import com.kelltontech.framework.utils.NativeHelper;
import com.kelltontech.framework.utils.StringUtil;
import com.kelltontech.maxisgetit.constants.AppConstants;
import com.kelltontech.maxisgetit.dao.GPS_Data;
import com.kelltontech.maxisgetit.dao.MaxisStore;
import android.content.Context;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

public class CombinedListRequest implements Parcelable {
	public static final String METHOD_CATEGORY_REFINE = "categoryRefine.xml";
	public static final String METHOD_REFINE_SEARCH_RES = "RefineSearch.xml";
	private static final String METHOD_COMPANY_LIST_BY_SEARCH = "search.xml";
	private static final String METHOD_DEAL_LIST_BY_SEARCH = "getGroupCategoryData.xml";// "getDeal.xml";//"search.xml";
	private static final String METHOD_COMPANY_LIST_BY_CATEGORY = "getGroupCategoryData.xml";// "getCompany.xml";
	// private static final String METHOD_DEAL_LIST_BY_CATEGORY =
	// "getGroupCategoryData.xml";//"getDeal.xml-- previous one";

	private static final String METHOD_DEAL_LIST_BY_CATEGORY = "getHotDeals.xml";// "getDeal.xml";
	private static final String RESULT_DEAL = "DEAL";
	private static final String RESULT_COMP = "COMPANY";
	private static final String KEY_RESULT_TYPE = "type";
	public static final String KEY_GROUP_ACTION_TYPE = "action_type";
	public static final String KEY_SEARCH_DISTANCE = "search_distance";

	private int pageNumber = 1;
	private String keywordOrCategoryId;
	private String selectedCategoryBySearch;
	private String selectedCategoryNameBySearch;
	private String localeCode;
	private double latitude;
	private double longitude;
	private boolean isCompanyListing;
	private boolean isBySearch;
	private String parentThumbUrl;
	private String mScreenType;
	private String categoryTitle;
	private String postJsonPayload;
	private String groupType;
	private String groupActionType;
	private String deviceId = MyApplication.getDeviceId();

	private String search_distance = "";
	private String pageView;

	public CombinedListRequest(Parcel in) {
		pageNumber = in.readInt();
		keywordOrCategoryId = in.readString();
		localeCode = in.readString();
		mScreenType = in.readString();
		latitude = in.readDouble();
		longitude = in.readDouble();
		isCompanyListing = in.readInt() == 0 ? false : true;
		isBySearch = in.readInt() == 0 ? false : true;
		parentThumbUrl = in.readString();
		categoryTitle = in.readString();
		postJsonPayload = in.readString();
		selectedCategoryBySearch = in.readString();
		selectedCategoryNameBySearch = in.readString();
		groupType = in.readString();
		groupActionType = in.readString();
		search_distance = in.readString();
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(pageNumber);
		dest.writeString(keywordOrCategoryId);
		dest.writeString(localeCode);
		dest.writeString(mScreenType);
		dest.writeDouble(latitude);
		dest.writeDouble(longitude);
		dest.writeInt(isCompanyListing ? 1 : 0);
		dest.writeInt(isBySearch ? 1 : 0);
		dest.writeString(parentThumbUrl);
		dest.writeString(categoryTitle);
		dest.writeString(postJsonPayload);
		dest.writeString(selectedCategoryBySearch);
		dest.writeString(selectedCategoryNameBySearch);
		dest.writeString(groupType);
		dest.writeString(groupActionType);
		dest.writeString(search_distance);
	}

	public String getSearch_distance() {
		return search_distance;
	}

	public void setSearch_distance(String search_distance) {
		this.search_distance = search_distance;
	}

	public String getPostJsonPayload() {
		return postJsonPayload;
	}

	public void setPostJsonPayload(String postJsonPayload) {
		this.postJsonPayload = postJsonPayload;
	}

	public String getCategoryTitle() {
		return categoryTitle;
	}

	public void setCategoryTitle(String categoryTitle) {
		this.categoryTitle = categoryTitle;
	}

	public int getPageNumber() {
		return pageNumber;
	}

	public void setPageNumber(int pageNumber) {
		this.pageNumber = pageNumber;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public static String getRefineMethod() {
		return METHOD_REFINE_SEARCH_RES;
	}

	public String getRequestMethod() {
		if (postJsonPayload != null) {
			if (isBySearch()) {

				if (!StringUtil.isNullOrEmpty(selectedCategoryBySearch)
						&& !selectedCategoryBySearch.equalsIgnoreCase("-1"))
					return METHOD_REFINE_SEARCH_RES;
				else {
					return METHOD_COMPANY_LIST_BY_SEARCH;
				}
			} else {
				return METHOD_REFINE_SEARCH_RES;
			}
		}
		if (isBySearch()) {
			if (isCompanyListing)
				return METHOD_COMPANY_LIST_BY_SEARCH;
			else
				return METHOD_DEAL_LIST_BY_CATEGORY;
		} else {
			if (isCompanyListing)
				return METHOD_COMPANY_LIST_BY_CATEGORY;
			else
				return METHOD_DEAL_LIST_BY_CATEGORY;
		}
	}

	public String getUrlAppendableRequestHeaders() {
		StringBuffer ht = new StringBuffer();
		ht.append("?" + MaxisBaseRequest.KEY_APP_KEY + "="
				+ MaxisBaseRequest.VALUE_APP_KEY);
		ht.append("&" + MaxisBaseRequest.KEY_PLATFORM + "="
				+ MaxisBaseRequest.VALUE_PLATFORM);
		ht.append("&" + MaxisBaseRequest.KEY_SCREEN_TYPE + "=" + mScreenType);
		ht.append("&" + MaxisBaseRequest.KEY_LANGUAGE + "=" + localeCode);
		ht.append("&" + MaxisBaseRequest.KEY_LATITUDE + "="
				+ GPS_Data.getLatitude() + "");
		ht.append("&" + MaxisBaseRequest.KEY_LONGITUDE + "="
				+ GPS_Data.getLongitude() + "");

		ht.append("&" + MaxisBaseRequest.DEVICE_ID + "=" + deviceId + "");

		if (isBySearch) {
			ht.append("&" + "keyword" + "=" + keywordOrCategoryId);
			if (!StringUtil.isNullOrEmpty(selectedCategoryBySearch)
					&& !selectedCategoryBySearch.equalsIgnoreCase("-1"))
				ht.append("&" + "category_id" + "=" + selectedCategoryBySearch);

			ht.append("&" + AppConstants.KEY_PAGE_REVIEW + "="
					+ AppConstants.Company_listing + "");
			ht.append("&" + KEY_SEARCH_DISTANCE + "=" + search_distance);
			
		} else if (!StringUtil.isNullOrEmpty(groupActionType)
				&& groupActionType
						.equalsIgnoreCase(AppConstants.GROUP_ACTION_TYPE_CATEGORY_LIST_FOR_GROUP)
				&& !StringUtil.isNullOrEmpty(groupType)
				&& groupType.equalsIgnoreCase(AppConstants.GROUP_TYPE_CATEGORY)) {
			if (!StringUtil.isNullOrEmpty(selectedCategoryBySearch)
					&& !selectedCategoryBySearch.equalsIgnoreCase("-1"))
				ht.append("&" + "category_id" + "=" + selectedCategoryBySearch);

		} else
			
			ht.append("&" + "category_id" + "=" + keywordOrCategoryId);
		ht.append("&" + "page_number" + "=" + pageNumber + "");
		if (isCompanyListing) {
			ht.append("&" + KEY_RESULT_TYPE + "=" + RESULT_COMP);
			ht.append("&" + AppConstants.KEY_PAGE_REVIEW + "="
					+ AppConstants.Company_listing + "");
			ht.append("&" + KEY_SEARCH_DISTANCE + "=" + search_distance);
		} else {
			ht.append("&" + KEY_RESULT_TYPE + "=" + RESULT_DEAL);
			ht.append("&" + AppConstants.KEY_PAGE_REVIEW + "="
					+ AppConstants.Deal_Listing + "");
		}
		return ht.toString();
	}

	public Hashtable<String, String> getRequestHeaders() {
		Hashtable<String, String> ht = new Hashtable<String, String>();
		ht.put(MaxisBaseRequest.KEY_APP_KEY, MaxisBaseRequest.VALUE_APP_KEY);
		ht.put(MaxisBaseRequest.KEY_PLATFORM, MaxisBaseRequest.VALUE_PLATFORM);
		ht.put(MaxisBaseRequest.KEY_SCREEN_TYPE, mScreenType);
		ht.put(MaxisBaseRequest.KEY_LANGUAGE, localeCode);
		 ht.put(MaxisBaseRequest.KEY_LATITUDE, GPS_Data.getLatitude() + "");
		 ht.put(MaxisBaseRequest.KEY_LONGITUDE, GPS_Data.getLongitude() + "");

//		ht.put(MaxisBaseRequest.KEY_LATITUDE, "3.1574399471"); 
//		 ht.put(MaxisBaseRequest.KEY_LONGITUDE, "101.7149963379");

		ht.put(MaxisBaseRequest.DEVICE_ID, deviceId + "");
		// ht.put(AppConstants.KEY_PAGE_REVIEW, AppConstants.Company_listing);
		if (isBySearch) {
			ht.put("keyword", keywordOrCategoryId);
			if (!StringUtil.isNullOrEmpty(groupActionType))
				ht.put(KEY_GROUP_ACTION_TYPE, groupActionType);
			if (!StringUtil.isNullOrEmpty(groupType))
				ht.put(KEY_RESULT_TYPE, groupType);

			ht.put(AppConstants.KEY_PAGE_REVIEW, AppConstants.Company_listing);
			ht.put(KEY_SEARCH_DISTANCE, search_distance);
		} else {
			ht.put("category_id", keywordOrCategoryId);
			if (!StringUtil.isNullOrEmpty(groupActionType))
				ht.put(KEY_GROUP_ACTION_TYPE, groupActionType);
			if (!StringUtil.isNullOrEmpty(groupType))
				ht.put(KEY_RESULT_TYPE, groupType);

			if (isCompanyListing) {
				ht.put(AppConstants.KEY_PAGE_REVIEW,
						AppConstants.Company_listing);
				ht.put(KEY_SEARCH_DISTANCE, search_distance);
			} else {
				ht.put(AppConstants.KEY_PAGE_REVIEW, AppConstants.Deal_Listing);
			}

		}
		ht.put("page_number", pageNumber + "");
		/*
		 * if (isCompanyListing) ht.put(KEY_RESULT_TYPE, RESULT_COMP); else
		 * ht.put(KEY_RESULT_TYPE, RESULT_DEAL);
		 */
		return ht;
	}

	public Hashtable<String, String> getCategoryRefineHeader() {
		Hashtable<String, String> ht = new Hashtable<String, String>();
		ht.put(MaxisBaseRequest.KEY_PLATFORM, MaxisBaseRequest.VALUE_PLATFORM);
		ht.put(MaxisBaseRequest.KEY_SCREEN_TYPE, mScreenType);
		ht.put(MaxisBaseRequest.KEY_LANGUAGE, localeCode);
		ht.put(MaxisBaseRequest.KEY_LATITUDE, GPS_Data.getLatitude() + "");
		ht.put(MaxisBaseRequest.KEY_LONGITUDE, GPS_Data.getLongitude() + "");
		ht.put("keyword", keywordOrCategoryId);
		ht.put("page_number", pageNumber + "");

		ht.put(MaxisBaseRequest.DEVICE_ID, deviceId + "");

		if (isCompanyListing)
			ht.put(KEY_RESULT_TYPE, RESULT_COMP);
		else
			ht.put(KEY_RESULT_TYPE, RESULT_DEAL);
		return ht;
	}

	public boolean isCompanyListing() {
		return isCompanyListing;
	}

	public void setCompanyListing(boolean isCompanyListing) {
		this.isCompanyListing = isCompanyListing;
	}

	public String getKeywordOrCategoryId() {
		return Uri.decode(keywordOrCategoryId);
	}

	public void setKeywordOrCategoryId(String keywordOrCategoryId) {
		this.keywordOrCategoryId = keywordOrCategoryId;
	}

	public boolean isBySearch() {
		return isBySearch;
	}

	public void setBySearch(boolean isBySearch) {
		this.isBySearch = isBySearch;
	}

	public static final Creator<CombinedListRequest> CREATOR = new Creator<CombinedListRequest>() {

		@Override
		public CombinedListRequest createFromParcel(Parcel source) {
			return new CombinedListRequest(source);
		}

		@Override
		public CombinedListRequest[] newArray(int size) {
			return new CombinedListRequest[size];
		}
	};

	public CombinedListRequest(Context context) {
		MaxisStore store = MaxisStore.getStore(context);
		localeCode = store.getLocaleCode();
		mScreenType = NativeHelper.getDisplayDensity(context);
	}

	@Override
	public int describeContents() {
		return 0;
	}

	public String getParentThumbUrl() {
		return parentThumbUrl;
	}

	public void setParentThumbUrl(String parentThumbUrl) {
		this.parentThumbUrl = parentThumbUrl;
	}

	public String getSelectedCategoryBySearch() {
		return selectedCategoryBySearch;
	}

	public void setSelectedCategoryBySearch(String categoryId,
			String categoryName) {
		this.selectedCategoryBySearch = categoryId;
		this.selectedCategoryNameBySearch = categoryName;
	}

	public String getSelectedCategoryNameBySearch() {
		return selectedCategoryNameBySearch;
	}

	public String getGroupType() {
		return groupType;
	}

	public void setGroupType(String groupType) {
		this.groupType = groupType;
	}

	public String getGroupActionType() {
		return groupActionType;
	}

	public void setGroupActionType(String groupActionType) {
		this.groupActionType = groupActionType;
	}

}
