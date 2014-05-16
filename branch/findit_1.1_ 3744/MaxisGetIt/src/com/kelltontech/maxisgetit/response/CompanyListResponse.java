package com.kelltontech.maxisgetit.response;

import java.util.ArrayList;
import android.os.Parcel;
import android.os.Parcelable;
import com.kelltontech.framework.model.IModel;
import com.kelltontech.framework.model.MaxisResponse;
import com.kelltontech.maxisgetit.dao.CategoryRefine;
import com.kelltontech.maxisgetit.dao.CompanyDesc;

public class CompanyListResponse extends MaxisResponse implements IModel, Parcelable {
	private int totalrecordFound;
	private int recordsPerPage;
	private int pageNumber;
	private ArrayList<CompanyDesc> companyArrayList = new ArrayList<CompanyDesc>();
	private ArrayList<CategoryRefine> categoryList = new ArrayList<CategoryRefine>();
	public void setCompanyList(ArrayList<CompanyDesc> compList){
		companyArrayList=compList;
	}
	public void appendCompListAtEnd(ArrayList<CompanyDesc> compList){
		companyArrayList.addAll(compList);
	}
	public ArrayList<CategoryRefine> getCategoryList() {
		return categoryList;
	}

	public void addCategory(CategoryRefine category) {
		this.categoryList.add(category);
	}

	public int getPagesCount() {
		int pages = 0;
		try {
			pages = totalrecordFound / recordsPerPage;
			if (totalrecordFound % recordsPerPage > 0)
				pages += 1;
		} catch (Exception e) {
		}
		return pages;
	}

	public CompanyListResponse() {
		// TODO Auto-generated constructor stub
	}

	public CompanyListResponse(Parcel in) {
		errorMessage = in.readString();
		errorCode = in.readInt();
		totalrecordFound = in.readInt();
		pageNumber = in.readInt();
		recordsPerPage = in.readInt();
		in.readTypedList(companyArrayList, CompanyDesc.CREATOR);
		in.readTypedList(categoryList, CategoryRefine.CREATOR);
	}

	public int getTotalrecordFound() {
		return totalrecordFound;
	}

	public void setTotalrecordFound(int totalrecordFound) {
		this.totalrecordFound = totalrecordFound;
	}

	public int getRecordsPerPage() {
		return recordsPerPage;
	}

	public void setRecordsPerPage(int recordsPerPage) {
		this.recordsPerPage = recordsPerPage;
	}

	public int getPageNumber() {
		return pageNumber;
	}

	public void setPageNumber(int pageNumber) {
		this.pageNumber = pageNumber;
	}

	public ArrayList<CompanyDesc> getCompanyArrayList() {
		return companyArrayList;
	}

	public void addCompanyDescription(CompanyDesc desc) {
		companyArrayList.add(desc);
	}

	public static final Creator<CompanyListResponse> CREATOR = new Creator<CompanyListResponse>() {

		@Override
		public CompanyListResponse createFromParcel(Parcel source) {
			return new CompanyListResponse(source);
		}

		@Override
		public CompanyListResponse[] newArray(int size) {
			return new CompanyListResponse[size];
		}
	};

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(errorMessage);
		dest.writeInt(errorCode);
		dest.writeInt(totalrecordFound);
		dest.writeInt(pageNumber);
		dest.writeInt(recordsPerPage);
		dest.writeTypedList(companyArrayList);
		dest.writeTypedList(categoryList);
	}

}