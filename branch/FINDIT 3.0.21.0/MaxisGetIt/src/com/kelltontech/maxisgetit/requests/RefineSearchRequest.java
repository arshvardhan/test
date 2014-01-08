package com.kelltontech.maxisgetit.requests;

import org.json.JSONObject;

/**
 * @author monish.agarwal
 */
public class RefineSearchRequest 
{
	private JSONObject postData;
	private String categoryId;
	private String searchKeyword;
	private boolean isDeal;
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
	}
	/**
	 * @param searchKeyword the searchKeyword to set
	 */
	public void setSearchKeyword(String searchKeyword) {
		this.searchKeyword = searchKeyword;
	}
	public boolean isDeal() {
		return isDeal;
	}
	public void setDeal(boolean isDeal) {
		this.isDeal = isDeal;
	}
}
