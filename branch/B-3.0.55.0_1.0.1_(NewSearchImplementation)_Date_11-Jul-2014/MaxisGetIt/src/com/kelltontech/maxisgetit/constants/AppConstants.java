package com.kelltontech.maxisgetit.constants;

/**
 * @author arsh.vardhan
 * @modified 30-Jul-2014
 */

public interface AppConstants {
	
	boolean PRODUCTION = false;
	
	/**
	* API_VERSION : "1.71" is given for App Version 1.27 (Vserv Integration)
	* API_VERSION : "1.7" is given for App version 1.26 and provided force update on Splash Screen
	* API_VERSION : "1.6" is given for App version 1.25 and provided force update on DealListing Screen
	*/	
	
	String API_VERSION = "1.8"; // Given for App Version 1.28 (Global Search Enhancement, Tappable Featured Banner, Paid companies Ad on Free Companies Detail Page)
	
	// String BASE_SERVER 			= "http://192.168.13.3/webservice/trunk/"; 						// local Aaditya
	// String BASE_SERVER 			= "http://192.168.13.144/webservice_dev/branch_search1.3/";
	// String BASE_SERVER 			= "http://192.168.13.22/webservice/trunk/"; 					// local Ankesh
	// String BASE_SERVER 			= "http://192.168.13.16/webservice/trunk/"; 					// local Ankesh
	// String BASE_SERVER 			= "http://192.168.13.22/webservice/dev1.2/"; 					// local Ankesh for Force Update
	// String BASE_SERVER 			= "http://test.kelltontech.com/getitfms/"; 						// live dev
	// String BASE_SERVER 			= "http://test.kelltontech.com/getitfms_qa/"; 					// QA
	// String BASE_SERVER 			= "https://staging.findit.com.my/";								// staging new
	// String BASE_SERVER 			= "http://203.115.222.57:8070/"; 								// staging 8070
	// String BASE_SERVER 			= "http://203.115.222.69/";										// Pre Production
	// String BASE_SERVER 			= "http://192.168.13.16/webservice/trunk/";						// local diwakar
	// String BASE_SERVER 			= "http://192.168.13.22/webserviceNew/dev_build_ver1.1/"; 		// local ankesh for deal
	// String BASE_SERVER 			= "http://192.168.13.63/webservice/trunk/";   					// Session Tracking Test
	 String BASE_SERVER 			= "http://203.115.222.103/"; 									// staging
	// String BASE_SERVER 			= "http://203.115.222.57/"; 									// pre-production
//	 String BASE_SERVER 			= "https://webservice.findit.com.my/";							// Production
	String BASE_URL 				= BASE_SERVER + "restapi/";
	String BASE_URL_CONTEST 		= BASE_SERVER + "restapicontest/";
	String BASE_URL_VIDEOS 			= "http://";
	String LIFE_CYCLE_WEBVIEW_SERVER = "http://203.115.222.57:8080/users/didLifeCycleApp/";
	
	/**
	 * string constant with following common parameters and their values- 1.
	 * platform=Andi 2. language_code=en
	 */
	String URL_ENCODED_PARAMS 		= "plateform=Andi&language_code=en";

	/**
	 * int constant for request timeout
	 */
	int MILLIS_1_MINUTE 			= 60000;
	int MILLIS_3_MINUTE 			= 3 * MILLIS_1_MINUTE;

	// String GMAP_DEBUG_KEY_V2="AIzaSyCyjqUmApNKfUjjlrrsnGiFNMEi17I7obM";
	// String GMAP_API_KEY_V2="AIzaSyCANUlQsVTaAop_W3GDFWQ72U3VJzwYOcU";
	
	String GMAP_API_KEY_V2 = "AIzaSyD_YqXS8m5-0B6KJNo6ALDZSgDKNXy-VSM";
	String FLURRY_KEY = "TGQV9Y2G8C9Y5P4C367P";
	int MAX_RECORD_COUNT = 100;
	
	/**
	 * FACEBOOK_APP_ID: "254393288080868" (To track App installations through Android App Ads on Facebook)	
	 */ 
	
