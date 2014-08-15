package com.kelltontech.maxisgetit.model.matta.packages.list;

import java.io.Serializable;
import java.util.List;

/**
 * @author arsh.vardhan
 * @modified 02-Aug-2014
 */
public class BannerModel implements Serializable {
	/**
	 * Auto generated serialVersionUID
	 */
	private static final long serialVersionUID = 935746828438036153L;
	
	private List <PackageListBanner> Banner;

	public List<PackageListBanner> getBanner() {
		return Banner;
	}

	public void setBanner(List<PackageListBanner> banner) {
		Banner = banner;
	}

}