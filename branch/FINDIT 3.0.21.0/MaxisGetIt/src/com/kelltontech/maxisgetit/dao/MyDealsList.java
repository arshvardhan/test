package com.kelltontech.maxisgetit.dao;

import java.util.ArrayList;

import android.os.Parcel;
import android.os.Parcelable;

public class MyDealsList implements Parcelable {
	private ArrayList<MyDeal> dealList = new ArrayList<MyDeal>();

	public MyDealsList() {
	}

	public ArrayList<MyDeal> getDealList() {
		return dealList;
	}

	public void addDeal(MyDeal deal) {
		this.dealList.add(deal);
	}

	public MyDealsList(Parcel in) {
		in.readTypedList(dealList, MyDeal.CREATOR);
	}

	@Override
	public int describeContents() {
		return 0;
	}

	public static final Creator<MyDealsList> CREATOR = new Creator<MyDealsList>() {

		@Override
		public MyDealsList createFromParcel(Parcel source) {
			return new MyDealsList(source);
		}

		@Override
		public MyDealsList[] newArray(int size) {
			return new MyDealsList[size];
		}
	};

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeTypedList(dealList);
	}
}
