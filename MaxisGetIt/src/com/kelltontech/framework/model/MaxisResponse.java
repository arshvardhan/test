package com.kelltontech.framework.model;


public class MaxisResponse implements IModel{
	protected String errorMessage;
	protected int errorCode;
//	protected ArrayList<String> banner = new ArrayList<String>();

	public boolean isErrorFromServer() {
		return errorCode == 0 ? false:true;
	}

	public int getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}

	public void setServerMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public String getServerMessage() {
		return errorMessage;
	}

/*	public ArrayList<String> getBanner() {
		return banner;
	}

	public void setBanner(ArrayList<String> banner) {
		this.banner = banner;
	}*/

}
