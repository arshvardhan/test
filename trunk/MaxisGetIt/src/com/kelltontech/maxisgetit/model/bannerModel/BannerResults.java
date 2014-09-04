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
	private String Source;
	private String HallId;
	private String CId;

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

	public String getSource() {
		return Source;
	}

	public void setSource(String source) {
		this.Source = source;
	}

	public String getHallId() {
		return HallId;
	}

	public void setHallId(String hallId) {
		this.HallId = hallId;
	}

	public String getCId() {
		return CId;
	}

	public void setCId(String cId) {
		this.CId = cId;
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
		dest.writeString(Source);
		dest.writeString(HallId);
		dest.writeString(CId);
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
		Source = in.readString();
		HallId = in.readString();
		CId = in.readString();
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
