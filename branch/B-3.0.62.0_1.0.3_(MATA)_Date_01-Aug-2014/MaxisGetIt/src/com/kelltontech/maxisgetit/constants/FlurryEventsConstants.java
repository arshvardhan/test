package com.kelltontech.maxisgetit.constants;

/**
 * 
 * @author arshvardhan.atreya Created on: 08-11-2013 Modified on:12-11-2013
 *         Description: FlurryEventsConstants class includes all the constants
 *         to define logs for FlurryAgent analytics tool
 * 
 */

public class FlurryEventsConstants {

	/*
	 * Event constants for flurry
	 */
	// Splash Screen
	public static final String APPLICATION_LAUNCH = "Application Launch";

	// Header
	public static final String BACK_CLICK = "Back icon is clicked";
	public static final String GO_TO_HOME_CLICK = "GoToHome icon is clicked";
	public static final String HOME_SEARCH_CLICK = "Search icon is clicked";
	public static final String SHOW_PROFILE_CLICK = "ShowProfile icon is clicked";

	// Footer
	public static final String FACEBOOK_CLICK = "Facebook icon is clicked";
	public static final String TWITTER_CLICK = "Twitter icon is clicked";

	// Home Screen, Category, and Sub-Category Events
	public static final String APPLICATION_HOME = "Application Home Screen";
	public static final String HOME_SCREEN_SEARCH = "Search performed with a specific keyword";
	public static final String HOME_SEARCH_TEXT = "Home search text";

	public static final String CATEGORY = "Category selected on Home Screen";
	public static final String Category_Title = "Category title";
	public static final String Category_Id = "Category id";

	public static final String SUB_CATEGORY = "Sub Category selected";
	public static final String Sub_Category_Title = "Sub category title";
	public static final String Sub_Category_Id = "Sub category id";

	public static final String PHOTO_CONTEST_CLICK = "PhotoContest button is clicked";

	// Combined List (Company List) Screen Events
	public static final String APPLICATION_COMBINED_LIST = "Company List Screen";
	public static final String COMBINED_LIST_VISITED_ITEMS_EXCEEDED_70 = "User visited more than 70 items";
	public static final String VIEW_ON_MAP_CLICK = "ViewOnMap button is clicked";
	public static final String MODIFY_SEARCH_CLICK = "ModifySearch button is clicked";
	public static final String HOT_DEALS_CLICK = "HotDeals button is clicked";

	public static final String VIDEO_THUMBNAIL_CLICK = "video thumbnail is clicked";
	
	// Deals Listing and Deals Detail Screen Events
	public static final String APPLICATION_DEALS = "Deals Screen";
	public static final String GET_INFO_CLICK = "GetInfo button is clicked";
	public static final String DEAL_LIST_ITEM_CLICK = "User tabs a deal in Deal Listing";
	public static final String User_Visits_the_Deals_outlet_Page = "Deals Outlet Screen";
	public static final String User_Visits_the_Deals_outlet_Modify_Page = "Deals Outlet Modify Screen";

	// Login Screen Events
	public static final String APPLICATION_LOGIN = "Login Screen";
	public static final String USER_LOGGED_IN = "User logged in";
	public static final String Logged_In_User_Name = "logged in user name";
	public static final String Logged_In_User_Mobile_No = "logged in user mobile no";
	public static final String NEW_USER_REGISTER_NOW_CLICK = "New user? (Register now) button is clicked";
	public static final String PROCEED_AS_GUEST_CLICK = "Proceed as Guest (Limited facilities) button is clicked";
	public static final String FORGOT_PASSWORD_CLICK = "ForgotPassword button is clicked";

	// MyAccount Screen Events
	public static final String APPLICATION_MY_ACCOUNT = "MyAccount Screen";
	public static final String EDIT_PROFILE_CLICK = "EditProfile button is clicked";
	public static final String LOGOUT_CLICK = "Logout button is clicked";
	public static final String VERIFY_EMAIL_CLICK = "Email Not Verified link is clicked";
	public static final String MY_COMPANY_CLICK = "MyCompany button is clicked";
	public static final String MY_CLASSIFIEDS_CLICK = "MyClassifieds button is clicked";
	public static final String MY_DEALS_CLICK = "MyDeals button is clicked";
	public static final String MY_FAVOURITE_CLICK = "My Favourite(s) button is clicked";
	

	// EditProfile Screen Events
	public static final String APPLICATION_EDIT_PROFILE = "EditProfile Screen";
	public static final String EDIT_PROFILE_SUBMIT_CLICK = "EditProfileSubmit button is clicked";
	

	// My Deals Screen Events
	public static final String APPLICATION_MY_DEALS = "My Deals Screen";
	public static final String POST_DEAL_CLICK = "PostADeal button is clicked";

	// Post a Deal Screen Events
	public static final String POST_DEAL_SUBMIT_CLICK = "User posts a new deal";

