package com.kelltontech.maxisgetit.dao;

import android.os.Parcel;
import android.os.Parcelable;

public class CategoryGroup extends CatgoryBase implements Parcelable{
		public CategoryGroup(Parcel in) {
		mCategoryId = in.readString();
		mCategoryTitle = in.readString();
		mIconUrl = in.readString();
		mThumbUrl=in.readString();
		mGroupActionType=in.readString();
		mGroupType=in.readString();
	}
public static final Creator<CategoryGroup> CREATOR=new Creator<CategoryGroup>() {

	@Override
	public CategoryGroup createFromParcel(Parcel source) {
		return new CategoryGroup(source);
	}

	@Override
	public CategoryGroup[] newArray(int size) {
		return new CategoryGroup[size];
	}
};
	public CategoryGroup() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

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
