package com.kelltontech.maxisgetit.response;

import java.util.ArrayList;
import java.util.List;

import com.kelltontech.framework.model.IModel;
import com.kelltontech.framework.model.MaxisResponse;
import com.kelltontech.maxisgetit.dao.CompanyReview;
import com.kelltontech.maxisgetit.dao.SubCategory;

import android.os.Parcel;
import android.os.Parcelable;

public class CompanyReviewsListResponse extends MaxisResponse  implements IModel, Parcelable{
	
	private ArrayList<CompanyReview> companyReviewList;
	private int totalRecords;
	
	public CompanyReviewsListResponse() {
		// TODO Auto-generated constructor stub
	}
	
	public ArrayList<CompanyReview> getCompanyReviewList() {
		if(companyReviewList == null)
			companyReviewList = new ArrayList<CompanyReview>();
		return companyReviewList;
	}
	
	public void setCompanyReviewList(ArrayList<CompanyReview> companyReviewList) {
		this.companyReviewList = companyReviewList;
	}
	
	public int getTotalRecords() {
		return totalRecords;
	}

	public void setTotalRecords(int totalRecords) {
		this.totalRecords = totalRecords;
	}

	
	/********** Code for Parcelable **************/
	
	public static final Creator<CompanyReviewsListResponse> CREATOR=new Creator<CompanyReviewsListResponse>() {

		@Override
		public CompanyReviewsListResponse createFromParcel(Parcel source) {
			return new CompanyReviewsListResponse(source);
		}

		@Override
		public CompanyReviewsListResponse[] newArray(int size) {
			return new CompanyReviewsListResponse[size];
		}
	};
	
	public CompanyReviewsListResponse(Parcel in) {
		errorMessage = in.readString();
		errorCode = in.readInt();
		in.readTypedList(companyReviewList, CompanyReview.CREATOR);
	}
	
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(errorMessage);
		dest.writeInt(errorCode);
		dest.writeTypedList(companyReviewList);
	}
	
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

}
