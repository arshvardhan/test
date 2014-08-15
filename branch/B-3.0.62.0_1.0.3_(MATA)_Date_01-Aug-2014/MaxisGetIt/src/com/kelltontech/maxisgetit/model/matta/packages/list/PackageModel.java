package com.kelltontech.maxisgetit.model.matta.packages.list;

import java.io.Serializable;

/**
 * @author arsh.vardhan
 * @modified 02-Aug-2014
 */
public class PackageModel implements Serializable {
	
	/**
	 * Auto generated serialVersionUID
	 */
	private static final long serialVersionUID = 4403051937863097868L;

	private String Id;
	private String Title;
	private String CName;
	private String L2Cat;
	private String L3Cat;
	private String Image;
	private String source;
	private PackageAttributeGroup Attribute_Group;
	
	public String getId() {
		return Id;
	}
	public void setId(String id) {
		Id = id;
	}
	public String getTitle() {
		return Title;
	}
	public void setTitle(String title) {
		Title = title;
	}
	public String getCName() {
		return CName;
	}
	public void setCName(String cName) {
		CName = cName;
	}
	public String getL2Cat() {
		return L2Cat;
	}
	public void setL2Cat(String l2Cat) {
		L2Cat = l2Cat;
	}
	public String getL3Cat() {
		return L3Cat;
	}
	public void setL3Cat(String l3Cat) {
		L3Cat = l3Cat;
	}
	public String getImage() {
		return Image;
	}
	public void setImage(String image) {
		Image = image;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public PackageAttributeGroup getAttribute_Group() {
		return Attribute_Group;
	}
	public void setAttribute_Group(PackageAttributeGroup attribute_Group) {
		Attribute_Group = attribute_Group;
	}

}