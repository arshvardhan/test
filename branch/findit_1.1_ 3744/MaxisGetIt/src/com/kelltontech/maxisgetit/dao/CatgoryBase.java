package com.kelltontech.maxisgetit.dao;

public class CatgoryBase {
	protected String mCategoryId;
	protected String mCategoryTitle;
	protected String mIconUrl;
	protected String mThumbUrl;
	protected String mGroupActionType;
	protected String mGroupType;

	public String getCategoryId() {
		return mCategoryId;
	}

	public void setCategoryId(String mCategoryId) {
		this.mCategoryId = mCategoryId;
	}

	public String getCategoryTitle() {
		return mCategoryTitle;
	}

	public void setCategoryTitle(String mCategoryTitle) {
		this.mCategoryTitle = mCategoryTitle;
	}

	public String getIconUrl() {
		return mIconUrl;
	}

	public void setIconUrl(String mIconUrl) {
		this.mIconUrl = mIconUrl;
	}

	public String getThumbUrl() {
		return mThumbUrl;
	}

	public void setThumbUrl(String mThumbUrl) {
		this.mThumbUrl = mThumbUrl;
	}
	public String getMgroupType() {
		return mGroupType;
	}

	public void setMgroupType(String mgroupType) {
		this.mGroupType = mgroupType;
	}

	public String getmGroupActionType() {
		return mGroupActionType;
	}

	public void setmGroupActionType(String mGroupActionType) {
		this.mGroupActionType = mGroupActionType;
	}
}
