package com.kelltontech.maxisgetit.dao;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class IconUrl implements Parcelable {

	private String dealIconUrl;

	public String getDealIconUrl() {
		return dealIconUrl;
	}

	public void setDealIconUrl(String dealIconUrl) {
		this.dealIconUrl = dealIconUrl;
	}

	public IconUrl() {
		// TODO Auto-generated constructor stub
	}

	public static final Creator<IconUrl> CREATOR = new Creator<IconUrl>() {

		@Override
		public IconUrl createFromParcel(Parcel source) {
			return new IconUrl(source);
		}

		@Override
		public IconUrl[] newArray(int size) {
			return new IconUrl[size];
		}
	};

	public IconUrl(Parcel in) {
		dealIconUrl = in.readString();
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(dealIconUrl);
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

}
