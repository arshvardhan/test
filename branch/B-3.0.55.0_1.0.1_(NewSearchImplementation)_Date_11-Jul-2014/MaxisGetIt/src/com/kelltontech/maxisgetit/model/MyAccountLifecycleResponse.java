package com.kelltontech.maxisgetit.model;

import android.os.Parcel;
import android.os.Parcelable;

public class MyAccountLifecycleResponse implements Parcelable {

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

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(error_code);
		dest.writeString(response_msg);
		// dest.write

	}

	public MyAccountLifecycleResponse(Parcel in) {
		error_code = in.readString();
		response_msg = in.readString();
	}

	public static final Creator<MyAccountLifecycleResponse> CREATOR = new Creator<MyAccountLifecycleResponse>() {

		@Override
		public MyAccountLifecycleResponse createFromParcel(Parcel source) {
			return new MyAccountLifecycleResponse(source);
		}

		@Override
		public MyAccountLifecycleResponse[] newArray(int size) {
			return new MyAccountLifecycleResponse[size];
		}
	};

}
