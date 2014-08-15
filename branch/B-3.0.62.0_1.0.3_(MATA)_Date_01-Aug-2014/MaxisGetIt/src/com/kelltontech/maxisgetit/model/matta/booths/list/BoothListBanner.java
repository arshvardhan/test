package com.kelltontech.maxisgetit.model.matta.booths.list;

import java.io.Serializable;

/**
 * @author arsh.vardhan
 * @modified 02-Aug-2014
 */
public class BoothListBanner implements Serializable {
	/**
	 *  Auto generated serialVersionUID
	 */
	private static final long serialVersionUID = 4086381581028267763L;
	protected String id;
	protected String image;
	protected String landingUrl;
	protected String screenName;
	protected String categoryId;
	protected String itemId;

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