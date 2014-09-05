package com.kelltontech.maxisgetit.model.matta.packages.list;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * @author arsh.vardhan
 * @modified 02-Aug-2014
 */
public class PackageAttributeGroup implements Serializable {
	
	/**
	 * Auto generated serialVersionUID
	 */

	private static final long serialVersionUID = -1946893760388378211L;

	private ArrayList<PackageAttrValues> Values;

	public ArrayList<PackageAttrValues> getValues() {
		return Values;
	}

	public void setValues(ArrayList<PackageAttrValues> values) {
		this.Values = values;
	}
	
}
