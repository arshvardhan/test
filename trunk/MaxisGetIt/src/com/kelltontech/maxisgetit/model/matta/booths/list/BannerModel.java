package com.kelltontech.maxisgetit.model.matta.booths.list;

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
	
	private List <BoothListBanner> Banner;

	public List<BoothListBanner> getBanner() {
		return Banner;
	}

	public void setBanner(List<BoothListBanner> banner) {
		this.Banner = banner;
	}

}