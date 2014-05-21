package com.kelltontech.maxisgetit.model;


public class MyAccountLifecycleResponse {
		
		private String error_code;
		private String response_msg;
		private Data data;

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

		public Data getData() {
			return data;
		}

		public void setData(Data data) {
			this.data = data;
		}


	}