	String FACEBOOK_APP_ID 			= "254393288080868";
	String COMP_LIST_DATA 			= "COMP_LIST_DATA";
	String COMP_DETAIL_DATA 		= "COMP_DETAIL_DATA";
	String OUTLET_DATA 				= "OUTLET_LIST_DATA";
	String OUTLET_DETAIL_DATA 		= "OUTLET_DETAIL_DATA";
	String COMP_ID 					= "COMP_ID";
	String DATA_CAT_LIST 			= "DATA_CAT_LIST";
	String DATA_SUBCAT_RESPONSE 	= "SUBCAT_RESPONSE";
	String THUMB_URL 				= "THUMB_URL";
	String SELECTED_CATEGORY_ID 	= "CATEGORY_ID_DATA";
	String DATA_LIST_REQUEST 		= "COMBINED_LIST_REQ";
	String FB_PAGE_URL 				= "https://www.facebook.com/pages/Findit333/532512713499660"; 	
	// https://www.facebook.com/pages/Getit-Transnational-Pvt-Ltd/167314303441395?fref=ts";
	String TWITTER_PAGE_URL 		= "http://twitter.com/findit333";								
	// "https://twitter.com/GetitTrans";
	String TNC_PAGE_URL 			= AppConstants.BASE_URL + "termsCondition.xml?language_code=en&plateform=Andi";
	String TNC_CONTEST_PAGE_URL 	= AppConstants.BASE_URL_CONTEST + "viewTermCondition?plateform=Andi&language_code=en";
	String REFINE_CAT_RESPONSE 		= "REFINE_CATEGORY_RESPONSE";
	String REFINE_ATTR_RESPONSE		= "REFINE_ATTRIBUTE_RESPONSE";
	String TEMPLET_DATA 			= "TEMPLET_RESPONSE_DATA";
	String TYPE_BY_CATEGORY_DATA 	= "TYPE_BY_CATEGORY_RESPONSE_DATA";
	String ACTION_IDENTIFIER 		= "lOGIN_ACTION";
	String USER_DETAIL_DATA 		= "User_detail_Data";
	String SELECTED_CAT_DATA 		= "SELECTED_CATEGORY_INFO";
	String LOCAL_SEARCH_RESPONSE 	= "lOCAL SEARCH DATA";
	String CLASSIFIED_LIST_RESPONSE = "CLASSIFIED LIST RESPONSE";
	String MY_DEALS_RESPONSE 		= "my_deals_resp";
	String USER_ID 					= "USER_ID";
	String DEAL_CATEGORY_RESPONSE 	= "DEAL_CAT_RESPONSE";
	String NOTIFICATION_TEXT 		= "aD_nOTIFICATION";
	String RESET 					= "RESET";
	String MY_LEADS_RESPONSE 		= "MY_LEADS_RESPONSE";
	String KEYWORD_CITY_OF_REFINE 	= "city_name";
	String ROUTE_DETAIL 			= "rOUTE_DETAIL_RESPONSE";
	String LOCALITY_DAO_DATA 		= "lOCALITY_DAO";
	String MOBILE_NUMBER 			= "mOBILE_NUMBER";
	String LOCATION_BEARING 		= "LOCATION_BEARING";
	String COMP_DETAIL_LIST 		= "COMP_DETAIL_LIST";
	String IS_SEARCH_KEYWORD 		= "IS_SEARCH_KEYWORD";
	String MAP_ALL_TITLE 			= "MAP_ALL_TITLE";
	String MAP_MODE 				= "MAP_MODE";
	String SPLIT_STRING 			= "~#~";
	String IS_DEAL_LIST 			= "IS_DEAL_LIST";
	String COMP_TYPE_DEAL 			= "Deal";
	String IS_FROM_DETAIL 			= "IS_FROM_DETAIL";
	String IS_FOR_ERROR_LOG 		= "IS_FOR_ERROR_LOG";
	String IS_FROM_COMP_DETAIL_ADD_FAV = "IS_FROM_COMP_DETAIL_ADD_FAV";
	String VIDEO_URL 				= "VIDEO_URL";
	String PHOTO_CONTEST_CAT_ID 	= "PHOTO_CONTEST";
	String GLOBAL_SEARCH_KEYWORD 	= "GLOBAL_SEARCH_KEYWORD";
	String CATEGORY_ID 				= "CATEGORY_ID";

	String LIFE_CYCLE_URL 			= "LIFE_CYCLE_URL";
	
	
	String FLOW_FROM_COMBIND_LIST 	= "CombindListActivity";
	String FLOW_FROM_SUB_CATEGORY_LIST = "SubCategoryListActivity";
	String FLOW_FROM_COMPANY_DETAIL = "CompanyDetailActivity";
	String FLOW_FROM_DEAL_DETAIL 	= "DealDetailActivity";

	String DEAL_DETAIL_SCREEN 		= "dealDetail";
	String COMPANY_DETAIL_SCREEN 	= "companyDetail";
	String COMPANY_LISTING_SCREEN 	= "companyListing";
	String SEARCH_SCREEN 			= "search";
	// TnC Extra
	String TNC_FROM 				= "TNC_FROM";

	String GROUP_ACTION_TYPE_DEAL 						= "D";
	String GROUP_ACTION_TYPE_LIST 						= "L";
	String GROUP_ACTION_TYPE_ATTRIBUTE 					= "A";
	String GROUP_ACTION_TYPE_CATEGORY_LIST_FOR_GROUP 	= "R";

	String GROUP_TYPE_CATEGORY 							= "G";
	String GROUP_TYPE_SUB_CATEGORY 						= "C";

