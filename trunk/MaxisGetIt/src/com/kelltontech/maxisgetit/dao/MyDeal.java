package com.kelltontech.maxisgetit.dao;

import android.os.Parcel;
import android.os.Parcelable;

public class MyDeal implements Parcelable {
	public static final String DEAL_TYPE_VOUCHERED="Voucher";
	public static final String DEAL_TYPE_NON_VOUCHERED="Informative";
	private String id;
	private String title;
	private String desc;
	private boolean dealType;
	private String category;
	private String validity;
	private String dealStatus;
	public static final Creator<MyDeal> CREATOR = new Creator<MyDeal>() {

		@Override
		public MyDeal createFromParcel(Parcel source) {
			return new MyDeal(source);
		}

		@Override
		public MyDeal[] newArray(int size) {
			return new MyDeal[size];
		}
	};

	public MyDeal() {
	}

	public MyDeal(Parcel in) {
		id = in.readString();
		title = in.readString();
		desc = in.readString();
		dealType = in.readInt() == 1 ? true : false;
		category = in.readString();
		validity = in.readString();
		dealStatus=in.readString();
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDesc() {
		return desc;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getValidity() {
		return validity;
	}

	public void setValidity(String validity) {
		this.validity = validity;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public boolean getDealType() {
		return dealType;
	}

	public void setDealType(boolean dealType) {
		this.dealType = dealType;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(id);
		dest.writeString(title);
		dest.writeString(desc);
		dest.writeInt(dealType ? 1 : 0);
		dest.writeString(category);
		dest.writeString(validity);
		dest.writeString(dealStatus);
	}

	public String getDealStatus() {
		return dealStatus;
	}

	public void setDealStatus(String dealStatus) {
		this.dealStatus = dealStatus;
	}
}
