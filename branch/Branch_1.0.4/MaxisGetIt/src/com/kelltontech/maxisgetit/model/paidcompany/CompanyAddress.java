package com.kelltontech.maxisgetit.model.paidcompany;

import com.kelltontech.framework.model.IModel;

import android.os.Parcel;
import android.os.Parcelable;

public class CompanyAddress implements IModel, Parcelable {

	private String City;
	private String State;
	private String PinCode;
	private String Locality;
	private String Street;
	private String Building;
	private String SubLocality;
	private String Landmark;

	public CompanyAddress() { }

	public CompanyAddress(Parcel in) {
		City = in.readString();
		State = in.readString();
		Locality = in.readString();
		PinCode = in.readString();
		Street = in.readString();
		Building = in.readString();
		SubLocality = in.readString();
		Landmark = in.readString();
	}

	public static final Creator<CompanyAddress> CREATOR = new Creator<CompanyAddress>() {

		@Override
		public CompanyAddress createFromParcel(Parcel source) {
			return new CompanyAddress(source);
		}

		@Override
		public CompanyAddress[] newArray(int size) {
			return new CompanyAddress[size];
		}
	};

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(City);
		dest.writeString(State);
		dest.writeString(Locality);
		dest.writeString(PinCode);
		dest.writeString(Street);
		dest.writeString(Building);
		dest.writeString(SubLocality);
		dest.writeString(Landmark);
	}

	public String getCity() {
		return City;
	}

	public void setCity(String City) {
		this.City = City;
	}

	public String getState() {
		return State;
	}

	public void setState(String State) {
		this.State = State;
	}

	public String getStreet() {
		return Street;
	}

	public void setStreet(String Street) {
		this.Street = Street;
	}
	
	public String getBuilding() {
		return Building;
	}

	public void setBuilding(String Building) {
		this.Building = Building;
	}

	public String getPinCode() {
		return PinCode;
	}

	public void setPinCode(String PinCode) {
		this.PinCode = PinCode;
	}

	public String getLocality() {
		return Locality;
	}

	public void setLocality(String Locality) {
		this.Locality = Locality;
	}

	public String getSubLocality() {
		return SubLocality;
	}

	public void setSubLocality(String subLocality) {
		SubLocality = subLocality;
	}

	public String getLandmark() {
		return Landmark;
	}

	public void setLandmark(String landmark) {
		Landmark = landmark;
	}
	
}