	String REGISTRATION_RESPONSE_MESSAGE 				= "REGISTRATION_TEXT";

	/******** Activity Result Constants *********/
	int AR_AUTH_REQUEST_SUCCESS 						= 1;
	int AR_AUTH_REQUEST_FAILURE 						= 2;
	int AR_REPORT_ERROR_SUCCESS 						= 3;
	int AR_REPORT_ERROR_FAILURE 						= 4;

	/*
	 * FullMapActivity Map Mode
	 */
	int MAP_DRIVING_MODE 								= 0;
	int MAP_WALKING_MODE 								= 1;

	/**
	 * Constants for Photo Contest app
	 */
	int PAGE_SIZE_POI_LIST_BY_DISTANCE 					= 10;
	int PAGE_SIZE_POI_LIST_SEACH_RESULTS 				= 10;

	int CAMERA_REQUEST 									= 1999;
	int GALLERY_REQUEST 								= 2000;

	String LIST_RESPONSE_PARCEL 						= "list_response_parcel";
	String LIST_RESPONSE_TYPE 							= "list_response_type";
	String CID_KEY 										= "cid_key";
	String CATEGORY_ID_KEY 								= "category_id_key";
	String COMPANY_NAME_KEY 							= "company_name_key";
	String EXTRA_SELECTED_CATEGORY 						= "extra_selected_category";
	String EXTRA_SEARCH_KEYWORD 						= "extra_search_keyword";
	String ADD_NEW_POI_KEY 								= "add_new_poi_key";
	String POST_IMAGE_REQUEST_KEY 						= "post_image_request_key";

	// TnC Extra
	int TNC_FROM_COMP 			= 0;
	int TNC_FROM_CONTEST 		= 1;
	int TNC_FROM_DEAL 			= 2;

	// Login flow in Report an error flow
	int ACTION_MY_ACCOUNT 		= 1110;
	int ACTION_SELL_POST 		= 1111;
	int ACTION_REPORT_ERROR 	= 1112;
	int ACTION_ADD_FAV 			= 1113;

	int CITY_REQUEST 			= 999;
	int LOCALITY_REQUEST 		= 1000;
	int LIFE_CYCLE_SCREEN 		= 5998;
	int SEARCH_CRITERIA_REQUEST = 1001;

	/**
	 * LogCat Tags
	 */
	
	String FINDIT_DEBUG_TAG 	= "Findit debug tag";
	String FINDIT_INFO_TAG 		= "Findit info tag";
	String FINDIT_ERROR_TAG 	= "Findit error tag";

	/**
	 * LogCat Error Messages
	 */
	
	// Error messages for UI Activities
	String DATE_ERROR_MSG 								= "Invalid time fomrmat. Use UTC timestamp.";
	String LOCATION_RECEIVER_UNREGISTER_ERROR_MSG 		= "Location receiver unregisteration is not successful.";
	String LOCATION_SERVICE_NOT_STOP_ERROR_MSG 			= "Location service is not stopped successfully.";
	String CONTEST_UPLOAD_IMAGE_ERROR_MSG 				= "Error occurred while uploading image.";
	String UPDATE_GEO_TAG_ERROR_MSG 					= "Error occurred while updating geo tag.";
	String DATA_VALIDATION_ERROR_MSG 					= "Error occurred while validating jason data.";
	String IMAGE_SIZE_TOO_LARGE_ERROR_MSG 				= "Image size too large to upload.";
	String VOUCHER_COUNT_ERROR_MSG 						= "Invalid voucher count.";
	String REDEEM_PRICE_ERROR_MSG 						= "Invalid redeem price.";
	String POST_DEAL_UPLOAD_IMG_NOT_FOUND_ERROR_MSG 	= "Post deal upload image not found.";
	String CURRENT_LOCATION_NOT_SET_ERROR_MSG 			= "Current location is not set successfully.";

	// Error messages for Controllers
	String REQUEST_SERVICE_ERROR_MSG 					= "Request service generated an error.";
	String RESPONSE_SERVICE_ERROR_MSG 					= "Response service generated an error.";
	String GOOGLE_MAP_DIRECTION_ERROR_MSG 				= "Error occured while getting direction from google map.";

	// Error messages for Services
	String GPS_LOCATION_FINDER_ERROR_MSG 				= "Error occured while getting current location from GPS location finder.";
	String UNREGISTER_GPS_LOCATION_FINDER_ERROR_MSG 	= "Error occured while unregistering GPS location finder.";
	String UNREGISTER_NETWORK_LOCATION_FINDER_ERROR_MSG = "Error occured while unregistering Network location finder.";

	//Parameter for pageview
	String KEY_PAGE_REVIEW 								= "pageView";
	
