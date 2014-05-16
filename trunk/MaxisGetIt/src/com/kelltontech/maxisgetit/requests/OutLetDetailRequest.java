package com.kelltontech.maxisgetit.requests;

import android.os.Parcel;
import android.os.Parcelable;

public class OutLetDetailRequest implements Parcelable {

	public OutLetDetailRequest() {
		// TODO Auto-generated constructor stub
	}

	private String deal_id;
	private String comp_id;
	private String l3cat_id;
	private int page_number;
	private String cityName;
	private String localityName;

	public int getPage_number() {
		return page_number;
	}

	public void setPage_number(int page_number) {
		this.page_number = page_number;
	}

	public String getL3cat_id() {
		return l3cat_id;
	}

	public void setL3cat_id(String l3cat_id) {
		this.l3cat_id = l3cat_id;
	}

	public String getDeal_id() {
		return deal_id;
	}

	public void setDeal_id(String deal_id) {
		this.deal_id = deal_id;
	}

	public String getComp_id() {
		return comp_id;
	}

	public void setComp_id(String comp_id) {
		this.comp_id = comp_id;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		dest.writeString(comp_id);
		dest.writeString(deal_id);
		dest.writeString(l3cat_id);
		dest.writeInt(page_number);
		dest.writeString(cityName);
		dest.writeString(localityName);

	}

	public OutLetDetailRequest(Parcel in) {
		comp_id = in.readString();
		deal_id = in.readString();
		l3cat_id = in.readString();
		page_number = in.readInt();
		cityName = in.readString();
		localityName = in.readString();

	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public String getLocalityName() {
		return localityName;
	}

	public void setLocalityName(String localityName) {
		this.localityName = localityName;
	}

	public static final Creator<OutLetDetailRequest> CREATOR = new Creator<OutLetDetailRequest>() {

		@Override
		public OutLetDetailRequest createFromParcel(Parcel source) {
			return new OutLetDetailRequest(source);
		}

		@Override
		public OutLetDetailRequest[] newArray(int size) {
			return new OutLetDetailRequest[size];
		}
	};

}
