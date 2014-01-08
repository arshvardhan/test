package com.kelltontech.maxisgetit.requests;

import java.util.Hashtable;
import com.kelltontech.framework.utils.NativeHelper;
import com.kelltontech.framework.utils.StringUtil;
import com.kelltontech.maxisgetit.dao.GPS_Data;
import com.kelltontech.maxisgetit.dao.MaxisStore;
import android.content.Context;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

public class CombinedListRequest implements Parcelable {
	public static final String METHOD_CATEGORY_REFINE="categoryRefine.xml";
	public static final String METHOD_REFINE_SEARCH_RES="RefineSearch.xml";
	private static final String METHOD_COMPANY_LIST_BY_SEARCH = "search.xml";
	private static final String METHOD_DEAL_LIST_BY_SEARCH = "getGroupCategoryData.xml";//"getDeal.xml";//"search.xml";
	private static final String METHOD_COMPANY_LIST_BY_CATEGORY = "getGroupCategoryData.xml";//"getCompany.xml";
	private static final String METHOD_DEAL_LIST_BY_CATEGORY = "getGroupCategoryData.xml";//"getDeal.xml";
	private static final String RESULT_DEAL = "DEAL";
	private static final String RESULT_COMP = "COMPANY";
	private static final String KEY_RESULT_TYPE = "type";
	public static final String KEY_GROUP_ACTION_TYPE = "action_type";
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
	
	
	public CombinedListRequest(Parcel in) {
		pageNumber = in.readInt();
		keywordOrCategoryId = in.readString();
		localeCode = in.readString();
		mScreenType=in.readString();
		latitude = in.readDouble();
		longitude = in.readDouble();
		isCompanyListing = in.readInt() == 0 ? false : true;
		isBySearch = in.readInt() == 0 ? false : true;
		parentThumbUrl=in.readString();
		categoryTitle=in.readString();
		postJsonPayload=in.readString();
		selectedCategoryBySearch=in.readString();
		selectedCategoryNameBySearch=in.readString();
		groupType = in.readString();
		groupActionType = in.readString();
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(pageNumber);
		dest.writeString(keywordOrCategoryId);
		dest.writeString(localeCode);
		dest.writeString(mScreenType);
		dest.writeDouble(latitude);
		dest.writeDouble(longitude);
		dest.writeInt(isCompanyListing?1:0);
		dest.writeInt(isBySearch?1:0);
		dest.writeString(parentThumbUrl);
		dest.writeString(categoryTitle);
		dest.writeString(postJsonPayload);
		dest.writeString(selectedCategoryBySearch);
		dest.writeString(selectedCategoryNameBySearch);
		dest.writeString(groupType);
		dest.writeString(groupActionType);
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
	public static String getRefineMethod(){
		return METHOD_REFINE_SEARCH_RES;
	}
	public String getRequestMethod() {
		if(postJsonPayload!=null)
			return METHOD_REFINE_SEARCH_RES;
		if (isBySearch()) {
			if (isCompanyListing)
				return METHOD_COMPANY_LIST_BY_SEARCH;
			else
				return METHOD_DEAL_LIST_BY_SEARCH;
		} else {
			if (isCompanyListing)
				return METHOD_COMPANY_LIST_BY_CATEGORY;
			else
				return METHOD_DEAL_LIST_BY_CATEGORY;
		}
	}
	public String getUrlAppendableRequestHeaders() {
		StringBuffer ht =new StringBuffer();
		ht.append("?"+MaxisBaseRequest.KEY_APP_KEY+"="+MaxisBaseRequest.VALUE_APP_KEY);
		ht.append("&"+MaxisBaseRequest.KEY_PLATFORM+"="+ MaxisBaseRequest.VALUE_PLATFORM);
		ht.append("&"+MaxisBaseRequest.KEY_SCREEN_TYPE+"="+ mScreenType);
		ht.append("&"+MaxisBaseRequest.KEY_LANGUAGE+"="+ localeCode);
		ht.append("&"+MaxisBaseRequest.KEY_LATITUDE+"="+ GPS_Data.getLatitude() + "");
		ht.append("&"+MaxisBaseRequest.KEY_LONGITUDE+"="+ GPS_Data.getLongitude() + "");
		if (isBySearch){
			ht.append("&"+"keyword"+"="+ keywordOrCategoryId);
			if(!StringUtil.isNullOrEmpty(selectedCategoryBySearch) && !selectedCategoryBySearch.equalsIgnoreCase("-1"))
				ht.append("&"+"category_id"+"="+ selectedCategoryBySearch);
		}
		else
			ht.append("&"+"category_id"+"="+ keywordOrCategoryId);
		ht.append("&"+"page_number"+"="+ pageNumber + "");
		if (isCompanyListing)
			ht.append("&"+KEY_RESULT_TYPE+"="+ RESULT_COMP);
		else
			ht.append("&"+KEY_RESULT_TYPE+"="+ RESULT_DEAL);
		return ht.toString();
	}
	public Hashtable<String,String> getRequestHeaders() {
		Hashtable<String, String> ht =new Hashtable<String, String>();
		ht.put(MaxisBaseRequest.KEY_APP_KEY, MaxisBaseRequest.VALUE_APP_KEY);
		ht.put(MaxisBaseRequest.KEY_PLATFORM, MaxisBaseRequest.VALUE_PLATFORM);
		ht.put(MaxisBaseRequest.KEY_SCREEN_TYPE, mScreenType);
		ht.put(MaxisBaseRequest.KEY_LANGUAGE, localeCode);
		ht.put(MaxisBaseRequest.KEY_LATITUDE, GPS_Data.getLatitude() + "");
		ht.put(MaxisBaseRequest.KEY_LONGITUDE, GPS_Data.getLongitude() + "");
		if (isBySearch)
		{
			ht.put("keyword", keywordOrCategoryId);
			if(!StringUtil.isNullOrEmpty(groupActionType))
				ht.put(KEY_GROUP_ACTION_TYPE, groupActionType);
			if(!StringUtil.isNullOrEmpty(groupType))
				ht.put(KEY_RESULT_TYPE, groupType);
		}
		else
		{
			ht.put("category_id", keywordOrCategoryId);
			if(!StringUtil.isNullOrEmpty(groupActionType))
				ht.put(KEY_GROUP_ACTION_TYPE, groupActionType);
			if(!StringUtil.isNullOrEmpty(groupType))
				ht.put(KEY_RESULT_TYPE, groupType);
		}
		ht.put("page_number", pageNumber + "");
		/*if (isCompanyListing)
			ht.put(KEY_RESULT_TYPE, RESULT_COMP);
		else
			ht.put(KEY_RESULT_TYPE, RESULT_DEAL);*/
		return ht;
	}
	public Hashtable<String,String> getCategoryRefineHeader() {
		Hashtable<String, String> ht =new Hashtable<String, String>();
		ht.put(MaxisBaseRequest.KEY_PLATFORM, MaxisBaseRequest.VALUE_PLATFORM);
		ht.put(MaxisBaseRequest.KEY_SCREEN_TYPE, mScreenType);
		ht.put(MaxisBaseRequest.KEY_LANGUAGE, localeCode);
		ht.put(MaxisBaseRequest.KEY_LATITUDE, GPS_Data.getLatitude() + "");
		ht.put(MaxisBaseRequest.KEY_LONGITUDE, GPS_Data.getLongitude() + "");
		ht.put("keyword", keywordOrCategoryId);
		ht.put("page_number", pageNumber + "");
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
		MaxisStore store=MaxisStore.getStore(context);
		localeCode = store.getLocaleCode();
		mScreenType=NativeHelper.getDisplayDensity(context);
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

	public void setSelectedCategoryBySearch(String categoryId,String categoryName) {
		this.selectedCategoryBySearch = categoryId;
		this.selectedCategoryNameBySearch=categoryName;
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