	// keys for page views.
	String Splash_Screen 								= "SplashScreen";									//SplashActivity
	String Home_Screen 									= "HomeScreen";										//HomeActivity
	String Category_Screen 								= "CategoryScreen";									//SubCategoryListActivity
	String Company_listing 								= "CompanyListScreen";								//CombinedListActivity
	String Company_detail 								= "CompanyDetailScreen";							//CompanyDetailActivity
	String RatenReview 									= "RateAndReviewScreen";							//RateCOmpanyActivity
	String ReportanError 								= "ReportAnErrorScreen";							//ReportErrorActivity
	String Modify_screen 								= "ModifyScreen";									//ModifySearchActivity
	String Filter_screen 								= "FilterScreen";        					// No Screen
	String Deal_Listing 								= "DealListingScreen";								//DealsActivity
	String Deal_Detail 									= "DealDetailScreen";								//DealDetailActivity
	String Login_screen 								= "LoginScreen";									//LoginActivity
	String Registration_Screen 							= "RegistrationScreen";								//RegistrationActivity
	String Forget_Password 								= "ForgetPasswordScreen";							//ForgetPasswordActivity
	String MyAccount 									= "MyAccountScreen";								//MyAccountActivity
	String Deal_Post 									= "DealPostScreen";									//DealPostActivity
	String MyFavourite 									= "MyFavouritesScreen";								//FavCompanyListActivity
	String ChangePassword 								= "ChangePasswordScreen";							//EditProfileActivity
	String Add_Post 									= "AddPostScreen";									//SaleTemplateActivity
	String ViewOnMap 									= "ViewOnMapScreen";								//ViewAllOnMapActivity
	String MapDirection 								= "MapDirectionScreen";						// No Screen
	String CompassDirection 							= "CompassDirectionScreen";							//CompassDirectionActivity
	String MyCompany 									= "MyCompanyScreen";								//LocalSearchActivity
	String MyClassifieds 								= "MyClassifiedScreen";								//ClassifiedScrollActivity
	String MyDeals 										= "MyDealScreen";									//MyDealsActivity
	String TnC 											= "TermAndConditionsScreen";						//TermsAndConditionActivity, TnCActivity
	String GetInfo 										= "GetInfoScreen";									//GetitInfoActivity
	String AppAuthentication 							= "AppAuthenticationScreen";						//AppAuthenticationActivity
	String AppAuthenticationCode 						= "AppAuthenticationCodeScreen";					//AppActivationActivity
	String CityScreen 									= "CityScreen";										//AdvanceSelectCityActivity
	String Locality_Screen 								= "LocalityScreen";									//AdvanceSelectLocalityActivity
	String Template_Sell_Buy 							= "TemplateSellBuyScreen";							//SaleTemplateTypeActivity
	String AfterSave_Template 							= "AfterSaveTemplateScreen";						//AdPostNotificationActivity
	String Company_Review 								= "CompanyReviewScreen";							//could not find any occurrences
	String PhotoContest_Home 							= "PhotoContestHomeScreen";							//PhotoContestHomeActivity 
	String POI_Screen 									= "PhotoContestPOIScreen";							//ContestPOISearchActivity
	String ListByCategory 								= "ListByCategoryScreen";							//ContestPoiListActivity
	String ListByDistance 								= "ListByDistanceScreen";							//ContestPoiListActivity
	String Add_New_POI 									= "AddNewPOIScreen";								//ContestAddNewPOIActivity
	String Add_Images_In_Existing_POI 					= "AddImagesInExistingPOIScreen";					//ContestUploadImageActivity
	String Search_Screen 								= "PhotoContestSearchScreen";						//ContestPoiListActivity
	String Screen_CompanyDetailMap 						= "CompanyDetailMapScreen";							//CompanyDetailMapActivity
	String Screen_videoPlay        						= "VideoPlayScreen";								//VideoPlayActivity		
	String Screen_DealOutletModify 						= "DealOutletModifyScreen";							//RefineOutletActivity
	String Screen_DealOutletList   						= "DealOutletListScreen";							//ViewAllOutletActivity
	//	String Screen_lifeCycle        					= "DidLifeCycle";							//
	String Screen_AfterUploadeImageContest 				= "AfterUploadeImageContestScreen"; 				//ContestUploadMoreActivity
	//	String Screen_LocalSearchLead    				= "LocalSearchLeadScreen";					//
	//	String Screen_CodeVerification   				= "CodeVerificationScreen";					//
	String Screen_DashBoardLifeCycle 					= "DashBoardLifeCycleScreen";				//
	String DashBoardLifeCycle 							= "DidLifeCycle";
	String Screen_DealTnC 								= "DealTnCScreen";									//TermsAndConditionActivity
	String Mall_listing 								= "MallListScreen";									//MallListActivity
	String Stamp_Company_listing 						= "StampCompanyListScreen";							//CombinedListActivity

}
