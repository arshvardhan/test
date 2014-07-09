package com.kelltontech.maxisgetit.utils;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.json.JSONObject;

/**
 * @author VIPIN JSONParser is a abstract class creates objects without exposing
 *         the instantiation logic to the client and Refers to the newly created
 *         object through a common interface
 */
public abstract class JSONParser
{
	public abstract void parse_JSON(JSONObject jsonObject) throws Exception;

	public abstract JSONObject getJSONObject(String uri);

	public abstract JSONObject getJSONObject(String url, List<NameValuePair> params);

	public abstract Cookie getCookiesValue(String string, String string2);

	public abstract JSONObject getJSONObjectLogin(String url, List<NameValuePair> params);

	/**
	 * @param url
	 *            the url for getting json response;
	 * @return String json response;
	 * @throws Exception
	 */
	public abstract String getStringJSON(String url) throws Exception;

	public abstract String getSessionStringJSON(String url) throws Exception;

	public abstract String getStringWithoutHeaderJSON(String url) throws Exception;

	public abstract String getSessionString();

	public abstract void setSessionString(String session_id);

	public abstract String getNewNounce(String url) throws Exception;

	public abstract String getNewSessionValue(String url, HttpPost httppost) throws Exception;

	public abstract List<Cookie> getCookies();

	public abstract void setCookies(List<Cookie> cookie);

	/**
	 * @param jsonString
	 *            enter the JSON String ;
	 * @param clasz
	 *            Model class for JSON Mapping
	 * @return Gson Mapped Class;
	 */
	public abstract <T> T mapFromJSON(String jsonString, Class<T> clasz);

	public abstract <T> T mapFromJSON(String jsonString, Type typeof);

	/**
	 * release all the resource and object held by this instance;
	 */
	public abstract void releaseObjects();

	public abstract void writeStringToFile(String name, String jsonString);

	public abstract String readStringFromFile(String name);

	public abstract String getStringJSON(String url,List<NameValuePair> nameValuePairs)
			throws Exception ;

}
