package com.kelltontech.maxisgetit.dao;

import android.os.Parcel;
import android.os.Parcelable;

public class CategoryRefine implements Parcelable {
	protected String mCategoryId;
	protected String mCategoryTitle;

	public CategoryRefine(Parcel in) {
		mCategoryId = in.readString();
		mCategoryTitle = in.readString();
	}

	public CategoryRefine() {
		// TODO Auto-generated constructor stub
	}

	public String getCategoryId() {
		return mCategoryId;
	}

	public void setCategoryId(String mCategoryId) {
		this.mCategoryId = mCategoryId;
	}

	public String getCategoryTitle() {
		return mCategoryTitle;
	}

	public void setCategoryTitle(String mCategoryTitle) {
		this.mCategoryTitle = mCategoryTitle;
	}

	public static final Creator<CategoryRefine> CREATOR = new Creator<CategoryRefine>() {

		@Override
		public CategoryRefine createFromParcel(Parcel source) {
			return new CategoryRefine(source);
		}

		@Override
		public CategoryRefine[] newArray(int size) {
			return new CategoryRefine[size];
		}
	};

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(mCategoryId);
		dest.writeString(mCategoryTitle);

	}

	@Override
	public String toString() {
		return mCategoryTitle;
	}
	@Override
	public boolean equals(Object o) {
		if(o instanceof CategoryRefine)
			return ((CategoryRefine)o).getCategoryTitle().equals(getCategoryTitle());
		return false;
	}
}
