package com.kelltontech.maxisgetit.requests.matta;

import org.json.JSONObject;

import android.content.Context;
/**
 * @author arsh.vardhan
 * @modified 08-Aug-2014
 */
public class MattaFilterSearchRequest extends MattaRequest {
	
	private JSONObject postData;
//	private String categoryId;
//	private String searchKeyword;
	private String searchType = "";
	private String id = "";
	
	public MattaFilterSearchRequest(Context context) {
		super(context);
	}

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
/*	*//**
	 * @return the categoryId
	 *//*
	public String getCategoryId() {
		return categoryId;
	}
	*//**
	 * @param categoryId the categoryId to set
	 *//*
	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}*/
/*	*//**
	 * @return the searchKeyword
	 *//*
	public String getSearchKeyword() {
		return searchKeyword;
//		return Uri.decode(searchKeyword);
	}
	*//**
	 * @param searchKeyword the searchKeyword to set
	 *//*
	public void setSearchKeyword(String searchKeyword) {
//		this.searchKeyword = searchKeyword;
		this.searchKeyword = Uri.encode(searchKeyword);
	}*/

	/**
	 * @return the searchType
	 */
	public String getsearchType() {
		return searchType;
	}
	/**
	 * @param searchType the searchType to set
	 */
	public void setsearchType(String searchType) {
		this.searchType = searchType;
	}
	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}
}
