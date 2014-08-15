package com.kelltontech.maxisgetit.model.matta.packages.detail;

import java.io.Serializable;

/**
 * @author arsh.vardhan
 * @modified 09-Aug-2014
 */
public class PackageDetailResults implements Serializable {

	/**
	 * Auto-generated serialVersionUID
	 */
	private static final long serialVersionUID = -5421295556205238156L;
	
	private String Error_Code;
	private String Error_Message;
	private PackageModel Package;

	public String getError_Code() {
		return Error_Code;
	}

	public void setError_Code(String error_Code) {
		this.Error_Code = error_Code;
	}

	public String getError_Message() {
		return Error_Message;
	}

	public void setError_Message(String error_Message) {
		this.Error_Message = error_Message;
	}

	public PackageModel getPackage() {
		return Package;
	}

	public void setPackage(PackageModel Package) {
		this.Package = Package;
	}

}