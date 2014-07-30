package com.kelltontech.maxisgetit.dao;

import android.os.Parcel;
import android.os.Parcelable;

public class SearchAttribute implements Parcelable {
	private String mType;
	private String mKeyword;
	private String mRecordFound;
	private String mLabel;

	public SearchAttribute(Parcel in) {
		mType 			= in.readString();
		mKeyword 		= in.readString();
		mRecordFound 	= in.readString();
		mLabel			= in.readString();
	}

	public SearchAttribute() { }

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

	public String getRecordFound() {
		return mRecordFound;
	}

	public void setRecordFound(String recordFound) {
		this.mRecordFound = recordFound;
	}

	public String getLabel() {
		return mLabel;
	}

	public void setLabel(String label) {
		this.mLabel = label;
	}

	public static final Creator<SearchAttribute> CREATOR = new Creator<SearchAttribute>() {

		@Override
		public SearchAttribute createFromParcel(Parcel source) {
			return new SearchAttribute(source);
		}

		@Override
		public SearchAttribute[] newArray(int size) {
			return new SearchAttribute[size];
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
		dest.writeString(mRecordFound);
		dest.writeString(mLabel);
	}

	@Override
	public String toString() {
		return mType;
	}
	@Override
	public boolean equals(Object o) {
		if(o instanceof SearchAttribute)
			return ((SearchAttribute)o).getType().equals(getType());
		return false;
	}
}