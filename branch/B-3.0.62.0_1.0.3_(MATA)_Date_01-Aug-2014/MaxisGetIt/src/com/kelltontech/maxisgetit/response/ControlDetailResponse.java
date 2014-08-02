package com.kelltontech.maxisgetit.response;

import java.util.ArrayList;

import android.os.Parcel;
import android.os.Parcelable;

import com.kelltontech.framework.model.IModel;
import com.kelltontech.framework.model.MaxisResponse;
import com.kelltontech.maxisgetit.dao.ControlDetails;

public class ControlDetailResponse extends MaxisResponse implements IModel, Parcelable {
	private String templetId;
	private String templetName;
	private String templetDescription;
	private ArrayList<ControlDetails> controlList = new ArrayList<ControlDetails>();

	public ControlDetailResponse(Parcel source) {
		errorMessage = source.readString();
		errorCode = source.readInt();
		templetId = source.readString();
		templetName = source.readString();
		templetDescription = source.readString();
		source.readTypedList(controlList, ControlDetails.CREATOR);
	}

	public ControlDetailResponse() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(errorMessage);
		dest.writeInt(errorCode);
		dest.writeString(templetId);
		dest.writeString(templetName);
		dest.writeString(templetDescription);
		dest.writeTypedList(controlList);

	}

	public static final Creator<ControlDetailResponse> CREATOR = new Creator<ControlDetailResponse>() {

		@Override
		public ControlDetailResponse createFromParcel(Parcel source) {
			return new ControlDetailResponse(source);
		}

		@Override
		public ControlDetailResponse[] newArray(int size) {
			return new ControlDetailResponse[size];
		}
	};

	public String getTempletId() {
		return templetId;
	}

	public void setTempletId(String templetId) {
		this.templetId = templetId;
	}

	public String getTempletName() {
		return templetName;
	}

	public void setTempletName(String templetName) {
		this.templetName = templetName;
	}

	public String getTempletDescription() {
		return templetDescription;
	}

	public void setTempletDescription(String templetDescription) {
		this.templetDescription = templetDescription;
	}

	public ArrayList<ControlDetails> getControlList() {
		return controlList;
	}

	public void addControlDetail(ControlDetails controlDetail) {
		this.controlList.add(controlDetail);
	}

	@Override
	public int describeContents() {
		return 0;
	}

}