	// Photo Contest Screen Events
	public static final String APPLICATION_CONTEST = "Photo Contest Screen";
	public static final String CAPTURE_NOW_CLICK = "CaptureNow button is clicked";

	public static final String CONTEST_POI_SEARCH = "Searched performed for POI";
	public static final String CONTEST_SEARCH_TEXT = "Contest search text";

	public static final String CATEGORY_CLICK = "ListByCategory button is clicked";
	public static final String DISTANCE_CLICK = "ListByDistance button is clicked";

	public static final String UPLOAD_IMAGE = "UploadYourImage button is clicked";
	public static final String CAPTURE_ANOTHER_IMAGE = "CaptureAnotherImage button is clicked";

	public static final String APPLICATION_ADD_NEW_POI = "AddNewPOI Screen";
	public static final String ADD_NEW_POI = "AddNewPOI button is clicked";
	public static final String SUBMIT_ADD_NEW_POI = "User adds a new POI";

	// Category Screen Events
	public static final String APPLICATION_CATEGORY = "Category Screen";

	// Company Detail Screen Events
	public static final String APPLICATION_COMPANY_DETAIL = "CompanyDetail Screen";
	
	// Company Detail Screen Events
	public static final String APPLICATION_DEAL_DETAIL = "DealsDetail Screen";
	
	//Video Play Screen
	
	public static final String APPLICATION_VIDEO_PLAY = "VideoPlayActivity Screen";

	// Company Detail Map Screen Events
	public static final String APPLICATION_COMPANY_DETAIL_MAP = "CompanyDetailMap Screen";
	public static final String ADD_TO_FAV_CLICK = "User adds a company to Favourite(s)";
	public static final String REMOVE_FAV_CLICK = "User removes a company from Favourite(s)";

	// My Favourite(s) Screen Events
	public static final String APPLICATION_MY_FAVOURITES = "My Favourite(s) Screen";
	public static final String FAV_LIST_ITEM_LONG_PRESS = "User removes a compnay from the Favourite(s) list";
	public static final String DELETE_FAV_CLICK = "User removes one or more company from the Favourite(s) list";
	public static final String EDIT_FAV_CLICK = "Edit Favourite(s) list button is clicked";
	
	
	public static final String APPLICATION_COMPANY_CATEGORY_SELECT = "Company/Category screen from Dashboard";
	
	// Force Update Dialog Events
	public static final String UPDATE_CLICK = "Force update performed";
	public static final String Current_API_Version = "Current API version";
	
	// MattaHallListing Screen Events
	public static final String MATTA_HALL_LISTING = "MattaHallList Screen";
	public static final String MATTA_BOOTH_LISTING = "MattaBoothList Screen";
	public static final String MATTA_BOOTH_DETAIL = "MattaBoothDetail Screen";
	public static final String MATTA_PACKAGE_LISTING = "MattaPackageList Screen";
	public static final String MATTA_PACKAGE_DETAIL = "MattaPackageDetail Screen";
	public static final String MATTA_FILTER_SEARCH = "MattaFilterSearch Screen";

	/*
	 * Error constants for flurry
	 */

	// Activities Errors
	public static final String DATE_ERR = "INVALID DATE";
	public static final String LOCATION_RECIEVER_UNREGISTER_ERR = "LOC RECIEVER";
	public static final String LOCATION_SERVICE_STOP_ERR = "LOC SERVICE";
	public static final String CONTEST_UPLOAD_IMAGE_ERR = "UPLOAD IMAGE";
	public static final String UPDATE_GEO_TAG_ERR = "UPDATE GEO TAG";
	public static final String DATA_VALIDATION_ERR = "DATA VALIDATION";
	public static final String IMAGE_SIZE_TOO_LARGE_ERR = "TOO LARGE IMG";
	public static final String VOUCHER_COUNT_ERR = "VOUCHER COUNT";
	public static final String REDEEM_PRICE_ERR = "REDEEM PRICE";
	public static final String POST_DEAL_UPLOAD_IMG_NOT_FOUND_ERR = "IMG NOT FOUND";
	public static final String CURRENT_LOCATION_NOT_SET_ERR = "LOCATION NOT SET";

	// Controllers Errors
	public static final String REQUEST_SERVICE_ERR = "REQUEST SERVICE";
	public static final String RESPONSE_SERVICE_ERR = "RESPONSE SERVICE";
	public static final String GOOGLE_MAP_DIRECTION_ERR = "GOOGLE MAP";

	// Services Errors
	public static final String GPS_LOCATION_FINDER_ERR = "GPS LOCATION";
	public static final String UNREGISTER_GPS_LOCATION_ERR = "UNREG GPS LOC";
	public static final String UNREGISTER_NETWORK_LOCATION_ERR = "UNREG NETWORK LOC";
}
