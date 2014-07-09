package com.kelltontech.maxisgetit.dao;

import java.util.ArrayList;
import android.os.Parcel;
import android.os.Parcelable;

public class CompanyCategory implements Parcelable {
	private String companyId;
	private String companyName;
	private ArrayList<CategoryWithCharge> categoryList = new ArrayList<CategoryWithCharge>();

	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public ArrayList<CategoryWithCharge> getCategoryList() {
		return categoryList;
	}

	public void addCategoryCharge(CategoryWithCharge category) {
		this.categoryList.add(category);
	}

	public CompanyCategory() {
		CategoryWithCharge temp=new CategoryWithCharge();
		temp.setCategoryId("-1");
		temp.setCategoryTitle("Select");
		categoryList.add(temp);
	}

	public CompanyCategory(Parcel in) {
		companyId = in.readString();
		companyName = in.readString();
		in.readTypedList(categoryList, CategoryWithCharge.CREATOR);
	}

	public static final Creator<CompanyCategory> CREATOR = new Creator<CompanyCategory>() {

		@Override
		public CompanyCategory createFromParcel(Parcel source) {
			return new CompanyCategory(source);
		}

		@Override
		public CompanyCategory[] newArray(int size) {
			return new CompanyCategory[size];
		}
	};

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(companyId);
		dest.writeString(companyName);
		dest.writeTypedList(categoryList);
	}

	@Override
	public String toString() {
		return companyName;
	}
}
