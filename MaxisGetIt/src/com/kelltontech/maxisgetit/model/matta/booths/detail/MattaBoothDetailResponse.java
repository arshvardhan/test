package com.kelltontech.maxisgetit.model.matta.booths.detail;

import java.io.Serializable;

/**
 * @author arsh.vardhan
 * @modified 02-Aug-2014
 */
public class MattaBoothDetailResponse implements Serializable {

	/**
	 * Auto-generated serialVersionUID
	 */
	private static final long serialVersionUID = -4771851908315746751L;
	
	private BoothDetailResults Results;

	public BoothDetailResults getResults() {
		return Results;
	}

	public void setResults(BoothDetailResults Results) {
		this.Results = Results;
	}

}
