package com.kelltontech.maxisgetit.dao;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class KeyValuePair implements Parcelable
{
	private String key;
	private String value;
	
	
	public KeyValuePair() {
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * @return the key
	 */
	public String getKey() {
		return key;
	}
	/**
	 * @param key the key to set
	 */
	public void setKey(String key) {
		this.key = key;
	}
	/**
	 * @return the value
	 */
	public String getValue() {
		return value;
	}
	/**
	 * @param value the value to set
	 */
	public void setValue(String value) {
		this.value = value;
	}
	@Override
	public String toString() {
		if (value != null)
			return value;
		return super.toString();
	}
	
	/********** Code for PArcelable **************/
	
	public static final Creator<KeyValuePair> CREATOR=new Creator<KeyValuePair>() {

		@Override
		public KeyValuePair createFromParcel(Parcel source) {
			return new KeyValuePair(source);
		}

		@Override
		public KeyValuePair[] newArray(int size) {
			return new KeyValuePair[size];
		}
	};
	
	public KeyValuePair(Parcel in) {
		key=in.readString();
		value = in.readString();
	}
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(key);
		dest.writeString(value);
	}
	
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}
}

