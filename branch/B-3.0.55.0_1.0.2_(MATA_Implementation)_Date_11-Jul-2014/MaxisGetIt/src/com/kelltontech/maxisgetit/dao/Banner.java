package com.kelltontech.maxisgetit.dao;

import android.os.Parcel;
import android.os.Parcelable;

public class Banner implements Parcelable {
	protected String id;
	protected String image;
	protected String landingUrl;
	protected String screenName;
	protected String categoryId;
	protected String itemId;

	public Banner(Parcel in) {
		id = in.readString();
		image = in.readString();
		landingUrl = in.readString();
		screenName = in.readString();
		categoryId = in.readString();
		itemId = in.readString();
	}

	public Banner() { }

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getLandingUrl() {
		return landingUrl;
	}

	public void setLandingUrl(String landingUrl) {
		this.landingUrl = landingUrl;
	}

	public String getScreenName() {
		return screenName;
	}

	public void setScreenName(String screenName) {
		this.screenName = screenName;
	}

	public String getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}

	public String getItemId() {
		return itemId;
	}

	public void setItemId(String itemId) {
		this.itemId = itemId;
	}

	public static final Creator<Banner> CREATOR = new Creator<Banner>() {

		@Override
		public Banner createFromParcel(Parcel source) {
			return new Banner(source);
		}

		@Override
		public Banner[] newArray(int size) {
			return new Banner[size];
		}
	};

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		
		dest.writeString(id);
		dest.writeString(image);
		dest.writeString(landingUrl);
		dest.writeString(screenName);
		dest.writeString(categoryId);
		dest.writeString(itemId);

	}

	@Override
	public String toString() {
		return screenName;
	}
	
	@Override
	public boolean equals(Object o) {
		if(o instanceof Banner)
			return ((Banner)o).getScreenName().equals(getScreenName());
		return false;
	}
}
