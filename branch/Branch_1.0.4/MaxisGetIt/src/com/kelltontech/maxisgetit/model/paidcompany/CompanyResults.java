package com.kelltontech.maxisgetit.model.paidcompany;

import java.util.ArrayList;

import android.os.Parcel;
import android.os.Parcelable;

import com.kelltontech.framework.model.IModel;
import com.kelltontech.maxisgetit.model.matta.packages.list.PackageModel;

/**
 * @author arsh.vardhan
 * @modified 10-Sep-2014
 */

public class CompanyResults implements IModel, Parcelable {

	private String Error_Code;
	private String Error_Message;
	private String Records_Per_Page;
	private String Page_Number;
	private String Total_Records_Found;
	private ArrayList <PaidCompany> Company = new ArrayList <PaidCompany>();

	public CompanyResults() { }
	
	public CompanyResults (Parcel in) {
		Error_Code = in.readString();
		Error_Message = in.readString();
		Records_Per_Page = in.readString();
		Page_Number = in.readString();
		Total_Records_Found = in.readString();
		in.readTypedList(Company, PaidCompany.CREATOR);
	}
	
	public static final Creator<CompanyResults> CREATOR = new Creator<CompanyResults>() {

		@Override
		public CompanyResults createFromParcel(Parcel source) {
			return new CompanyResults(source);
		}

		@Override
		public CompanyResults[] newArray(int size) {
			return new CompanyResults[size];
		}
	};
	
	public String getErrorCode() {
		return Error_Code;
	}

	public void setErrorCode(String errorCode) {
		this.Error_Code = errorCode;
	}

	public String getErrorMessage() {
		return Error_Message;
	}

	public void setErrorMessage(String errorMessage) {
		this.Error_Message = errorMessage;
	}
	
	public String getRecordsPerPage() {
		return Records_Per_Page;
	}

	public void setRecordsPerPage(String recordsPerPage) {
		this.Records_Per_Page = recordsPerPage;
	}

	public String getPageNumber() {
		return Page_Number;
	}

	public void setPageNumber(String pageNumber) {
		this.Page_Number = pageNumber;
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
	
	public void appendCompanyListAtEnd(ArrayList<PaidCompany> Company) {
		this.Company.addAll(Company);
	}
	
	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(Error_Code);
		dest.writeString(Error_Message);
		dest.writeString(Records_Per_Page);
		dest.writeString(Page_Number);
		dest.writeString(Total_Records_Found);
		dest.writeTypedList(Company);
	}

}