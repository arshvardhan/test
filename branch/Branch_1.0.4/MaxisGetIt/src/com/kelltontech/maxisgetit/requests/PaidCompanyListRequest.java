package com.kelltontech.maxisgetit.requests;

import java.io.Serializable;

/**
 * @author arsh.vardhan
 * @modified 10-Sep-2014
 */

public class PaidCompanyListRequest implements Serializable {
	
	/**
	 * Auto-generated serialVersionUID
	 */
	private static final long serialVersionUID = 1833647332887218835L;
	private String categoryId;
	private int pageNumber = 1;
	private String keyword;
	private int perPageRecord = 4;

	public String getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}

	public int getPageNumber() {
		return pageNumber;
	}

	public void setPageNumber(int pageNumber) {
		this.pageNumber = pageNumber;
	}

	public String getKeyword() {
		return keyword;
	}
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public int getPerPageRecord() {
		return perPageRecord;
	}

	public void setPerPageRecord(int perPageRecord) {
		this.perPageRecord = perPageRecord;
	}
	
}