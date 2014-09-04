package com.kelltontech.maxisgetit.model.matta.packages.list;

import java.io.Serializable;

/**
 * @author arsh.vardhan
 * @modified 02-Aug-2014
 */
public class PackageAttrValues implements Serializable {
	
	/**
	 * Auto generated serialVersionUID
	 */
	private static final long serialVersionUID = -3019788944593704846L;

	private String Label;
	private String Value;
	
	public String getLabel() {
		return Label;
	}
	public void setLabel(String label) {
		this.Label = label;
	}
	public String getValue() {
		return Value;
	}
	public void setValue(String value) {
		this.Value = value;
	}

}
