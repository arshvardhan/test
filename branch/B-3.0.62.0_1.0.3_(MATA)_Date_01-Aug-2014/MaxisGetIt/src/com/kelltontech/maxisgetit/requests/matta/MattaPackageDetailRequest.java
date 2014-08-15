package com.kelltontech.maxisgetit.requests.matta;

import java.io.Serializable;

/**
 * @author arsh.vardhan
 * @modified 14-Aug-2014
 */
public class MattaPackageDetailRequest implements Serializable {
	/**
	 * Auto-generated serialVersionUID
	 */
	private static final long serialVersionUID = -6797073551429377536L;
	private String packageId;
	private String source;
	

	/*public MattaPackageDetailRequest(Context context, String packageId, String source) {
		super(context);
		this.packageId = packageId;
		this.source = source;
	}*/

	public String getPackageId() {
		return packageId;
	}

	public void setPackageId(String packageId) {
		this.packageId = packageId;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}
	
}
