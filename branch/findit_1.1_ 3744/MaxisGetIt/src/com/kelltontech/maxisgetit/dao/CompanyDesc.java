package com.kelltontech.maxisgetit.dao;

import java.util.ArrayList;

import android.os.Parcel;
import android.os.Parcelable;

public class CompanyDesc implements Parcelable {
	private String iconUrl;
	private String compId;
	private String title;
	private String desc;
	private String additionalInfo;
	private String distance;
	private float rating;
	private String city;
	private String state;
	private String attributes;
	private double latitude;
	private double longitude;
	private String pincode;
	private String locality;
	private String contactNo;
	private String cat_id;
	
	private boolean ischecked;

	public boolean isIschecked() {
		return ischecked;
	}

	public void setIschecked(boolean ischecked) {
		this.ischecked = ischecked;
	}

	public String getCat_id() {
		return cat_id;
	}

	public void setCat_id(String cat_id) {
		this.cat_id = cat_id;
	}

	private ArrayList<AttributeGroup> attrGroups = new ArrayList<AttributeGroup>();

	public CompanyDesc() {
		// TODO Auto-generated constructor stub
	}

	public CompanyDesc(Parcel in) {
		iconUrl = in.readString();
		compId = in.readString();
		title = in.readString();
		desc = in.readString();
		additionalInfo = in.readString();
		distance = in.readString();
		rating = in.readFloat();
		city = in.readString();
		state = in.readString();
		attributes = in.readString();
		latitude = in.readDouble();
		longitude = in.readDouble();
		locality = in.readString();
		pincode = in.readString();
		contactNo = in.readString();
		cat_id = in.readString();
		in.readTypedList(attrGroups, AttributeGroup.CREATOR);
	}

	public static final Creator<CompanyDesc> CREATOR = new Creator<CompanyDesc>() {

		@Override
		public CompanyDesc createFromParcel(Parcel source) {

			return new CompanyDesc(source);
		}

		@Override
		public CompanyDesc[] newArray(int size) {
			return new CompanyDesc[size];
		}
	};

	public String getIconUrl() {
		return iconUrl;
	}

	public void setIconUrl(String iconUrl) {
		this.iconUrl = iconUrl;
	}

	public String getCompId() {
		return compId;
	}

	public void setCompId(String compId) {
		this.compId = compId;
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

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getAdditionalInfo() {
		return additionalInfo;
	}

	public void setAdditionalInfo(String additionalInfo) {
		this.additionalInfo = additionalInfo;
	}

	public String getDistance() {
		return distance;
	}

	public void setDistance(String distance) {
		this.distance = distance;
	}

	public float getRating() {
		return rating;
	}

	public void setRating(float rating) {
		this.rating = rating;
	}

	public String getAttributes() {
		/*
		 * if(attrGroups == null || attrGroups.size() < 0) { return attributes;
		 * } else{ attributes = ""; for(int i = 0; i< attrGroups.size() && i <
		 * 2; i++){ attributes += "<b>" + attrGroups.get(i).getLable() +
		 * " : </b>" + attrGroups.get(i).getValues().get(0); if(i < 1)
		 * attributes += "<br>"; } }
		 */

		return attributes;
	}

	public void appendAttributes(String value) {
		if (attributes == null) {
			attributes = value;
		} else
			attributes += ", " + value;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(iconUrl);
		dest.writeString(compId);
		dest.writeString(title);
		dest.writeString(desc);
		dest.writeString(additionalInfo);
		dest.writeString(distance);
		dest.writeFloat(rating);
		dest.writeString(city);
		dest.writeString(state);
		dest.writeString(attributes);
		dest.writeDouble(latitude);
		dest.writeDouble(longitude);
		dest.writeString(locality);
		dest.writeString(pincode);
		dest.writeString(contactNo);
		dest.writeString(cat_id);
		dest.writeTypedList(attrGroups);
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public String getContactNo() {
		return contactNo;
	}

	public void setContactNo(String contactNo) {
		this.contactNo = contactNo;
	}

	public String getPincode() {
		return pincode;
	}

	public void setPincode(String pincode) {
		this.pincode = pincode;
	}

	public String getLocality() {
		return locality;
	}

	public void setLocality(String locality) {
		this.locality = locality;
	}

	public void setAttributes(String attributes) {
		this.attributes = attributes;
	}

	public ArrayList<AttributeGroup> getAttrGroups() {
		return attrGroups;
	}

	public void addAttrGroups(AttributeGroup attrGroup) {
		this.attrGroups.add(attrGroup);
	}

}
