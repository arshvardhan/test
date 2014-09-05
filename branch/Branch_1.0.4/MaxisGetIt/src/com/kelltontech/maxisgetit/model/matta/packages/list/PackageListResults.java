package com.kelltontech.maxisgetit.model.matta.packages.list;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author arsh.vardhan
 * @modified 02-Aug-2014
 */
public class PackageListResults implements Serializable {

	/**
	 * Auto generated serialVersionUID
	 */
	private static final long serialVersionUID = -6313882741940927073L;

	private String Error_Code;
	private String Error_Message;
	private String Total_Records_Found;
	private String Records_Per_Page;
	private int Page_Number;
	private ArrayList <PackageModel> Package;
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
	public String getTotal_Records_Found() {
		return Total_Records_Found;
	}
	public void setTotal_Records_Found(String total_Records_Found) {
		this.Total_Records_Found = total_Records_Found;
	}
	public String getRecords_Per_Page() {
		return Records_Per_Page;
	}
	public void setRecords_Per_Page(String records_Per_Page) {
		this.Records_Per_Page = records_Per_Page;
	}
	public int getPage_Number() {
		return Page_Number;
	}
	public void setPage_Number(int page_Number) {
		this.Page_Number = page_Number;
	}
	public ArrayList<PackageModel> getPackage() {
		return Package;
	}
	public void setPackage(ArrayList<PackageModel> packageModel) {
		this.Package = packageModel;
	}
	
	public void appendPackageListAtEnd(ArrayList<PackageModel> packageModel) {
		this.Package.addAll(packageModel);
	}
	
	public List<BannerModel> getBanners() {
		return Banners;
	}
	public void setBanners(List<BannerModel> banners) {
		this.Banners = banners;
	}
	
}