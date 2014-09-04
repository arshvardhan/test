package com.kelltontech.maxisgetit.model.matta.booths.list;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * @author arsh.vardhan
 * @modified 12-Aug-2014
 */
public class AttributeGroup implements Serializable {
	
	/**
	 *  Auto generated serialVersionUID
	 */
	private static final long serialVersionUID = -6817470855426838617L;
	private ArrayList<BoothAttrValues> Values;

	public ArrayList<BoothAttrValues> getValues() {
		return Values;
	}

	public void setValues(ArrayList<BoothAttrValues> values) {
		this.Values = values;
	}
	
}