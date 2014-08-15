package com.kelltontech.maxisgetit.requests.matta;

import java.io.Serializable;

import android.content.Context;

/**
 * @author arsh.vardhan
 * @modified 08-Aug-2014
 */

public class MattaBoothDetailRequest /*extends MattaRequest */implements Serializable {
	
	/**
	 * Auto-generated serialVersionUID
	 */
	private static final long serialVersionUID = 5210843336253066293L;

	private String companyId;
	private String keyword;
	private String categoryTitle;

	/*public MattaBoothDetailRequest(Context context, String companyId) {
//		super(context);
		this.companyId = companyId;
	}*/

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
	
}