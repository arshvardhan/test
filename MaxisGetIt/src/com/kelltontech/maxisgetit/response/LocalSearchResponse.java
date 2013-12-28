package com.kelltontech.maxisgetit.response;

import java.util.ArrayList;

import android.os.Parcel;
import android.os.Parcelable;

import com.kelltontech.framework.model.IModel;
import com.kelltontech.framework.model.MaxisResponse;
import com.kelltontech.maxisgetit.dao.CompanyDetail;

public class LocalSearchResponse extends MaxisResponse implements Parcelable, IModel {
	private ArrayList<CompanyDetail> companiesDetails = new ArrayList<CompanyDetail>();

	public LocalSearchResponse() {
		// TODO Auto-generated constructor stub
	}

	public LocalSearchResponse(Parcel in) {
		errorMessage = in.readString();
		errorCode = in.readInt();
		in.readTypedList(companiesDetails, CompanyDetail.CREATOR);
	}

	public ArrayList<CompanyDetail> getCompaniesDetails() {
		return companiesDetails;
	}

	public void addCompanyDetail(CompanyDetail companyDetail) {
		this.companiesDetails.add(companyDetail);
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(errorMessage);
		dest.writeInt(errorCode);
		dest.writeTypedList(companiesDetails);
	}

	public static final Creator<LocalSearchResponse> CREATOR = new Creator<LocalSearchResponse>() {

		@Override
		public LocalSearchResponse createFromParcel(Parcel source) {
			return new LocalSearchResponse(source);
		}

		@Override
		public LocalSearchResponse[] newArray(int size) {
			return new LocalSearchResponse[size];
		}
	};
}
