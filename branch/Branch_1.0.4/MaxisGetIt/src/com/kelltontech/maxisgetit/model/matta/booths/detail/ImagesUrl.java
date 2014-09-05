package com.kelltontech.maxisgetit.model.matta.booths.detail;

import java.io.Serializable;
import java.util.ArrayList;

public class ImagesUrl implements Serializable {
	/**
	 * Auto-generated serialVersionUID
	 */
	private static final long serialVersionUID = 637678441055848889L;
	
	private ArrayList<String> Image;

	public ArrayList<String> getImage() {
		return Image;
	}

	public void setImage(ArrayList<String> image) {
		Image = image;
	}

}
