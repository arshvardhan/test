package com.kelltontech.maxisgetit.dao;

import android.os.Parcel;
import android.os.Parcelable;

public class CompanyReview implements Parcelable{
	
	private String userName;
	private String reviewDesc;
	private String postedOn;
	private float rating;
	
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getReviewDesc() {
		return reviewDesc;
	}

	public void setReviewDesc(String reviewDesc) {
		this.reviewDesc = reviewDesc;
	}

	public String getReportedOn() {
		return postedOn;
	}

	public void setReportedOn(String reportedOn) {
		this.postedOn = reportedOn;
	}

	public float getRating() {
		return rating;
	}

	public void setRating(float rating) {
		this.rating = rating;
	}

	//********************* Parcelable Code ***********************//
	public CompanyReview() {
		// TODO Auto-generated constructor stub
	}

	public static final Creator<CompanyReview> CREATOR = new Creator<CompanyReview>() {

		@Override
		public CompanyReview createFromParcel(Parcel source) {
			return new CompanyReview(source);
		}

		@Override
		public CompanyReview[] newArray(int size) {
			return new CompanyReview[size];
		}
	};
	
	public CompanyReview(Parcel in) {
		userName = in.readString();
		reviewDesc = in.readString();
		postedOn = in.readString();
		rating = in.readFloat();
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(userName);
		dest.writeString(reviewDesc);
		dest.writeString(postedOn);
		dest.writeFloat(rating);
	}
	
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

}
