package com.kelltontech.maxisgetit.constants.matta;

/**
 * @author arsh.vardhan
 * @modified 20-Aug-2014
 */

public interface MattaConstants {

	String DATA_MATTA_BOOTH_LIST_REQUEST 		= "BOOTH_LIST_REQUEST";
	String DATA_MATTA_BOOTH_DETAIL_REQUEST 		= "BOOTH_DETAIL_REQUEST";
	String DATA_MATTA_PACKAGE_LIST_REQUEST 		= "PACKAGE_LIST_REQUEST";
	String DATA_MATTA_PACKAGE_DETAIL_REQUEST 	= "PACKAGE_DETAIL_REQUEST";
	
	String DATA_MATTA_HALL_LIST_RESPONSE 		= "HALL_LIST_RESPONSE";
	String DATA_MATTA_BOOTH_LIST_RESPONSE 		= "BOOTH_LIST_RESPONSE";
	String DATA_MATTA_BOOTH_DETAIL_RESPONSE 	= "BOOTH_DETAIL_RESPONSE";
	String DATA_MATTA_PACKAGE_LIST_RESPONSE 	= "PACKAGE_LIST_RESPONSE";
	String DATA_MATTA_PACKAGE_DETAIL_RESPONSE 	= "PACKAGE_DETAIL_RESPONSE";
	String DATA_MATTA_SEARCH_FILTER_RESPONSE 	= "SEARCH_FILTER_RESPONSE";
	String DATA_MATTA_BOOTH_LIST_TITLE 			= "BOOTH_LIST_TITLE";
	
	String FLOW_FROM_MATTA_HALL_LIST 			= "MattaHallListActivity";
	String FLOW_FROM_MATTA_BOOTH_LIST 			= "MattaBoothListActivity";
	String FLOW_FROM_MATTA_BOOTH_DETAIL 		= "MattaBoothDetailActivity";
	String FLOW_FROM_MATTA_PACKAGE_LIST 		= "MattaPackageListActivity";
	String FLOW_FROM_MATTA_PACKAGE_DETAIL 		= "MattaPackageDetailActivity";

	// keys for page views.
	String Matta_Hall_Listing 					= "MattaHallListingScreen";							//MattaHallListActivity
	String Matta_Booth_Listing 					= "MattaBoothListingScreen";						//MattaBoothListActivity
	String Matta_Booth_Detail 					= "MattaBoothDetailScreen";							//MattaBoothDetailActivity
	String Matta_Package_Listing 				= "MattaPackageListingScreen";						//MattaPackageListActivity
	String Matta_Package_Detail 				= "MattaPackageDetailScreen";						//MattaPackageDetailActivity
	String Matta_Filter_Search 					= "MattaFilterSearchScreen";						//MattaFilterSearchActivity
	
	String MattaBoothListType					= "BL";
	String MattaPackageListType					= "PL";
	
	String MattaFilterBooth 					= "B";
	String MattaFilterPackage					= "P";
	
	int MAX_RECORD_COUNT = 100000;

}