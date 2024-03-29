package com.kelltontech.maxisgetit.response;

import android.os.Parcel;
import android.os.Parcelable;

import com.kelltontech.framework.model.IModel;
import com.kelltontech.framework.model.MaxisResponse;
import com.kelltontech.maxisgetit.dao.CategoryRefine;
import com.kelltontech.maxisgetit.dao.CompanyDesc;

public class ImageDataResponse extends MaxisResponse implements IModel, Parcelable  {
	private String imagename;
	private String imageId;
	
	public ImageDataResponse() {
	}
	
	public String getImageId() {
		return imageId;
	}

	public void setImageId(String imageId) {
		this.imageId = imageId;
	}

	public ImageDataResponse(Parcel in) {
		imagename = in.readString();
		imageId = in.readString();
	}
	
	public String getImagename() {
		return imagename;
	}

	public void setImagename(String imagename) {
		this.imagename = imagename;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(imagename);
		dest.writeString(imageId);
	}

}

