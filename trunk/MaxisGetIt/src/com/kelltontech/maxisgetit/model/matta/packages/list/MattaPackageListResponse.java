package com.kelltontech.maxisgetit.model.matta.packages.list;

import java.io.Serializable;

/**
 * @author arsh.vardhan
 * @modified 09-Aug-2014
 */
public class MattaPackageListResponse implements Serializable {
	/**
	 * Auto generated serialVersionUID
	 */
	private static final long serialVersionUID = 9165729041870841465L;

	private PackageListResults Results;

	public PackageListResults getResults() {
		return Results;
	}

	public void setResults(PackageListResults results) {
		this.Results = results;
	}

}