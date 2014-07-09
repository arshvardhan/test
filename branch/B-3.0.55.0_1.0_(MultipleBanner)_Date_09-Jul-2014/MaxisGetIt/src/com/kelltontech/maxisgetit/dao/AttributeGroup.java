package com.kelltontech.maxisgetit.dao;

import java.util.ArrayList;

import android.os.Parcel;
import android.os.Parcelable;

public class AttributeGroup implements Parcelable{
	private String lable;
	private ArrayList<String> values=new ArrayList<String>();
	public AttributeGroup(Parcel in) {
		lable=in.readString();
		in.readStringList(values);
	}
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(lable);
		dest.writeStringList(values);
	}
	public static final Creator<AttributeGroup> CREATOR=new Creator<AttributeGroup>() {

		@Override
		public AttributeGroup createFromParcel(Parcel source) {
			return new AttributeGroup(source);
		}

		@Override
		public AttributeGroup[] newArray(int size) {
			return new AttributeGroup[size];
		}
	};
	public AttributeGroup() {
	}
	public String getLable() {
		return lable;
	}
	public void setLable(String lable) {
		this.lable = lable;
	}
	public ArrayList<String> getValues() {
		return values;
	}
	public void addValue(String value) {
		this.values.add(value);
	}
	public void setValues(ArrayList<String> values) {
		this.values = values;
	}
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

}
