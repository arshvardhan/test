package com.kelltontech.maxisgetit.dao;

import android.os.Parcel;
import android.os.Parcelable;

public class Classified_Base implements Parcelable {
	private String id;
	private String title;
	private String desc;
	private String AdStatus;
	private String category;
	private String validity;
	private boolean isPaid;
	public static final Creator<Classified_Base> CREATOR = new Creator<Classified_Base>() {

		@Override
		public Classified_Base createFromParcel(Parcel source) {
			return new Classified_Base(source);
		}

		@Override
		public Classified_Base[] newArray(int size) {
			return new Classified_Base[size];
		}
	};

	public Classified_Base() {
	}

	public Classified_Base(Parcel in) {
		id = in.readString();
		title = in.readString();
		desc = in.readString();
		AdStatus = in.readString();
		category=in.readString();
		validity=in.readString();
		isPaid=in.readInt()==1?true:false;
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

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(id);
		dest.writeString(title);
		dest.writeString(desc);
		dest.writeString(AdStatus);
		dest.writeString(category);
		dest.writeString(validity);
		dest.writeInt(isPaid?1:0);
	}

	public String getAdStatus() {
		return AdStatus;
	}

	public void setAdStatus(String adStatus) {
		AdStatus = adStatus;
	}

	public boolean isPaid() {
		return isPaid;
	}

	public void setPaid(boolean isPaid) {
		this.isPaid = isPaid;
	}
}
