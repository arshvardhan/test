package com.kelltontech.maxisgetit.model.uploadImage;

import org.json.JSONObject;

public class RequestUploadPhoto {
	private String title;
	private String name;
	private String categoryId;
	public String getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}
	public String getcId() {
		return cId;
	}
	public void setcId(String cId) {
		this.cId = cId;
	}
	private String cId;
	private String number;
	private double latitude; 
	private double longitude;
	private String imageData;
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
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
				JSONObject jsonObject=new JSONObject();
		try{
			jsonObject.put("image_title", getTitle()); 
			jsonObject.put("user_name", getName());
			jsonObject.put("user_number", getNumber());
			jsonObject.put("cat_id", getCategoryId());
			jsonObject.put("cid", getcId());
			JSONObject jsonObject2=new JSONObject();
			jsonObject.put("image_loc_info", jsonObject2);
			jsonObject2.put("image_lat", getLatitude()+"");
			jsonObject2.put("image_long", getLongitude()+"");
			jsonObject.put("image_data",getImageData());
			
		}
		catch(Exception exception){}
		return jsonObject.toString();
	}
}
