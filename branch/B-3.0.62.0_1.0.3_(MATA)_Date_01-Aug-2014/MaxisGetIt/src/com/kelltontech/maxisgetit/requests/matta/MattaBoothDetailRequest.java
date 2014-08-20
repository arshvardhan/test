package com.kelltontech.maxisgetit.requests.matta;

import java.io.Serializable;

/**
 * @author arsh.vardhan
 * @modified 19-Aug-2014
 */

public class MattaBoothDetailRequest implements Serializable {
	
	/**
	 * Auto-generated serialVersionUID
	 */
	private static final long serialVersionUID = 5210843336253066293L;

	private String companyId;
	private String keyword;
	private String categoryTitle;
	private String source;
	private String hallId;

	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
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

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getHallId() {
		return hallId;
	}

	public void setHallId(String hallId) {
		this.hallId = hallId;
	}
	
}