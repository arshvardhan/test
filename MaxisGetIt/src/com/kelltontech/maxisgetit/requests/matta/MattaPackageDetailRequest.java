package com.kelltontech.maxisgetit.requests.matta;

import java.io.Serializable;

/**
 * @author arsh.vardhan
 * @modified 19-Aug-2014
 */
public class MattaPackageDetailRequest implements Serializable {
	/**
	 * Auto-generated serialVersionUID
	 */
	private static final long serialVersionUID = -6797073551429377536L;
	private String packageId;
	private String source;
	private String keyword;

	public String getPackageId() {
		return packageId;
	}

	public void setPackageId(String packageId) {
		this.packageId = packageId;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	
}