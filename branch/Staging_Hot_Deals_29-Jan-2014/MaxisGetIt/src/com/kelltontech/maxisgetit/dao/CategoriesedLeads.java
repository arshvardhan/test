package com.kelltontech.maxisgetit.dao;

import java.util.ArrayList;
import android.os.Parcel;
import android.os.Parcelable;

public class CategoriesedLeads implements Parcelable {
	private String categoryName;
	private ArrayList<LeadDao> leadsList = new ArrayList<LeadDao>();

	public CategoriesedLeads() {
	}

	public CategoriesedLeads(Parcel in) {
		categoryName = in.readString();
		in.readTypedList(leadsList, LeadDao.CREATOR);
	}
	public static final Creator<CategoriesedLeads> CREATOR=new Creator<CategoriesedLeads>() {

		@Override
		public CategoriesedLeads createFromParcel(Parcel source) {
			return new CategoriesedLeads(source);
		}

		@Override
		public CategoriesedLeads[] newArray(int size) {
			return new CategoriesedLeads[size];
		}
	};
	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public ArrayList<LeadDao> getLeadList() {
		return leadsList;
	}

	public void setLeadList(ArrayList<LeadDao> leadList) {
		this.leadsList = leadList;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(categoryName);
		dest.writeTypedList(leadsList);

	}

}
