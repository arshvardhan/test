package com.kelltontech.maxisgetit.model.matta.booths.list;

import java.io.Serializable;

/**
 * @author arsh.vardhan
 * @modified 02-Aug-2014
 */
public class BoothAttrValues implements Serializable {
	/**
	 *  Auto generated serialVersionUID
	 */
	private static final long serialVersionUID = -2703923686557295055L;
	private String Label;
	private String Value;

	public String getLabel() {
		return Label;
	}

	public void setLabel(String Label) {
		this.Label = Label;
	}

	public String getValue() {
		return Value;
	}

	public void setValue(String Value) {
		this.Value = Value;
	}

}
