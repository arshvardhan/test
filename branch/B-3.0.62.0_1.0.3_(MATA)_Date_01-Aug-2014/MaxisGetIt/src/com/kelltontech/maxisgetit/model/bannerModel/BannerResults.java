package com.kelltontech.maxisgetit.model.bannerModel;

import android.os.Parcel;
import android.os.Parcelable;

public class BannerResults implements Parcelable {

	private String Error_Code;
	private String Error_Message;
	private String landingUrl;
	private String screenName;
	private String categoryId;
	private String catName;
	private String itemId;
	private String keyword;
	private String actionType;
	private String type;

	public String getError_Code() {
		return Error_Code;
	}

	public void setError_Code(String error_Code) {
		Error_Code = error_Code;
	}

	public String getError_Message() {
		return Error_Message;
	}

	public void setError_Message(String error_Message) {
		Error_Message = error_Message;
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

	public String getCatName() {
		return catName;
	}

	public void setCatName(String catName) {
		this.catName = catName;
	}

	public String getItemId() {
		return itemId;
	}

	public void setItemId(String itemId) {
		this.itemId = itemId;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public String getActionType() {
		return actionType;
	}

	public void setActionType(String actionType) {
		this.actionType = actionType;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(Error_Code);
		dest.writeString(Error_Message);
		dest.writeString(landingUrl);
		dest.writeString(screenName);
		dest.writeString(categoryId);
		dest.writeString(catName);
		dest.writeString(itemId);
		dest.writeString(keyword);
		dest.writeString(actionType);
		dest.writeString(type);
	}

	public BannerResults (Parcel in)
	{
		Error_Code = in.readString();
		Error_Message = in.readString();
		landingUrl = in.readString();
		screenName = in.readString();
		categoryId = in.readString();
		catName = in.readString();
		itemId = in.readString();
		keyword = in.readString();
		actionType = in.readString();
		type = in.readString();
	}
	
	public static final Creator<BannerResults> CREATOR = new Creator<BannerResults>() {

		@Override
		public BannerResults createFromParcel(Parcel source) {
			return new BannerResults(source);
		}

		@Override
		public BannerResults[] newArray(int size) {
			return new BannerResults[size];
		}
	};

}
