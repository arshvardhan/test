/**
 *
 */
package com.kelltontech.framework.utils;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

/**
 * @author Dheeresh
 * 
 */
public class CollectionsUtils {

	public static <T> void copyList(List<T> source, List<T> dest) throws IllegalArgumentException {
		if (null == dest || null == source)
			throw new IllegalArgumentException("destination or source can't be null");
		dest.clear();
		for (int i = 0; i < source.size(); i++) {
			dest.add(source.get(i));
		}
	}

	public static <T> void copyHastable(Hashtable<String, T> sourceHT, Hashtable<String, T> destHashtable) {
		if (null == sourceHT || null == destHashtable)
			throw new IllegalArgumentException("destination or source can't be null");
		// loop over all of the keys
		Enumeration<String> e = sourceHT.keys();
		while (e.hasMoreElements()) {
			// retrieve the object_key
			String object_key = (String) e.nextElement();
			destHashtable.put(object_key, sourceHT.get(object_key));
		}
	}
}
