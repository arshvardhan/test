package com.kelltontech.maxisgetit.model.bannerModel;

import android.os.Parcel;
import android.os.Parcelable;

public class LogBannerReportResponse implements Parcelable {

	private BannerResults Results;

	public BannerResults getResults() {
		return Results;
	}

	public void setResults(BannerResults Results) {
		this.Results = Results;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {

	}

	public LogBannerReportResponse(Parcel in) {
	}

	public static final Creator<LogBannerReportResponse> CREATOR = new Creator<LogBannerReportResponse>() {

		@Override
		public LogBannerReportResponse createFromParcel(Parcel source) {
			return new LogBannerReportResponse(source);
		}

		@Override
		public LogBannerReportResponse[] newArray(int size) {
			return new LogBannerReportResponse[size];
		}
	};

}
