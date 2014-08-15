package com.kelltontech.maxisgetit.model.matta.booths.detail;

import java.io.Serializable;

/**
 * @author arsh.vardhan
 * @modified 08-Aug-2014
 */

public class PackageList implements Serializable {
	/**
	 *  Auto-generated serialVersionUID
	 */
	private static final long serialVersionUID = 9113590202476165849L;
	
	private String Id;
	private String Key;
	private String Value;
	private String Image;

	public String getId() {
		return Id;
	}

	public void setId(String Id) {
		this.Id = Id;
	}
	
	public String getKey() {
		return Key;
	}

	public void setKey(String key) {
		this.Key = key;
	}

	public String getValue() {
		return Value;
	}

	public void setValue(String value) {
		this.Value = value;
	}

	public String getImage() {
		return Image;
	}

	public void setImage(String image) {
		this.Image = image;
	}

}