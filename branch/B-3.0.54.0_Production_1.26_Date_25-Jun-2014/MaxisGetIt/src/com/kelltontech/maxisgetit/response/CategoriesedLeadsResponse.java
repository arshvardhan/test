package com.kelltontech.maxisgetit.response;

import java.util.ArrayList;

import android.os.Parcel;
import android.os.Parcelable;

import com.kelltontech.framework.model.IModel;
import com.kelltontech.framework.model.MaxisResponse;
import com.kelltontech.maxisgetit.dao.CategoriesedLeads;

public class CategoriesedLeadsResponse extends MaxisResponse implements IModel, Parcelable {
	private ArrayList<CategoriesedLeads> catLeads = new ArrayList<CategoriesedLeads>();
	private String companyName;
	public CategoriesedLeadsResponse() {
	}

	public CategoriesedLeadsResponse(Parcel in) {
		errorMessage = in.readString();
		errorCode = in.readInt();
		companyName=in.readString();
		in.readTypedList(catLeads, CategoriesedLeads.CREATOR);
	}

	public ArrayList<CategoriesedLeads> getCatLeads() {
		return catLeads;
	}

	public void addCatLeads(CategoriesedLeads catLeads) {
		this.catLeads.add(catLeads);
	}

	@Override
	public int describeContents() {
		return 0;
	}

	public static final Creator<CategoriesedLeadsResponse> CREATOR = new Creator<CategoriesedLeadsResponse>() {

		@Override
		public CategoriesedLeadsResponse createFromParcel(Parcel source) {
			return new CategoriesedLeadsResponse(source);
		}

		@Override
		public CategoriesedLeadsResponse[] newArray(int size) {
			return new CategoriesedLeadsResponse[size];
		}
	};

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(errorMessage);
		dest.writeInt(errorCode);
		dest.writeString(companyName);
		dest.writeTypedList(catLeads);
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
}
