package com.kelltontech.maxisgetit.model.paidcompany;

import android.os.Parcel;
import android.os.Parcelable;

public class PaidCompanyResponse implements Parcelable {

	private Results Results;

	public Results getResults() {
		return Results;
	}

	public void setResults(Results Results) {
		this.Results = Results;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {

	}

	public PaidCompanyResponse(Parcel in) {
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
