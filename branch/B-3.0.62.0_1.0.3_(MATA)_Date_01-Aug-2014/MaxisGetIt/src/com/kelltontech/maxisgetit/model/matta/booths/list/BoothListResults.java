package com.kelltontech.maxisgetit.model.matta.booths.list;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author arsh.vardhan
 * @modified 13-Aug-2014
 */
public class BoothListResults implements Serializable {

	/**
	 * Auto generated serialVersionUID
	 */
	private static final long serialVersionUID = -732486654227231265L;
	private String Error_Code;
	private String Error_Message;
	private String Total_Records_Found;
	private String Records_Per_Page;
	private int Page_Number;
	private ArrayList <BoothModel> Booth;
	private List <BannerModel> Banners;

	public String getError_Code() {
		return Error_Code;
	}

	public void setError_Code(String error_Code) {
		this.Error_Code = error_Code;
	}

	public String getError_Message() {
		return Error_Message;
	}

	public void setError_Message(String error_Message) {
		this.Error_Message = error_Message;
	}

	public String getTotalRecordsFound() {
		return Total_Records_Found;
	}

	public void setTotalRecordsFound(String totalRecordsFound) {
		this.Total_Records_Found = totalRecordsFound;
	}

	public String getRecords_Per_Page() {
		return Records_Per_Page;
	}

	public void setRecords_Per_Page(String records_Per_Page) {
		this.Records_Per_Page = records_Per_Page;
	}

	public int getPageNumber() {
		return Page_Number;
	}

	public void setPageNumber(int pageNumber) {
		this.Page_Number = pageNumber;
	}

	public ArrayList<BoothModel> getBooth() {
		return Booth;
	}

	public void setBooth(ArrayList<BoothModel> Booth) {
		this.Booth = Booth;
	}

	public void appendBoothListAtEnd(ArrayList<BoothModel> booth) {
		Booth.addAll(booth);
	}

	public List<BannerModel> getBanners() {
		return Banners;
	}

	public void setBanners(List<BannerModel> banners) {
		this.Banners = banners;
	}

}