package com.kelltontech.maxisgetit.model.paidcompany;

import android.os.Parcel;
import android.os.Parcelable;

public class PaidCompanyResponse implements Parcelable {

	private CompanyResults Results;

	public CompanyResults getResults() {
		return Results;
	}

	public void setResults(CompanyResults Results) {
		this.Results = Results;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeParcelable(Results, PARCELABLE_WRITE_RETURN_VALUE);
	}

	public PaidCompanyResponse(Parcel in) {
		Results = in.readParcelable(CompanyResults.class.getClassLoader());
	}

	public static final Creator<PaidCompanyResponse> CREATOR = new Creator<PaidCompanyResponse>() {

		@Override
		public PaidCompanyResponse createFromParcel(Parcel source) {
			return new PaidCompanyResponse(source);
		}

		@Override
		public PaidCompanyResponse[] newArray(int size) {
			return new PaidCompanyResponse[size];
		}
	};

}
