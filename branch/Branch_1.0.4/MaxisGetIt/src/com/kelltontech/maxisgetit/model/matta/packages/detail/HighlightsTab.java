package com.kelltontech.maxisgetit.model.matta.packages.detail;

import java.io.Serializable;

/**
 * @author arsh.vardhan
 * @modified 08-Aug-2014
 */
public class HighlightsTab implements Serializable {

	/**
	 *  Auto-generated serialVersionUID
	 */
	private static final long serialVersionUID = -8734075741181713015L;

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