package com.kelltontech.framework.db;

//http://www.anddev.org/viewtopic.php?t=428
public interface DBManifest {

	// The Android's default system path of your application database.
	String DB_PATH = "/data/data/tnd/databases/";
	String DB_NAME = "tmd.db";
	int DATABASE_VERSION = 1;

	// Table Entries.........................
	int TABLE_INDEX_OF_BANNERDATA = 0;
	// TODO : can we make to go with single but 2-D array....
	// String[] TABLE_NAMES= new
	// String[]{PendingLocationDataHandler.TABLE_NAME};
	// String[] CREATE_QUERIES= new
	// String[]{AppConfigPreHomeTableHandler.CREATE_QUERY,HomeScreenTableHandler.CREATE_QUERY,OffersTableHandler.CREATE_QUERY,AdsTableHandler.CREATE_QUERY};
}
