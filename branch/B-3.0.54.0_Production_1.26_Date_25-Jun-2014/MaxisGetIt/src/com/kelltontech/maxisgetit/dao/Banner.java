package com.kelltontech.maxisgetit.dao;

import java.util.ArrayList;

public class Banner {
	
	private ArrayList<BannerUrl> Urls = new ArrayList<BannerUrl>();

	public ArrayList<BannerUrl> getUrls() {
		return Urls;
	}

	public void setUrls(ArrayList<BannerUrl> urls) {
		Urls = urls;
	}

}
