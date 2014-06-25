package com.kelltontech.maxisgetit.model.uploadImage;

import org.json.JSONObject;

public class RequestAddNewPOI {

	private String companyName;
	private String companyAddress;
	private String businessType;
	private String companyNumber;
	private String userName;
	private String userNumber;
	private double latitude;
	private double longitude;
	private String imageData;

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getCompanyAddress() {
		return companyAddress;
	}

	public void setCompanyAddress(String companyAddress) {
		this.companyAddress = companyAddress;
	}

	public String getBusinessType() {
		return businessType;
	}

	public void setBusinessType(String businessType) {
		this.businessType = businessType;
	}

	public String getCompanyNumber() {
		return companyNumber;
	}

	public void setCompanyNumber(String companyNumber) {
		this.companyNumber = companyNumber;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserNumber() {
		return userNumber;
	}

	public void setUserNumber(String userNumber) {
		this.userNumber = userNumber;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public String getImageData() {
		return imageData;
	}

	public void setImageData(String imageDate) {
		this.imageData = imageDate;
	}

	public String toJson() {
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("company_name", getCompanyName());
			jsonObject.put("company_address", getCompanyAddress());
			jsonObject.put("business_type", getBusinessType());
			jsonObject.put("company_contact", getCompanyNumber());
			jsonObject.put("user_name", getUserName());
			jsonObject.put("user_number", getUserNumber());
			jsonObject.put("latitude", getLatitude() + "");
			jsonObject.put("longitude", getLongitude() + "");
			jsonObject.put("image_data", getImageData());

		} catch (Exception exception) {
		}
		return jsonObject.toString();
	}
}
