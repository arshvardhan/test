package com.kelltontech.maxisgetit.model.matta.packages.detail;

import java.io.Serializable;
import java.util.List;

public class ImagesUrl implements Serializable {
	/**
	 * Auto-generated serialVersionUID
	 */
	private static final long serialVersionUID = 2017337979349954217L;
	
	private List<String> Image;

	public List<String> getImage() {
		return Image;
	}

	public void setImage(List<String> image) {
		this.Image = image;
	}
	
}
