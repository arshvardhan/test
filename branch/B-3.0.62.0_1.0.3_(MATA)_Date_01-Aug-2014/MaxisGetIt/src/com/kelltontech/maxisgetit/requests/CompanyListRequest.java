//package com.kelltontech.maxisgetit.requests;
//
//import java.util.Hashtable;
//
//import com.kelltontech.maxisgetit.dao.GPS_Data;
//
//import android.content.Context;
//
//public class CompanyListRequest extends MaxisBaseRequest {
//	private boolean isDealLising=false;
//	private int pageNumber;
//	private String categoryId;
//	private String companyListingMethod = "getCompany.xml";
//	private String dealListingMethod = "getDeal.xml";
////	private boolean isSearchByCategory=false;
////	private String catThumbUrl;
//
//	public CompanyListRequest(Context context) {
//		super(context);
//	}
//
//	public int getPageNumber() {
//		return pageNumber;
//	}
//
//	public void setPageNumber(int pageNumber) {
//		this.pageNumber = pageNumber;
//	}
//
//	public String getCategoryId() {
//		return categoryId;
//	}
//
//	public void setCategoryId(String categoryId) {
//		this.categoryId = categoryId;
//	}
//
//	public String getCompanyListingMethod() {
//		return companyListingMethod;
//	}
//
//	public void setCompanyListingMethod(String companyListingMethod) {
//		this.companyListingMethod = companyListingMethod;
//	}
//
//	public String getRequestMethod() {
//		return companyListingMethod;
//	}
//	public String getDealRequestMethod() {
//		return dealListingMethod;
//	}
//
//	@Override
//	public Hashtable getRequestHeaders() {
//		Hashtable<String, String> hashtable = getDefaultHeaders();
//		hashtable.put(KEY_CATEGORY_ID, categoryId);
//		hashtable.put(KEY_PAGE_NUMBER, pageNumber + "");
//		hashtable.put(KEY_LATITUDE, GPS_Data.latitude+"");
//		hashtable.put(KEY_LONGITUDE, GPS_Data.longitude+"");
//		return hashtable;
//	}
//
//	public boolean isDealLising() {
//		return isDealLising;
//	}
//
//	public void setDealLising(boolean isDealLising) {
//		this.isDealLising = isDealLising;
//	}
//
////	public String getCatThumbUrl() {
////		return catThumbUrl;
////	}
////
////	public void setCatThumbUrl(String catThumbUrl) {
////		this.catThumbUrl = catThumbUrl;
////		isSearchByCategory=true;
////	}
////
////	public boolean isSearchByCategory() {
////		return isSearchByCategory;
////	}
////
////	public void setSearchByCategory(boolean isSearchByCategory) {
////		this.isSearchByCategory = isSearchByCategory;
////	}
//
//}
