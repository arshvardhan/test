package com.kelltontech.maxisgetit.response;

import com.kelltontech.framework.model.IModel;
import com.kelltontech.framework.model.MaxisResponse;

public class ImageDataResponse extends MaxisResponse implements IModel {
	private String imagename;

	public String getImagename() {
		return imagename;
	}

	public void setImagename(String imagename) {
		this.imagename = imagename;
	}

}
