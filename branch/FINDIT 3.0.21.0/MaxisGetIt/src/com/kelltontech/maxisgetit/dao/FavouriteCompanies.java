package com.kelltontech.maxisgetit.dao;

import android.os.Parcel;
import android.os.Parcelable;

public class FavouriteCompanies implements Parcelable {

	private int id;
	private String favComIdCategoryId;

	public static final Creator<FavouriteCompanies> CREATOR = new Creator<FavouriteCompanies>() {

		@Override
		public FavouriteCompanies createFromParcel(Parcel source) {
			return new FavouriteCompanies(source);
		}

		@Override
		public FavouriteCompanies[] newArray(int size) {
			return new FavouriteCompanies[size];
		}
	};

	public FavouriteCompanies() {
	}

	public FavouriteCompanies(Parcel in) {
		id = in.readInt();
		favComIdCategoryId = in.readString();
	}

	@Override
	public int describeContents() {
		return 0;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getFavComIdCategoryId() {
		return favComIdCategoryId;
	}

	public void setFavComIdCategoryId(String favComIdCategoryId) {
		this.favComIdCategoryId = favComIdCategoryId;
	}

	@Override
	public String toString() {
		if (favComIdCategoryId != null)
			return favComIdCategoryId;
		return super.toString();
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(id);
		dest.writeString(favComIdCategoryId);
	}

}
