package com.kelltontech.maxisgetit.model.paidcompany;

import java.util.ArrayList;

import android.os.Parcel;
import android.os.Parcelable;

import com.kelltontech.framework.model.IModel;

public class Results implements IModel, Parcelable {

	private String Error_Code;
	private String Error_Message;
	private String Total_Records_Found;
	private ArrayList <PaidCompany> Company;

	public Results () { }
	
	public Results (Parcel in) {
		Error_Code = in.readString();
		Error_Message = in.readString();
		Total_Records_Found = in.readString();
		in.readTypedList(Company, PaidCompany.CREATOR);
	}
	
	public static final Creator<Results> CREATOR = new Creator<Results>() {

		@Override
		public Results createFromParcel(Parcel source) {
			return new Results(source);
		}

		@Override
		public Results[] newArray(int size) {
			return new Results[size];
		}
	};
	
	
	public String getError_Code() {
		return Error_Code;
	}

	public void setError_Code(String error_Code) {
		Error_Code = error_Code;
	}

	public String getError_Message() {
		return Error_Message;
	}

	public void setError_Message(String error_Message) {
		Error_Message = error_Message;
	}

	public String getTotalRecordsFound() {
		return Total_Records_Found;
	}

	public void setTotalRecordsFound(String totalRecordsFound) {
		this.Total_Records_Found = totalRecordsFound;
	}

	public ArrayList<PaidCompany> getCompany() {
		return Company;
	}

	public void setCompany(ArrayList<PaidCompany> Company) {
		this.Company = Company;
	}
	
	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(Error_Code);
		dest.writeString(Error_Message);
		dest.writeString(Total_Records_Found);
		dest.writeTypedList(Company);
	}

}