package com.kelltontech.maxisgetit.dao;

import android.os.Parcel;
import android.os.Parcelable;

public class SearchDisplayIn implements Parcelable {
	private String mType;
	private String mKeyword;
	private String mStampId;

	public SearchDisplayIn(Parcel in) {
		mType 			= in.readString();
		mKeyword 		= in.readString();
		mStampId		= in.readString();
	}

	public SearchDisplayIn() { }

	public String getType() {
		return mType;
	}

	public void setType(String type) {
		this.mType = type;
	}

	public String getKeyword() {
		return mKeyword;
	}

	public void setKeyword(String keyword) {
		this.mKeyword = keyword;
	}
	
	public String getStampId() {
		return mStampId;
	}

	public void setStampId(String stampId) {
		this.mStampId = stampId;
	}

	public static final Creator<SearchDisplayIn> CREATOR = new Creator<SearchDisplayIn>() {

		@Override
		public SearchDisplayIn createFromParcel(Parcel source) {
			return new SearchDisplayIn(source);
		}

		@Override
		public SearchDisplayIn[] newArray(int size) {
			return new SearchDisplayIn[size];
		}
	};

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(mType);
		dest.writeString(mKeyword);
		dest.writeString(mStampId);
	}

	@Override
	public String toString() {
		return mType;
	}
	@Override
	public boolean equals(Object o) {
		if(o instanceof SearchDisplayIn)
			return ((SearchDisplayIn)o).getType().equals(getType());
		return false;
	}
}