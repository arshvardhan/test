package com.kelltontech.maxisgetit.response;

import java.util.ArrayList;

import android.os.Parcel;
import android.os.Parcelable;
import com.kelltontech.framework.model.IModel;
import com.kelltontech.framework.model.MaxisResponse;
import com.kelltontech.maxisgetit.dao.CompanyCategory;
import com.kelltontech.maxisgetit.dao.MyDealsList;

public class DealCategoriesResponse extends MaxisResponse implements Parcelable, IModel {
	private ArrayList<CompanyCategory> compCategoryList = new ArrayList<CompanyCategory>();
	private ArrayList<MyDealsList> dealGroupList = new ArrayList<MyDealsList>();

	public DealCategoriesResponse() {
		CompanyCategory temp=new CompanyCategory();
		temp.setCompanyId("-1");
		temp.setCompanyName("Select");
		compCategoryList.add(temp);
	}

	public DealCategoriesResponse(Parcel in) {
		errorMessage = in.readString();
		errorCode = in.readInt();
		in.readTypedList(compCategoryList, CompanyCategory.CREATOR);
		in.readTypedList(dealGroupList, MyDealsList.CREATOR);
	}

	public ArrayList<CompanyCategory> getCompCategoryList() {
		return compCategoryList;
	}

	public void addCompCategoryList(CompanyCategory compCategory) {
		this.compCategoryList.add(compCategory);
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(errorMessage);
		dest.writeInt(errorCode);
		dest.writeTypedList(compCategoryList);
		dest.writeTypedList(dealGroupList);
	}

	public static final Creator<DealCategoriesResponse> CREATOR = new Creator<DealCategoriesResponse>() {

		@Override
		public DealCategoriesResponse createFromParcel(Parcel source) {
			return new DealCategoriesResponse(source);
		}

		@Override
		public DealCategoriesResponse[] newArray(int size) {
			return new DealCategoriesResponse[size];
		}
	};
}
