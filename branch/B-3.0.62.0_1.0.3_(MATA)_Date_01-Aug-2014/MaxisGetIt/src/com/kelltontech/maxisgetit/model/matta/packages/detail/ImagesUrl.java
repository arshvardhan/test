package com.kelltontech.maxisgetit.model.matta.packages.detail;

import java.io.Serializable;
import java.util.ArrayList;

public class ImagesUrl implements Serializable {
	/**
	 * Auto-generated serialVersionUID
	 */
	private static final long serialVersionUID = 2017337979349954217L;
	
	private ArrayList<String> Image;

	public ArrayList<String> getImage() {
		return Image;
	}

	public void setImage(ArrayList<String> image) {
		this.Image = image;
	}
	
}
