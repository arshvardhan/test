package com.kelltontech.maxisgetit.requests;

import android.os.Parcel;
import android.os.Parcelable;

public class OutletRefineRequest implements Parcelable {

	public OutletRefineRequest() {
		super();
	}

	private String deal_id;
	private String cityName;
	private String localityName;

	public String getDeal_id() {
		return deal_id;
	}

	public void setDeal_id(String deal_id) {
		this.deal_id = deal_id;
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

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		dest.writeString(deal_id);
		dest.writeString(cityName);
		dest.writeString(localityName);
	}

	public OutletRefineRequest(Parcel in) {
		deal_id = in.readString();
		cityName = in.readString();
		localityName = in.readString();
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
