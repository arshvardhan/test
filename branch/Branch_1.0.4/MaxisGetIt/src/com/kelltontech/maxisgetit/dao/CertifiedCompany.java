package com.kelltontech.maxisgetit.dao;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author arsh.vardhan
 * @modified 08-Sep-2014
 */

public class CertifiedCompany implements Parcelable{

	private String stampId;
	private String stampImage;
	private String stampTitle;
	private String stampCodeLabel;
	private String stampCodeValue;
	private String stampExpDateLabel;
	private String stampExpDateValue;
	
	public String getStampId() {
		return stampId;
	}

	public void setStampId(String stampId) {
		this.stampId = stampId;
	}

	public String getStampImage() {
		return stampImage;
	}

	public void setStampImage(String stampImage) {
		this.stampImage = stampImage;
	}

	public String getStampTitle() {
		return stampTitle;
	}

	public void setStampTitle(String stampTitle) {
		this.stampTitle = stampTitle;
	}

	public String getStampCodeLabel() {
		return stampCodeLabel;
	}

	public void setStampCodeLabel(String stampCodeLabel) {
		this.stampCodeLabel = stampCodeLabel;
	}

	public String getStampCodeValue() {
		return stampCodeValue;
	}

	public void setStampCodeValue(String stampCodeValue) {
		this.stampCodeValue = stampCodeValue;
	}

	public String getStampExpDateLabel() {
		return stampExpDateLabel;
	}

	public void setStampExpDateLabel(String stampExpDateLabel) {
		this.stampExpDateLabel = stampExpDateLabel;
	}

	public String getStampExpDateValue() {
		return stampExpDateValue;
	}

	public void setStampExpDateValue(String stampExpDateValue) {
		this.stampExpDateValue = stampExpDateValue;
	}

	public CertifiedCompany() {
	}

	public static final Creator<CertifiedCompany> CREATOR = new Creator<CertifiedCompany>() {

		@Override
		public CertifiedCompany createFromParcel(Parcel source) {
			return new CertifiedCompany(source);
		}

		@Override
		public CertifiedCompany[] newArray(int size) {
			return new CertifiedCompany[size];
		}
	};

	public CertifiedCompany(Parcel in) {
		stampId = in.readString();
		stampImage = in.readString();
		stampTitle = in.readString();
		stampCodeLabel = in.readString();
		stampCodeValue = in.readString();
		stampExpDateLabel = in.readString();
		stampExpDateValue = in.readString();
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(stampId);
		dest.writeString(stampImage);
		dest.writeString(stampTitle);
		dest.writeString(stampCodeLabel);
		dest.writeString(stampCodeValue);
		dest.writeString(stampExpDateLabel);
		dest.writeString(stampExpDateValue);
	}

	@Override
	public int describeContents() {
		return 0;
	}
	
}