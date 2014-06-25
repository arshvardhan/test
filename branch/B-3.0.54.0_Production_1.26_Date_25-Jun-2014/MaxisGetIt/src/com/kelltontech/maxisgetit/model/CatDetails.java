package com.kelltontech.maxisgetit.model;

import android.os.Parcel;
import android.os.Parcelable;

public class CatDetails implements Parcelable{

	private String catid;
	private String catname;
	private String myTotalView;
	private String totalView;
	private String ccDid;
	private String ccBillingNo;
	private String ccDate;
	private String ccPaymentStatus;
	private String ccLeadStatus;
	private String ccSubscriptionStatus;
	private String ccSubscription;
	private String ccMaxisStatus;
	private String maxisDidNumber;
	private String maxisStatus;
	private String maxisProvisionStatus;
	private String subscription;
	private String totalLeads;
	private String message;
	private String price;

	public String getCatid() {
		return catid;
	}

	public void setCatid(String catid) {
		this.catid = catid;
	}

	public String getCatname() {
		return catname;
	}

	public void setCatname(String catname) {
		this.catname = catname;
	}

	public String getMyTotalView() {
		return myTotalView;
	}

	public void setMyTotalView(String myTotalView) {
		this.myTotalView = myTotalView;
	}

	public String getTotalView() {
		return totalView;
	}

	public void setTotalView(String totalView) {
		this.totalView = totalView;
	}

	public String getCcDid() {
		return ccDid;
	}

	public void setCcDid(String ccDid) {
		this.ccDid = ccDid;
	}

	public String getCcBillingNo() {
		return ccBillingNo;
	}

	public void setCcBillingNo(String ccBillingNo) {
		this.ccBillingNo = ccBillingNo;
	}

	public String getCcDate() {
		return ccDate;
	}

	public void setCcDate(String ccDate) {
		this.ccDate = ccDate;
	}

	public String getCcPaymentStatus() {
		return ccPaymentStatus;
	}

	public void setCcPaymentStatus(String ccPaymentStatus) {
		this.ccPaymentStatus = ccPaymentStatus;
	}

	public String getCcLeadStatus() {
		return ccLeadStatus;
	}

	public void setCcLeadStatus(String ccLeadStatus) {
		this.ccLeadStatus = ccLeadStatus;
	}

	public String getCcSubscriptionStatus() {
		return ccSubscriptionStatus;
	}

	public void setCcSubscriptionStatus(String ccSubscriptionStatus) {
		this.ccSubscriptionStatus = ccSubscriptionStatus;
	}

	public String getCcSubscription() {
		return ccSubscription;
	}

	public void setCcSubscription(String ccSubscription) {
		this.ccSubscription = ccSubscription;
	}

	public String getCcMaxisStatus() {
		return ccMaxisStatus;
	}

	public void setCcMaxisStatus(String ccMaxisStatus) {
		this.ccMaxisStatus = ccMaxisStatus;
	}

	public String getMaxisDidNumber() {
		return maxisDidNumber;
	}

	public void setMaxisDidNumber(String maxisDidNumber) {
		this.maxisDidNumber = maxisDidNumber;
	}

	public String getMaxisStatus() {
		return maxisStatus;
	}

	public void setMaxisStatus(String maxisStatus) {
		this.maxisStatus = maxisStatus;
	}

	public String getMaxisProvisionStatus() {
		return maxisProvisionStatus;
	}

	public void setMaxisProvisionStatus(String maxisProvisionStatus) {
		this.maxisProvisionStatus = maxisProvisionStatus;
	}

	public String getSubscription() {
		return subscription;
	}

	public void setSubscription(String subscription) {
		this.subscription = subscription;
	}

	public String getTotalLeads() {
		return totalLeads;
	}

	public void setTotalLeads(String totalLeads) {
		this.totalLeads = totalLeads;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		dest.writeString(catid);
		dest.writeString(catname);
		dest.writeString(myTotalView);
		dest.writeString(totalView);
		dest.writeString(ccDid);
		dest.writeString(ccBillingNo);
		dest.writeString(ccDate);
		dest.writeString(ccPaymentStatus);
		dest.writeString(ccLeadStatus);
		dest.writeString(ccSubscriptionStatus);
		dest.writeString(ccSubscription);
		dest.writeString(ccMaxisStatus);
		dest.writeString(maxisDidNumber);
		dest.writeString(maxisStatus);
		dest.writeString(maxisProvisionStatus);
		dest.writeString(subscription);
		dest.writeString(totalLeads);
		dest.writeString(message);
		dest.writeString(price);
		
		
	}
	
	public static final Creator<CatDetails> CREATOR = new Creator<CatDetails>() {

		@Override
		public CatDetails createFromParcel(Parcel source) {
			return new CatDetails(source);
		}

		@Override
		public CatDetails[] newArray(int size) {
			// TODO Auto-generated method stub
			return new CatDetails[size];
		}
	};


	public CatDetails(Parcel in) {
		catid= in.readString();
		catname= in.readString();
		myTotalView= in.readString();
		totalView= in.readString();
		ccDid= in.readString();
		ccBillingNo= in.readString();
		ccDate= in.readString();
		ccPaymentStatus= in.readString();
		ccLeadStatus= in.readString();
		ccSubscriptionStatus= in.readString();
		ccSubscription= in.readString();
		ccMaxisStatus= in.readString();
		maxisDidNumber= in.readString();
		maxisStatus= in.readString();
		maxisProvisionStatus= in.readString();
		subscription= in.readString();
		totalLeads= in.readString();
		message= in.readString();
		price= in.readString();

		
	}


}
