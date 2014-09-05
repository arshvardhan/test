package com.kelltontech.maxisgetit.response;

import java.util.ArrayList;

import android.os.Parcel;
import android.os.Parcelable;

import com.kelltontech.framework.model.IModel;
import com.kelltontech.framework.model.MaxisResponse;
import com.kelltontech.maxisgetit.dao.FavouriteCompanies;

public class UserDetailResponse extends MaxisResponse implements IModel, Parcelable {
	private String name;
	private String userId;
	private String email;
	private String mobile;
	private boolean emailStatus;
	private boolean mobileStatus;
	private boolean isCompany;
	private boolean isOTP;
	private FavouriteCompanies favCompany;
	private ArrayList<FavouriteCompanies> companyIdCategoryId;

	public UserDetailResponse(Parcel source) {
		errorMessage = source.readString();
		errorCode = source.readInt();
		userId = source.readString();
		name = source.readString();
		email = source.readString();
		mobile = source.readString();
		emailStatus = source.readInt() == 1 ? true : false;
		mobileStatus = source.readInt() == 1 ? true : false;
		isCompany = source.readInt() == 1 ? true : false;
		isOTP = source.readInt() == 1 ? true : false;
		companyIdCategoryId = new ArrayList<FavouriteCompanies>();
		source.readTypedList(companyIdCategoryId, FavouriteCompanies.CREATOR);
		favCompany = source.readParcelable(FavouriteCompanies.class.getClassLoader());
	}

	
	public UserDetailResponse() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(errorMessage);
		dest.writeInt(errorCode);
		dest.writeString(userId);
		dest.writeString(name);
		dest.writeString(email);
		dest.writeString(mobile);
		dest.writeInt(emailStatus ? 1 : 0);
		dest.writeInt(mobileStatus ? 1 : 0);
		dest.writeInt(isCompany ? 1 : 0);
		dest.writeInt(isOTP ? 1 : 0);
		dest.writeTypedList(companyIdCategoryId);
		dest.writeParcelable(favCompany, 0);
	}

	public static final Creator<UserDetailResponse> CREATOR = new Creator<UserDetailResponse>() {

		@Override
		public UserDetailResponse createFromParcel(Parcel source) {
			return new UserDetailResponse(source);
		}

		@Override
		public UserDetailResponse[] newArray(int size) {
			return new UserDetailResponse[size];
		}
	};

	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public boolean isVerifiedEmail() {
		return emailStatus;
	}

	public void setEmailStatus(boolean emailStatus) {
		this.emailStatus = emailStatus;
	}

	public boolean isVerifiedMobile() {
		return mobileStatus;
	}

	public void setMobileStatus(boolean mobileStatus) {
		this.mobileStatus = mobileStatus;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	public boolean isCompany() {
		return isCompany;
	}

	public void setCompany(boolean isCompany) {
		this.isCompany = isCompany;
	}

	public boolean isOTP() {
		return isOTP;
	}

	public void setOTP(boolean isOTP) {
		this.isOTP = isOTP;
	}

	public FavouriteCompanies getFavCompany() {
		return favCompany;
	}

	public void setFavCompany(FavouriteCompanies favCompany) {
		this.favCompany = favCompany;
	}

	public ArrayList<FavouriteCompanies> getCompanyIdCategoryId() {
		return companyIdCategoryId;
	}

	public void addCompanyIdCategoryId(ArrayList<FavouriteCompanies>  companyIdCategoryId) {
		this.companyIdCategoryId = companyIdCategoryId;
		}
	}

