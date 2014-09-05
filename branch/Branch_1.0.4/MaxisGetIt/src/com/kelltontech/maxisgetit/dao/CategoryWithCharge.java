package com.kelltontech.maxisgetit.dao;

import java.util.Comparator;

import android.os.Parcel;
import android.os.Parcelable;

public class CategoryWithCharge extends CategoryRefine implements Parcelable,Comparable,Comparator<CategoryWithCharge> {
	private float charge;

	public CategoryWithCharge(Parcel in) {
		super(in);
		charge = in.readFloat();
	}

	public CategoryWithCharge() {
		// TODO Auto-generated constructor stub
	}

	public float getCharge() {
		return charge;
	}

	public static final Creator<CategoryWithCharge> CREATOR = new Creator<CategoryWithCharge>() {

		@Override
		public CategoryWithCharge createFromParcel(Parcel source) {
			return new CategoryWithCharge(source);
		}

		@Override
		public CategoryWithCharge[] newArray(int size) {
			return new CategoryWithCharge[size];
		}
	};

	public void setCharge(float charge) {
		this.charge = charge;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		super.writeToParcel(dest, flags);
		dest.writeFloat(charge);

	}
	@Override
	public String toString() {
		return super.toString();
	}
	@Override
	public boolean equals(Object o) {
		return super.equals(o);
	}

	@Override
	public int compare(CategoryWithCharge lhs, CategoryWithCharge rhs) {
		if(lhs.getCategoryTitle()==null && rhs.getCategoryTitle()==null)
			return 0;
		else if(lhs.getCategoryTitle()==null)
			return 1;
		else if(rhs.getCategoryTitle()==null)
			return -1;
		else
			return lhs.getCategoryTitle().compareTo(rhs.getCategoryTitle());
	}

	@Override
	public int compareTo(Object another) {
		CategoryWithCharge rhs=(CategoryWithCharge) another;
		if(this.getCategoryTitle()==null && rhs.getCategoryTitle()==null)
			return 0;
		else if(this.getCategoryTitle()==null)
			return 1;
		else if(this.getCategoryTitle()==null)
			return -1;
		else
			return this.getCategoryTitle().compareTo(rhs.getCategoryTitle());
	}
}
