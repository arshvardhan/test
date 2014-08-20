package com.kelltontech.maxisgetit.model.matta.packages.detail;

import java.io.Serializable;

/**
 * @author arsh.vardhan
 * @modified 20-Aug-2014
 */
public class MattaPackageDetailResponse implements Serializable {

	/**
	 * Auto-generated serialVersionUID
	 */
	private static final long serialVersionUID = -8940203345473241457L;

	private PackageDetailResults Results;

	public PackageDetailResults getResults() {
		return Results;
	}

	public void setResults(PackageDetailResults Results) {
		this.Results = Results;
	}

}
