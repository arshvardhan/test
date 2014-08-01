package com.kelltontech.maxisgetit.requests;

import org.json.JSONObject;

import android.net.Uri;

/**
 * @author monish.agarwal
 */
public class RefineSearchRequest 
{
	private JSONObject postData;
	private String categoryId;
	private String searchKeyword;
	private boolean isDeal;
	private String searchIn = "";
	private String stampId = "";
	/**
	 * @return the postData
	 */
	public JSONObject getPostData() {
		return postData;
	}
	/**
	 * @param postData the postData to set
	 */
	public void setPostData(JSONObject postData) {
		this.postData = postData;
	}
	/**
	 * @return the categoryId
	 */
	public String getCategoryId() {
		return categoryId;
	}
	/**
	 * @param categoryId the categoryId to set
	 */
	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}
	/**
	 * @return the searchKeyword
	 */
	public String getSearchKeyword() {
		return searchKeyword;
//		return Uri.decode(searchKeyword);
	}
	/**
	 * @param searchKeyword the searchKeyword to set
	 */
	public void setSearchKeyword(String searchKeyword) {
//		this.searchKeyword = searchKeyword;
		this.searchKeyword = Uri.encode(searchKeyword);
	}
	public boolean isDeal() {
		return isDeal;
	}
	public void setDeal(boolean isDeal) {
		this.isDeal = isDeal;
	}
	/**
	 * @return the searchIn
	 */
	public String getSearchIn() {
		return searchIn;
	}
	/**
	 * @param searchIn the searchIn to set
	 */
	public void setSearchIn(String searchIn) {
		this.searchIn = searchIn;
	}
	/**
	 * @return the stampId
	 */
	public String getstampId() {
		return stampId;
	}
	/**
	 * @param stampId the stampId to set
	 */
	public void setStampId(String stampId) {
		this.stampId = stampId;
	}
}
