package com.kelltontech.maxisgetit.dao;

public class PostDealCityOrLoc {

	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		if (name != null)
			return name;
		return super.toString();
	}
}