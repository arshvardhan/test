package com.kelltontech.maxisgetit.model.matta.booths.list;

import java.io.Serializable;

/**
 * @author arsh.vardhan
 * @modified 13-Aug-2014
 */
public class MattaBoothListResponse implements Serializable {

	/**
	 * Auto generated serialVersionUID
	 */
	private static final long serialVersionUID = 1635217377537037624L;
	
	private BoothListResults Results;

	public BoothListResults getResults() {
		return Results;
	}

	public void setResults(BoothListResults Results) {
		this.Results = Results;
	}

}