package com.kelltontech.maxisgetit.response.matta;

import java.util.ArrayList;

import android.os.Parcel;
import android.os.Parcelable;

import com.kelltontech.framework.model.IModel;
import com.kelltontech.framework.model.MaxisResponse;
import com.kelltontech.maxisgetit.dao.matta.MattaBanner;
import com.kelltontech.maxisgetit.dao.matta.MattaHallList;

/**
 * @author arsh.vardhan
 * @modified 02-Aug-2014
 */
public class MattaHallListResponse extends MaxisResponse implements IModel, Parcelable {
	private ArrayList<MattaHallList> mHallList = new ArrayList<MattaHallList>();
	private ArrayList<MattaBanner> bannerList = new ArrayList<MattaBanner>();

	public ArrayList<MattaHallList> getMattaHallList() {
		return mHallList;
	}

	public void addMattaHallList(MattaHallList hallList) {
		mHallList.add(hallList);
	}

	public ArrayList<MattaBanner> getBannerList() {
		return bannerList;
	}

	public void addBanner(MattaBanner banner) {
		this.bannerList.add(banner);
	}

	public static final Creator<MattaHallListResponse> CREATOR = new Creator<MattaHallListResponse>() {

		@Override
		public MattaHallListResponse createFromParcel(Parcel source) {
			return new MattaHallListResponse(source);
		}

		@Override
		public MattaHallListResponse[] newArray(int size) {
			return new MattaHallListResponse[size];
		}
	};

	@Override
	public int describeContents() {
		return 0;
	}

	public MattaHallListResponse(Parcel in) {
		errorMessage = in.readString();
		errorCode = in.readInt();
		in.readTypedList(mHallList, MattaHallList.CREATOR);
		in.readTypedList(bannerList, MattaBanner.CREATOR);
	}

	public MattaHallListResponse() {
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(errorMessage);
		dest.writeInt(errorCode);
		dest.writeTypedList(mHallList);
		dest.writeTypedList(bannerList);
	}
}
