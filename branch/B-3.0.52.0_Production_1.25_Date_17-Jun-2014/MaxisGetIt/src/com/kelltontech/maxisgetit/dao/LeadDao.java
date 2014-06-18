package com.kelltontech.maxisgetit.dao;

import android.os.Parcel;
import android.os.Parcelable;

public class LeadDao implements Parcelable {
	private String callerId;
	private String startTime;
	private String duration;

	public LeadDao() {
	}

	public LeadDao(Parcel in) {
		callerId = in.readString();
		startTime = in.readString();
		duration = in.readString();
	}

	public static final Creator<LeadDao> CREATOR = new Creator<LeadDao>() {

		@Override
		public LeadDao createFromParcel(Parcel source) {
			return new LeadDao(source);
		}

		@Override
		public LeadDao[] newArray(int size) {
			return new LeadDao[size];
		}
	};

	public String getCallerId() {
		return callerId;
	}

	public void setCallerId(String callerId) {
		this.callerId = callerId;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getDuration() {
		return duration;
	}

	public void setDuration(String duration) {
		this.duration = duration;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(callerId);
		dest.writeString(startTime);
		dest.writeString(duration);
	}

}
