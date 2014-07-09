package com.kelltontech.maxisgetit.dao;

import android.os.Parcel;
import android.os.Parcelable;

public class SubCategory extends CatgoryBase implements Parcelable {

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	public SubCategory(Parcel in) {
		mCategoryId = in.readString();
		mCategoryTitle = in.readString();
		mIconUrl = in.readString();
		mThumbUrl = in.readString();
		mGroupActionType=in.readString();
		mGroupType=in.readString();
	}

	public SubCategory() {
		// TODO Auto-generated constructor stub
	}

	public static final Creator<SubCategory> CREATOR = new Creator<SubCategory>() {

		@Override
		public SubCategory createFromParcel(Parcel source) {
			return new SubCategory(source);
		}

		@Override
		public SubCategory[] newArray(int size) {
			return new SubCategory[size];
		}
	};

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(mCategoryId);
		dest.writeString(mCategoryTitle);
		dest.writeString(mIconUrl);
		dest.writeString(mThumbUrl);
		dest.writeString(mGroupActionType);
		dest.writeString(mGroupType);
	}

}
