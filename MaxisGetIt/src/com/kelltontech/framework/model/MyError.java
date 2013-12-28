/**
 * Request.java
 * © DbyDx Software ltd. @2011 - 2011+1
 * Confidential and proprietary.
 */
package com.kelltontech.framework.model;

/**
 */
public class MyError implements IModel {
	// Constants....
	public static final int NETWORK_NOT_AVAILABLE = 0;
	public static final int EXCEPTION = 1;
	public static final int UNDEFINED = -1;
	// MemberVariables

	private int mErrorcode;
	private String mErrorMsg;
	private String mErrorLocation;

	public MyError(int mErrorcode) {
		super();
		this.mErrorcode = mErrorcode;
	}

	public String getErrorMsg() {
		return mErrorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.mErrorMsg = errorMsg;
	}

	public int getErrorcode() {
		return mErrorcode;
	}

	public void setErrorcode(int errorcode) {
		this.mErrorcode = errorcode;
	}

	public String toString() {
		return mErrorMsg;
	}

	public String getErrorLocation() {
		return mErrorLocation;
	}

	public void setErrorLocation(String errorLocation) {
		this.mErrorLocation = errorLocation;
	}

}
