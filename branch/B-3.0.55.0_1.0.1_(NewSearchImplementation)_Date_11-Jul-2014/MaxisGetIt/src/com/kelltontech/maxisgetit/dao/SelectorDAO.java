package com.kelltontech.maxisgetit.dao;

import java.util.ArrayList;

import android.os.Parcel;
import android.os.Parcelable;

public class SelectorDAO implements Parcelable{
	private String searchKey;
	private String displayName;
	private int selectedIndex;
	private ArrayList<String> selectorValues=new ArrayList<String>();
	public SelectorDAO() {
		selectorValues.add("Select");
	}
	public String getSearchKey() {
		return searchKey;
	}

	public void setSearchKey(String searchKey) {
		this.searchKey = searchKey;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public int getSelectedIndex() {
		return selectedIndex;
	}

	public void setSelectedIndex(int selectedIndex) {
		this.selectedIndex = selectedIndex;
	}

	public ArrayList<String> getSelectorValues() {
		return selectorValues;
	}
	
	public void addSelectorValues(String selectorValue) {
		selectorValues.add(selectorValue);
	}
	public static final Creator<SelectorDAO> CREATOR=new Creator<SelectorDAO>() {

		@Override
		public SelectorDAO createFromParcel(Parcel source) {
			return new SelectorDAO(source);
		}

		@Override
		public SelectorDAO[] newArray(int size) {
			return new SelectorDAO[size];
		}
	};
	public SelectorDAO(Parcel in) {
		searchKey=in.readString();
		displayName=in.readString();
		selectedIndex=in.readInt();
		in.readStringList(selectorValues);
	}
	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(searchKey);
		dest.writeString(displayName);
		dest.writeInt(selectedIndex);
		dest.writeStringList(selectorValues);
	}

}
