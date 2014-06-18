package com.kelltontech.maxisgetit.dao;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class OutLet implements Parcelable{

	private String id;
	private String title;
	private String phone_no;
	private String icon_url;
	private String address;
	private String lat;
	private String longt;
	private String catid;

	public OutLet()
	{
		
	}
	
	public String getCatid() {
		return catid;
	}

	public void setCatid(String catid) {
		this.catid = catid;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
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

	public String getPhone_no() {
		return phone_no;
	}

	public void setPhone_no(String phone_no) {
		this.phone_no = phone_no;
	}

	public String getIcon_url() {
		return icon_url;
	}

	public void setIcon_url(String icon_url) {
		this.icon_url = icon_url;
	}

	public String getLat() {
		return lat;
	}

	public void setLat(String lat) {
		this.lat = lat;
	}

	public String getLongt() {
		return longt;
	}

	public void setLongt(String longt) {
		this.longt = longt;
	}

	public OutLet(Parcel in) {
		id = in.readString();
		title = in.readString();
		phone_no = in.readString();
		icon_url = in.readString();
		address = in.readString();
		lat = in.readString();
		longt = in.readString();
		catid = in.readString();
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(id);
		dest.writeString(title);
		dest.writeString(phone_no);
		dest.writeString(icon_url);
		dest.writeString(address);
		dest.writeString(lat);
		dest.writeString(longt);
		dest.writeString(catid);
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}
	
	public static final Creator<OutLet> CREATOR = new Creator<OutLet>() {

		@Override
		public OutLet createFromParcel(Parcel source) {
			return new OutLet(source);
		}

		@Override
		public OutLet[] newArray(int size) {
			return new OutLet[size];
		}
	};
}
