package com.kelltontech.maxisgetit.response;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import android.os.Parcel;
import android.os.Parcelable;

import com.kelltontech.framework.model.IModel;
import com.kelltontech.framework.model.MaxisResponse;
import com.kelltontech.maxisgetit.dao.Banner;
import com.kelltontech.maxisgetit.dao.CategoryRefine;
import com.kelltontech.maxisgetit.dao.CompanyDesc;
import com.kelltontech.maxisgetit.dao.SearchAttribute;
import com.kelltontech.maxisgetit.dao.SearchDisplayIn;

public class CompanyListResponse extends MaxisResponse implements IModel, Parcelable {
	private int totalrecordFound;
	private int recordsPerPage;
	private int pageNumber;
	private ArrayList<CompanyDesc> companyArrayList = new ArrayList<CompanyDesc>();
	private ArrayList<CategoryRefine> categoryList = new ArrayList<CategoryRefine>();
	private ArrayList<Banner> bannerList = new ArrayList<Banner>();
	private ArrayList<SearchAttribute> searchAttributeList = new ArrayList<SearchAttribute>();
	private SearchDisplayIn displayIn;
	private String search_distance;
	
	public String getSearch_distance() {
		return search_distance;
	}

	public void setSearch_distance(String search_distance) {
		this.search_distance = search_distance;
	}

	public void setCompanyList(ArrayList<CompanyDesc> compList) {
		companyArrayList = compList;
	}

	public void appendCompListAtEnd(ArrayList<CompanyDesc> compList , boolean isFromFavList) {
		
		if(isFromFavList)
		{
		LinkedHashMap<String, Object> noDuplicates = new LinkedHashMap<String, Object>();
		for (CompanyDesc companyDesc : companyArrayList) {
			noDuplicates.put(companyDesc.getCompId_catId(), companyDesc);
		}
		for (CompanyDesc companyDesc : compList) {
			noDuplicates.put(companyDesc.getCompId_catId(), companyDesc);
		}
		companyArrayList.removeAll(companyArrayList);
		for (String key : noDuplicates.keySet()) {
			companyArrayList.add((CompanyDesc) noDuplicates.get(key));
		}
		}
		else
		{
		 companyArrayList.addAll(compList);
		}
	}

	public ArrayList<CategoryRefine> getCategoryList() {
		return categoryList;
	}

	public void addCategory(CategoryRefine category) {
		this.categoryList.add(category);
	}
	
	public ArrayList<Banner> getBannerList() {
		return bannerList;
	}

	public void addBanner(Banner banner) {
		this.bannerList.add(banner);
	}
	
	public ArrayList<SearchAttribute> getSearchAttributeList() {
		return searchAttributeList;
	}

	public void addSearchAttributeList(SearchAttribute searchAttribute) {
		this.searchAttributeList.add(searchAttribute);
	}
	
	public SearchDisplayIn getDisplayIn() {
		return displayIn;
	}

	public void setDisplayIn(SearchDisplayIn displayIn) {
		this.displayIn = displayIn;
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


	public CompanyListResponse() { }

	public CompanyListResponse(Parcel in) {
		errorMessage = in.readString();
		errorCode = in.readInt();
		totalrecordFound = in.readInt();
		pageNumber = in.readInt();
		recordsPerPage = in.readInt();
		in.readTypedList(companyArrayList, CompanyDesc.CREATOR);
		in.readTypedList(categoryList, CategoryRefine.CREATOR);
		in.readTypedList(bannerList, Banner.CREATOR);
		in.readTypedList(searchAttributeList, SearchAttribute.CREATOR);
		displayIn = in.readParcelable(SearchDisplayIn.class.getClassLoader());
		search_distance= in.readString();
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
		dest.writeTypedList(bannerList);
		dest.writeTypedList(searchAttributeList);
		dest.writeParcelable(displayIn, PARCELABLE_WRITE_RETURN_VALUE);
		dest.writeString(search_distance);
	}
}
