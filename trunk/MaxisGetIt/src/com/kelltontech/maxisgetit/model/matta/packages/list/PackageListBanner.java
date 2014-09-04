package com.kelltontech.maxisgetit.model.matta.packages.list;

import java.io.Serializable;

/**
 * @author arsh.vardhan
 * @modified 09-Aug-2014
 */
public class PackageListBanner implements Serializable {

	/**
	 * Auto generated serialVersionUID
	 */
	private static final long serialVersionUID = -5771240837939040296L;

	private String id;
	private String image;
	private String landingUrl;
	private String screenName;
	private String categoryId;
	private String itemId;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public String getLandingUrl() {
		return landingUrl;
	}
	public void setLandingUrl(String landingUrl) {
		this.landingUrl = landingUrl;
	}
	public String getScreenName() {
		return screenName;
	}
	public void setScreenName(String screenName) {
		this.screenName = screenName;
	}
	public String getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}
	public String getItemId() {
		return itemId;
	}
	public void setItemId(String itemId) {
		this.itemId = itemId;
	}

}