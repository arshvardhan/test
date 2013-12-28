package com.kelltontech.maxisgetit.dao;

import java.util.ArrayList;
import com.kelltontech.framework.model.MaxisResponse;
import com.kelltontech.framework.model.IModel;
import android.os.Parcel;
import android.os.Parcelable;

public class CompanyDetail extends MaxisResponse implements Parcelable,IModel{
	private String id;
	private String title;
	private ArrayList<String> contacts;
	private String mailId;
	private String website;
	private double latitude;
	private double longitude;
	private String description;
	private String imageUrl;
	private String sharingText;
	private String locality;
	private String city;
	private String pincode;
	private String state;
	private ArrayList<AttributeGroup> attrGroups=new ArrayList<AttributeGroup>();
	private String callNumber;
	private String smsNumber;
	private boolean isPaid;
	private boolean isContactChannelExists=false;
	private String distance;
	private float rating;
	private int ratedUserCount;
	private String building;
	private String subLocality;
	private String landmark;
	private String street;
	private String catId;
	private int totalReviewCount;
	private ArrayList<CompanyReview> companyReviewList =  new ArrayList<CompanyReview>();
	private String recordType;
	

	public ArrayList<CompanyReview> getCompanyReviewList() {
		/*if(companyReviewList == null)
			companyReviewList = new ArrayList<CompanyReview>();*/
		return companyReviewList;
	}
	
	public void setCompanyReviewList(ArrayList<CompanyReview> companyReviewList) {
		this.companyReviewList = companyReviewList;
	}
	
	public String getCallNumber() {
		return callNumber;
	}

	public String getDistance() {
		return distance;
	}

	public void setDistance(String distance) {
		this.distance = distance;
	}

	public void setPaid(boolean isPaid) {
		this.isPaid = isPaid;
	}

	public void setCallNumber(String callNumber) {
		this.callNumber = callNumber;
	}

	public String getSmsNumber() {
		return smsNumber;
	}

	public void setSmsNumber(String smsNumber) {
		this.smsNumber = smsNumber;
	}

	public String getBillingNumber() {
		return billingNumber;
	}

	public void setBillingNumber(String billingNumber) {
		this.billingNumber = billingNumber;
	}

	private String billingNumber;
	public CompanyDetail(Parcel in) {
		id = in.readString();
		title = in.readString();
		contacts=new ArrayList<String>();
		in.readStringList(contacts);
		mailId = in.readString();
		website = in.readString();
		latitude = in.readDouble();
		longitude = in.readDouble();
		description = in.readString();
		imageUrl = in.readString();
		sharingText = in.readString();
		locality=in.readString();
		city=in.readString();
		pincode=in.readString();
		in.readTypedList(attrGroups, AttributeGroup.CREATOR);
		callNumber=in.readString();
		smsNumber=in.readString();
		billingNumber=in.readString();
		isPaid=in.readInt()==1?true:false;
		isContactChannelExists=in.readInt()==1?true:false;
		distance=in.readString();
		state = in.readString();
		building = in.readString();
		subLocality = in.readString();
		landmark = in.readString();
		street = in.readString();
		catId = in.readString();
		rating = in.readFloat();
		ratedUserCount = in.readInt();
		in.readTypedList(companyReviewList, CompanyReview.CREATOR);
		totalReviewCount = in.readInt();
		recordType = in.readString();
	}

	public static final Creator<CompanyDetail> CREATOR = new Creator<CompanyDetail>() {

		@Override
		public CompanyDetail createFromParcel(Parcel source) {
			return new CompanyDetail(source);
		}

		@Override
		public CompanyDetail[] newArray(int size) {
			return new CompanyDetail[size];
		}
	};

	public CompanyDetail() {
		// TODO Auto-generated constructor stub
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public ArrayList<String> getContacts() {
		return contacts;
	}

	public void setContacts(ArrayList<String> contacts) {
		this.contacts = contacts;
	}

	public String getMailId() {
		return mailId;
	}

	public void setMailId(String mailId) {
		this.mailId = mailId;
	}

	public String getWebsite() {
		return website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getSharingText() {
		return sharingText;
	}

	public void setSharingText(String sharingText) {
		this.sharingText = sharingText;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(id);
		dest.writeString(title);
		dest.writeStringList(contacts);
		dest.writeString(mailId);
		dest.writeString(website);
		dest.writeDouble(latitude);
		dest.writeDouble(longitude);
		dest.writeString(description);
		dest.writeString(imageUrl);
		dest.writeString(sharingText);
		dest.writeString(locality);
		dest.writeString(city);
		dest.writeString(pincode);
		dest.writeTypedList(attrGroups);
		dest.writeString(callNumber);
		dest.writeString(smsNumber);
		dest.writeString(billingNumber);
		dest.writeInt(isPaid?1:0);
		dest.writeInt(isContactChannelExists?1:0);
		dest.writeString(distance);
		dest.writeString(state);
		dest.writeString(building);
		dest.writeString(subLocality);
		dest.writeString(landmark);
		dest.writeString(street);
		dest.writeString(catId);
		dest.writeFloat(rating);
		dest.writeInt(ratedUserCount);
		dest.writeTypedList(companyReviewList);
		dest.writeInt(totalReviewCount);
		dest.writeString(recordType);
	}

	public ArrayList<AttributeGroup> getAttrGroups() {
		return attrGroups;
	}

	public void addAttrGroups(AttributeGroup attrGroup) {
		this.attrGroups.add(attrGroup);
	}

	public String getLocality() {
		return locality;
	}

	public void setLocality(String locality) {
		this.locality = locality;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getBuilding() {
		return building;
	}

	public void setBuilding(String building) {
		this.building = building;
	}

	public String getSubLocality() {
		return subLocality;
	}

	public void setSubLocality(String subLocality) {
		this.subLocality = subLocality;
	}

	public String getLandmark() {
		return landmark;
	}

	public void setLandmark(String landmark) {
		this.landmark = landmark;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getPincode() {
		return pincode;
	}

	public void setPincode(String pincode) {
		this.pincode = pincode;
	}

	public boolean isPaid() {
		return isPaid;
	}

	public void setPaid(int isPaid) {
		if(isPaid==1)
			this.isPaid=true;
		else
			this.isPaid = false;
	}

	public float getRating() {
		return rating;
	}

	public void setRating(float rating) {
		this.rating = rating;
	}

	

	public int getRatedUserCount() {
		return ratedUserCount;
	}

	public void setRatedUserCount(int ratedUserCount) {
		this.ratedUserCount = ratedUserCount;
	}

	public boolean isContactChannelExists() {
		return isContactChannelExists;
	}

	public void setContactChannelExists(boolean isContactChannelExists) {
		this.isContactChannelExists = isContactChannelExists;
	}

	/**
	 * @return the catId
	 */
	public String getCatId() {
		return catId;
	}

	/**
	 * @param catId the catId to set
	 */
	public void setCatId(String catId) {
		this.catId = catId;
	}

	public int getTotalReviewCount() {
		return totalReviewCount;
	}

	public void setTotalReviewCount(int totalReviewCount) {
		this.totalReviewCount = totalReviewCount;
	}

	public String getRecordType() {
		return recordType;
	}

	public void setRecordType(String recordType) {
		this.recordType = recordType;
	}

}
