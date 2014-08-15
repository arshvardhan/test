package com.kelltontech.maxisgetit.model.matta.booths.detail;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * @author arsh.vardhan
 * @modified 09-Aug-2014
 */

public class PackagesCollection implements Serializable {
	/**
	 * Auto-generated serialVersionUID
	 */
	private static final long serialVersionUID = -8643786718662823713L;
	
	private ArrayList<PackageList> Package;

	public ArrayList<PackageList> getPackage() {
		return Package;
	}

	public void setPackage(ArrayList<PackageList> Package) {
		this.Package = Package;
	}

}