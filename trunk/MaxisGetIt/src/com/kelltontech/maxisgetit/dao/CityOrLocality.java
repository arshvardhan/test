package com.kelltontech.maxisgetit.dao;

public class CityOrLocality {
	private int id;
	private String name;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

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
