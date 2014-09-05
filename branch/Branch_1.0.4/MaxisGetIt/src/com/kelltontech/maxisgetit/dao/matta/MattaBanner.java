package com.kelltontech.maxisgetit.dao.matta;

import com.kelltontech.framework.model.IModel;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author arsh.vardhan
 * @modified 02-Aug-2014
 */
public class MattaBanner implements IModel, Parcelable {
	protected String id;
	protected String image;
	protected String landingUrl;
	protected String screenName;
	protected String categoryId;
	protected String itemId;

	public MattaBanner(Parcel in) {
		id = in.readString();
		image = in.readString();
		landingUrl = in.readString();
		screenName = in.readString();
		categoryId = in.readString();
		itemId = in.readString();
	}

	public MattaBanner() { }

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

	public static final Creator<MattaBanner> CREATOR = new Creator<MattaBanner>() {

		@Override
		public MattaBanner createFromParcel(Parcel source) {
			return new MattaBanner(source);
		}

		@Override
		public MattaBanner[] newArray(int size) {
			return new MattaBanner[size];
		}
	};

	@Override
	public int describeContents() {
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
		if(o instanceof MattaBanner)
			return ((MattaBanner)o).getScreenName().equals(getScreenName());
		return false;
	}
}
