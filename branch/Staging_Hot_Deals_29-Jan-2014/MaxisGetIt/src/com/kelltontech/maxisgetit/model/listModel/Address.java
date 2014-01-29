/**
 * 
 */
package com.kelltontech.maxisgetit.model.listModel;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 
 */
public class Address implements Parcelable {

	private String	building;
	private String	street;
	private String	landmark;
	private String	subLocality;
	private String	locality;
	
	/**
	 * @return the building
	 */
	public String getBuilding() {
		return building;
	}

	/**
	 * @param building
	 *            the building to set
	 */
	public void setBuilding(String building) {
		this.building = building;
	}

	/**
	 * @return the street
	 */
	public String getStreet() {
		return street;
	}

	/**
	 * @param street
	 *            the street to set
	 */
	public void setStreet(String street) {
		this.street = street;
	}

	/**
	 * @return the landmark
	 */
	public String getLandmark() {
		return landmark;
	}

	/**
	 * @param landmark
	 *            the landmark to set
	 */
	public void setLandmark(String landmark) {
		this.landmark = landmark;
	}

	/**
	 * @return the subLocality
	 */
	public String getSubLocality() {
		return subLocality;
	}

	/**
	 * @param subLocality
	 *            the subLocality to set
	 */
	public void setSubLocality(String subLocality) {
		this.subLocality = subLocality;
	}

	/**
	 * @return the locality
	 */
	public String getLocality() {
		return locality;
	}

	/**
	 * @param locality
	 *            the locality to set
	 */
	public void setLocality(String locality) {
		this.locality = locality;
	}

	// Code for Pracelable

	/**
	 * Empty constructor for Pracelable
	 */
	public Address() {
	}

	/**
	 * constructor for Pracelable
	 * @param parcel
	 */
	public Address(Parcel parcel) {
		building = parcel.readString();
		street = parcel.readString();
		building = parcel.readString();
		subLocality = parcel.readString();
		locality = parcel.readString();
	}

	/**
	 * writeToParcel for Pracelable
	 */
	@Override
	public void writeToParcel(Parcel parcel, int arg1) {
		parcel.writeString(building);
		parcel.writeString(street);
		parcel.writeString(landmark);
		parcel.writeString(subLocality);
		parcel.writeString(locality);

	}

	/**
	 * CREATOR for Pracelable
	 */
	public final static Parcelable.Creator<Address>	CREATOR	= new Parcelable.Creator<Address>() {
		public Address createFromParcel(
				Parcel in) {
			return new Address(
					in);
		}

		public Address[] newArray(
				int size) {
			return new Address[size];
		}
	};

	/**
	 * describeContents for Pracelable
	 */
	@Override
	public int describeContents() {
		return 0;
	}
	
	/**
	 * @param jsonString
	 * @return
	 */
	public Address fromJson(String jsonString) {
		try {
			JSONObject jsonObject = new JSONObject(jsonString);
			building = jsonObject.optString("building");
			street = jsonObject.optString("street");
			landmark = jsonObject.optString("landmark");
			subLocality = jsonObject.optString("sublocality");
			locality = jsonObject.optString("locality");

		} catch (JSONException e) {
			e.printStackTrace();
		}

		return this;
	}

	/**
	 * We receive parts of address from API.
	 * Complete address string is created locally.
	 * This variable is to avoid recreation. 
	 */
	private String	addressStr;
	
	/**
	 * @return the addressStr
	 */
	public String getAddressStr() {
		return addressStr;
	}

	/**
	 * @param addressStr the addressStr to set
	 */
	public void setAddressStr(String addressStr) {
		this.addressStr = addressStr;
	}
}
