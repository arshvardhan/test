package com.kelltontech.maxisgetit.model.matta.booths.detail;

import java.io.Serializable;
import java.util.List;

/**
 * @author arsh.vardhan
 * @modified 09-Aug-2014
 */

public class PackagesCollection implements Serializable {
	/**
	 * Auto-generated serialVersionUID
	 */
	private static final long serialVersionUID = -8643786718662823713L;
	
	private List<PackageList> Package;

	public List<PackageList> getPackage() {
		return Package;
	}

	public void setPackage(List<PackageList> Package) {
		this.Package = Package;
	}

}