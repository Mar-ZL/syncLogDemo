/**
 * 
 */
package com.sitech.util;

import java.util.ResourceBundle;

/**
 * @author cloud
 *
 */
public class PropertyUtils {
	
	public final static String DEFAULT_BASE_NAME = "params";
	
	public final static ResourceBundle DEFAULT_BUNDLE = ResourceBundle.getBundle(DEFAULT_BASE_NAME);

	public static String getString(String key) {
		String value = DEFAULT_BUNDLE.getString(key).trim();
		return value;
	}

	public static int getInt(String key) {
		String value = DEFAULT_BUNDLE.getString(key);
		return Integer.parseInt(value);
	}

	public static String getString(String key, String baseName) {		
		ResourceBundle bundle = ResourceBundle.getBundle(baseName);
		String value = bundle.getString(key).trim();
		return value;
	}

	public static int getInt(String key, String baseName) {		
		ResourceBundle bundle = ResourceBundle.getBundle(baseName);
		String value = bundle.getString(key).trim();
		return Integer.parseInt(value);
	}


}
