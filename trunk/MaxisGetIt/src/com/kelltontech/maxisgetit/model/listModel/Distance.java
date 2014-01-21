package com.kelltontech.maxisgetit.model.listModel;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Parcel;
import android.os.Parcelable;

import com.kelltontech.framework.model.IModel;

public class Distance  implements Parcelable,IModel{
	private int cId;
	private int categoryId;
	private String companyName;
	private String distance;
	private int count;
	
	private Address address;
	
	/**
	 * @return the cId
	 */
	public int getcId() {
		return cId;
	}

	/**
	 * @param cId the cId to set
	 */
	public void setcId(int cId) {
		this.cId = cId;
	}

	/**
	 * @return the categoryId
	 */
	public int getCategoryId() {
		return categoryId;
	}

	/**
	 * @param categoryId the categoryId to set
	 */
	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}

	/**
	 * @return the companyName
	 */
	public String getCompanyName() {
		return companyName;
	}

	/**
	 * @param companyName the companyName to set
	 */
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	/**
	 * @return the distance
	 */
	public String getDistance() {
		return distance;
	}

	/**
	 * @param distance the distance to set
	 */
	public void setDistance(String distance) {
		this.distance = distance;
	}

	/**
	 * @return the count
	 */
	public int getCount() {
		return count;
	}

	/**
	 * @param count the count to set
	 */
	public void setCount(int count) {
		this.count = count;
	}

	/**
	 * @return the address
	 */
	public Address getAddress() {
		return address;
	}

	/**
	 * @param address the address to set
	 */
	public void setAddress(Address address) {
		this.address = address;
	}

	public Distance(){}
	
	public Distance(Parcel parcel)
	{
		count=parcel.readInt();
		cId=parcel.readInt();
		categoryId=parcel.readInt();
		distance=parcel.readString();
		companyName=parcel.readString();
		
		byte addressFlag = parcel.readByte();
		if( addressFlag == 1 ) {
			address = new Address(parcel);
		}
	}
	@Override
	public void writeToParcel(Parcel parcel, int arg1) {
		parcel.writeInt(count);
		parcel.writeInt(cId);
		parcel.writeInt(categoryId);
		parcel.writeString(distance);
		parcel.writeString(companyName);
		
		// address with flag for null 
		if( address == null ) {
			parcel.writeByte((byte)0);
		} else {
			parcel.writeByte((byte)1);
			address.writeToParcel(parcel, arg1);
		}
	}
	
	
	public Distance fromJson(String jsonString)
	{
		try {
			JSONObject jsonObject = new JSONObject(jsonString);
			count=jsonObject.optInt("count");
			cId=jsonObject.optInt("cid");
			categoryId=jsonObject.optInt("cat_id");
			companyName=jsonObject.optString("company_name");
			distance=jsonObject.optString("distance");
			address = new Address().fromJson(jsonObject.optString("address"));
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return this;
	}
	public final static Parcelable.Creator<Distance> CREATOR = new Parcelable.Creator<Distance>()
			{
		public Distance createFromParcel(Parcel in)
		{
			return new Distance(in);
		}
		public Distance[] newArray(int size)
		{
			return new Distance[size];
		}
			};
			@Override
			public int describeContents() {
				return 0;
			}

}
