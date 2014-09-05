package com.kelltontech.maxisgetit.dao;

import java.util.ArrayList;

import android.os.Parcel;
import android.os.Parcelable;

import com.kelltontech.framework.model.IModel;
import com.kelltontech.framework.model.MaxisResponse;

public class ReportErrorMandatoryFields  extends MaxisResponse implements Parcelable, IModel{
	ArrayList<KeyValuePair> errorStatusList;
	
	public ReportErrorMandatoryFields() {
		// TODO Auto-generated constructor stub
	}
	
/**
	 * @return the errorStatusList
	 */
	public ArrayList<KeyValuePair> getErrorStatusList() {
		return errorStatusList;
	}
	/**
	 * @param errorStatusList the errorStatusList to set
	 */
	public void setErrorStatusList(ArrayList<KeyValuePair> errorStatusList) {
		this.errorStatusList = errorStatusList;
	}

/********** Code for Parcelable **************/
	
	public static final Creator<ReportErrorMandatoryFields> CREATOR=new Creator<ReportErrorMandatoryFields>() {

		@Override
		public ReportErrorMandatoryFields createFromParcel(Parcel source) {
			return new ReportErrorMandatoryFields(source);
		}

		@Override
		public ReportErrorMandatoryFields[] newArray(int size) {
			return new ReportErrorMandatoryFields[size];
		}
	};
	
	public ReportErrorMandatoryFields(Parcel in) {
		in.readTypedList(errorStatusList, KeyValuePair.CREATOR);
	}
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeTypedList(errorStatusList);
	}
	
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}
}

