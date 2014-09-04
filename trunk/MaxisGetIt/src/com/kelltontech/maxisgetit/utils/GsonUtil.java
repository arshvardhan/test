package com.kelltontech.maxisgetit.utils;

import java.lang.reflect.Type;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * 
 *         This Class is used for mapping json String to T Class or T Type ;
 *         This Class take care of handling the Object and Array problem in Json
 * @param <T>
 * 
 */
public final class GsonUtil {

	private GsonUtil() {

	}

	/**
	 * @param jsonString
	 * @param clazz
	 * @return T class
	 * 
	 *         Use this method only when u need to take care of Object and Array
	 *         of same name ;
	 */
	public static <T> T mapFromJSONHanldeObject(String jsonString, Class<T> clazz) {
		Gson gson = new GsonBuilder().registerTypeAdapterFactory(new GenericAdapterFactory()).create();
		return gson.fromJson(jsonString, clazz);
	}

	/**
	 * @param jsonString
	 * @param typeof
	 * @return T class
	 * 
	 *         Use this method only when u need to take care of Object and Array
	 *         of same name ;
	 */
	public static <T> T mapFromJSONHanldeObject(String jsonString, Type typeof) {
		Gson gson = new GsonBuilder().registerTypeAdapterFactory(new GenericAdapterFactory()).create();
		return gson.fromJson(jsonString, typeof);
	}

	/**
	 * @param jsonString
	 * @param clazz
	 * @return
	 */
	public static <T> T mapFromJSON(String jsonString, Class<T> clazz) {
		Gson gson = new Gson();
		return gson.fromJson(jsonString, clazz);
	}

	/**
	 * @param jsonString
	 * @param typeof
	 * @return
	 */
	public static <T> T mapFromJSON(String jsonString, Type typeof) {
		Gson gson = new Gson();
		return gson.fromJson(jsonString, typeof);
	}

}
