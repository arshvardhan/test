package com.kelltontech.maxisgetit.response;

import java.util.ArrayList;

import android.os.Parcel;
import android.os.Parcelable;

import com.kelltontech.framework.model.IModel;
import com.kelltontech.framework.model.MaxisResponse;
import com.kelltontech.maxisgetit.dao.CompanyCategory;
import com.kelltontech.maxisgetit.dao.MyDeal;
import com.kelltontech.maxisgetit.dao.MyDealsList;

public class DealsListResponse extends MaxisResponse implements Parcelable, IModel {
	public static final String USER_TYPE_COMPANY = "Company";
	private ArrayList<CompanyCategory> compCategoryList = new ArrayList<CompanyCategory>();
	private ArrayList<MyDealsList> dealGroupList = new ArrayList<MyDealsList>();
	private MyDeal latestDeal;
	public ArrayList<MyDealsList> getDealGroupList() {
		return dealGroupList;
	}

	public void addDealGroupList(MyDealsList dealGroup) {
		this.dealGroupList.add(dealGroup);
	}

	public DealsListResponse() {
		CompanyCategory temp=new CompanyCategory();
		temp.setCompanyId("-1");
		temp.setCompanyName("Select");
		compCategoryList.add(temp);
	}

	public DealsListResponse(Parcel in) {
		errorMessage = in.readString();
		errorCode = in.readInt();
		in.readTypedList(compCategoryList, CompanyCategory.CREATOR);
		in.readTypedList(dealGroupList, MyDealsList.CREATOR);
		latestDeal=in.readParcelable(MyDeal.class.getClassLoader());
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
		dest.writeParcelable(latestDeal, 0);
	}

	public MyDeal getLatestDeal() {
		return latestDeal;
	}

	public void setLatestDeal(MyDeal latestDeal) {
		this.latestDeal = latestDeal;
	}
	public ArrayList<CompanyCategory> getCompCategoryList() {
		return compCategoryList;
	}

	public void addCompCategoryList(CompanyCategory compCategory) {
		this.compCategoryList.add(compCategory);
	}
	public static final Creator<DealsListResponse> CREATOR = new Creator<DealsListResponse>() {

		@Override
		public DealsListResponse createFromParcel(Parcel source) {
			return new DealsListResponse(source);
		}

		@Override
		public DealsListResponse[] newArray(int size) {
			return new DealsListResponse[size];
		}
	};
}
