package com.kelltontech.maxisgetit.requests;

public class FavCompanyListRequest {

	private int pageNumber = 1;
	private String userId;
	
	public int getPageNumber() {
		return pageNumber;
	}

	public void setPageNumber(int pageNumber) {
		this.pageNumber = pageNumber;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}


}
