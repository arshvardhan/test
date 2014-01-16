package com.kelltontech.maxisgetit.dao;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class NearOutLets implements Parcelable {

	private String outLetLat;
	private String outLetLong;

	public String getOutLetLat() {
		return outLetLat;
	}

	public void setOutLetLat(String outLetLang) {
		this.outLetLat = outLetLang;
	}

	public String getOutLetLong() {
		return outLetLong;
	}

	public void setOutLetLong(String outLetLong) {
		this.outLetLong = outLetLong;
	}

	public static final Creator<NearOutLets> CREATOR = new Creator<NearOutLets>() {

		@Override
		public NearOutLets createFromParcel(Parcel source) {
			return new NearOutLets(source);
		}

		@Override
		public NearOutLets[] newArray(int size) {
			return new NearOutLets[size];
		}
	};

	public NearOutLets() {
		// TODO Auto-generated constructor stub
	}

	public NearOutLets(Parcel in) {
		outLetLat = in.readString();
		outLetLong = in.readString();
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(outLetLat);
		dest.writeString(outLetLong);
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

}
