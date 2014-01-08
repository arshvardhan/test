package com.kelltontech.framework;

public class C {
	// Frame work level
	public static String FRAMEWORK_VERSION = "0.0.01";
	//
	public static int MAX_CONCURRENT = 10;
	// file log settings
	public static boolean FILE_LOG_ENABLED = false;
	public static String LOG_FILE_NAME = "application.log";
	public static int MAX_LOG_FILE_SIZE = 10 * 1024 * 1024; /* 10MB */

	// event log settings
	public static boolean EVENT_LOG_ENABLED = false;

	// Actions
	public static int LOG_ACTION = 500001;
	public static final int ERROR_OK = 200;
	public static final int ERROR_SERVICE = 980;
	public static final int ERROR_TIMEOUT = 981;

	// Application specific settings
	public static boolean DEBUG = false;
	public static String SERVICE_TAG = "not set";
	public static long CONNECTION_TIMEOUT = 15000;
	public static long CACHE_MAX_AGE = (4 * 60 * 60 * 1000);
	//
	public static final long GUID = 0x5b318af46efd83bL;
	//
	public static final int ACTION_FETCH = 1000;
	public static final int ACTION_FETCHED = 1001;
	public static final int ACTION_CLEAR_CACHE = 1002;
	//
	public static String CONTENT_TYPE_JSON = "text/json";
	public static String DEFAULT_ENCODING = "UTF-8";

	public static final String XMLNS = "http://github.com/droidfu/schema";
}
