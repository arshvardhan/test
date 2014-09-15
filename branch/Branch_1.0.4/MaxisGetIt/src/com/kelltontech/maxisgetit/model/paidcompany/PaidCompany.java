package com.kelltontech.maxisgetit.model.paidcompany;

import android.os.Parcel;
import android.os.Parcelable;

import com.kelltontech.framework.model.IModel;

public class PaidCompany implements IModel, Parcelable {
	private String 	Id;
	private String 	Title;
	private String 	L3Catid;
	private String 	L3Cat;
	private String 	Icon_Url;
	private String 	Distance;
	private CompanyAddress Address;
	private String  Is_Paid;

	public PaidCompany(Parcel in) {
		L3Cat 		= in.readString();
		Id 			= in.readString();
		Title 		= in.readString();
		Icon_Url 	= in.readString();
		Distance 	= in.readString();
		L3Catid 	= in.readString();
		Address		= in.readParcelable(CompanyAddress.class.getClassLoader());
		Is_Paid 	= in.readString();
	}

	public PaidCompany() { }
	
	public String getL3Cat() {
		return L3Cat;
	}

	public void setL3Cat(String L3Cat) {
		this.L3Cat = L3Cat;
	}

	public String getId() {
		return Id;
	}

	public void setId(String Id) {
		this.Id = Id;
	}

	public String getTitle() {
		return Title;
	}

	public void setTitle(String Title) {
		this.Title = Title;
	}

	public String getIconUrl() {
		return Icon_Url;
	}

	public void setIconUrl(String IconUrl) {
		this.Icon_Url = IconUrl;
	}

	public String getDistance() {
		return Distance;
	}

	public void setDistance(String Distance) {
		this.Distance = Distance;
	}

	public String getL3Catid() {
		return L3Catid;
	}

	public void setL3Catid(String L3Catid) {
		this.L3Catid = L3Catid;
	}

	public CompanyAddress getAddress() {
		return Address;
	}

	public void setAddress(CompanyAddress address) {
		this.Address = address;
	}
	
	public String getIs_Paid() {
		return Is_Paid;
	}

	public void setIs_Paid(String isPaid) {
		this.Is_Paid = isPaid;
	}

	public boolean IsPaid() {
		if(Is_Paid.equals("1"))
			return true;
		else
			return false;
	}

	public void setIsPaid(boolean Is_Paid) {
		if(Is_Paid)
			this.Is_Paid="1";
		else
			this.Is_Paid ="0";
	}

	public static final Creator<PaidCompany> CREATOR = new Creator<PaidCompany>() {

		@Override
		public PaidCompany createFromParcel(Parcel source) {

			return new PaidCompany(source);
		}

		@Override
		public PaidCompany[] newArray(int size) {
			return new PaidCompany[size];
		}
	};

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(L3Cat);
		dest.writeString(Id);
		dest.writeString(Title);
		dest.writeString(Icon_Url);
		dest.writeString(Distance);
		dest.writeString(L3Catid);
		dest.writeParcelable(Address, PARCELABLE_WRITE_RETURN_VALUE);
		dest.writeString(Is_Paid);
	}

}
