package com.kelltontech.maxisgetit.response;

import java.util.ArrayList;
import android.os.Parcel;
import android.os.Parcelable;
import com.kelltontech.framework.model.IModel;
import com.kelltontech.framework.model.MaxisResponse;
import com.kelltontech.maxisgetit.dao.Classified_Base;

public class ClassifiedListResponse extends MaxisResponse implements Parcelable, IModel {
	private ArrayList<Classified_Base> classifiedList = new ArrayList<Classified_Base>();
	private Classified_Base latestAd;
	public ClassifiedListResponse() {
		// TODO Auto-generated constructor stub
	}

	public ClassifiedListResponse(Parcel in) {
		errorMessage = in.readString();
		errorCode = in.readInt();
		in.readTypedList(classifiedList, Classified_Base.CREATOR);
		latestAd=in.readParcelable(Classified_Base.class.getClassLoader());
	}

	public ArrayList<Classified_Base> getClassifiedList() {
		return classifiedList;
	}

	public void addClassified(Classified_Base classifiedSDetail) {
		this.classifiedList.add(classifiedSDetail);
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(errorMessage);
		dest.writeInt(errorCode);
		dest.writeTypedList(classifiedList);
		dest.writeParcelable(latestAd, 0);
	}

	public Classified_Base getLatestAd() {
		return latestAd;
	}

	public void setLatestAd(Classified_Base latestAd) {
		this.latestAd = latestAd;
	}

	public static final Creator<ClassifiedListResponse> CREATOR = new Creator<ClassifiedListResponse>() {

		@Override
		public ClassifiedListResponse createFromParcel(Parcel source) {
			return new ClassifiedListResponse(source);
		}

		@Override
		public ClassifiedListResponse[] newArray(int size) {
			return new ClassifiedListResponse[size];
		}
	};
}
