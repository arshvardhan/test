package com.kelltontech.maxisgetit.dao;

public class GPS_Data {
	 private static double latitude = 0;
	 private static double longitude = 0;
//	private static double latitude = 3.132869;// 3.157861948;
//	private static double longitude = 101.683909;// 101.699852;

	public static void resetCordinates() {
		latitude = 0;
		longitude = 0;
	}

	 public static void setLatitude(double lat) {
	 latitude = lat;
	 }
	
	 public static void setLongitude(double longi) {
	 longitude = longi;
	 }
//	public static void setLatitude(double lat) {
//		latitude = 3.132869;// 3.157861948;
//	}
//
//	public static void setLongitude(double longi) {
//		longitude = 101.683909;// 101.699852;
//	}

	public static double getLatitude() {
		return latitude;
	}

	public static double getLongitude() {
		return longitude;
	}
}
