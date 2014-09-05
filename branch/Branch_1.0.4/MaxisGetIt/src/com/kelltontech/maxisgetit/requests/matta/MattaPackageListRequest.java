package com.kelltontech.maxisgetit.requests.matta;

import java.io.Serializable;

/**
 * @author arsh.vardhan
 * @modified 19-Aug-2014
 */

public class MattaPackageListRequest implements Serializable {
	
	/**
	 * Auto-generated serialVersionUID
	 */
	private static final long serialVersionUID = -2364430963571174295L;
	private String source;
	private String listType;
	private String hallId;
	private String companyId;
	private int pageNumber = 1;
	private boolean isSearchRefined;
	private String mHallTitle;
	private String mMattaThumbUrl;
	private String postJsonPayload = "";
	private String keyword;
	private String categoryTitle;

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getListType() {
		return listType;
	}

	public void setListType(String listType) {
		this.listType = listType;
	}

	public String getHallId() {
		return hallId;
	}

	public void setHallId(String hallId) {
		this.hallId = hallId;
	}
	
	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public int getPageNumber() {
		return pageNumber;
	}

	public void setPageNumber(int pageNumber) {
		this.pageNumber = pageNumber;
	}

	public boolean isSearchRefined() {
		return isSearchRefined;
	}

	public void setSearchRefined(boolean isSearchRefined) {
		this.isSearchRefined = isSearchRefined;
	}

	public String getmHallTitle() {
		return mHallTitle;
	}

	public void setmHallTitle(String mHallTitle) {
		this.mHallTitle = mHallTitle;
	}

	public String getmMattaThumbUrl() {
		return mMattaThumbUrl;
	}

	public void setmMattaThumbUrl(String mMattaThumbUrl) {
		this.mMattaThumbUrl = mMattaThumbUrl;
	}

	public String getPostJsonPayload() {
		return postJsonPayload;
	}

	public void setPostJsonPayload(String postJsonPayload) {
		this.postJsonPayload = postJsonPayload;
	}
	public String getKeyword() {
		return keyword;
	}
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	public String getCategoryTitle() {
		return categoryTitle;
	}
	public void setCategoryTitle(String categoryTitle) {
		this.categoryTitle = categoryTitle;
	}
	
}