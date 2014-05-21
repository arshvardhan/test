package com.kelltontech.maxisgetit.model;

import java.util.ArrayList;

public class MyAccountDashboardResponse {
	
	private String error_code;
	private String response_msg;
	private ArrayList<Data> data;

	public String getError_code() {
		return error_code;
	}

	public void setError_code(String error_code) {
		this.error_code = error_code;
	}

	public String getResponse_msg() {
		return response_msg;
	}

	public void setResponse_msg(String response_msg) {
		this.response_msg = response_msg;
	}

	public ArrayList<Data> getData() {
		return data;
	}

	public void setData(ArrayList<Data> data) {
		this.data = data;
	}

}
