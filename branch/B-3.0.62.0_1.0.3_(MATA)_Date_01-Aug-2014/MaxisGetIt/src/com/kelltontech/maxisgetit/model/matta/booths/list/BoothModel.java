package com.kelltontech.maxisgetit.model.matta.booths.list;

import java.io.Serializable;

/**
 * @author arsh.vardhan
 * @modified 12-Aug-2014
 */
public class BoothModel implements Serializable {
	/**
	 *  Auto generated serialVersionUID
	 */
	private static final long serialVersionUID = 9164063875659249012L;
	private String BoothLocation;
	private String CName;
	private String CId;
	private String Itmid;
	private String L2Cat;
	private String L3Cat;
	private String Image;
	private String Source;
	private AttributeGroup Attribute_Group;

	public String getL3Cat() {
		return L3Cat;
	}

	public void setL3Cat(String L3Cat) {
		this.L3Cat = L3Cat;
	}

	public String getBoothLocation() {
		return BoothLocation;
	}

	public void setBoothLocation(String BoothLocation) {
		this.BoothLocation = BoothLocation;
	}

	public String getCName() {
		return CName;
	}

	public void setCName(String CName) {
		this.CName = CName;
	}

	public String getL2Cat() {
		return L2Cat;
	}

	public void setL2Cat(String L2Cat) {
		this.L2Cat = L2Cat;
	}

	public String getCId() {
		return CId;
	}

	public void setCId(String CId) {
		this.CId = CId;
	}
	
	public String getItmid() {
		return Itmid;
	}

	public void setItmid(String itmid) {
		this.Itmid = itmid;
	}

	public AttributeGroup getAttribute_Group() {
		return Attribute_Group;
	}

	public void setAttribute_Group(AttributeGroup attribute_Group) {
		this.Attribute_Group = attribute_Group;
	}

	public String getImage() {
		return Image;
	}

	public void setImage(String image) {
		this.Image = image;
	}

	public String getSource() {
		return Source;
	}

	public void setSource(String source) {
		this.Source = source;
	}
	
}